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
package paweltypiak.weatherial.dialogsInitializing.dialogInitializers;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.noEmailApplicationDialogInitializing.NoEmailApplicationDialogInitializer;

public class AuthorDialogInitializer {

    public static AlertDialog getAuthorDialog(Activity activity){
        return initializeAuthorDialog(activity);
    }

    private static AlertDialog initializeAuthorDialog(Activity activity){
        View dialogView=initializeDialogView(activity);
        setEmailLayoutOnClickListener(activity,dialogView);
        setGithubLayoutOnClickListener(activity,dialogView);
        AlertDialog authorDialog = buildAlertDialog(activity,dialogView);
        setAlertDialogOnShowListener(activity,authorDialog);
        return authorDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_author,null);
        return dialogView;
    }

    private static void setEmailLayoutOnClickListener(final Activity activity,View dialogView){
        RelativeLayout emailLayout=(RelativeLayout)dialogView.findViewById(R.id.author_dialog_email_button_layout);
        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeEmailIntent(activity);
            }
        });
    }

    private static void initializeEmailIntent(Activity activity){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        String address=activity.getString(R.string.author_email_address);
        intent.setData(Uri.parse("mailto:"+address));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            activity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            AlertDialog dialog=NoEmailApplicationDialogInitializer.getNoEmailApplicationDialog(activity);
            dialog.show();
        }
    }

    private static void setGithubLayoutOnClickListener(final Activity activity,View dialogView){
        RelativeLayout githubLayout=(RelativeLayout)dialogView.findViewById(R.id.author_dialog_github_button_layout);
        githubLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeWebIntent(activity,activity.getString(R.string.github_address));
            }
        });
    }

    private static void initializeWebIntent(Context context, String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    private static AlertDialog buildAlertDialog(Activity activity, View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.author_dialog_title),
                R.drawable.author_icon,
                null,
                false,
                null,
                null,
                null,
                null,
                activity.getString(R.string.author_dialog_negative_button),
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private static void setAlertDialogOnShowListener(final Activity activity, final AlertDialog authorDialog){
        authorDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,authorDialog);
            }
        });
    }
}
