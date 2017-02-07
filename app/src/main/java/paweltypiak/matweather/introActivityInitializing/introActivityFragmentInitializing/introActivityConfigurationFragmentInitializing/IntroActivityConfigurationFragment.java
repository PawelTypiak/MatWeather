package paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityConfigurationFragmentInitializing;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import paweltypiak.matweather.R;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.IntroActivityGeolocalizationMethodFragment;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.IntroActivityLanguageFragment;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.IntroActivityLoadingFragment;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLocationFragmentInitializing.IntroActivityLocationFragment;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class IntroActivityConfigurationFragment extends Fragment{

    private FragmentTransaction fragmentTransaction;
    private IntroActivityLocationFragment locationFragment;
    private IntroActivityLoadingFragment loadingFragment;
    private IntroActivityGeolocalizationMethodFragment geolocalizationMethodsFragment;
    private boolean isFirstLaunch;
    private boolean isAfterChoosingGeolocalizationMethod =false;
    private int selectedDefeaultLocationOption =-1;
    private int selectedGeolocalizationMethod =-1;

    public static IntroActivityConfigurationFragment newInstance(Activity activity,boolean isFirstLaunch ) {
        IntroActivityConfigurationFragment configurationFragment = new IntroActivityConfigurationFragment();
        Bundle extras = new Bundle();
        extras.putBoolean(activity.getString(R.string.extras_is_first_launch_key), isFirstLaunch);
        configurationFragment.setArguments(extras);
        return configurationFragment;
    }

    private void getExtras(){
        //get information if it is first launch
        isFirstLaunch = getArguments().getBoolean(getString(R.string.extras_is_first_launch_key));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
        return inflater.inflate(R.layout.intro_activity_fragment_configuration, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeSettingsFragmentInserting();
    }

    private void initializeSettingsFragmentInserting(){
        final ImageView appIconImageView=(ImageView)getActivity().findViewById(R.id.intro_activity_configuration_fragment_app_icon_image);
        Picasso.with(getActivity()).load(R.drawable.logo_intro).fit().centerInside().into(appIconImageView, new Callback() {
            @Override
            public void onSuccess() {
                if(isFirstLaunch) {
                    insertNestedFragment(new IntroActivityLanguageFragment(),getString(R.string.intro_activity_language_fragment_tag));
                }
                else {
                    initializeLoadingLocation(null);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    public void insertNestedFragment(android.support.v4.app.Fragment nestedFragment,String tag) {
        //set nested fragment
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.intro_activity_configuration_fragment_placeholder, nestedFragment,tag)
                .commit();
    }

    public void showNoDifferentLocationSelectedDialogInLocationFragment(){
        //show dialog with information about empty location name
        locationFragment.showNoDifferentLocationSelectedDialog();
    }

    public void initializeLoadingLocation(CardView nextCardViewButton){
        if(isFirstLaunch){
            if(isAfterChoosingGeolocalizationMethod ==true){
                //geolocalization after selected geololocalization method
                initializeGeolocalizationDownloadingAfterGeolocalizationMethodsSelecting(nextCardViewButton);
            }
            else{
                selectedDefeaultLocationOption = getDefeaultLocationOptionFromLocationFragment();
                if(selectedDefeaultLocationOption ==0){
                    showGeolocalizationMethodsFragment();
                }
                else{
                    initializeWeatherDataDownloadingForDifferentLocation(nextCardViewButton);
                }
            }
        }
        else {
            insertLoadingFragment(-1,-1,"",null);
        }
    }

    private void initializeGeolocalizationDownloadingAfterGeolocalizationMethodsSelecting(CardView nextCardViewButton){
        selectedGeolocalizationMethod = geolocalizationMethodsFragment.getSelectedGeolocalizationMethod();
        insertLoadingFragment(selectedGeolocalizationMethod,0, "",nextCardViewButton);
        UsefulFunctions.setViewInvisible(nextCardViewButton);
        isAfterChoosingGeolocalizationMethod =false;
    }

    private void showGeolocalizationMethodsFragment(){
        if(isAfterChoosingGeolocalizationMethod ==false){
            //insert fragment with geolocalization methods
            insertGeolocalizationMethodsFragment();
            isAfterChoosingGeolocalizationMethod =true;
        }
    }

    private void initializeWeatherDataDownloadingForDifferentLocation(CardView nextCardViewButton){
        String differentLocationName=getDifferentLocationNameFromLocationFragment();
        if(differentLocationName.equals(getString(R.string.first_launch_defeault_location_different))){
            //dialog with information that user didn't provided location name
            showNoDifferentLocationSelectedDialogInLocationFragment();
        }
        else{
            insertLoadingFragment(
                    -1,
                    selectedDefeaultLocationOption,
                    differentLocationName,
                    nextCardViewButton);
        }
    }

    private void insertLoadingFragment(int selectedGeolocalizationMethod,
                                       int selectedDefeaultLocationOption,
                                       String differentLocationName,
                                       CardView startCardViewButton) {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        loadingFragment
                = IntroActivityLoadingFragment.newInstance(
                getActivity(),
                isFirstLaunch,
                selectedDefeaultLocationOption,
                selectedGeolocalizationMethod,differentLocationName);
        fragmentTransaction.replace(
                R.id.intro_activity_configuration_fragment_placeholder,
                loadingFragment,
                getString(R.string.intro_activity_loading_fragment_tag));
        fragmentTransaction.commit();
        if(startCardViewButton!=null)UsefulFunctions.setViewInvisible(startCardViewButton);
    }

    private void insertGeolocalizationMethodsFragment() {
        fragmentTransaction = getChildFragmentManager().beginTransaction();
        geolocalizationMethodsFragment = new IntroActivityGeolocalizationMethodFragment();
        fragmentTransaction.replace(
                R.id.intro_activity_configuration_fragment_placeholder,
                geolocalizationMethodsFragment,
                getString(R.string.intro_activity_geolocaliztion_methods_fragment_tag));
        fragmentTransaction.commit();
    }

    private int getDefeaultLocationOptionFromLocationFragment(){
        //get information if selected defeault location is current location, or different location
        try{
            locationFragment=(IntroActivityLocationFragment)getChildFragment(getString(R.string.intro_activity_defeault_location_fragment_tag));
            return locationFragment.getSelectedDefeaultLocationOption();
        }catch (Exception exception){
            return 0;
        }
    }

    private String getDifferentLocationNameFromLocationFragment(){
        //get name for selected location
        locationFragment=(IntroActivityLocationFragment)getChildFragment(getString(R.string.intro_activity_defeault_location_fragment_tag));
        String differentLocationNameString=locationFragment.getDifferentLocationName();
        return differentLocationNameString;
    }

    private Fragment getChildFragment(String tag){
        //get child fragment by tag
        //// TODO: move to fragmentInsertionInitializer
        Fragment childFragment =
                getChildFragmentManager().findFragmentByTag(tag);
        return childFragment;
    }

    public int getSelectedDefeaultLocationOption() {
        return selectedDefeaultLocationOption;
    }
}