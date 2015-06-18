package com.lesso.data.ui;

import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 输入框删除view
 * Created by meisl on 2015/6/18.
 */
public class EditTextHolder implements View.OnFocusChangeListener, TextView.OnEditorActionListener {

    /**
     * EditText 输入框
     */
    private EditText mEditText;
    private EditTextListener editTextListener;

    public interface EditTextListener {
        void onEditTextFocusChange(View v, boolean hasFocus);

        boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent);
    }

    public EditTextHolder(EditText editText, EditTextListener listener) {
        mEditText = editText;
        editTextListener = listener;
        mEditText.setOnFocusChangeListener(this);
        mEditText.setOnEditorActionListener(this);
    }

    boolean isHasFocus = false;

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        isHasFocus = hasFocus;
        if (editTextListener != null) {
            editTextListener.onEditTextFocusChange(v, hasFocus);
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (editTextListener != null) {
            editTextListener.onEditorAction(textView, i, keyEvent);
        }
        return false;
    }

}
