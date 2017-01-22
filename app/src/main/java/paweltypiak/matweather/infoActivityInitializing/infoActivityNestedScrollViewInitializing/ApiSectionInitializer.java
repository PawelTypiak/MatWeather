package paweltypiak.matweather.infoActivityInitializing.infoActivityNestedScrollViewInitializing;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import paweltypiak.matweather.R;

public class ApiSectionInitializer {

    public ApiSectionInitializer(Activity activity,
                                 InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        initializeWeatherApiLayout(activity,nestedScrollViewInitializer);
        initializeGeocodingApiLayout(activity,nestedScrollViewInitializer);
    }

    private void initializeWeatherApiLayout(Activity activity,
                                            InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){

        setWeatherApiLayoutListeners(activity,nestedScrollViewInitializer);
        setWeatherApiLayoutWebIcon(activity,nestedScrollViewInitializer);
    }

    private void setWeatherApiLayoutListeners(final Activity activity,
                                              final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        LinearLayout weatherApiLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_api_weather_layout);
        String weatherApiAddress=activity.getString(R.string.info_activity_api_weather_address);
        setWeatherApiLayoutOnClickListener(activity,nestedScrollViewInitializer,weatherApiLayout,weatherApiAddress);
        setWeatherApiLayoutOnLongClickListener(activity,nestedScrollViewInitializer,weatherApiLayout,weatherApiAddress);
    }

    private void setWeatherApiLayoutOnClickListener(final Activity activity,
                                                    final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                    LinearLayout weatherApiLayout,
                                                    final String weatherApiString){
        weatherApiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollViewInitializer.initializeWebIntent(activity,weatherApiString);
            }
        });
    }

    private void setWeatherApiLayoutOnLongClickListener(final Activity activity,
                                                        final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                        LinearLayout weatherApiLayout,
                                                        final String weatherApiString){
        weatherApiLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                nestedScrollViewInitializer.copyAddressToClipboard(activity,weatherApiString);
                return true;
            }
        });
    }

    private void setWeatherApiLayoutWebIcon(Activity activity,
                                            final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        ImageView weatherApiImageView
                =(ImageView)activity.findViewById(R.id.info_activity_api_weather_web_icon_image);
        nestedScrollViewInitializer.setWebIcon(activity,weatherApiImageView);
    }

    private void initializeGeocodingApiLayout(Activity activity,
                                              final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        setGeocodingApiLayoutListeners(activity,nestedScrollViewInitializer);
        setGeocodingApiLayoutWebIcon(activity,nestedScrollViewInitializer);
    }

    private void setGeocodingApiLayoutListeners(final Activity activity,
                                              final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        LinearLayout geocodingApiLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_api_geocoding_layout);
        String geocodingApiAddress=activity.getString(R.string.info_activity_api_weather_address);
        setGeocodingApiLayoutOnClickListener(activity,nestedScrollViewInitializer,geocodingApiLayout,geocodingApiAddress);
        setGeocodingApiLayoutOnLongClickListener(activity,nestedScrollViewInitializer,geocodingApiLayout,geocodingApiAddress);
    }

    private void setGeocodingApiLayoutOnClickListener(final Activity activity,
                                                      final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                      LinearLayout geocodingApiLayout,
                                                      final String geocodingApiAddress){
        geocodingApiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollViewInitializer.initializeWebIntent(activity,geocodingApiAddress);
            }
        });
    }

    private void setGeocodingApiLayoutOnLongClickListener(final Activity activity,
                                                          final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                          LinearLayout geocodingApiLayout,
                                                          final String geocodingApiAddress){
        geocodingApiLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                nestedScrollViewInitializer.copyAddressToClipboard(activity,geocodingApiAddress);
                return true;
            }
        });
    }

    private void setGeocodingApiLayoutWebIcon(Activity activity,
                                              final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        ImageView geocodingApiImageView
                =(ImageView)activity.findViewById(R.id.info_activity_api_geocoding_web_icon_image);
        nestedScrollViewInitializer.setWebIcon(activity,geocodingApiImageView);
    }
}
