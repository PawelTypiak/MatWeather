package paweltypiak.matweather.introActivityInitializing;

import android.app.Activity;
import android.util.Log;

import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

class IntroActivityLaunchProcessInitializer {

    IntroActivityLaunchProcessInitializer(Activity activity,
                                                 boolean isFirstLaunch,
                                                 IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer,
                                                 IntroActivityNextButtonInitializer introActivityNextButtonInitializer){
        if(isFirstLaunch){
            initializeFirstLaunch(activity,fragmentInsertionInitializer,introActivityNextButtonInitializer);
        }
        else{
            initializeNextLaunch(activity,fragmentInsertionInitializer,introActivityNextButtonInitializer);
        }
    }

    private void initializeFirstLaunch(Activity activity,
                                       IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer,
                                       IntroActivityNextButtonInitializer introActivityNextButtonInitializer){
        initializeStartFragment(activity,fragmentInsertionInitializer);
        setStartButton(activity,true,introActivityNextButtonInitializer);
    }

    private void initializeStartFragment(Activity activity,
                                         IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer){
        fragmentInsertionInitializer.initializeStartFragment(activity);
    }

    private void initializeNextLaunch(Activity activity,
                                      IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer,
                                      IntroActivityNextButtonInitializer introActivityNextButtonInitializer){
        //every next launch of application
        Log.d("launch", "next launch");
        setLanguageVersion(activity);
        initializeConfigurationFragment(activity,fragmentInsertionInitializer);
        setStartButton(activity,false,introActivityNextButtonInitializer);
    }

    private void setLanguageVersion(Activity activity){
        //set saved language version
        int languageVersion=SharedPreferencesModifier.getLanguageVersion(activity);
        if(languageVersion!=-1) UsefulFunctions.setLocale(activity,languageVersion);
    }

    private void initializeConfigurationFragment(Activity activity,
                                                 IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer){
        fragmentInsertionInitializer.insertConfigurationFragment(activity);
    }

    private void setStartButton(Activity activity,
                                boolean isFirstLaunch,
                                IntroActivityNextButtonInitializer introActivityNextButtonInitializer){
        introActivityNextButtonInitializer.setStartButton(activity,isFirstLaunch);
    }
}