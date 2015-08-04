package com.kenturf.signalcapture;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;


/**
 * Created by reegan on 29-Jun-15.
 * Kenturf Technology Solution
 */
public class CustomDrawing extends DialogFragment implements View.OnClickListener {

    Context context;
    private DoodleView doodleView;
    private Button btnDrawingClear,btnDrawingSave;

    public CustomDrawing() {
        this.context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_create_custom_marker,container,false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        btnDrawingClear = (Button)view.findViewById(R.id.btnDrawingClear);
        btnDrawingSave = (Button)view.findViewById(R.id.btnDrawingSave);
        doodleView = (DoodleView)view.findViewById(R.id.doodleView);
//        doodleView.setAlpha(127);
        btnDrawingClear.setOnClickListener(this);
        btnDrawingSave.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDrawingClear:
                getDoodleView().clear();
                break;
            case R.id.btnDrawingSave:
                getDoodleView().saveImage();
                getDialog().dismiss();
                break;
        }
    }

    // returns the DoodleView
    public DoodleView getDoodleView()
    {
        return doodleView;
    }

}
