package com.kenturf.signalcapture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by reegan on 19-May-15.
 * Kenturf Technology Solution
 */
public class ResultOfBuildingList extends ActionBarActivity {
    GridView listImage;
    ArrayList<String> buildingList = new ArrayList<String>();// list of file paths
    File[] listFile;
    public String buildingName;
    DBClass dbClass;
    Context ctx = this;
    static Bitmap bitmap;
    Bitmap locationBitmap;
    ArrayAdapter<String> arrayAdapter;
    ListView listBuildingName;
    int i,j;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_building_list);
        setTitle("Building List");
        listBuildingName = (ListView)findViewById(R.id.lst_ResultBuilding);
        dbClass = new DBClass(ctx);
        dbClass.open();
        Cursor readBuildingInfo = dbClass.readBuilding();
        if (readBuildingInfo != null && readBuildingInfo.moveToFirst()){
            j = readBuildingInfo.getCount();
            for (i = 0; i <= j;readBuildingInfo.moveToNext()){
                buildingName = readBuildingInfo.getString(i);
                String bName = buildingName.substring(buildingName.lastIndexOf("/")+1);
                ParameterGetSet.image_Name = bName;
                String buildingAllList = bName.replaceAll("^.*\\.(.*)$", "$1");
                buildingList.add(buildingAllList);
                buildingName = "";
                i++;
            }
            ParameterGetSet.fileResultPath = buildingList;
            arrayAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    buildingList );
        }
        listBuildingName.setAdapter(arrayAdapter);
        listBuildingName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ResultViewBuilding.class);
                i.putExtra("id", position);
                startActivity(i);

            }
        });

    }
}
