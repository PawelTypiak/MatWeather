package paweltypiak.matweather.settingsActivityInitializing.dialogPreferencesInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceFragment;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public abstract class CustomDialogPreference extends DialogPreference{
    //dialog preference with custom layout
    private String dialogTitle;

    public CustomDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setPositiveButtonText(getContext().getString(R.string.preferences_dialog_positive_button));
        setNegativeButtonText(getContext().getString(R.string.preferences_dialog_negative_button));
    }

    protected void setPreferenceIcon(String fragmentTag, String preferenceKey, int drawableId){
        PreferenceFragment fragment=(PreferenceFragment)((Activity) getContext()).getFragmentManager().findFragmentByTag(fragmentTag);
        CustomDialogPreference test = (CustomDialogPreference) fragment.findPreference(preferenceKey);
        Drawable icon= UsefulFunctions.getColoredDrawable((Activity)getContext(), drawableId, ContextCompat.getColor(getContext(),R.color.colorPrimary));
        Log.d("preference_test", "test: "+test);
        Log.d("preference_test", "icon: "+icon);
        test.setIcon(icon);
    }

    protected void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    protected RadioButton setRadioButtonLayout(String radioButtonText,int radioButtonId,int radioButtonBottomMargin){
        RadioButton radioButton=new RadioButton(getContext());
        radioButton.setText(UsefulFunctions.fromHtml(radioButtonText));
        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,getContext().getResources().getDimensionPixelSize(R.dimen.dialog_text_size));
        radioButton.setTextColor(ContextCompat.getColor(getContext(),R.color.textSecondaryLightBackground));
        radioButton.setId(radioButtonId);
        setRadioButtonMargins(radioButton,radioButtonBottomMargin);
        return radioButton;
    }

    public void setRadioButtonMargins(RadioButton radioButton,int radioButtonBottomMargin){
        LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0,0, radioButtonBottomMargin);
        radioButton.setLayoutParams(layoutParams);
    }

    @Override
    protected View onCreateDialogView() {
        View view = View.inflate(getContext(),R.layout.dialog_radiogroup,null);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group_dialog_radiogroup);
        buildRadioGroup(radioGroup);
        return view;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        builder.setTitle(dialogTitle);
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        AlertDialog dialog=(AlertDialog)getDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult){
            onPositiveResult();
        }
    }
    protected abstract void buildRadioGroup(RadioGroup radioGroup);

    protected abstract void onPositiveResult();

    protected abstract void setPreferenceSummary();
}