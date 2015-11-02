package com.windyroad.nghia.common;

import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * Created by Nghia-PC on 8/25/2015.
 * Lớp kiểm tra View hợp lệ
 * Tự hiện lỗi
 */
public class ValidateUtil {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{7}";
    private static final String POSITIVE_NUMBER_REGEX = "^[0-9]{1,10}$";


    /**
     * Kiểm tra Email, tự set lỗi
     * @param editText
     * @param errorMessage
     * @return true: đúng email
     */
    public static boolean isEmailAddress(TextView editText, String errorMessage) {
        return isRegexValid(editText, EMAIL_REGEX, errorMessage);
    }

    /**
     * Kiểm tra Email, tự set lỗi
     * @param inputLayout
     * @param errorMessage
     * @return true: đúng email
     */
    public static boolean isEmailAddress(TextInputLayout inputLayout, String errorMessage) {
        return isRegexValid(inputLayout, EMAIL_REGEX, errorMessage);
    }


    /**
     * Kiểm tra Phone, tự set lỗi
     * @param textView
     * @param errorMessage
     * @return true: đúng phone
     */
    public static boolean isPhoneNumber(TextView textView, String errorMessage) {
        return isRegexValid(textView, PHONE_REGEX, errorMessage);
    }

    /**
     * Kiểm tra Phone, tự set lỗi
     * @param inputLayout
     * @param errorMessage
     * @return true: đúng phone
     */
    public static boolean isPhoneNumber(TextInputLayout inputLayout, String errorMessage) {
        return isRegexValid(inputLayout, PHONE_REGEX, errorMessage);
    }


    /**
     * Kiểu tra Regex, tự set lỗi
     * @param textView
     * @param regex
     * @param errorMessage
     * @return true: trùng regex
     */
    public static boolean isRegexValid(TextView textView, String regex, String errorMessage) {
        textView.setError(null);  // clear error

        String text = textView.getText().toString().trim();

        // pattern doesn't match so returning false
        if (!Pattern.matches(regex, text)) {
            textView.setError(errorMessage);
            return false;
        }

        return true;
    }

    /**
     * Kiểu tra Regex, tự set lỗi
     * @param inputLayout
     * @param regex
     * @param errorMessage
     * @return true: trùng regex
     */
    public static boolean isRegexValid(TextInputLayout inputLayout, String regex, String errorMessage) {
        // clear error
        inputLayout.setError(null);
        inputLayout.setErrorEnabled(false);

        if (!isRegexValid(inputLayout.getEditText(), regex, errorMessage)) {
            // KHông trùng Pattern => lỗi
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(errorMessage);

            return false;
        }

        return true;
    }


    /** kiểm tra có Text, tự set lỗi
     * True: nếu có Text*/
    public static boolean hasText(TextView textView, String errorMessage) {
        textView.setError(null);  // clear error

        String text = textView.getText().toString().trim();

        if (TextUtils.isEmpty(text)) {
            // Không có text
            textView.setError(errorMessage);
            return false;
        }

        return true;
    }

    /** kiểm tra có Text, tự set lỗi
     * True: nếu có Text*/
    public static boolean hasText(TextInputLayout inputLayout, String errorMessage) {
        // clear error
        inputLayout.setError("");
        inputLayout.setErrorEnabled(false);

        if (!hasText(inputLayout.getEditText(), errorMessage)){
            // Không có text => lỗi
            inputLayout.setErrorEnabled(true);
            inputLayout.setError(errorMessage);

            return false;
        }

        return true;
    }


    /**
     * Kiểm tra trùng
     * @param textView
     * @param editText2
     * @param errorMessage
     * @return true: nếu trùng
     */
    public static boolean isTextEquals(TextView textView, EditText editText2, String errorMessage) {
        // clear error
        textView.setError(null);
        editText2.setError(null);

        String text1 = textView.getText().toString().trim();
        String text2 = editText2.getText().toString().trim();

        if (!text1.equals(text2)) {
            // Không có text
            textView.setError(errorMessage);
            editText2.setError(errorMessage);

            return false;
        }

        return true;
    }

    /**
     * Kiểm tra trùng
     * @param inputLayout1
     * @param inputLayout2
     * @param errorMessage
     * @return true: nếu trùng
     */
    public static boolean isTextEquals(TextInputLayout inputLayout1, TextInputLayout inputLayout2, String errorMessage) {

        // clear error
        inputLayout1.setError(null);
        inputLayout2.setError(null);
        inputLayout1.setErrorEnabled(false);
        inputLayout2.setErrorEnabled(false);

        if (!isTextEquals(inputLayout1.getEditText(), inputLayout2.getEditText(), errorMessage)) {
            // Không có text
            inputLayout1.setErrorEnabled(true);
            inputLayout2.setErrorEnabled(true);
            inputLayout1.setError(errorMessage);
            inputLayout2.setError(errorMessage);

            return false;
        }

        return true;
    }

    public static boolean isPositiveNumber(TextInputLayout inputLayout, String errorMessage) {
        return isRegexValid(inputLayout, POSITIVE_NUMBER_REGEX, errorMessage);
    }

    public static boolean isPositiveNumber(TextView textView, String errorMessage) {
        return isRegexValid(textView, POSITIVE_NUMBER_REGEX, errorMessage);
    }

    // TODO: 10/26/2015 new
    public static boolean hasText(String text) {
        return !TextUtils.isEmpty(text);
    }

    public static boolean isRegexValid(String text, String regex){
        return Pattern.matches(regex, text);
    }

    public static boolean isTextEquals(String... texts){
        for (int i = 1; i < texts.length; i++){
            if (!texts[i].equals(texts[i-1])){
                return false;
            }
        }
        return true;
    }

    public static boolean isPositiveNumber(String text) {
        return isRegexValid(text, POSITIVE_NUMBER_REGEX);
    }
}
