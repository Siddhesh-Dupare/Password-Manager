package com.oto.passwordmanager;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class AndroidUtil {
    private static Executor executor;
    private static BiometricPrompt biometricPrompt;
    private static BiometricPrompt.PromptInfo promptInfo;
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
    public static void coloringTextView(TextView textView, int color, int number) {
        String str = textView.getText().toString();
        Spannable spannable = new SpannableString(str);
        spannable.setSpan(new ForegroundColorSpan(color), str.length() - number, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
    }
    public static void biometricManager(Context context, @NonNull FragmentActivity activity, String title, String subtitle, String negativeButtonText) {
        BiometricManager biometricManager = BiometricManager.from(context);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                AndroidUtil.toast(context, "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                AndroidUtil.toast(context, "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                break;
        }
        executor = ContextCompat.getMainExecutor(context);
        biometricPrompt = new BiometricPrompt(activity,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setNegativeButtonText(negativeButtonText)
                .build();
        biometricPrompt.authenticate(promptInfo);
    }
}