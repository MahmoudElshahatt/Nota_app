package com.example.notaapp;

import androidx.appcompat.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;

public class SettingsActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.settings);
        RadioGroup radioGroup = findViewById(R.id.radio_group);

        SharedPreferences pref = getSharedPreferences("settings", MODE_PRIVATE);
        String code = pref.getString("code", "en");
        switch (code) {
            case "en":
                radioGroup.check(R.id.rd_english);
                break;
            case "ar":
                radioGroup.check(R.id.rd_arabic);
                break;
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rd_arabic:
                    saveLanguage("ar");
                    break;
                case R.id.rd_english:
                    saveLanguage("en");
                    break;
            }
        });

    }

    private void saveLanguage(String languageCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage(R.string.close_app)
                .setPositiveButton(R.string.language_dialog_positive, (dialog, which) -> {
                    SharedPreferences.Editor editor =
                            getSharedPreferences("settings", MODE_PRIVATE).edit();
                    editor.putString("code", languageCode);
                    editor.apply();
                    finishAffinity();
                })
                .setCancelable(false)
                .show();
    }

}