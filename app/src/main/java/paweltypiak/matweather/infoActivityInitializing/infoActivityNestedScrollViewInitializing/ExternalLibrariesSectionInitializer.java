package paweltypiak.matweather.infoActivityInitializing.infoActivityNestedScrollViewInitializing;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import paweltypiak.matweather.R;

public class ExternalLibrariesSectionInitializer {

    public ExternalLibrariesSectionInitializer(Activity activity,
                                               InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        initializeExternalLibrariesLicenseHiperlink(activity,nestedScrollViewInitializer);
        initializeExternalLibrariesHiperlinks(activity,nestedScrollViewInitializer);
    }

    private void initializeExternalLibrariesLicenseHiperlink(final Activity activity,
                                                             InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        TextView licenseTextView
                =(TextView)activity.findViewById(R.id.info_activity_external_libraries_license_text);
        setExternalLibrariesLicesceTextViewOnClickListener(activity,nestedScrollViewInitializer,licenseTextView);
        setExternalLibrariesLicenseTextViewHighlight(activity,licenseTextView);
    }

    private void setExternalLibrariesLicesceTextViewOnClickListener(final Activity activity,
                                                                    final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer,
                                                                    TextView licenseTextView){
        licenseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String webAddress=activity.getString(R.string.info_activity_apache_license_address);
                nestedScrollViewInitializer.initializeWebIntent(activity,webAddress);
            }
        });
    }

    private void setExternalLibrariesLicenseTextViewHighlight(Activity activity,
                                                              TextView licenseTextView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setRippleHighlight(activity,licenseTextView);
        }
        else{
            setStaticHighlight(activity,licenseTextView);
        }
    }

    private void setRippleHighlight(Activity activity,TextView textView){
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = activity.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        textView.setBackgroundResource(backgroundResource);
        typedArray.recycle();
    }

    private void setStaticHighlight(Activity activity,TextView textView){
        StateListDrawable states = new StateListDrawable();
        states.addState(
                new int[] {android.R.attr.state_pressed},
                ContextCompat.getDrawable(activity,R.drawable.rounded_button_background_pressed));
        states.addState(
                new int[] { },
                ContextCompat.getDrawable(activity,R.drawable.rounded_button_background_normal));
        textView.setBackground(states);
    }

    private void initializeExternalLibrariesHiperlinks(final Activity activity,
                                                       InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        setPicassoLibraryLayoutOnClickListener(activity,nestedScrollViewInitializer);
        setSmoothAppBarLayoutLibraryLayoutOnClickListener(activity,nestedScrollViewInitializer);
        setCollapsingToolbarLibraryLayoutOnClickListener(activity,nestedScrollViewInitializer);
    }

    private void setPicassoLibraryLayoutOnClickListener(final Activity activity,
                                                        final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        LinearLayout picassoLibraryLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_external_libraries_picasso_library_layout);
        picassoLibraryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollViewInitializer.initializeWebIntent(
                        activity,
                        activity.getString(R.string.info_activity_picasso_library_address));
            }
        });
    }

    private void setSmoothAppBarLayoutLibraryLayoutOnClickListener(final Activity activity,
                                                                   final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        LinearLayout smoothAppBarLayoutLibraryLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_external_libraries_smooth_app_bar_layout_library_layout);
        smoothAppBarLayoutLibraryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollViewInitializer.initializeWebIntent(
                        activity,
                        activity.getString(R.string.info_activity_smooth_app_bar_layout_library_address));
            }
        });
    }

    private void setCollapsingToolbarLibraryLayoutOnClickListener(final Activity activity,
                                                                  final InfoActivityNestedScrollViewInitializer nestedScrollViewInitializer){
        LinearLayout collapsingToolbarLibraryLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_external_libraries_multiline_collapsingtoolbar_library_layout);
        collapsingToolbarLibraryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nestedScrollViewInitializer.initializeWebIntent(
                        activity,
                        activity.getString(R.string.info_activity_multiline_collapsingtoolbar_library_address));
            }
        });
    }
}
