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
package paweltypiak.weatherial;

import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.exitDialogInitializing.ExitDialogInitializer;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityDataDownloading.MainActivityDataDownloader;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnTimeChangeLayoutUpdater;
import paweltypiak.weatherial.settingsActivityInitializing.SettingsActivity;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.weatherial.utils.UsefulFunctions;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;

public class MainActivity extends AppCompatActivity {

    private MainActivityLayoutInitializer mainActivityLayoutInitializer;
    private MainActivityDataDownloader mainActivityDataDownloader;
    private WeatherDataParser weatherDataParser;
    private AlertDialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLayout();
        initializeDataDownloading(savedInstanceState);
    }

    private void initializeLayout(){
        setContentView(R.layout.activity_main);
        mainActivityLayoutInitializer =new MainActivityLayoutInitializer(this);
    }

    public MainActivityLayoutInitializer getMainActivityLayoutInitializer() {
        return mainActivityLayoutInitializer;
    }

    private void initializeDataDownloading(Bundle savedInstanceState){
        mainActivityDataDownloader=new MainActivityDataDownloader(this,savedInstanceState,mainActivityLayoutInitializer);
    }

    public MainActivityDataDownloader getMainActivityDataDownloader() {
        return mainActivityDataDownloader;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mainActivityDataDownloader.
                getGeolocalizationDownloader().
                getOnRequestLocationPermissionsResultCallback().
                onRequestLocationPermissionsResult(requestCode,grantResults);
    }

    @Override
    public void onBackPressed() {
        boolean isDrawerLayoutOpened= mainActivityLayoutInitializer.getAppBarLayoutInitializer()
                .getAppBarLayoutButtonsInitializer()
                .getNavigationDrawerInitializer()
                .closeDrawerLayout();
        if(isDrawerLayoutOpened==false){
            if(exitDialog==null){
                exitDialog= ExitDialogInitializer.getExitDialog(this,1,null);
            }
            exitDialog.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mainActivityLayoutInitializer.getAppBarLayoutInitializer()
                    .getAppBarLayoutButtonsInitializer()
                    .getNavigationDrawerInitializer()
                    .openDrawerLayout();
        }
        return super.onKeyDown(keyCode, e);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        //saving current weather information for recreate
        super.onSaveInstanceState(state);
        WeatherDataParser currentWeatherDataParser = OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser();
        state.putParcelable(getString(R.string.extras_data_initializer_key), currentWeatherDataParser);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //receiving information from settings
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //postdelay to call recreate() after onResume()
                if(SettingsActivity.isUnitsPreferencesChanged()) {
                    //units change
                    refreshLayoutAfterUnitsPreferencesChange();
                    SettingsActivity.setUnitsPreferencesChanged(false);
                }
                if(SettingsActivity.isLanguagePreferencesChanged()){
                    //language change
                    recreate();
                    SettingsActivity.setLanguagePreferencesChanged(false);
                }
            }
        }, 0);
    }

    private void refreshLayoutAfterUnitsPreferencesChange(){
        mainActivityLayoutInitializer.updateLayoutOnWeatherDataChange(this, weatherDataParser,true,false);
    }

    private OnTimeChangeLayoutUpdater getOnTimeChangeLayoutUpdater(){
        if(mainActivityLayoutInitializer!=null){
            return mainActivityLayoutInitializer.getOnTimeChangeLayoutUpdater();
        }
        else return null;
    }

    @Override
    protected  void onPause(){
        super.onPause();
        //stop updating current time in AppBar
        OnTimeChangeLayoutUpdater onTimeChangeLayoutUpdater=getOnTimeChangeLayoutUpdater();
        if(onTimeChangeLayoutUpdater!=null){
            onTimeChangeLayoutUpdater.pauseUiThread();
        }
        weatherDataParser = OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start updating current time in AppBar
        OnTimeChangeLayoutUpdater onTimeChangeLayoutUpdater=getOnTimeChangeLayoutUpdater();
        if(onTimeChangeLayoutUpdater!=null){
            onTimeChangeLayoutUpdater.resumeUiThread();
        }
        UsefulFunctions.setTaskDescription(this);
    }

    @Override
    protected void onDestroy() {
        //killing UI thread, which updates layout every second
        OnTimeChangeLayoutUpdater onTimeChangeLayoutUpdater=getOnTimeChangeLayoutUpdater();
        if(onTimeChangeLayoutUpdater!=null){
            onTimeChangeLayoutUpdater.interruptUiThread();
        }
        super.onDestroy();
    }
}
