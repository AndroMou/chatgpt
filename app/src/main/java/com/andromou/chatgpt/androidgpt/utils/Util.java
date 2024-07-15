package com.andromou.chatgpt.androidgpt.utils;

import static android.content.Context.CLIPBOARD_SERVICE;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andromou.chatgpt.androidgpt.R;

import java.util.Calendar;

public class Util {

    private static final String PLAY_STORE_ID = "https://play.google.com/store/apps/details?id=";

    private static final String MARKET_ID = "market://details?id=";

    public static void executeMethod(Context context) {
        final String PREFS_NAME = "ExecutionPrefs";
        final String LAST_EXECUTION_TIMESTAMP = "lastExecutionTimestamp";
        final int MAX_ALLOWED_EXECUTIONS = 5;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int executionsCount = prefs.getInt(LAST_EXECUTION_TIMESTAMP, 0);
        if (executionsCount <= MAX_ALLOWED_EXECUTIONS) {
            int lastExecutionTime = prefs.getInt(LAST_EXECUTION_TIMESTAMP, 0);
            long currentTime = Calendar.getInstance().getTimeInMillis();

            // Check if it has been more than 24 hours since the last execution
            if (currentTime - lastExecutionTime >= 24 * 60 * 60 * 1000) {
                // Execute the method
                // ...
              //  showRewordedAds();
                // Update the executions count and timestamp
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(LAST_EXECUTION_TIMESTAMP, currentTime);
                editor.putInt(LAST_EXECUTION_TIMESTAMP, executionsCount + 1);
                editor.apply();
            }
        } else {
           // addToChat("you have just 5 times in 24 hours", Message.SENT_BY_BOT);
            //       messageList.add(new Message("you have just 5 times in 24 hours", Message.SENT_BY_BOT));
            showToast(context, "you have just 5 times in 24 hours"); ;
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void showToast(Context context, String message) {
        // Inflate custom layout for toast
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);

        // Set message to the custom layout
        TextView text = layout.findViewById(R.id.text);
        text.setText(message);

        // Create and show the toast
        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


    public static void getMoreApps( Context context){
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(PLAY_STORE_ID + context.getString(R.string.google_developer_id))));
        } catch (ActivityNotFoundException ante) {
            showToast(context, context.getString(R.string.error_occ));
        }
    }

    public static void shareApp( Context context){

        final String appPackageName = context.getPackageName();
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sage GPT");
            String shareMessage="Ask ChatGPT ❤\uD83E\uDD29\n\nApp Link:  ";
            shareMessage = shareMessage + PLAY_STORE_ID + appPackageName ;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void rateApp( Context context){
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_ID + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_ID + appPackageName)));
        }
    }

    public static void onPaste(Context context, EditText editText) {
        ClipboardManager clipboardManager =  (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        if (clipboardManager.hasPrimaryClip()) {
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                CharSequence text = clipData.getItemAt(0).coerceToText(editText.getContext());
                if (text != null) {
                    // Set the text to the EditText
                    editText.setText(text);
                }
            }
        }
    }

    public static void copyText(Context context, String mText){
        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", mText);
        clipboardManager.setPrimaryClip(clipData);
        showToast(context, "Text is Copied");
    }

    public static void startAppCompatActivity(Context context, Class mClass){
        Intent intent = new Intent(context, mClass);
        context.startActivity(intent);
      //  ((Activity)context).finish();
    }


}
