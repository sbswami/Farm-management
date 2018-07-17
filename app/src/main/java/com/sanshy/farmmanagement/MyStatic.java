package com.sanshy.farmmanagement;

import android.app.AlertDialog;
import android.content.Context;

public class MyStatic {

    public static final String YEAR_OF_REAPING = "YearOfReaping";
    public static final String YEAR_OF_SOWING = "YearOfSowing";
    public static final String CURRENT_CROP_ID = "CurrentCropId";
    public static final String CURRENT_CROP_NAME = "CurrentCropName";

    public static void ShowDialog(Context context, String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(text)
                .setPositiveButton(context.getString(R.string.ok),null);

        builder.create().show();
    }
    public static void DateRequestDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_date)
                .setMessage(context.getString(R.string.please_choose_any_date))
                .setPositiveButton(context.getString(R.string.ok),null)
                .create()
                .show();
    }
    public static void ErrorFeedbackDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.error)
                .setMessage(context.getString(R.string.something_is_wrong)+"\n" +
                        context.getString(R.string.try_again_later_or_send_feedback))
                .setPositiveButton(context.getString(R.string.ok),null)
                .create().show();
    }
}
