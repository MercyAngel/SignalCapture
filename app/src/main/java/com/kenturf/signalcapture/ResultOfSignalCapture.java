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
public class ResultOfSignalCapture extends ActionBarActivity {
    GridView listImage;
    ArrayList<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;
    public String imgPath;
    DBClass dbClass;
    Context ctx = this;
    static Bitmap bitmap;
    Bitmap locationBitmap;
    ArrayAdapter<String> arrayAdapter;
    ListView listResultImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_resultsignalcapture);
        setTitle(R.string.resultfloorname);
        dbClass = new DBClass(ctx);
        dbClass.open();
        getFromSdcard();
        Intent intent = this.getIntent();
        imgPath = getIntent().getStringExtra(MainActivity.SELECTED_IMG_PATH);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pinimgnew);
        locationBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.location);
//        listImage = (GridView)findViewById(R.id.result_image);
        listResultImage = (ListView)findViewById(R.id.lst_Result);
//        listImage.setAdapter(imageAdapter);

        listResultImage.setAdapter(arrayAdapter);
        listResultImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ResultView.class);
                i.putExtra("id", position);
                startActivity(i);

            }
        });

    }


    public void getFromSdcard()
    {
        File file= new File(android.os.Environment.getExternalStorageDirectory(),"mySignalCapture");
    try {
        if (file.isDirectory())
        {
            listFile = file.listFiles();
            for (File resultPath : listFile) {
                String folderResult = resultPath.toString();
                String subfolderResult = folderResult.substring(folderResult.lastIndexOf("/") + 1);
                Cursor readResultImgName = dbClass.checkImage(subfolderResult);
                if (readResultImgName != null && readResultImgName.moveToFirst()) {
                    String imageName = readResultImgName.getString(0);
                    if (imageName.equals(subfolderResult)) {
                        String sortName = subfolderResult;
                        f.add(sortName);
                    } else {
                        Message.message(getBaseContext(), "No Record Found", Toast.LENGTH_SHORT);
                    }
                    ParameterGetSet.fileResultPath = f;
                    readResultImgName.close();
                }
            }
        }
        dbClass.close();
        arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                f );

    }catch (Exception e){
        e.printStackTrace();
    }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);

    }

}
