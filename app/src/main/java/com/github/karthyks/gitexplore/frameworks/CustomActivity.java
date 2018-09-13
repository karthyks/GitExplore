package com.github.karthyks.gitexplore.frameworks;

import android.support.v7.app.AppCompatActivity;

public abstract class CustomActivity extends AppCompatActivity {

    private CustomAlertDialog alertDialog;

    public void showProgress(String message) {
        alertDialog = new CustomAlertDialog.Builder(this)
                .withMessage(message)
                .setCancelable(false)
                .build();
        alertDialog.showAlertDialog();
    }


    public void dismissProgress() {
        if (alertDialog != null) {
            alertDialog.dismissAlertDialog();
        }
    }
}
