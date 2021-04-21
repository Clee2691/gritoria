package edu.neu.madcourse.gritoria.bosses;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class FinishFightDialog extends AppCompatDialogFragment {
    public final String TAG = "LOOT";
    private String message;

    public FinishFightDialog(String message) {
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder finishFightBuilder = new AlertDialog.Builder(getActivity());
        finishFightBuilder.setTitle("Boss Killed!").setMessage(message).
                setPositiveButton("OK", (dialog, which) -> getDialog().dismiss());
        return finishFightBuilder.create();
    }
}
