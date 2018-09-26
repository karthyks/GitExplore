package com.github.karthyks.gitexplore.frameworks;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

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

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
