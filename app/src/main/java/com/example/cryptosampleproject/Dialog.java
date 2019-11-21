package com.example.cryptosampleproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

public class Dialog extends AppCompatDialogFragment {
    private EditText baseCurrency;
    private DialogListener listener;
    public static final int REQUEST_CODE = 10;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_dialog,null);
        baseCurrency = view.findViewById(R.id.get_code);
        baseCurrency.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(5)});
        builder.setView(view)
                .setTitle("Add new currency")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String base = baseCurrency.getText().toString().toLowerCase();
                        if(base.length()!=0){
                            sendResult(REQUEST_CODE, base);
                        }
                    }
                });



        return builder.create();
    }
    public interface DialogListener{
        void applyBaseCurrency(String base);
    }

    public void sendResult(int requestCode, String value){
        Intent intent = new Intent();
        intent.putExtra("currency_code",value);
        getTargetFragment().onActivityResult(requestCode, Activity.RESULT_OK, intent);
    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            listener = (DialogListener) context;
//        }
//        catch (Exception e){
//            throw new ClassCastException(context.toString()+
//                    "must implement dialog listener!");
//        }
//    }
}
