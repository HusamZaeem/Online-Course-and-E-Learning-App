package com.example.onlinecourseande_learningapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class OtpTextWatcher implements TextWatcher {
    private final EditText currentEditText;
    private final EditText nextEditText;

    public OtpTextWatcher(EditText currentEditText, EditText nextEditText) {
        this.currentEditText = currentEditText;
        this.nextEditText = nextEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 1 && nextEditText != null) {
            nextEditText.requestFocus();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}