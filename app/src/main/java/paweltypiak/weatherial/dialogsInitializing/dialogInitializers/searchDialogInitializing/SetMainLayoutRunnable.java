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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.searchDialogInitializing;

import android.app.Activity;

import paweltypiak.weatherial.MainActivity;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;

class SetMainLayoutRunnable implements Runnable {

    private Activity activity;
    private WeatherDataParser dataInitializer;

    public SetMainLayoutRunnable(Activity activity,
                                 WeatherDataParser dataInitializer) {
        this.activity=activity;
        this.dataInitializer = dataInitializer;
    }

    public void run() {
        ((MainActivity)activity).getMainActivityLayoutInitializer().
                updateLayoutOnWeatherDataChange(activity,dataInitializer,true,false);
    }
}
