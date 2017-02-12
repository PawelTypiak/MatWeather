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
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.AlertDialogBuilder;

public class ProgressDialogInitializer {

    public static AlertDialog getProgressDialog(Activity activity,
                                                String message){
        return initializeProgressDialog(activity,message);
    }

    private static AlertDialog initializeProgressDialog(Activity activity,
                                         String message){
        View dialogView = initializeDialogView(activity);
        initializeMessageTextView(message,dialogView);
        AlertDialog progressDialog =buildAlertDialog(activity,dialogView);
        return progressDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_progress,null);
        return dialogView;
    }

    private static void initializeMessageTextView(String message, View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.progress_dialog_message_text);
        messageTextView.setText(message);
    }

    private static AlertDialog buildAlertDialog(Activity activity,View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                null,
                0,
                null,
                true,
                null,
                null,
                null,
                null,
                null,
                null);
        return alertDialogBuilder.getAlertDialog();
    }
}
