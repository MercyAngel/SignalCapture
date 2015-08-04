package com.kenturf.signalcapture;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.*;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.telephony.*;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.*;

/**
 * Created by DELL on 29-May-15.
 */
public class ResultView extends ActionBarActivity {
    RelativeLayout resultImage;
    String imgPath;
    BitmapDrawable drawable;
    Context ctx = this;
    String iImageName,floor_image_path;
    DBClass dbClasses=new DBClass(ctx);
    String xAxis,network_selection,location,signal_option;
    String yAxis;
    String pinNo;
    RelativeLayout.LayoutParams rparams,rtxtparams;
    static Bitmap bitmap;
    ArrayList<String> list;
    Cursor readDataDB;
    ImageView pinBtn;
    TextView txtLocation;
    HashMap<Integer, ArrayList<String>> pinDetailHash;
    Bitmap bitmapNetwork,locationBitmap,notePadBitmap;
    Button back,newImg;
    private static final int RESULT_LOAD_IMG = 1;
    public static final String SELECTED_IMG_PATH = "com.kenturf.signalcapture.IMAGE_PATH";
    /* For display data */
    String iuser_name,iNetwork_Selection,iProvider_Detail,iFrequency,iWiFimode,iBssi,iSignal_Strength,iSignal_Quality,iNetwork_Technology,iPinMark_Hint;
    String iNetworkSelectionMode,iPinId,iFloorPath;
    String iXValue;
    String iYValue;
    String igetNetworkTechnology,igetNetworkProviderName,igetNetworkCellId,ishowNetworkHint,ishowSignalStr,ishowChannelNo;
    /* Display stored data dialog */
    TextView getSetOperatorName,getSetMacAddress,getSetSignalStrength,getSetFloorHint,getSetWifiMode,getSetWifiFrequency;

    TextView getNetworkTechnology,getNetworkProviderName,getNetworkCellId,showNetworkHint,showSignalStr,showNetworkChannelNo,getSignalQuality;


    WifiManager wifiManager;
    WifiReceiver wifiRecevier;
    List<ScanResult> scanwifilist;
    WifiInfo wifiInfo;
    String availableWifiList[],channelNumber;
    int level,signalStrengthNetwork;
    public static final String WPA2 = "WPA2";
    public static final String WEP = "WEP";
    public static final String EAP = "EAP";
    public static final String OPEN = "Open";
    public TelephonyManager telephonyManager;
    SignalStrength ss;
    String getPValue,getPBSSI,CurrentNetworkType,operatorName,cellIdentity_int,wifiNetworkCode,cameraLocationPath;
    int btnId,getFrequency;
    TextView wifiOperatorName,wifiMacAddress,wifiSignalStr,wifiMode;
    TextView txtSignal,txtNetworkTech,txtProvider,txtNetworkSelectionHint,txtChannelNo;
    EditText setHint;
    String getHint,getNetworkHint;
    Button btnCancel;
    Button btnFitPin;
    Dialog dialog;
    myPhoneStateListener psListener;
    LinearLayout showLayout,showNetworkLayout,notePadLayout;

    /* For Network popup Display */
    TextView networkTechnology,netProviderName,networkCellId,networkSignalStr,networkChannelNo;
    TextView networkCellSignalQuality;
    EditText setNetworkHint,editNotePad;

    AlertDialog.Builder alertDialogBox;

    TextView signalview,deviceName,cell_id,network_technology,providerName,txtPhoneType;

    /* Wifi and network Show layout */
    LinearLayout wifiShowLayout,networkShowLayout,notepadShowLayout,cameraLayout,cameraShowLayout,drawLayout;
    /* For Note pad */
    TextView showCmtType,showPopupNote;
    String iShowCmtType,iShowPopupNote,setUserName;
    int pinMarkid,x,y;
    TextView txt;
    ArrayList<String> setValues;
    int i;
    String signalQuality;
    ProgressDialog pdialog;
    Drawable newImageDraw;
    String pinDrawPath;
    TextView showCapturedLocation;
    ImageView showCapturedImage,showDrawedImage;
    BitmapDrawable newCapturedImage;
    String iShowCapturedLocation,igetCapturedPath,iuserName;
    EditText cameraLocation;
    Bitmap cameraBitmap;
    Dialog getPlanPartName;
    EditText editCamPicName;
    Button camCancel,camCapture;
    String strCamImgName;
    EditText editImageName;
    File mediaStorageDir,mediaFile;
    private static final int CAMERA_REQUEST = 100;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    ImageView btnCustomDraw;
    int psc,pci;
    String getOperator,bandNo,networktech;
    int LTErsrq,dBm,LTErsrp;
    TextView showTRSCP,showTId,showTEcno,showTUarfcn,showFRSRQ,showFId,showFRSRP,shoFeUarfcn;
    TextView showPRscp,showPCid,showPEcno,showPUarfcn;
    Bitmap btmp;
    public static int drawErase = 0;
    DrawOnFloorPlan drawOnFloorPlan;
    Button skecthSave,sketchErase;
    public Bitmap myBitmap;

    LinearLayout sketchLayout,pinLayout;
    Button wifiPin,networkPin,notepadPin,cameraPin,drawingPin,savePin;
    FrameLayout frameLayout_screenShot;
    int image_xvalue;
   int image_yvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_resaultview);
        setTitle(R.string.viewfloorname);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wifisignal);
        pinDetailHash = new HashMap<>();
        bitmapNetwork = BitmapFactory.decodeResource(getResources(),R.drawable.mobilesignal);
        locationBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.notes);
        notePadBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.notes);
        cameraBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.action_camera);
        resultImage = (RelativeLayout) findViewById(R.id.resulRl);

        // get intent data
        Intent getGV = getIntent();
        // Selected image id
        int position = getGV.getExtras().getInt("id");
        String s= ParameterGetSet.fileResultPath.get(position);
//        Message.message(getBaseContext(),""+s, Toast.LENGTH_SHORT);
        imgPath = s;
        Helper.imageNameResult = imgPath;
        psListener = new myPhoneStateListener();
        dbClasses.open();
        readDataDB = dbClasses.readImageName(imgPath);
        int count = readDataDB.getCount();
        readDataDB.moveToFirst();
        iImageName = readDataDB.getString(0);


        try {
            if (iImageName.equals(imgPath)) {
                iFloorPath = readDataDB.getString(14);
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                Bitmap btmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(iFloorPath),size.x,size.y,true);
                ParameterGetSet.floorPlanBitmap = btmp;
                newImageDraw = new BitmapDrawable(btmp);
                resultImage.setBackground(newImageDraw);
                image_yvalue = resultImage.getHeight();
                image_xvalue = resultImage.getWidth();

                ParameterGetSet.image_xValue = image_xvalue;
                ParameterGetSet.image_yValue = image_yvalue;

                if (readDataDB != null) {
                    do {
                        list = new ArrayList<String>();
                        iuserName = readDataDB.getString(17);
                        xAxis = readDataDB.getString(11);
                        if (xAxis == null){
                            readDataDB.moveToNext();
                        }else
                        iuserName = readDataDB.getString(17);
                        yAxis = readDataDB.getString(12);
                        xAxis = readDataDB.getString(11);
                        pinNo = readDataDB.getString(2);
                        location = readDataDB.getString(10);
                        signal_option = readDataDB.getString(1);
                        cameraLocationPath = readDataDB.getString(15);
                        /*list.add(xAxis);
                        list.add(yAxis);
                        list.add(pinNo);
                        list.add(signal_option);
                        list.add(location);
                        pinDetailHash.put(pinid,list);*/
                        rparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        rtxtparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        rparams.addRule(RelativeLayout.ACCESSIBILITY_LIVE_REGION_ASSERTIVE);
                        rtxtparams.addRule(RelativeLayout.ACCESSIBILITY_LIVE_REGION_ASSERTIVE);
                        rparams.leftMargin = Integer.parseInt(String.valueOf(xAxis));
                        rparams.topMargin = Integer.parseInt(String.valueOf(yAxis));
                        rtxtparams.leftMargin = Integer.parseInt(String.valueOf(xAxis));
                        rtxtparams.topMargin = Integer.parseInt(String.valueOf(yAxis));
                        rparams.height = 48;
                        rparams.width = 60;
                        pinBtn = new ImageView(getBaseContext());
                        txtLocation = new TextView(getBaseContext());
                        pinBtn = new ImageView(getBaseContext());
                        int pinID = Integer.parseInt(pinNo);
                        pinBtn.setId(pinID);
                        txtLocation.setId(pinID);
//                        txtLocation.setText(location);
                        txtLocation.setTextAppearance(getBaseContext(), R.style.txtViewStyle);
                        txtLocation.setTextColor(Color.BLACK);
                        pinBtn.setLayoutParams(rparams);
                        txtLocation.setLayoutParams(rtxtparams);
                        if(signal_option == null){
                            readDataDB.moveToNext();
                        }else {
                            switch (signal_option) {
                                case "Wifi":
                                    pinBtn.setImageBitmap(bitmap);
                                    break;
                                case "Network":
                                    pinBtn.setImageBitmap(bitmapNetwork);
                                    break;
                                case "Notepad":
                                    pinBtn.setImageBitmap(notePadBitmap);
                                    break;
                                case "Camera":
                                    pinBtn.setImageBitmap(cameraBitmap);
                                    break;
                                case "Drawing":

                                    pinDrawPath = cameraLocationPath;
                                    Bitmap mybitmap = BitmapFactory.decodeFile(pinDrawPath);
                                    pinBtn.setImageBitmap(mybitmap);
                                    pinBtn.setId(pinID);
                                    pinBtn.setLayoutParams(rparams);
                            }
                        }
                        resultImage.addView(pinBtn);
                        resultImage.addView(txtLocation);

                        pinBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pinMarkid = v.getId();
//                            Dialog showStoredData = new Dialog(ResultView.this);
                                Dialog showStoredData = new Dialog(ResultView.this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth);
                                showStoredData.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                showStoredData.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                showStoredData.setContentView(R.layout.dialog_display_stored_data);

                /* For title */
                                TextView changeTitle = (TextView) showStoredData.findViewById(R.id.changeTitle);

                                wifiShowLayout = (LinearLayout) showStoredData.findViewById(R.id.wifiShowLayout);
                                networkShowLayout = (LinearLayout) showStoredData.findViewById(R.id.networkShowLayout);
                                notepadShowLayout = (LinearLayout) showStoredData.findViewById(R.id.popupShowLayout);
                                drawLayout = (LinearLayout) showStoredData.findViewById(R.id.showPopupDraw);


                /* For wifi */
                                getSetOperatorName = (TextView) showStoredData.findViewById(R.id.setOperatorName);
                                getSetMacAddress = (TextView) showStoredData.findViewById(R.id.setMacAddress);
                                getSetSignalStrength = (TextView) showStoredData.findViewById(R.id.setSignalStr);
                                getSetWifiMode = (TextView) showStoredData.findViewById(R.id.setWifiMode);
                                getSetFloorHint = (TextView) showStoredData.findViewById(R.id.setFloorHint);
                                getSetWifiFrequency = (TextView) showStoredData.findViewById(R.id.setWifiFrequency);

                /* For Network */
                                getNetworkTechnology = (TextView) showStoredData.findViewById(R.id.getNetworkTechnology);
                                getNetworkProviderName = (TextView) showStoredData.findViewById(R.id.getNetworkProviderName);
                                getNetworkCellId = (TextView) showStoredData.findViewById(R.id.getNetworkCellId);
                                showNetworkHint = (TextView) showStoredData.findViewById(R.id.showNetworkHint);
                                showSignalStr = (TextView) showStoredData.findViewById(R.id.showSignalStr);
                                getSignalQuality = (TextView) showStoredData.findViewById(R.id.showSignalQuality);
                                showNetworkChannelNo = (TextView)showStoredData.findViewById(R.id.showChannelNo);

                /* For NotePad*/
                                showCmtType = (TextView) showStoredData.findViewById(R.id.showCmtType);
                                showPopupNote = (TextView) showStoredData.findViewById(R.id.showPopupNote);
                                showCapturedImage = (ImageView) showStoredData.findViewById(R.id.imgCameraCapture);
                                cameraShowLayout = (LinearLayout) showStoredData.findViewById(R.id.popupCameraShowLayout);

                /*For Draw Pin*/
                                showDrawedImage = (ImageView) showStoredData.findViewById(R.id.imgDraw);

                                showPRscp = (TextView) showStoredData.findViewById(R.id.cellSignalStrength);
                                showPCid = (TextView) showStoredData.findViewById(R.id.cellCellId);
                                showPEcno = (TextView) showStoredData.findViewById(R.id.show_cellSignalQuality);
                                showPUarfcn = (TextView) showStoredData.findViewById(R.id.show_cellChannelNo);

//                                pinid = v.getId();
//                                network_selection = setValues.get(0);
                                getImageNameNetworkModeDB();

            /*network_selection = setValues.get(0);
            getImageNameNetworkModeDB();*/

                                switch (network_selection) {
                                    case "Wifi":
                                        changeTitle.setText("Wifi Network");
                                        wifiShowLayout.setVisibility(View.VISIBLE);
                                        break;
                                    case "Network":
                                        try {
                                            /*if (networktech.equals("3G")){
                                                showPRscp.setText(R.string.networkSignalStrRSCP);
                                                showPCid.setText(R.string.networkCellIdentifierPsc);
                                                showPEcno.setText(R.string.networkSignalQualityEcno);
                                                showPUarfcn.setText(R.string.networkChannelNumberUarfcn);
                                            } else if (networktech.equals("4G")){
                                                showPRscp.setText(R.string.networkSignalStrRSRP);
                                                showPCid.setText(R.string.networkCellIdentifierPCI);
                                                showPEcno.setText(R.string.networkSignalQualityRSRQ);
                                                showPUarfcn.setText(R.string.networkChannelNumbereUARFCN);
                                            }*/
                                            changeTitle.setText("Mobile Network");
                                            networkShowLayout.setVisibility(View.VISIBLE);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case "Notepad":
                                        changeTitle.setText("NotePad");
                                        notepadShowLayout.setVisibility(View.VISIBLE);
                                        break;
                                    case "Camera":
                                        try {
                                            changeTitle.setText("Camera");
                                            cameraShowLayout.setVisibility(View.VISIBLE);
                                            Display display = getWindowManager().getDefaultDisplay();
                                            Point size = new Point();
                                            display.getSize(size);
                                            Bitmap btmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(igetCapturedPath), size.x, size.y, true);
                                            Drawable dBnewCapturedImage = new BitmapDrawable(getResources(), btmp);
                                            showCapturedImage.setImageDrawable(dBnewCapturedImage);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case "Drawing":
                                        try {
                                            changeTitle.setText("Drawing");
                                            drawLayout.setVisibility(View.VISIBLE);
                                            Display display = getWindowManager().getDefaultDisplay();
                                            Point size = new Point();
                                            display.getSize(size);
                                            Bitmap btmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(igetCapturedPath), size.x, size.y, true);
                                            Drawable dBnewCapturedImage = new BitmapDrawable(getResources(), btmp);
                                            showDrawedImage.setImageDrawable(dBnewCapturedImage);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                }

                                if (!"".equals(network_selection)) {
                /* For wifi */
                                    getSetOperatorName.setText(iProvider_Detail);
                                    getSetMacAddress.setText(iBssi);
                                    getSetSignalStrength.setText(iSignal_Strength + "dBm");
                                    getSetFloorHint.setText(iPinMark_Hint);
                                    getSetWifiMode.setText(iWiFimode);
                                    getSetWifiFrequency.setText(iFrequency + " MHz");



                /* For network */
                                    getNetworkTechnology.setText(igetNetworkTechnology);
                                    getNetworkProviderName.setText(igetNetworkProviderName);
                                    getNetworkCellId.setText(igetNetworkCellId);
                                    showSignalStr.setText(ishowSignalStr + "dBm");
                                    showNetworkHint.setText(ishowNetworkHint);
                                    getSignalQuality.setText(iSignal_Quality);
                                    showNetworkChannelNo.setText(ishowChannelNo);

                /* For Note Pad */
                                    showCmtType.setText(iShowCmtType);
                                    showPopupNote.setText(iShowPopupNote);


                                    showStoredData.show();


                                    //                    Message.message(getBaseContext(), "" + iPinId + "\n" + iXValue + "\n" + iYValue + "\n" + network_selection, Toast.LENGTH_LONG);

                                    iNetworkSelectionMode = "";
                                    iuser_name = "";
                                    //                            iNetwork_Selection = "";
                                    iProvider_Detail = "";
                                    iFrequency = "";
                                    iWiFimode = "";
                                    iBssi = "";
                                    iSignal_Strength = "";
                                    iSignal_Quality = "";
                                    iNetwork_Technology = "";
                                    iPinMark_Hint = "";

    /* For network */

                                    igetNetworkTechnology = "";
                                    igetNetworkProviderName = "";
                                    igetNetworkCellId = "";
                                    ishowNetworkHint = "";
                                    ishowSignalStr = "";


    /* For Popup */

                                    iShowCmtType = "";
                                    iShowPopupNote = "";
                                    network_selection.equals("");

                                }
                            }
                        });
                        pinBtn.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(final View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResultView.this)
                                        .setMessage("Are you Sure Want to Delect the Pointer?")
                                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                try {
                                                    int marker_id = v.getId();
                                                    if (marker_id != 0) {
                                                        v.setVisibility(View.GONE);

//                                                        View txtV = new View(getBaseContext());

                                                       /* View viewId = txtLocation.findViewById(marker_id);
                                                        ViewGroup txtGroup = (ViewGroup)viewId.getParent();
                                                        int index = txtGroup.indexOfChild(viewId);
                                                        txtGroup.removeView(viewId);*/

                                                       /* txtLocation.findViewById(marker_id).setVisibility(View.GONE);
                                                        txtLocation.setVisibility(View.GONE);*/
//                                                        viewId.setVisibility(View.GONE);
                                                        resultImage.removeView(v);
                                                    }
                                                    dbClasses.open();
                                                    boolean value = dbClasses.deleteRemovePin(iFloorPath, String.valueOf(marker_id));
                                                    if (value) {
                                                        View myView = findViewById(marker_id);
                                                        ViewGroup parent = (ViewGroup) myView.getParent();
                                                        parent.removeView(myView);
                                                        View viewId = txtLocation.findViewById(marker_id);
                                                        ViewGroup txtGroup = (ViewGroup)viewId.getParent();
                                                        txtGroup.removeView(viewId);
                                                    }
                                                    dbClasses.close();
                                                } catch (Exception resEx) {
                                                    resEx.printStackTrace();
                                                }

                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                builder.show();
                                return true;
                            }

                        });
                    } while (readDataDB.moveToNext());
                            readDataDB.moveToLast();
                    pinNo = readDataDB.getString(2);
                    i = Integer.parseInt(pinNo);
                    if(i==0){
                        i++;
                    }
                    i++;
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        wifiPin = (Button)findViewById(R.id.wifiOperators);
        networkPin = (Button)findViewById(R.id.networkOperators);
        notepadPin  = (Button)findViewById(R.id.notepad);
        cameraPin = (Button)findViewById(R.id.cameraDialog);
        drawingPin = (Button)findViewById(R.id.drawing);
        savePin = (Button)findViewById(R.id.save);
        pinLayout = (LinearLayout)findViewById(R.id.pinLayout);

        skecthSave = (Button)findViewById(R.id.btnStickSave);
        sketchErase = (Button)findViewById(R.id.btnStickErase);

        wifiPin.setVisibility(View.VISIBLE);
        networkPin.setVisibility(View.VISIBLE);
        notepadPin.setVisibility(View.VISIBLE);
        cameraPin.setVisibility(View.VISIBLE);
        drawingPin.setVisibility(View.VISIBLE);
        skecthSave.setVisibility(View.INVISIBLE);
        sketchErase.setVisibility(View.INVISIBLE);

        wifiPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    iNetwork_Selection = "Wifi";
                    wifiManager.setWifiEnabled(true);

                    dialog = new Dialog(ResultView.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_wifi_store);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                    wifiOperatorName = (TextView) dialog.findViewById(R.id.wifiOperatorName);
                    wifiMacAddress = (TextView) dialog.findViewById(R.id.wifiMacAddress);
                    wifiSignalStr = (TextView) dialog.findViewById(R.id.wifiSignalStr);
                    wifiMode = (TextView) dialog.findViewById(R.id.wifiMode);

                    setHint = (EditText) dialog.findViewById(R.id.getHintName);
                    scaning();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        networkPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    dialog = new Dialog(ResultView.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    dialog.setContentView(R.layout.dialog_network_store);

                    iNetwork_Selection = "Network";

                    txtPhoneType = (TextView) dialog.findViewById(R.id.txtPhoneType);
                    networkTechnology = (TextView) dialog.findViewById(R.id.networkTechnology);
                    netProviderName = (TextView) dialog.findViewById(R.id.netProviderName);
                    networkCellId = (TextView) dialog.findViewById(R.id.networkCellId);
                    networkCellSignalQuality = (TextView) dialog.findViewById(R.id.networkSignalQuality);
                    networkSignalStr = (TextView) dialog.findViewById(R.id.networkSignalStr);
                    networkChannelNo = (TextView) dialog.findViewById(R.id.networkChannelNo);

                    showFId = (TextView) dialog.findViewById(R.id.txtCellId);
                    showFRSRQ = (TextView) dialog.findViewById(R.id.cellSignalQuality);
                    showFRSRP = (TextView) dialog.findViewById(R.id.txtCellSignalStr);
                    shoFeUarfcn = (TextView) dialog.findViewById(R.id.cellChannelNo);

                    openNetworkPopup();
                    networktech = getTechnology(telephonyManager);
                    if (networktech.equals("3G")) {
                        showFRSRP.setText(R.string.networkSignalStrRSCP);
                        showFId.setText(R.string.networkCellIdentifierPsc);
                        showFRSRQ.setText(R.string.networkSignalQualityEcno);
                        shoFeUarfcn.setText(R.string.networkChannelNumberUarfcn);
                    } else if (networktech.equals("4G")) {
                        showFRSRP.setText(R.string.networkSignalStrRSRP);
                        showFId.setText(R.string.networkCellIdentifierPCI);
                        showFRSRQ.setText(R.string.networkSignalQualityRSRQ);
                        shoFeUarfcn.setText(R.string.networkChannelNumbereUARFCN);
                    }

                    getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        notepadPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    dialog = new Dialog(ResultView.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_wifiphone_networks);
                    dialog.setTitle(R.string.notePad);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                    iNetwork_Selection = "Notepad";
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        cameraPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    iNetwork_Selection = "Camera";
                    getPlanPartName = new Dialog(getBaseContext());
                    dialog = new Dialog(ResultView.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_camera_store);
                    dialog.setTitle(R.string.floorCapture);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri fileUri = getOutputMediaFileUri(CAMERA_REQUEST);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //set Image file name
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        drawingPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    drawOnFloorPlan = new DrawOnFloorPlan(getBaseContext());
                    resultImage.addView(drawOnFloorPlan);
                    wifiPin.setVisibility(View.INVISIBLE);
                    networkPin.setVisibility(View.INVISIBLE);
                    notepadPin.setVisibility(View.INVISIBLE);
                    cameraPin.setVisibility(View.INVISIBLE);
                    drawingPin.setVisibility(View.INVISIBLE);

                    sketchErase.setVisibility(View.VISIBLE);
                    skecthSave.setVisibility(View.VISIBLE);
//                    pinLayout.setVisibility(View.GONE);
                    iNetwork_Selection = "Draw";
                    dialog = new Dialog(ResultView.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_wifiphone_networks);
                    dialog.setTitle(R.string.notePad);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    DrawOnFloorPlan.DrawBitmap = null;

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        skecthSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    wifiPin.setVisibility(View.VISIBLE);
                    networkPin.setVisibility(View.VISIBLE);
                    notepadPin.setVisibility(View.VISIBLE);
                    cameraPin.setVisibility(View.VISIBLE);
                    drawingPin.setVisibility(View.VISIBLE);

                    sketchErase.setVisibility(View.INVISIBLE);
                    skecthSave.setVisibility(View.INVISIBLE);
                    DrawOnFloorPlan.mPaint = null;
                    dialog = new Dialog(ResultView.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_drawimage_paint);
                    iNetwork_Selection = "DrawSave";
                    btnFitPin = (Button)dialog.findViewById(R.id.btnFitPin);
                    btnCancel = (Button)dialog.findViewById(R.id.btnCancel);
                    dialog.show();
                    btnFitPin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {

                                combineImages(ParameterGetSet.floorPlanBitmap, ParameterGetSet.drawingBitmap);
                                DrawOnFloorPlan.DrawBitmap = null;
                                drawOnFloorPlan.clear();
                                resultImage.removeView(drawOnFloorPlan);
                                dbClasses.open();
                                dbClasses.updateColumn(ParameterGetSet.image_Name);
                                extractDB();
                                Display display = getWindowManager().getDefaultDisplay();
                                Point size = new Point();
                                display.getSize(size);
                                btmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(ParameterGetSet.floor_Image_Path), size.x, size.y, true);
                                ParameterGetSet.floorPlanBitmap = btmp;
                                Drawable newImageDraw = new BitmapDrawable(btmp);
                                resultImage.setBackground(newImageDraw);
                                image_yvalue = resultImage.getHeight();
                                image_xvalue = resultImage.getWidth();
                                ParameterGetSet.image_xValue = image_xvalue;
                                ParameterGetSet.image_yValue = image_yvalue;
                                dialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            DrawOnFloorPlan.DrawBitmap = null;
                            wifiPin.setVisibility(View.INVISIBLE);
                            networkPin.setVisibility(View.INVISIBLE);
                            notepadPin.setVisibility(View.INVISIBLE);
                            cameraPin.setVisibility(View.INVISIBLE);
                            drawingPin.setVisibility(View.VISIBLE);

                            sketchErase.setVisibility(View.VISIBLE);
                            skecthSave.setVisibility(View.VISIBLE);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        sketchErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawOnFloorPlan.clear();
                resultImage.removeView(drawOnFloorPlan);
                wifiPin.setVisibility(View.INVISIBLE);
                networkPin.setVisibility(View.INVISIBLE);
                notepadPin.setVisibility(View.INVISIBLE);
                cameraPin.setVisibility(View.INVISIBLE);
                drawingPin.setVisibility(View.VISIBLE);

                sketchErase.setVisibility(View.VISIBLE);
                skecthSave.setVisibility(View.VISIBLE);
            }
        });
/* End of Pined image display */


    /* for wifi */
    wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
    wifiInfo= wifiManager.getConnectionInfo();
    level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 20);
    resultImage.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            try {
//            if (!"".equals(network_selection)) {
                if (iNetwork_Selection != null) {
                    pinBtn = new ImageView(getBaseContext());
                    x = (int) event.getX();
                    y = (int) event.getY();
                    rparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    int xAxis = x;
                    int yAxis = y;
                    rparams.leftMargin = xAxis;
                    rparams.topMargin = yAxis;
                    rparams.height = 48;
                    rparams.width = 60;
                  /* for wifi */
                    showLayout = (LinearLayout) dialog.findViewById(R.id.displayData);

                    showNetworkLayout = (LinearLayout) dialog.findViewById(R.id.displayMobileData);
                    notePadLayout = (LinearLayout) dialog.findViewById(R.id.notePadLayout);
                    cameraLayout = (LinearLayout) dialog.findViewById(R.id.cameraLayout);

                    setNetworkHint = (EditText) dialog.findViewById(R.id.getNetworkHint);

                    editNotePad = (EditText) dialog.findViewById(R.id.editNotePad);
                    cameraLocation = (EditText) dialog.findViewById(R.id.cameraLocation);
                /* save and cancel btn */
                    btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                    btnFitPin = (Button) dialog.findViewById(R.id.btnFitPin);
                    switch (iNetwork_Selection) {
                        case "Wifi":

                            showLayout.setVisibility(View.VISIBLE);

                            wifiOperatorName.setText(getPValue);
                            wifiMacAddress.setText(getPBSSI);
                            wifiSignalStr.setText(String.valueOf(signalStrengthNetwork));
                            wifiMode.setText(wifiNetworkCode);

                            dialog.show();
                            saveSignalData();
                            break;
                        case "Network":
                            try {
                                showNetworkLayout.setVisibility(View.VISIBLE);
                                networktech = getTechnology(telephonyManager);

                                networkTechnology.setText(getTechnology(telephonyManager));
                                netProviderName.setText(operatorName);
                                networkCellId.setText(cellIdentity_int);
                                networkCellSignalQuality.setText(signalQuality);
                                networkSignalStr.setText(String.valueOf(psListener.signalStrengthValue));
                                txtPhoneType.setText(getDeviceName());
                                networkChannelNo.setText(String.valueOf(displayChannelNo()));

                                dialog.show();
                                saveSignalData();
                            } catch (Exception exNet) {
                                exNet.printStackTrace();
                            }

                            break;
                        case "Notepad":
                            try {
                                notePadLayout.setVisibility(View.VISIBLE);
                                dialog.show();
                                saveSignalData();
                            } catch (Exception exNot) {
                                exNot.printStackTrace();
                            }
                            break;
                        case "Camera":
                            try {
                                cameraLayout.setVisibility(View.VISIBLE);
                                dialog.show();
                                saveSignalData();
                            } catch (Exception ca) {
                                ca.printStackTrace();
                            }
                            break;
                        case "Drawing":
                            try {
                                pinDrawPath = ParameterGetSet.getCustomDrawingPath();
                                Bitmap mybitmap = BitmapFactory.decodeFile(pinDrawPath);
                                pinBtn.setImageBitmap(mybitmap);
                                pinBtn.setId(i);
                                pinBtn.setLayoutParams(rparams);
                                resultImage.addView(pinBtn);

                                setValues = new ArrayList<String>();
                                setValues.add("Drawing");
                                setValues.add(String.valueOf(pinBtn.getId()));
                                setValues.add(String.valueOf(xAxis));
                                setValues.add(String.valueOf(yAxis));
                                insertParam();
                                dbClasses.open();
                                dbClasses.insertImagePath();
                                dbClasses.close();


                            } catch (Exception dr) {
                                dr.printStackTrace();
                            }
                    }
                    if (iNetwork_Selection.equals("Drawing")) {
                        iNetwork_Selection = "";
                        iNetwork_Selection.equals("");
                        i++;
                    } else {
                    /* Click the Cancel Button */
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                iNetwork_Selection = "";
                                network_selection = "";
                                dialog.dismiss();
                            }
                        });
                    }

                    pinBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                        Message.message(getBaseContext(), "Pin Selected", Toast.LENGTH_SHORT);

                            Dialog showStoredData = new Dialog(ResultView.this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth);
                            showStoredData.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                            showStoredData.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            showStoredData.setContentView(R.layout.dialog_display_stored_data);

                /* For title */
                            TextView changeTitle = (TextView) showStoredData.findViewById(R.id.changeTitle);

                            wifiShowLayout = (LinearLayout) showStoredData.findViewById(R.id.wifiShowLayout);
                            networkShowLayout = (LinearLayout) showStoredData.findViewById(R.id.networkShowLayout);
                            notepadShowLayout = (LinearLayout) showStoredData.findViewById(R.id.popupShowLayout);
                            drawLayout = (LinearLayout) showStoredData.findViewById(R.id.showPopupDraw);

                /* For wifi */
                            getSetOperatorName = (TextView) showStoredData.findViewById(R.id.setOperatorName);
                            getSetMacAddress = (TextView) showStoredData.findViewById(R.id.setMacAddress);
                            getSetSignalStrength = (TextView) showStoredData.findViewById(R.id.setSignalStr);
                            getSetWifiMode = (TextView) showStoredData.findViewById(R.id.setWifiMode);
                            getSetFloorHint = (TextView) showStoredData.findViewById(R.id.setFloorHint);
                            getSetWifiFrequency = (TextView) showStoredData.findViewById(R.id.setWifiFrequency);

                /* For Network */
                            getNetworkTechnology = (TextView) showStoredData.findViewById(R.id.getNetworkTechnology);
                            getNetworkProviderName = (TextView) showStoredData.findViewById(R.id.getNetworkProviderName);
                            getNetworkCellId = (TextView) showStoredData.findViewById(R.id.getNetworkCellId);
                            showNetworkHint = (TextView) showStoredData.findViewById(R.id.showNetworkHint);
                            showSignalStr = (TextView) showStoredData.findViewById(R.id.showSignalStr);

                /* For NotePad*/
                            showCmtType = (TextView) showStoredData.findViewById(R.id.showCmtType);
                            showPopupNote = (TextView) showStoredData.findViewById(R.id.showPopupNote);


                    /*For Camera*/
                            showCapturedLocation = (TextView) showStoredData.findViewById(R.id.showPopupCamera);
                            showCapturedImage = (ImageView) showStoredData.findViewById(R.id.imgCameraCapture);
                            cameraShowLayout = (LinearLayout) showStoredData.findViewById(R.id.popupCameraShowLayout);


                /*For Draw Pin*/
                            showDrawedImage = (ImageView) showStoredData.findViewById(R.id.imgDraw);

                            pinMarkid = v.getId();
                            getImageNameNetworkModeDB();

            /*network_selection = setValues.get(0);
            getImageNameNetworkModeDB();*/

                            switch (network_selection) {
                                case "Wifi":
                                    changeTitle.setText("Wifi Network");
                                    wifiShowLayout.setVisibility(View.VISIBLE);
                                    break;
                                case "Network":
                                    try {
                                        if (networktech.equals("3G")) {
                                            showPRscp.setText(R.string.networkSignalStrRSCP);
                                            showPCid.setText(R.string.networkCellIdentifierPsc);
                                            showPEcno.setText(R.string.networkSignalQualityEcno);
                                            showPUarfcn.setText(R.string.networkChannelNumberUarfcn);
                                        } else if (networktech.equals("4G")) {
                                            showPRscp.setText(R.string.networkSignalStrRSRP);
                                            showPCid.setText(R.string.networkCellIdentifierPCI);
                                            showPEcno.setText(R.string.networkSignalQualityRSRQ);
                                            showPUarfcn.setText(R.string.networkChannelNumbereUARFCN);
                                        }
                                        changeTitle.setText("Mobile Network");
                                        networkShowLayout.setVisibility(View.VISIBLE);
                                    } catch (Exception net) {
                                        net.printStackTrace();
                                    }
                                    break;
                                case "Notepad":
                                    changeTitle.setText("NotePad");
                                    notepadShowLayout.setVisibility(View.VISIBLE);
                                    break;
                                case "Camera":
                                    try {
                                        changeTitle.setText("Camera");
                                        cameraShowLayout.setVisibility(View.VISIBLE);
                                        Display display = getWindowManager().getDefaultDisplay();
                                        Point size = new Point();
                                        display.getSize(size);
                                        Bitmap btmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(igetCapturedPath), size.x, size.y, true);
                                        newCapturedImage = new BitmapDrawable(btmp);
                                        showCapturedImage.setImageDrawable(newCapturedImage);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "Drawing":
                                    try {
                                        changeTitle.setText("Drawn");
                                        drawLayout.setVisibility(View.VISIBLE);
                                        Display display = getWindowManager().getDefaultDisplay();
                                        Point size = new Point();
                                        display.getSize(size);
                                        Bitmap btmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(igetCapturedPath), size.x, size.y, true);
                                        Drawable dBnewCapturedImage = new BitmapDrawable(getResources(), btmp);
                                        showDrawedImage.setImageDrawable(dBnewCapturedImage);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }

                /* For wifi */
                            getSetOperatorName.setText(iProvider_Detail);
                            getSetMacAddress.setText(iBssi);
                            getSetSignalStrength.setText(iSignal_Strength + "dBm");
                            getSetFloorHint.setText(iPinMark_Hint);
                            getSetWifiMode.setText(iWiFimode);
                            getSetWifiFrequency.setText(iFrequency + " MHz");



                /* For network */
                            getNetworkTechnology.setText(igetNetworkTechnology);
                            getNetworkProviderName.setText(igetNetworkProviderName);
                            getNetworkCellId.setText(igetNetworkCellId);
                            showSignalStr.setText(ishowSignalStr + "dBm");
                            showNetworkHint.setText(ishowNetworkHint);

                /* For Note Pad */
                            showCmtType.setText(iShowCmtType);
                            showPopupNote.setText(iShowPopupNote);

                            network_selection = "";
                            showStoredData.show();

                            iNetworkSelectionMode = "";
                            iuser_name = "";
                            //                            iNetwork_Selection = "";
                            iProvider_Detail = "";
                            iFrequency = "";
                            iWiFimode = "";
                            iBssi = "";
                            iSignal_Strength = "";
                            iSignal_Quality = "";
                            iNetwork_Technology = "";
                            iPinMark_Hint = "";

    /* For network */

                            igetNetworkTechnology = "";
                            igetNetworkProviderName = "";
                            igetNetworkCellId = "";
                            ishowNetworkHint = "";
                            ishowSignalStr = "";


    /* For Popup */

                            iShowCmtType = "";
                            iShowPopupNote = "";

                        }
                    });
                    pinBtn.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(final View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ResultView.this)
                                    .setMessage("Are you Sure Want to Delect the Pointer?")
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            int id = v.getId();
                                            if (id != 0) {
                                                v.setVisibility(View.GONE);
                                                resultImage.removeView(v);
                                                txt.findViewById(id).setVisibility(View.GONE);

                                            }
                                            dbClasses.open();
                                            boolean value = dbClasses.deleteRemovePin(iFloorPath, String.valueOf(id));
                                            txt.setOnTouchListener(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View v, MotionEvent event) {
                                                    int txtId = v.getId();
                                                    if (txtId != 0) {
                                                        v.setVisibility(View.GONE);
                                                        resultImage.removeView(v);

                                                        txt.findViewById(txtId).setVisibility(View.GONE);
                                                    }
                                                    return false;
                                                }
                                            });
                                       /* if (value) {
                                            View myView = findViewById(id);
                                            ViewGroup parent = (ViewGroup) myView.getParent();
                                            parent.removeView(myView);
                                            *//*pinBtn.setVisibility(View.GONE);
                                            txt.setVisibility(View.GONE);*//*
                                        }
                                        else {
                                            Message.message(getBaseContext(),"Pin Not Removed",Toast.LENGTH_SHORT);
                                        }*/
                                            dbClasses.close();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            builder.show();
                            return true;
                        }

                    });
//                    iNetwork_Selection = "";
                }
//            }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            String imgDecodableString;
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();

                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);

                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);


                Intent intent = new Intent(ResultView.this,SelectImage.class);
                intent.putExtra(SELECTED_IMG_PATH, imgDecodableString);
                startActivityForResult(intent,1);

            }
        } catch (Exception e) {
            Message.message(getBaseContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG);
        }
    }
    private void getImageNameNetworkModeDB() {
    try {
        dbClasses.open();
        Cursor readDataDB = dbClasses.readNetworkMode(iImageName, pinMarkid);
        if (readDataDB.moveToNext()) {
            network_selection = readDataDB.getString(1);
            switch (network_selection) {
                case "Wifi":
                    wifiShowLayout.setVisibility(View.VISIBLE);
                    iImageName = readDataDB.getString(0);
                    iPinId = readDataDB.getString(2);
                    iProvider_Detail = readDataDB.getString(3);
                    iFrequency = readDataDB.getString(4);
                    iWiFimode = readDataDB.getString(5);
                    iBssi = readDataDB.getString(7);
                    iSignal_Strength = readDataDB.getString(8);
                    iNetwork_Technology = readDataDB.getString(9);
                    iPinMark_Hint = readDataDB.getString(11);
                    break;
                case "Network":
                    igetNetworkTechnology = readDataDB.getString(10);
                    igetNetworkProviderName = readDataDB.getString(3);
                    igetNetworkCellId = readDataDB.getString(14);
                    ishowSignalStr = readDataDB.getString(8);
                    iSignal_Quality = readDataDB.getString(9);
                    ishowNetworkHint = readDataDB.getString(11);
                    ishowChannelNo = readDataDB.getString(6);
                    break;
                case "Notepad":
                    iShowCmtType = readDataDB.getString(1);
                    iShowPopupNote = readDataDB.getString(11);
                    break;
                case "Camera":
                    iShowCapturedLocation = readDataDB.getString(11);
                    igetCapturedPath = readDataDB.getString(15);
                    break;
                case "Drawing":
                    igetCapturedPath = readDataDB.getString(15);
                    break;
                default:
//                    Message.message(getBaseContext(), "Network", Toast.LENGTH_LONG);
                    break;
            }
        }
        readDataDB.close();
    }catch (Exception e){
        e.printStackTrace();
    }
    }
    public class myPhoneStateListener extends PhoneStateListener {
        public int signalStrengthValue;

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            try {
                String[] signalstrength = signalStrength.toString().split(" ");
                LTErsrq = Integer.valueOf(signalstrength[10]); // LTE - 4G SignalQuality

                if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
                    LTErsrp = Integer.valueOf(signalstrength[8]); // LTE - 4G SignalStrength
                    signalStrengthValue = LTErsrp * 2 - 113;// LTE - 4G SignalStrength
                    signalQuality = String.valueOf(LteRsrq(LTErsrq));
                }
                if (signalStrength.isGsm()){
                    if(signalStrength.getGsmSignalStrength() != 99) {
                        signalStrengthValue = -113 + 2 * signalStrength.getGsmSignalStrength(); // 3G SignalStrength
                    } else {
                        signalStrengthValue = signalStrength.getGsmSignalStrength();// 3G SignalStrength
                    }
                }
                /* CDMA NETWORK */
                /*if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_CDMA){
                    signalStrengthValue = signalStrength.getCdmaDbm();
                }*/

                signalview.setText(String.valueOf(signalStrengthValue) + " dBm");
                if (getTechnology(telephonyManager).equals("3G")){
                    signalQuality = String.valueOf(signalStrength.getEvdoEcio());
                }
                 /*if (signalStrength.isGsm()) {
                    if (signalStrength.getGsmSignalStrength() != 99)
                        signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113;
                    else
                        signalStrengthValue = signalStrength.getGsmSignalStrength();
                } else {
                    signalStrengthValue = signalStrength.getCdmaDbm();
                }
                signalview.setText(String.valueOf(signalStrengthValue));*/
            /*if (getTechnology(telephonyManager) == "3G"){
                signalQuality = String.valueOf(signalStrength.getEvdoEcio());
            }*/
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        public void onCellInfoChanged(List<CellInfo> cellInfo) {
            if (cellInfo != null) {
                onCellsChanged(cellInfo);
            }
            super.onCellInfoChanged(cellInfo);
        }
    }

    private int displayChannelNo() {
        String operatorName = telephonyManager.getNetworkOperatorName();
        dbClasses.open();
        Cursor dataCursor = dbClasses.readData(operatorName);
        if (dataCursor != null && dataCursor.moveToFirst()) {
            getOperator = dataCursor.getString(0);
            bandNo = dataCursor.getString(1);
        }
        return Integer.valueOf(bandNo) * 5;
    }

    public static int LteRsrq(int i)
    {
        if (i > 0)
        {
            return i * -1;
        } else
        {
            return i;
        }
    }

    /* class WifiReceiver */
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    class WifiReceiver extends BroadcastReceiver {

        // This method call when number of wifi connections changed
        public void onReceive(final Context c, Intent intent) {
            scanwifilist = wifiManager.getScanResults();

            /* sorting of wifi provider based on level */
            Collections.sort(scanwifilist, new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult lhs, ScanResult rhs) {
                    return (lhs.level > rhs.level ? -1 : (lhs.level == rhs.level ? 0 : 1));
                }
            });
            availableWifiList = new String[scanwifilist.size()];

            LayoutInflater layoutInflater= (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewAlert= layoutInflater.inflate(R.layout.popup_userdetail,null);

            AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(ResultView.this);
            alertDialogBuilder.setView(viewAlert);

            final AlertDialog alertDialog= alertDialogBuilder.create();
            final ListView wifiPopdetail=(ListView)viewAlert.findViewById(R.id.user_wifi_detail);

            for (int i = 0; i < scanwifilist.size(); i++) {
                /* to get SSID and BSSID of wifi provider*/
                availableWifiList[i]= (scanwifilist.get(i).SSID);
            }

            wifiPopdetail.setAdapter(new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,availableWifiList));
            if (wifiPopdetail.isEnabled()){
                pdialog.dismiss();
            }
            /*Button cancelBtn = (Button)viewAlert.findViewById(R.id.cancelBtn);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iNetwork_Selection = "";
                    network_selection = "";
                    alertDialog.show();
                }
            });*/
            alertDialog.show();

            wifiPopdetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    getPValue = scanwifilist.get(position).SSID;
                    getPBSSI= scanwifilist.get(position).BSSID;
                    signalStrengthNetwork =scanwifilist.get(position).level;

                    getFrequency = scanwifilist.get(position).frequency;
                    final String cap = scanwifilist.get(position).capabilities;
                    final String[] securityModes = { WEP, WPA2, EAP };
                    for (int i = securityModes.length - 1; i >= 0; i--) {
                        if (cap.contains(securityModes[i])) {
                            wifiNetworkCode = securityModes[i];
                        }
                    }

                    if(getPValue != null) {
                        wifiOperatorName.setText(getPValue);
                        wifiMacAddress.setText(getPBSSI);
                        wifiSignalStr.setText(String.valueOf(signalStrengthNetwork));
                        wifiMode.setText(wifiNetworkCode);
                    } else {
                        Message.message(getBaseContext(),"Null found",Toast.LENGTH_LONG);
                    }
                    alertDialog.dismiss();
                }
            });
            unregisterReceiver(wifiRecevier);
        }

    }
    private void scaning() {
        // wifi scanned value broadcast receiver
        wifiRecevier = new WifiReceiver();

        // Register broadcast receiver,Broadcast receiver will automatically call when number of wifi connections changed
        registerReceiver(wifiRecevier, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        wifiManager.startScan();
        pdialog=new ProgressDialog(this);
        pdialog.setCancelable(true);
        pdialog.setMessage("Loading ....");
        pdialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selectimage, menu);
        return true;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void onCellsChanged(List<CellInfo> cellInfoList) {
        for(CellInfo cellInfo : cellInfoList){
            if (cellInfo instanceof CellInfoLte) {
                CellIdentityLte identity1 =  ((CellInfoLte) cellInfo).getCellIdentity();
                pci = identity1.getPci();
            }else if(cellInfo instanceof  CellInfoWcdma){
                CellIdentityWcdma cellInfoWcdma = ((CellInfoWcdma)cellInfo).getCellIdentity();
                psc =cellInfoWcdma.getPsc();
            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.wifiOperators:
                iNetwork_Selection = "Wifi";
                wifiManager.setWifiEnabled(true);

                dialog = new Dialog(ResultView.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.setContentView(R.layout.dialog_wifi_store);
                dialog.setTitle(R.string.wifi);

                wifiOperatorName = (TextView) dialog.findViewById(R.id.wifiOperatorName);
                wifiMacAddress = (TextView) dialog.findViewById(R.id.wifiMacAddress);
                wifiSignalStr = (TextView) dialog.findViewById(R.id.wifiSignalStr);
                wifiMode = (TextView) dialog.findViewById(R.id.wifiMode);

                setHint = (EditText) dialog.findViewById(R.id.getHintName);
                scaning();
                return true;
            case R.id.networkOperators:
                try {

                    iNetwork_Selection = "Network";

                    dialog = new Dialog(ResultView.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    dialog.setContentView(R.layout.dialog_network_store);

                    txtPhoneType = (TextView) dialog.findViewById(R.id.txtPhoneType);
                    networkTechnology = (TextView) dialog.findViewById(R.id.networkTechnology);
                    netProviderName = (TextView) dialog.findViewById(R.id.netProviderName);
                    networkCellId = (TextView) dialog.findViewById(R.id.networkCellId);
                    networkCellSignalQuality = (TextView)dialog.findViewById(R.id.networkSignalQuality);
                    networkSignalStr = (TextView) dialog.findViewById(R.id.networkSignalStr);
                    networkChannelNo = (TextView)dialog.findViewById(R.id.networkChannelNo);

                    showFId = (TextView)dialog.findViewById(R.id.txtCellId);
                    showFRSRQ = (TextView)dialog.findViewById(R.id.cellSignalQuality);
                    showFRSRP = (TextView)dialog.findViewById(R.id.txtCellSignalStr);
                    shoFeUarfcn = (TextView)dialog.findViewById(R.id.cellChannelNo);

                    openNetworkPopup();
                    networktech = getTechnology(telephonyManager);
                    if (networktech.equals("3G")){
                        showFRSRP.setText(R.string.networkSignalStrRSCP);
                        showFId.setText(R.string.networkCellIdentifierPsc);
                        showFRSRQ.setText(R.string.networkSignalQualityEcno);
                        shoFeUarfcn.setText(R.string.networkChannelNumberUarfcn);
                    } else if (networktech.equals("4G")){
                        showFRSRP.setText(R.string.networkSignalStrRSRP);
                        showFId.setText(R.string.networkCellIdentifierPCI);
                        showFRSRQ.setText(R.string.networkSignalQualityRSRQ);
                        shoFeUarfcn.setText(R.string.networkChannelNumbereUARFCN);
                    }
                    getIntent().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } catch (Exception nop) {
                    nop.printStackTrace();
                }
                return true;
            case R.id.notepad:
                dialog = new Dialog(ResultView.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.setContentView(R.layout.popup_wifiphone_networks);
                dialog.setTitle(R.string.notePad);

                iNetwork_Selection = "Notepad";
                return true;
            case R.id.save:
                View view = getWindow().getDecorView().getRootView();
                view.setDrawingCacheEnabled(true);
                myBitmap = view.getDrawingCache();
                saveBitmap(myBitmap);
                onBackPressed();
                return true;
            case R.id.BuildingInfo:
                Helper.imageFrom = "ResultPage";
                ResultViewBuilding resultViewBuilding = new ResultViewBuilding();
                resultViewBuilding.show(getFragmentManager(), "My Building View");
                return true;
            case R.id.cameraDialog:
                iNetwork_Selection = "Camera";
                network_selection = "Camera";
                getPlanPartName = new Dialog(this);
                dialog = new Dialog(ResultView.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_camera_store);
                dialog.setTitle(R.string.floorCapture);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri fileUri = getOutputMediaFileUri(CAMERA_REQUEST);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //set Image file name
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

                return true;
           /* case R.id.customDrawing :
                iNetwork_Selection = "Drawing";
                dialog = new Dialog(ResultView.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                CustomDrawing customDrawing = new CustomDrawing();
                customDrawing.show(getFragmentManager(),"My Custom Drawing");
                return true;*/
            case R.id.drawing :
                try {
                    sketchLayout.setVisibility(View.VISIBLE);
                    iNetwork_Selection = "Draw";
                    dialog = new Dialog(ResultView.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_wifiphone_networks);
                    dialog.setTitle(R.string.notePad);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    drawOnFloorPlan = new DrawOnFloorPlan(getBaseContext());
                    resultImage.addView(drawOnFloorPlan);
                    drawOnFloorPlan.destroyDrawingCache();
                }catch (Exception e){
                    e.printStackTrace();
                }

                return true;
           /* case R.id.drawingSave :
                try{
                    DrawOnFloorPlan.DrawBitmap = null;
                    dialog = new Dialog(ResultView.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_drawimage_paint);
                    iNetwork_Selection = "DrawSave";
                    btnFitPin = (Button)dialog.findViewById(R.id.btnFitPin);
                    btnCancel = (Button)dialog.findViewById(R.id.btnCancel);
                    dialog.show();
                    btnFitPin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                drawOnFloorPlan = new DrawOnFloorPlan(getBaseContext());
                                combineImages(ParameterGetSet.floorPlanBitmap, ParameterGetSet.drawingBitmap);
                                *//*drawOnFloorPlan.clear();
                                drawOnFloorPlan = new DrawOnFloorPlan(getBaseContext());*//*
                                dbClasses.open();
                                dbClasses.updateColumn(iImageName);
                                extractDB();
                                Display display = getWindowManager().getDefaultDisplay();
                                Point size = new Point();
                                display.getSize(size);
                                btmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(ParameterGetSet.floor_Image_Path), size.x, size.y, true);
                                ParameterGetSet.floorPlanBitmap = btmp;
                                Drawable newImageDraw = new BitmapDrawable(btmp);
                                resultImage.setBackground(newImageDraw);
                                dialog.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.drawingErase:
            try {
//                DrawOnFloorPlan.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                drawOnFloorPlan.clear();
                drawErase = 1;
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;*/
            /*case R.id.drawingDelete :
                DrawOnFloorPlan.mPaint.setXfermode(null);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /*Compine Two Bitmap Image*/
    public Bitmap combineImages(Bitmap c, Bitmap s) {
        Bitmap bmOverlay = Bitmap.createBitmap(c.getWidth(), c.getHeight(), c.getConfig());
        Bitmap newResizedBitmap = Bitmap.createScaledBitmap(s, c.getWidth(), c.getHeight(), false);
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(c, new Matrix(), null);
        canvas.drawBitmap(newResizedBitmap, 0, 0, null);
        String root = Environment.getExternalStorageDirectory() + "/mySignalCapture";
        File myDir = new File(root + "/sketchDrawing");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String bName = iImageName;
        String buildingAllList = bName.substring(0, bName.lastIndexOf("."));
        String drawingName = buildingAllList +".png";
        File file = new File (myDir, drawingName);
        if (file.exists ()) file.deleteOnExit();
        try {
            ParameterGetSet.floor_Image_Path = file.getPath();
            FileOutputStream out = new FileOutputStream(file);
            bmOverlay.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmOverlay;
    }
    private Uri getOutputMediaFileUri(int mediaTypeImage) {
        return Uri.fromFile(getOutputMediaFile(mediaTypeImage));
    }

    private void saveBitmap(Bitmap bitmap) {
        Calendar c = Calendar.getInstance();
        File mkdir = new File(Environment.getExternalStorageDirectory(),"signalCapture");
        if (!mkdir.exists()) {
            mkdir.mkdir();
        }
        String filePath = Environment.getExternalStorageDirectory() + "/signalCapture" + File.separator+ c.getTimeInMillis() +"screenshot.png";
        File imagePath = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
//            Message.message(getBaseContext(),"Screen Shot Success, the path is"+imagePath,Toast.LENGTH_LONG);
            String abImgPath = imagePath.getPath();
            Intent intent = new Intent(ResultView.this,SelectImage.class);
            intent.putExtra(PdfScreenShot.IMAGE_URL,abImgPath);
            startActivity(intent);

        } catch (FileNotFoundException e) {
            Message.message(getBaseContext()," File Not Found : " + e.getMessage(),Toast.LENGTH_LONG);
        } catch (IOException e) {
            Message.message(getBaseContext(),"Error : " +e.getMessage(),Toast.LENGTH_LONG);
        }
    }
    private File getOutputMediaFile(int type) {
        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory("mySignalCapture"),"CameraApp");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CameraApp", "Failed to create Directory");
                return null;
            }
        }

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);

        if (type == CAMERA_REQUEST) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "cam_" + n + ".jpg");
            ParameterGetSet.camera_location_path = mediaFile.getPath();
        } else {
            mediaFile = null;
        }
        return mediaFile;
    }
    public void openNetworkPopup() {
        Button scanNetwork;
        try {

            alertDialogBox = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View openPop = inflater.inflate(R.layout.layout_mobile_network,null);

            alertDialogBox.setView(openPop);

            scanNetwork = (Button)openPop.findViewById(R.id.scanNetwork);

            deviceName =(TextView)openPop.findViewById(R.id.phoneModel);
            txtNetworkTech =(TextView)openPop.findViewById(R.id.txtTechnology);
            txtProvider = (TextView)openPop.findViewById(R.id.txtProvider);
            network_technology =(TextView)openPop.findViewById(R.id.get_technology);
            providerName =(TextView)openPop.findViewById(R.id.operator_name);
            cell_id =(TextView)openPop.findViewById(R.id.cid);
            signalview =(TextView)openPop.findViewById(R.id.id_signal);
            txtChannelNo = (TextView)openPop.findViewById(R.id.networkShowChannelNo);

            txtNetworkSelectionHint = (TextView)openPop.findViewById(R.id.txtAlertNetworkChoose);

            showTRSCP = (TextView)openPop.findViewById(R.id.txtCellStrength);
            showTId = (TextView)openPop.findViewById(R.id.cellid);
            showTEcno = (TextView)openPop.findViewById(R.id.txtCellSignalQuality);
            showTUarfcn = (TextView)openPop.findViewById(R.id.txtCellChannelNo);

            telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        /* Get and Set the Network Type(3G or 4G) */
            CurrentNetworkType = String.valueOf(telephonyManager.getNetworkType());

            operatorName = telephonyManager.getNetworkOperatorName();
            providerName.setText(operatorName);
            network_technology.setText(getTechnology(telephonyManager));

            if (cellIdentity_int == null){
//                getCellID();
            }
            cell_id.setText(cellIdentity_int);
            txtChannelNo.setText(String.valueOf(displayChannelNo()));

            deviceName.setText(getDeviceName());


            scanNetwork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                    startActivity(intent);
                }
            });
            alertDialogBox.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    networkTechnology.setText(getTechnology(telephonyManager));
                    netProviderName.setText(operatorName);
                    networkCellId.setText(cellIdentity_int);
                    networkCellSignalQuality.setText(signalQuality);
                    networkSignalStr.setText(String.valueOf(psListener.signalStrengthValue));
                }
            });
            alertDialogBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    iNetwork_Selection = "";
                    network_selection = "";
                    dialog.dismiss();
                }
            });
            alertDialogBox.create();
            alertDialogBox.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

  /*  public void getCellID(){
        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
        for (CellInfo cellInfo : cellInfoList)
        {
            if (cellInfo instanceof CellInfoLte)
            {
                CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
                int ltePciValue = cellIdentityLte.getPci();
                cellIdentity_int = String.valueOf(ltePciValue);
            }
            else if(cellInfo instanceof CellInfoGsm){
                CellInfoGsm cellInfoGsm = (CellInfoGsm)cellInfo;
                CellIdentityGsm cellIdentityGsm = cellInfoGsm.getCellIdentity();
                int  gsmCid = cellIdentityGsm.getCid();
                cellIdentity_int = String.valueOf(gsmCid);
            }
            else if (cellInfo instanceof CellInfoWcdma){
                CellInfoWcdma cellInfoWcdma = (CellInfoWcdma)cellInfo;
                CellIdentityWcdma cellIdentityWcdma = cellInfoWcdma.getCellIdentity();
                int pscWcdma = cellIdentityWcdma.getPsc();
                cellIdentity_int = String.valueOf(pscWcdma);
            }else if (cellInfo instanceof CellInfoCdma){
                CellInfoCdma cellInfocdma = (CellInfoCdma)cellInfo;
                CellIdentityCdma cellIdentitycdma = cellInfocdma.getCellIdentity();
                int pscCdma = cellIdentitycdma.getBasestationId();
                cellIdentity_int = String.valueOf(pscCdma);
            }
        }
    }
*/
    private String getTechnology(TelephonyManager telephonyManager) {
        int netType = telephonyManager.getNetworkType();
        GsmCellLocation cellLocation;
        switch (netType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                cellLocation = (GsmCellLocation)telephonyManager.getCellLocation();
                cellIdentity_int = String.valueOf(cellLocation.getPsc());
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                cellIdentity_int = String.valueOf(psc);
                showTRSCP.setText(R.string.networkSignalStrRSCP);
                showTId.setText(R.string.networkCellIdentifierPsc);
                showTEcno.setText(R.string.networkSignalQualityEcno);
                showTUarfcn.setText(R.string.networkChannelNumberUarfcn );
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                cellIdentity_int = String.valueOf(pci);
                showTRSCP.setText(R.string.networkSignalStrRSRP);
                showTId.setText(R.string.networkCellIdentifierPCI);
                showTEcno.setText(R.string.networkSignalQualityRSRQ);
                showTUarfcn.setText(R.string.networkChannelNumbereUARFCN );
                return "4G";
            default:
                return "Unknown Network";
        }
    }
    public void saveSignalData() {
        btnFitPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                switch (iNetwork_Selection) {
                    case "Wifi":
                        try{
                            if (setHint.getText().toString().equals("")) {
                                setHint.setError("Please Enter the Location");
                            } else {
                                txt = new TextView(getBaseContext());
                                getHint = setHint.getText().toString();
                                pinBtn.setId(i);
                                pinBtn.setImageBitmap(bitmap);
                                pinBtn.setLayoutParams(rparams);
                                txt.setLayoutParams(rparams);
                                btnId = pinBtn.getId();

                                setValues = new ArrayList<String>();

                                setValues.add("Wifi");
                                setValues.add(String.valueOf(btnId));
                                setValues.add(getPValue); // operator name
                                setValues.add(getPBSSI); // mac address
                                setValues.add(String.valueOf(signalStrengthNetwork)); //signal strength
                                setValues.add(String.valueOf(getFrequency));
                                setValues.add(wifiNetworkCode);
                                setValues.add(channelNumber);
                                setValues.add(signalQuality);
                                setValues.add(CurrentNetworkType);
                                setValues.add(operatorName);
                                setValues.add(getHint);
                                setValues.add(String.valueOf(x));
                                setValues.add(String.valueOf(y));
                                ParameterGetSet.pinmark_Hint = getHint;
//                                txt.setText(ParameterGetSet.pinmark_Hint);
                                txt.setTextAppearance(getBaseContext(), R.style.txtViewStyle);
                                txt.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                                txt.setId(i);
                                txt.setTextColor(Color.BLACK);
                                i++;
                                resultImage.addView(txt);
                                resultImage.addView(pinBtn);

                                dbClasses.open();



                                insertParam();
                                dbClasses.insertImagePath();
                                dbClasses.close();

                                dialog.dismiss();
                                iNetwork_Selection = "";
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case "Network":
                        try{

                            if (setNetworkHint.getText().toString().equals("")) {
                                setNetworkHint.setError("Please Enter the Location");
                            } else {
                                txt = new TextView(getBaseContext());
                                getNetworkHint = setNetworkHint.getText().toString();
                                pinBtn.setId(i);
                                pinBtn.setImageBitmap(bitmapNetwork);
                                pinBtn.setLayoutParams(rparams);
                                txt.setLayoutParams(rparams);
                                btnId = pinBtn.getId();

                                setValues = new ArrayList<String>();

                                setValues.add("Network");
                                setValues.add(String.valueOf(btnId));
                                setValues.add(operatorName);
                                setValues.add(getNetworkHint);
                                setValues.add(getTechnology(telephonyManager));
                                setValues.add(String.valueOf(psListener.signalStrengthValue));
                                setValues.add(cellIdentity_int);
                                setValues.add(String.valueOf(displayChannelNo()));
                                setValues.add(getHint);
                                setValues.add(String.valueOf(x));
                                setValues.add(String.valueOf(y));
                                setValues.add(signalQuality);
                                ParameterGetSet.pinmark_Hint = getNetworkHint;
//                                txt.setText(ParameterGetSet.pinmark_Hint);
                                txt.setTextAppearance(getBaseContext(), R.style.txtViewStyle);
                                txt.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                                txt.setTextColor(Color.BLACK);
                                txt.setId(i);
                                i++;
                                resultImage.addView(pinBtn);
                                resultImage.addView(txt);
                                dbClasses.open();
                                insertParam();
                                dbClasses.insertImagePath();
                                dbClasses.close();

                                dialog.dismiss();
                                iNetwork_Selection = "";
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case "Notepad":
                        try {
                            if (editNotePad.getText().toString().equals("")) {
                                editNotePad.setError("Please Enter your Comment");
                            } else {
                                txt = new TextView(getBaseContext());
                                pinBtn.setId(i);
                                pinBtn.setImageBitmap(notePadBitmap);
                                pinBtn.setLayoutParams(rparams);
                                txt.setLayoutParams(rparams);
                                btnId = pinBtn.getId();

                                setValues = new ArrayList<String>();
                                setValues.add("Notepad");
                                setValues.add(editNotePad.getText().toString());
                                setValues.add(String.valueOf(btnId));
                                setValues.add(String.valueOf(x));
                                setValues.add(String.valueOf(y));
                                ParameterGetSet.pinmark_Hint = editNotePad.getText().toString();
//                                txt.setText(ParameterGetSet.pinmark_Hint);
                                txt.setTextAppearance(getBaseContext(), R.style.txtViewStyle);
                                txt.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                                txt.setTextColor(Color.BLACK);
                                txt.setId(i );
                                i++;
                                resultImage.addView(pinBtn);
                                resultImage.addView(txt);
                                dbClasses.open();
                                insertParam();
                                dbClasses.insertImagePath();
                                extractDB();
                                dbClasses.close();

                                dialog.dismiss();
                                iNetwork_Selection = "";
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        break;
                    case "Camera":
                        try {
                            if (cameraLocation.getText().toString().equals("")) {
                                cameraLocation.setError("Please Enter your Comment");
                            } else {
                                txt = new TextView(getBaseContext());
                                pinBtn.setId(i);
                                pinBtn.setImageBitmap(cameraBitmap);
                                pinBtn.setLayoutParams(rparams);
                                txt.setLayoutParams(rparams);
                                btnId = pinBtn.getId();

                                setValues = new ArrayList<String>();
                                setValues.add("Camera");
                                setValues.add(cameraLocation.getText().toString());
                                setValues.add(String.valueOf(btnId));
                                setValues.add(String.valueOf(x));
                                setValues.add(String.valueOf(y));
                                ParameterGetSet.pinmark_Hint = cameraLocation.getText().toString();
//                                txt.setText(ParameterGetSet.pinmark_Hint);
                                txt.setId(i);
                                txt.setTextAppearance(getBaseContext(), R.style.txtViewStyle);
                                txt.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                                txt.setTextColor(Color.BLACK);
                                i++;
                                resultImage.addView(pinBtn);
                                resultImage.addView(txt);
                                dbClasses.open();
                                insertParam();
                                dbClasses.insertImagePath();
                                dbClasses.close();
                                dialog.dismiss();
                                iNetwork_Selection = "";
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }
    public void extractDB() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            if (sd.canWrite()) {
                String currentDBPath = getFilesDir().getParent()+"/databases/signalCapture.db";
                String backupDBPath = "/mySignalCapture/signalCapture.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    try {
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                    } catch (IOException e) {
                        Message.message(getBaseContext(),"Error : "+ e.getMessage(),Toast.LENGTH_SHORT);
                    }

                } else {
                    Message.message(getBaseContext(), "Path is not exists :" + backupDBPath, Toast.LENGTH_SHORT);
                }

            }
        } catch (Exception e) {
            Message.message(getBaseContext(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT);
        }
    }
    public void insertParam() {
        try{
            ParameterGetSet.floor_Image_Path = iFloorPath;
            ParameterGetSet.image_Name = setUserName;
            ParameterGetSet.valuesHash = setValues;
            ParameterGetSet.image_Name = imgPath;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

