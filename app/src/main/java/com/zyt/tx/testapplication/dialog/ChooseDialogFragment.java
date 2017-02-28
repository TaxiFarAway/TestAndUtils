package com.zyt.tx.testapplication.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.zyt.tx.testapplication.R;

/**
 * Created by admin on 2017/2/11.
 */

public class ChooseDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setTitle("选择框")
                .setIcon(R.drawable.icon).setMessage("yes or no?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "yes", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "no", Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }
}
