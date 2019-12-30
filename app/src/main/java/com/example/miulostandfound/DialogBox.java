package com.example.miulostandfound;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogBox extends AppCompatDialogFragment {
    int position;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(getActivity());
        alertbox.setMessage("Are you sure you want to Delete post ? ");
        alertbox.setTitle("Alert");
        alertbox.setPositiveButton("Delete ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PostAdapter ps = PostAdapter.instance;
                ps.deleteItem(position);
            }
        });

        alertbox.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {

                    }
                });
        return alertbox.show();
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
