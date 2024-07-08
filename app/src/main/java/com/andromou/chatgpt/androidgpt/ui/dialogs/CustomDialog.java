package com.andromou.chatgpt.androidgpt.ui.dialogs;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.andromou.chatgpt.androidgpt.databinding.CustomDialogBinding;

public class CustomDialog extends Dialog implements View.OnClickListener {

    private final Context context;
    private CustomDialogBinding binding;

    SharedPreferences sharedPreferences;

    public CustomDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = CustomDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.dialogButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == binding.dialogButton.getId()) {
            // Handle the button click, for example, get the text from the EditText
            String enteredText = binding.editText.getText().toString();
            sharedPreferences = context.getSharedPreferences("app", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("sentpassword", enteredText);
            editor.apply();
            // Do something with the entered text
            // ...
            if (sharedPreferences.getString("sentpassword", "").equals("ChatGPTBasedAppFreeLancer")) {
                dismiss(); // Close the dialog
            }
        }
    }
}
