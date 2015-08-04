package com.kenturf.signalcapture;

import android.app.*;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.*;
import android.widget.*;

/**
 * Created by DELL on 29-May-15.
 */
public class ResultViewBuilding extends DialogFragment {
    DBClass dbClass;
    Cursor readBuildingInfo;
    Intent eachBuilding;
    TextView buildingName, address1, address2, city, state, zipcode;
    String imageName, imageFromLoadName,buildingInfoNew;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_building_info, container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        buildingName = (TextView) v.findViewById(R.id.txtBuildingName);
        address1 = (TextView) v.findViewById(R.id.txtAddr1);
        address2 = (TextView) v.findViewById(R.id.txtAddr2);
        city = (TextView) v.findViewById(R.id.txtCity);
        state = (TextView) v.findViewById(R.id.txtState);
        zipcode = (TextView) v.findViewById(R.id.txtZip);
        dbClass = new DBClass(getActivity());
        dbClass.open();
        Cursor readData = dbClass.checkImage(ParameterGetSet.image_Name);
        readData.moveToFirst();
        if (Helper.imageFrom.equals("PathLoadImage")) {
            if (readData.getCount() == 0){
                buildingInfoNew = ParameterGetSet.building_name;
                imageName = "";
                buildingName.setText(ParameterGetSet.building_name);
                address1.setText(ParameterGetSet.Address1);
                address2.setText(ParameterGetSet.Address2);
                city.setText(ParameterGetSet.City);
                state.setText(ParameterGetSet.State);
                zipcode.setText(ParameterGetSet.ZipCode);
            }else {
                imageName = ParameterGetSet.image_Name;
                imageFromLoadName = imageName.substring(imageName.lastIndexOf("/") + 1);
            }
        } else if (Helper.imageFrom.equals("ResultPage")) {
            imageName = Helper.imageNameResult;
            imageFromLoadName = imageName.substring(imageName.lastIndexOf("/") + 1);
        }
        readBuildingInfo = dbClass.readBuildingInfo(imageFromLoadName);
        if (readBuildingInfo != null && readBuildingInfo.moveToFirst()) {
            String buildName = readBuildingInfo.getString(0);
            String bName = buildName.substring(buildName.lastIndexOf("/") + 1);
            ParameterGetSet.image_Name = bName;
            String buildingAllList = bName.substring(0, bName.lastIndexOf("."));
            String add1 = readBuildingInfo.getString(1);
            String addr2 = readBuildingInfo.getString(2);
            String cityDetail = readBuildingInfo.getString(3);
            String stateDetail = readBuildingInfo.getString(4);
            String zip = readBuildingInfo.getString(5);
            buildingName.setText(buildingAllList);
            address1.setText(add1);
            address2.setText(addr2);
            city.setText(cityDetail);
            state.setText(stateDetail);
            zipcode.setText(zip);
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_building_info);
        setTitle("Building Info");
        buildingName = (TextView)findViewById(R.id.txtBuildingName);
        address1 = (TextView)findViewById(R.id.txtAddr1);
        address2 = (TextView)findViewById(R.id.txtAddr2);
        city = (TextView)findViewById(R.id.txtCity);
        state = (TextView)findViewById(R.id.txtState);
        zipcode = (TextView)findViewById(R.id.txtZip);
        dbClass = new DBClass(getActivity());
        *//*eachBuilding = getIntent();
        int position = eachBuilding.getExtras().getInt("id");
        String s= ParameterGetSet.fileResultPath.get(position);*//*
        dbClass.open();

        if (Helper.imageFrom.equals("PathLoadImage")){
            imageName = ParameterGetSet.image_Name;
            imageFromLoadName = imageName.substring(imageName.lastIndexOf("/")+1);
        }else if (Helper.imageFrom.equals("ResultPage")){
            imageName = Helper.imageNameResult;
            imageFromLoadName = imageName.substring(imageName.lastIndexOf("/")+1);
        }
        readBuildingInfo = dbClass.readBuildingInfo(imageFromLoadName);
        if (readBuildingInfo != null && readBuildingInfo.moveToFirst()){
            String buildName = readBuildingInfo.getString(0);
            String bName = buildName.substring(buildName.lastIndexOf("/")+1);
            ParameterGetSet.image_Name = bName;
            String buildingAllList = bName.substring(0, bName.lastIndexOf("."));
            String add1 = readBuildingInfo.getString(1);
            String addr2 = readBuildingInfo.getString(2);
            String cityDetail = readBuildingInfo.getString(3);
            String stateDetail = readBuildingInfo.getString(4);
            String zip = readBuildingInfo.getString(5);
                buildingName.setText(buildingAllList);
                address1.setText(add1);
                address2.setText(addr2);
                city.setText(cityDetail);
                state.setText(stateDetail);
                zipcode.setText(zip);
        }

    }
}

*/