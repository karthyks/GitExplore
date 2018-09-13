package com.github.karthyks.gitexplore.frameworks;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.karthyks.gitexplore.R;

public class CustomAlertDialog {

    private AlertDialog alertDialog;

    private CustomAlertDialog(final Builder builder) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(builder.activity)
                .setMessage(builder.message);
        if (builder.positiveButtonText != null && !builder.positiveButtonText.isEmpty()) {
            alertDialogBuilder.setPositiveButton(builder.positiveButtonText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (builder.listener != null) {
                                builder.listener.onCustomAlertDialogResult(builder.requestCode, true);
                            }
                        }
                    });
        }
        alertDialogBuilder.setCancelable(builder.cancelable);
        if (builder.negativeButtonText != null && !builder.negativeButtonText.isEmpty()) {
            alertDialogBuilder.setNegativeButton(builder.negativeButtonText,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (builder.listener != null) {
                                builder.listener.onCustomAlertDialogResult(builder.requestCode, false);
                            }
                        }
                    });
        }
        if (builder.title != null && !builder.title.isEmpty()) {
            View view = builder.activity.getLayoutInflater().inflate(R.layout.custom_dialog_header,
                    null, false);
            TextView tvHeaderView = view.findViewById(R.id.tv_custom_dialog_header);
            tvHeaderView.setText(builder.title);
            alertDialogBuilder.setCustomTitle(view);
        }
        alertDialog = alertDialogBuilder.create();
    }

    public void showAlertDialog() {
        if (alertDialog != null) {
            alertDialog.show();
        }
    }

    public void dismissAlertDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    public static class Builder {

        private int requestCode;
        private AppCompatActivity activity;
        private String title;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private IListener listener;
        private boolean cancelable = false;

        public Builder(AppCompatActivity activity) {
            this.activity = activity;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withPositiveButton(String positiveButtonText) {
            this.positiveButtonText = positiveButtonText;
            return this;
        }

        public Builder withNegativeButton(String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        public Builder withCallback(IListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public CustomAlertDialog build() {
            return new CustomAlertDialog(this);
        }
    }

    public interface IListener {
        void onCustomAlertDialogResult(int requestCode, boolean positive);
    }
}