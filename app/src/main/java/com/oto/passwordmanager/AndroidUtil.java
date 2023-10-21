package com.oto.passwordmanager;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class AndroidUtil {
    public final static String field = "Field should not empty";
    public final static String fieldMatch = "Field should match";
    public final static String passwordLength = "Length should up to 6 characters";
    public static void fieldEmptyOrNot(EditText editText, String text) {
        String editT = editText.getText().toString();
        if (editT.isEmpty())
            editText.setHint(text);
        else
            editText.setHint("");
    }
    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}