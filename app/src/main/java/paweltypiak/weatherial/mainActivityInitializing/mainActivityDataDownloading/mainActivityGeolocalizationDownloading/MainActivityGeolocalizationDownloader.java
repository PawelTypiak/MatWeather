/*******************************************************************************
 * Copyright 2017 Paweł Typiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package paweltypiak.weatherial.mainActivityInitializing.mainActivityDataDownloading.mainActivityGeolocalizationDownloading;

import android.Manifest;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.GeolocalizationFailureDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.GeolocalizationPermissionsDeniedDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.InternetFailureDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.ProgressDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.ServiceFailureDialogInitializer;
import paweltypiak.weatherial.dataDownloading.currentLocationDataDownloading.CurrentLocationCoordinatesDownloader;
import paweltypiak.weatherial.dataDownloading.currentLocationDataDownloading.GeocodingCallback;
import paweltypiak.weatherial.dataDownloading.currentLocationDataDownloading.GeocodingDownloader;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityDataDownloading.MainActivityDataDownloader;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;

public class MainActivityGeolocalizationDownloader implements
        GeocodingCallback,
        OnRequestLocationPermissionsResultCallback {

    private Activity activity;
    private String geocodingLocation;
    private MainActivityDataDownloader dataDownloader;
    private CurrentLocationCoordinatesDownloader currentLocationCoordinatesDownloader;
    private AlertDialog geolocalizationProgressDialog;
    private TextView progressDialogMessageTextView;
    private AlertDialog geocodingInternetFailureDialog;
    private AlertDialog geocodingServiceFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private AlertDialog coordinatesDownloadFailureDialog;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;

    public MainActivityGeolocalizationDownloader(Activity activity,
                                                 MainActivityDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializeDialogs(activity);
    }

    private void initializeDialogs(Activity activity){
        geolocalizationProgressDialog
                = ProgressDialogInitializer.getProgressDialog(
                activity,
                activity.getString(R.string.waiting_for_localization_progress_message));
        geocodingInternetFailureDialog
                = InternetFailureDialogInitializer.getInternetFailureDialog(
                activity,
                1,
                geocodingRunnable,
                null);
        geocodingServiceFailureDialog
                = ServiceFailureDialogInitializer.getServiceFailureDialog(
                activity,
                1,
                startGeolocalizationRunnable,
                null);
        permissionDeniedDialog
                = GeolocalizationPermissionsDeniedDialogInitializer.getGeolocalizationPermissionsDeniedDialog(
                activity,
                1,
                startGeolocalizationRunnable,
                null);
        coordinatesDownloadFailureDialog=
                GeolocalizationFailureDialogInitializer.getGeolocalizationFailureDialog(
                        activity,
                        1,
                        startGeolocalizationRunnable,
                        null);
    }

    public AlertDialog getGeolocalizationProgressDialog() {
        return geolocalizationProgressDialog;
    }

    @Override
    public void geocodingServiceSuccess(String location) {
        progressDialogMessageTextView.setText(activity.getString(R.string.downloading_weather_data_progress_message));
        geocodingLocation=location;
        dataDownloader.getWeatherDataDownloader().initializeWeatherDataDownloading(0,location);
    }

    @Override
    public void geocodingServiceFailure(int errorCode) {
        geolocalizationProgressDialog.dismiss();
        if(errorCode==0){
            geocodingInternetFailureDialog.show();
        }
        else if(errorCode==1){
            geocodingServiceFailureDialog.show();
        }
    }

    public String getGeocodingLocation() {
        return geocodingLocation;
    }

    public void initializeCurrentLocationDataDownloading(){
        //permissions for Android 6.0
        if(areGeolocalizationPermissionsGranted()==true){
            downloadCurrentLocationCoordinates();
        }
        else{
            ActivityCompat.requestPermissions(
                    activity,
                    new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private boolean areGeolocalizationPermissionsGranted(){
        if ( ContextCompat.checkSelfPermission( activity, Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED )
        {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onRequestLocationPermissionsResult(int requestCode, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadCurrentLocationCoordinates();
                    Log.d("permissions_downloader", "granted");
                } else {
                    permissionDeniedDialog.show();
                    Log.d("permissions_downloader", "denied");
                }
            }
        }
    }

    public OnRequestLocationPermissionsResultCallback getOnRequestLocationPermissionsResultCallback() {
        return this;
    }

    private void downloadCurrentLocationCoordinates(){
        geolocalizationProgressDialog.show();
        if(progressDialogMessageTextView==null){
            progressDialogMessageTextView
                    =(TextView)geolocalizationProgressDialog.findViewById(R.id.progress_dialog_message_text);
        }
        int geolocalizationMethod= SharedPreferencesModifier.getGeolocalizationMethod(activity);
        currentLocationCoordinatesDownloader =new CurrentLocationCoordinatesDownloader(
                activity,
                this,
                coordinatesDownloadFailureDialog,
                permissionDeniedDialog,
                geolocalizationProgressDialog,
                progressDialogMessageTextView,
                null,
                geolocalizationMethod
        );
    }

    private Runnable startGeolocalizationRunnable = new Runnable() {
        public void run() {
            initializeCurrentLocationDataDownloading();}
    };

    private Runnable geocodingRunnable = new Runnable() {
        public void run() {
            geolocalizationProgressDialog.show();
            progressDialogMessageTextView.setText(activity.getString(R.string.retrieving_address_progress_message));
            new GeocodingDownloader(
                    activity,
                    currentLocationCoordinatesDownloader.getLocation(),
                    MainActivityGeolocalizationDownloader.this,
                    progressDialogMessageTextView);
        }
    };
}
