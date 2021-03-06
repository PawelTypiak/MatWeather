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
package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.ServiceFailureDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;

public class IntroActivityLoadingFragmentWeatherServiceFailureDialogInitializer {

    private Activity activity;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog weatherServiceFailureDialog;

    public IntroActivityLoadingFragmentWeatherServiceFailureDialogInitializer(Activity activity,
                                                                              IntroActivityLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializeWeatherServiceFailureDialog();
    }

    public AlertDialog getWeatherServiceFailureDialog() {
        return weatherServiceFailureDialog;
    }

    private void initializeWeatherServiceFailureDialog(){
        weatherServiceFailureDialog = ServiceFailureDialogInitializer.getServiceFailureDialog(
                activity,
                0,
                positiveButtonRunnable,
                getNegativeButtonRunnable()
        );
    }

    private Runnable positiveButtonRunnable =new Runnable() {
        @Override
        public void run() {
            String location=dataDownloader.getLocation();
            dataDownloader.getWeatherDataDownloader().downloadWeatherData(location);
        }
    };

    private Runnable getNegativeButtonRunnable(){
        return new ShowLoadingFragmentExitDialogRunnable(activity,showWeatherServiceFailureDialogRunnable);
    }

    private Runnable showWeatherServiceFailureDialogRunnable = new Runnable() {
        public void run() {weatherServiceFailureDialog.show();}
    };
}
