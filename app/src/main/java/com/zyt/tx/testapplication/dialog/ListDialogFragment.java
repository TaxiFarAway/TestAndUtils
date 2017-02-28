package com.zyt.tx.testapplication.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

/**
 * Created by admin on 2017/2/11.
 */

public class ListDialogFragment extends DialogFragment {


    private String[] fruits = {"水果", "苹果", "梨子"};

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("我的神").setItems(fruits, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "which=" + which, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });
        return builder.create();
    }
}
