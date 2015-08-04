package com.kenturf.signalcapture;

import android.annotation.TargetApi;
import android.app.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.*;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.telephony.*;
import android.telephony.gsm.GsmCellLocation;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.telephony.CellIdentityLte;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.security.acl.LastOwnerException;
import java.text.SimpleDateFormat;
import java.util.*;


public class SelectImage extends ActionBarActivity {

    /* load image */
    String imgPath;
    String imageName;
    String imageExtension;
    static String imageNewPath;
    static String setUserName;
    SignalStrength ss;
    /* For Background Image Bitmap */
    Bitmap setBitmap;
    Bitmap btmp;

    /*For getImage Name dialog */
    Button cancelImageName,saveImageName;
    EditText editImageName;

    TextView putUserName,noteFloorPopup,signalview,phoneType,cell_id,deviceName,network_technology,providerName;
    TextView showTRSCP,showTId,showTEcno,showTUarfcn,showFRSRQ,showFId,showFRSRP,shoFeUarfcn;
    TextView showPRscp,showPCid,showPEcno,showPUarfcn;
    TextView txtSignal,txtNetworkTech,txtProvider,txtNetworkSelectionHint,txtChannelNo;
    Button btnLoadImage;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    EditText getHintPopupImg;
    public RelativeLayout selectedPicture;
    private long mStartRX = 0;
    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    int[] viewCoords = new int[2];
    AlertDialog.Builder alertDialogBox;
    /* For wifi */
    String availableWifiList[],channelNumber;
    WifiManager wifiManager;
    WifiReceiver wifiRecevier;
    List<ScanResult> scanwifilist;
    WifiInfo wifiInfo;
    int level,signalStrengthNetwork;
    public static final String WPA2 = "WPA2";
    public static final String WEP = "WEP";
    public static final String EAP = "EAP";
    public static final String OPEN = "Open";
    Dialog dialog;
    int pci;

    /* For network */
    public TelephonyManager telephonyManager;
    String getPValue,getPBSSI,CurrentNetworkType,operatorName,networkOperator,cellIdentity_int,wifiNetworkCode;
    int mcc,mnc;
    myPhoneStateListener psListener;

    /* For popup */
    Bitmap bitmap,towerBitmap,tmp,pdfTmp,notePadBitmap,cameraBitmap;
    Button saveDetail,cancelDetail;
    RelativeLayout.LayoutParams rparams;
    int btnId,getFrequency,pinid;
    int i = 1,x,y;
    ProgressDialog pdialog;
    BitmapDrawable drawable,loadImgDrawable;

    TextView wifiOperatorName,wifiMacAddress,wifiSignalStr,wifiMode;
    EditText setHint;
    String getHint,getNetworkHint,getNotes;
    Button btnCancel;
    Button btnFitPin;
    public ImageView btn;
    TextView txt;
    LinearLayout showLayout,showNetworkLayout,notePadLayout,cameraLayout,chooseAny;
    static ArrayList<String> setValues;

    /* For Network Display */
    String signalQuality,networktech;
    TextView getNetworkTechnology,getNetworkProviderName,getNetworkCellId,showNetworkHint,showSignalStr,getSignalQuality,showNetworkChannelNo;
    String igetNetworkTechnology,igetNetworkProviderName,igetNetworkCellId,ishowNetworkHint,ishowSignalStr,ishowChannelNo;
    /* For Network popup Display */
    TextView txtPhoneType,networkTechnology,netProviderName,networkCellId,networkSignalStr;
    EditText setNetworkHint,editNotePad,cameraLocation;
    EditText editAddressOne,editAddressTwo,editCity,editState,editZip;
    String getOperator,bandNo;
    Integer channelNo;
    /* For display data */
    String iuser_name,iNetwork_Selection,iProvider_Detail,iFrequency,iWiFimode,iBssi,iSignal_Strength,iSignal_Quality,iNetwork_Technology,iPinMark_Hint;
    String iNetworkSelectionMode,iPinId;

    /* For Note pad */
    TextView showCmtType,showPopupNote,showCapturedLocation;
    ImageView showCapturedImage,showDrawedImage;

    String savedImageName;

    EditText editCamPicName;
    Button camCancel,camCapture;
    String strCamImgName;
    private static final int CAMERA_REQUEST = 100;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 200;

    Uri fileUri;
    File mediaStorageDir,mediaFile;

    int id;
    int lte;

    /* Display stored data dialog */
    TextView getSetOperatorName,getSetMacAddress,getSetSignalStrength,getSetFloorHint,getSetWifiMode,getSetWifiFrequency;

    /* Wifi and network Show layout */
    LinearLayout wifiShowLayout,networkShowLayout,notepadShowLayout,cameraShowLayout,drawLayout;

    /* For savedInstanceState */
    private static final String IMAGE_DATA = "image_resource";
    private static final String IMAGE_PDF_DATA = "image_pdf_resource";

    private GestureDetectorCompat mDetector;

    DBClass dbClass;
    ParameterGetSet parameterGetSet;

    public static int drawErase = 0;

    /* for db */
    String EngUserName,fileName,network_selection;

    /* db */
    String iImageName;
    ScrollView sv;

    /* for create folder */
    File fileImage;

    String savedImagePath;

    String iXValue;
    String iYValue;

//    Result Page
    String iFloorPath,rImageName;
    ImageView pinBtn;
    TextView txtLocation;
    HashMap<Integer, ArrayList<String>> pinDetailHash;
    String iShowCmtType,iShowPopupNote,iShowCapturedLocation,igetCapturedPath;
    int pinMarkid;
    int ecio;
    TextView networkCellSignalQuality,networkChannelNo;
    Dialog getPlanPartName;
    BitmapDrawable newCapturedImage;
    View test;
    String pinDrawPath;
    PhoneStateListener stateListener;
    int psc;
    int LTErsrq,LTErsrp,dBm;

    DrawOnFloorPlan drawOnFloorPlan;

    Button skecthSave,sketchErase;
    LinearLayout sketchLayout,pinLayout;
   Button wifiPin,networkPin,notepadPin,cameraPin,drawingPin,savePin;
    FrameLayout frameLayout_screenShot;

    /*SYNC Parameter*/
    String User_Name,lastName,contactNo,emailAddress,address1,address2,city,state,zip,Floor_Image_Path,Image_Name,Network_Selection,Pin_Id,Provider_Detail,Bssi,Signal_Strength, Frequency,
            WiFimode,Channel_Number,Network_Technology,Cell_Id,Signal_Quality, PinMark_Hint,Camera_LocationPath,Camera_LocationName,XValue,YValue,dateTime,keyID;
    JSONObject jsonObject;
    JSONObject objJSONObjectForuser;


    String jUser_Name,jlastName,jcontactNo,jemailAddress,jaddress1,jaddress2,jcity,jstate,jzip,jFloor_Image_Path,jImage_Name,jNetwork_Selection,jPin_Id,jProvider_Detail,jBssi,jSignal_Strength, jFrequency,
            jWiFimode,jChannel_Number,jNetwork_Technology,jCell_Id,jSignal_Quality, jPinMark_Hint,jCamera_LocationPath,jCamera_LocationName,jXValue,jYValue,jdateTime,jkeyID;

    InputStream inputStream;

    public static int image_xvalue;
    public static int image_yvalue;

    public static int x_btn;
    public static int y_btn;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_selectimage);

        /* Initialize db */
        dbClass = new DBClass(this);

        /* Initialize parameter Get Set  */
        parameterGetSet = new ParameterGetSet();

        EngUserName = ParameterGetSet.Engineer_Name;

        /* Picture Layout */
        selectedPicture = (RelativeLayout)findViewById(R.id.imgView);
        frameLayout_screenShot = (FrameLayout)findViewById(R.id.frameLayout_screen_shot);
        sv = new ScrollView(this);
        sv.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));


        psListener = new myPhoneStateListener();

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wifisignal);
        towerBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.tower);
        notePadBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.notes);
        cameraBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.action_camera);


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

        wifiPin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    iNetwork_Selection = "Wifi";
                    wifiManager.setWifiEnabled(true);

                    dialog = new Dialog(SelectImage.this);
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

        networkPin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    dialog = new Dialog(SelectImage.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    dialog.setContentView(R.layout.dialog_network_store);

                    iNetwork_Selection = "Network";

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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        notepadPin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    dialog = new Dialog(SelectImage.this);
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

        cameraPin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    iNetwork_Selection = "Camera";
                    getPlanPartName = new Dialog(getBaseContext());
                    dialog = new Dialog(SelectImage.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_camera_store);
                    dialog.setTitle(R.string.floorCapture);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri fileUri = getOutputMediaFileUri(CAMERA_REQUEST);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //set Image file name
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        drawingPin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    drawOnFloorPlan = new DrawOnFloorPlan(getBaseContext());
                    selectedPicture.addView(drawOnFloorPlan);
                    wifiPin.setVisibility(View.INVISIBLE);
                    networkPin.setVisibility(View.INVISIBLE);
                    notepadPin.setVisibility(View.INVISIBLE);
                    cameraPin.setVisibility(View.INVISIBLE);
                    drawingPin.setVisibility(View.INVISIBLE);

                    sketchErase.setVisibility(View.VISIBLE);
                    skecthSave.setVisibility(View.VISIBLE);
//                    pinLayout.setVisibility(View.GONE);
                    iNetwork_Selection = "Draw";
                    dialog = new Dialog(SelectImage.this);
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


        skecthSave.setOnClickListener(new OnClickListener() {
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
                dialog = new Dialog(SelectImage.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_drawimage_paint);
                iNetwork_Selection = "DrawSave";
                btnFitPin = (Button)dialog.findViewById(R.id.btnFitPin);
                btnCancel = (Button)dialog.findViewById(R.id.btnCancel);
                dialog.show();
                btnFitPin.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            combineImages(ParameterGetSet.floorPlanBitmap, ParameterGetSet.drawingBitmap);
                            DrawOnFloorPlan.DrawBitmap = null;
                            drawOnFloorPlan.clear();
                            selectedPicture.removeView(drawOnFloorPlan);
                            dbClass.open();
                            dbClass.updateColumn(ParameterGetSet.image_Name);
                            extractDB();
                            Display display = getWindowManager().getDefaultDisplay();
                            Point size = new Point();
                            display.getSize(size);
                            btmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(ParameterGetSet.floor_Image_Path), size.x, size.y, true);
                            ParameterGetSet.floorPlanBitmap = btmp;
                            Drawable newImageDraw = new BitmapDrawable(btmp);
                            selectedPicture.setBackground(newImageDraw);
                            image_yvalue = selectedPicture.getHeight();
                            image_xvalue = selectedPicture.getWidth();
                            ParameterGetSet.image_xValue = image_xvalue;
                            ParameterGetSet.image_yValue = image_yvalue;
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            e.getMessage();
                            Message.msgLong(getBaseContext(),"Make Free Your Phone Memory Space");

                        }
                    }
                });
                btnCancel.setOnClickListener(new OnClickListener() {
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
        sketchErase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawOnFloorPlan.clear();
                selectedPicture.removeView(drawOnFloorPlan);
                wifiPin.setVisibility(View.INVISIBLE);
                networkPin.setVisibility(View.INVISIBLE);
                notepadPin.setVisibility(View.INVISIBLE);
                cameraPin.setVisibility(View.INVISIBLE);
                drawingPin.setVisibility(View.VISIBLE);

                sketchErase.setVisibility(View.VISIBLE);
                skecthSave.setVisibility(View.VISIBLE);
            }
        });
        if(Helper.imageFilePath.equals("PdfFileClick"))
        {
            imgPath = getIntent().getStringExtra(PdfScreenShot.IMAGE_URL);
        }
        if(Helper.imageFilePath.equals("LoadImageClick")) {
            imgPath = getIntent().getStringExtra(MainActivity.SELECTED_IMG_PATH);
        }
        try {
            if (imgPath != null) {
                final File imgFile = new File(imgPath);
                if (imgFile.exists()) {
                    final Dialog getImageNameDialog = new Dialog(this);
                    getImageNameDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    getImageNameDialog.setContentView(R.layout.dialog_getimagename);
                    getImageNameDialog.setCancelable(false);
                    getImageNameDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    cancelImageName = (Button)getImageNameDialog.findViewById(R.id.cancelImgName);
                    saveImageName = (Button)getImageNameDialog.findViewById(R.id.saveImageName);
                    editImageName = (EditText)getImageNameDialog.findViewById(R.id.editImageName);
                    editAddressOne = (EditText)getImageNameDialog.findViewById(R.id.addressOne);
                    editAddressTwo = (EditText)getImageNameDialog.findViewById(R.id.addressTwo);
                    editCity = (EditText)getImageNameDialog.findViewById(R.id.city);
                    editState = (EditText)getImageNameDialog.findViewById(R.id.state);
                    editZip = (EditText)getImageNameDialog.findViewById(R.id.zipCode);

                    imageName = imgPath.substring(imgPath.lastIndexOf("/") + 1);

                    if (imageName.indexOf(".") > 0) {
                        imageName = imageName.substring(0,imageName.lastIndexOf("."));
                    }

                    imageExtension = imgPath.replaceAll("^.*\\.(.*)$", "$1");
                    final DisplayMetrics metrics = new DisplayMetrics();
                    this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    saveImageName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String getEditName = editImageName.getText().toString();
                            ParameterGetSet.floor_Image_Path = getEditName;
                            String str_AddressOne = editAddressOne.getText().toString();
                            String str_AddressTwo = editAddressTwo.getText().toString();
                            String str_City = editCity.getText().toString();
                            String str_State = editState.getText().toString();
                            String str_Zip = editZip.getText().toString();
                            /*Building Info Page*/
                            ParameterGetSet.building_name = getEditName;
                            ParameterGetSet.Address1 = editAddressOne.getText().toString();
                            ParameterGetSet.Address2 = editAddressTwo.getText().toString();
                            ParameterGetSet.City = editCity.getText().toString();
                            ParameterGetSet.State = editState.getText().toString();
                            ParameterGetSet.ZipCode = editZip.getText().toString();

                            ParameterGetSet.setBuilding_Address1(str_AddressOne);
                            ParameterGetSet.setBuilding_Address2(str_AddressTwo);
                            ParameterGetSet.setBuilding_City(str_City);
                            ParameterGetSet.setBuilding_State(str_State);
                            ParameterGetSet.setBuilding_ZipCode(str_Zip);

                            setUserName = getEditName + "." + imageExtension;
                            ParameterGetSet.image_Name = setUserName;

                            if (editZip.getText().length() !=0 ){
                                if (str_Zip.length() != 5){
                                    editZip.setError("Not Valid Zipcode");
                                    getImageNameDialog.show();
                                }else {
                                    copyNewImage(imgFile, getEditName, imageExtension);
                                    Display display = getWindowManager().getDefaultDisplay();
                                    Point size = new Point();
                                    display.getSize(size);
                                    btmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imageNewPath),size.x,size.y,true);
                                    ParameterGetSet.floorPlanBitmap = btmp;
                                    Drawable newImageDraw = new BitmapDrawable(btmp);
                                    selectedPicture.setBackground(newImageDraw);
                                    image_yvalue = selectedPicture.getHeight();
                                    image_xvalue = selectedPicture.getWidth();
                                    ParameterGetSet.image_xValue = image_xvalue;
                                    ParameterGetSet.image_yValue = image_yvalue;
                                    getImageNameDialog.dismiss();
                                }
                            }
                            else if (getEditName.equals("")) {
                                editImageName.setError("Please Enter the Image Name");
                                getImageNameDialog.show();
                            } else {
                                dbClass.open();
                                Boolean result = dbClass.checkImageNameExists(getEditName + "." + imageExtension);
                                if (!result) {
                                    File folder = new File(Environment.getExternalStorageDirectory() + "/mySignalCapture");
                                    if (!folder.exists()) {
                                        folder.mkdir();
                                    }
                                    copyNewImage(imgFile, getEditName, imageExtension);
                                    Display display = getWindowManager().getDefaultDisplay();
                                    Point size = new Point();
                                    display.getSize(size);
                                    btmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(imageNewPath),size.x,size.y,true);
                                    ParameterGetSet.floorPlanBitmap = btmp;
                                    Drawable newImageDraw = new BitmapDrawable(btmp);
                                    selectedPicture.setBackground(newImageDraw);
                                    image_yvalue = selectedPicture.getHeight();
                                    image_xvalue = selectedPicture.getWidth();

                                    ParameterGetSet.image_xValue = image_xvalue;
                                    ParameterGetSet.image_yValue = image_yvalue;

                                    int screenWidth = metrics.widthPixels;
                                    int screenHeight = metrics.heightPixels;
                                    Bitmap decodedImage = decode(screenWidth-15,screenHeight-15);
                                    Drawable myDraw = new BitmapDrawable(decodedImage);

                                } else {
                                    editImageName.setError("ImageName already Exists");
                                }
                                extractDB();
                                dbClass.close();

                                getImageNameDialog.dismiss();
                            }
                        }
                    });

                    cancelImageName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getImageNameDialog.dismiss();
                            onBackPressed();
                        }
                    });
                    getImageNameDialog.show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Message.msgLong(getBaseContext(), "Make Free Your Phone Memory Space");
        }


        /* for wifi */
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifiInfo= wifiManager.getConnectionInfo();
        level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 20);

        if(savedInstanceState != null) {
            tmp = savedInstanceState.getParcelable(IMAGE_DATA);
            if (tmp != null) {
                drawable = new BitmapDrawable(getResources(), tmp);
                selectedPicture.setBackgroundDrawable(drawable);
                image_yvalue = selectedPicture.getHeight();
                image_xvalue = selectedPicture.getWidth();
                ParameterGetSet.image_xValue = image_xvalue;
                ParameterGetSet.image_yValue = image_yvalue;
            }
            pdfTmp = savedInstanceState.getParcelable(IMAGE_PDF_DATA);
            if (pdfTmp != null) {
                loadImgDrawable = new BitmapDrawable(getResources(), pdfTmp);
                selectedPicture.setBackgroundDrawable(loadImgDrawable);
                image_yvalue = selectedPicture.getHeight();
                image_xvalue = selectedPicture.getWidth();

                ParameterGetSet.image_xValue = image_xvalue;
                ParameterGetSet.image_yValue = image_yvalue;
            }
            Log.d("savedState called", "successfully called savedInstanceState");
        }

        selectedPicture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                if (!"".equals(iNetwork_Selection)) {
                    if ((iNetwork_Selection != null)) {
                        btn = new ImageView(getBaseContext());
                        x = (int) e.getX();
                        y = (int) e.getY();

                        rparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        int xAxis = x;
                        int yAxis = y;
                        rparams.leftMargin = xAxis;
                        rparams.topMargin = yAxis;
                        rparams.height = 48;
                        rparams.width = 60;

                        /* for wifi */
                        showLayout = (LinearLayout) dialog.findViewById(R.id.displayData);
                        /* for network */
                        showNetworkLayout = (LinearLayout) dialog.findViewById(R.id.displayMobileData);
                        /* for notepad */
                        notePadLayout = (LinearLayout) dialog.findViewById(R.id.notePadLayout);
                        /* for Camera */
                        cameraLayout = (LinearLayout) dialog.findViewById(R.id.cameraLayout);

                        setNetworkHint = (EditText) dialog.findViewById(R.id.getNetworkHint);
                        editNotePad = (EditText) dialog.findViewById(R.id.editNotePad);
                        cameraLocation = (EditText) dialog.findViewById(R.id.cameraLocation);

                        /* save and cancel btn */
                        btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                        btnFitPin = (Button) dialog.findViewById(R.id.btnFitPin);

                        switch (iNetwork_Selection) {
                            case "Wifi":
                                try {
                                    showLayout.setVisibility(View.VISIBLE);
                                    wifiOperatorName.setText(getPValue);
                                    wifiMacAddress.setText(getPBSSI);
                                    wifiSignalStr.setText(String.valueOf(signalStrengthNetwork) + "dBm");
                                    wifiMode.setText(wifiNetworkCode);

                                    dialog.show();
                                    saveSignalData();
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                                break;
                            case "Network":

                                try {
                                    showNetworkLayout.setVisibility(View.VISIBLE);
                                    networktech = getTechnology(telephonyManager);

                                    networkTechnology.setText(getTechnology(telephonyManager));
                                    netProviderName.setText(operatorName);
                                    networkCellId.setText(cellIdentity_int);
                                    networkCellSignalQuality.setText(signalQuality);
                                    networkSignalStr.setText(String.valueOf(psListener.signalStrengthValue) + " dBm");
                                    txtPhoneType.setText(getDeviceName());
                                    networkChannelNo.setText(String.valueOf(displayChannelNo()));

                                    dialog.show();
                                    saveSignalData();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                break;
                            case "Notepad":
                                try {
                                    notePadLayout.setVisibility(View.VISIBLE);
                                    dialog.show();
                                    saveSignalData();
                                } catch (Exception ne) {
                                    ne.printStackTrace();
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
                                    btn.setImageBitmap(mybitmap);
                                    btn.setId(i);
                                    btn.setLayoutParams(rparams);
                                    selectedPicture.addView(btn);

                                    /*x_btn = btn.getLeft();
                                    y_btn = btn.getTop();
                                    ParameterGetSet.image_xValue = x_btn;
                                    ParameterGetSet.image_yValue = y_btn;*/

                                    setValues = new ArrayList<String>();
                                    setValues.add("Drawing");
                                    setValues.add(String.valueOf(btn.getId()));
                                    setValues.add(String.valueOf(xAxis));
                                    setValues.add(String.valueOf(yAxis));
                                    insertParam();
                                    dbClass.open();
                                    dbClass.insertImagePath();
                                    extractDB();
                                    dbClass.close();


                                } catch (Exception dr) {
                                    dr.printStackTrace();
                                }
                                break;
                            case "Draw":
                                invalidateButton_Click(v);
                                break;
                        }
                        if (iNetwork_Selection.equals("Drawing")) {
                            iNetwork_Selection = "";
                            iNetwork_Selection.equals("");
                            i++;
                        } else {
                    /* Click the Cancel Button */
                            btnCancel.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    iNetwork_Selection = "";
                                    network_selection = "";
                                    dialog.dismiss();
                                }
                            });
                        }

                        btn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Dialog showStoredData = new Dialog(SelectImage.this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth);
                                showStoredData.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                                showStoredData.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                                showStoredData.setContentView(R.layout.dialog_display_stored_data);


                    /* For title */
                                TextView changeTitle = (TextView) showStoredData.findViewById(R.id.changeTitle);

                                wifiShowLayout = (LinearLayout) showStoredData.findViewById(R.id.wifiShowLayout);
                                networkShowLayout = (LinearLayout) showStoredData.findViewById(R.id.networkShowLayout);
                                notepadShowLayout = (LinearLayout) showStoredData.findViewById(R.id.popupShowLayout);
                                cameraShowLayout = (LinearLayout) showStoredData.findViewById(R.id.popupCameraShowLayout);
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

                    /*For Camera*/
                                showCapturedLocation = (TextView) showStoredData.findViewById(R.id.showPopupCamera);
                                showCapturedImage = (ImageView) showStoredData.findViewById(R.id.imgCameraCapture);

                    /*For Draw Pin*/
                                showDrawedImage = (ImageView) showStoredData.findViewById(R.id.imgDraw);

                                showPRscp = (TextView) showStoredData.findViewById(R.id.cellSignalStrength);
                                showPCid = (TextView) showStoredData.findViewById(R.id.cellCellId);
                                showPEcno = (TextView) showStoredData.findViewById(R.id.show_cellSignalQuality);
                                showPUarfcn = (TextView) showStoredData.findViewById(R.id.show_cellChannelNo);

                                pinid = v.getId();
                                network_selection = setValues.get(0);
                                getImageNameNetworkModeDB();

                                switch (network_selection) {
                                    case "Wifi":
                                        try {
                                            changeTitle.setText("Wifi Network");
                                            wifiShowLayout.setVisibility(View.VISIBLE);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case "Network":
                                        try {
                                            if (networktech.equals("3G")){
                                                showPRscp.setText(R.string.networkSignalStrRSCP);
                                                showPCid.setText(R.string.networkCellIdentifierPsc);
                                                showPEcno.setText(R.string.networkSignalQualityEcno);
                                                showPUarfcn.setText(R.string.networkChannelNumberUarfcn);
                                            } else if (networktech.equals("4G")){
                                                showPRscp.setText(R.string.networkSignalStrRSRP);
                                                showPCid.setText(R.string.networkCellIdentifierPCI);
                                                showPEcno.setText(R.string.networkSignalQualityRSRQ);
                                                showPUarfcn.setText(R.string.networkChannelNumbereUARFCN);
                                            }
                                            changeTitle.setText("Mobile Network");
                                            networkShowLayout.setVisibility(View.VISIBLE);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case "Notepad":
                                        try {
                                            changeTitle.setText("NotePad");
                                            notepadShowLayout.setVisibility(View.VISIBLE);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                    case "Camera":
                                        try {
                                            changeTitle.setText("Camera");
                                            cameraShowLayout.setVisibility(View.VISIBLE);
                                            Display display = getWindowManager().getDefaultDisplay();
                                            Point size = new Point();
                                            display.getSize(size);
                                            Bitmap btmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(igetCapturedPath), size.x, size.y, true);
                                            Drawable dBnewCapturedImage = new BitmapDrawable(getResources(),btmp);
                                            showCapturedImage.setImageDrawable(dBnewCapturedImage);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Message.msgLong(getBaseContext(),"Make Free Your Phone Memory Space");
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
                                            Drawable dBnewCapturedImage = new BitmapDrawable(getResources(),btmp);
                                            showDrawedImage.setImageDrawable(dBnewCapturedImage);

                                        }catch (Exception e){
                                            e.printStackTrace();
                                            Message.msgLong(getBaseContext(),"Make Free Your Phone Memory Space");
                                        }
                                        break;
                                }
                                network_selection = "";

                    /* For wifi */
                                getSetOperatorName.setText(iProvider_Detail);
                                getSetMacAddress.setText(iBssi);
                                getSetSignalStrength.setText(iSignal_Strength);
                                getSetFloorHint.setText(iPinMark_Hint);
                                getSetWifiMode.setText(iWiFimode);
                                getSetWifiFrequency.setText(iFrequency + " MHz");



                    /* For network */
                                getNetworkTechnology.setText(igetNetworkTechnology);
                                getNetworkProviderName.setText(igetNetworkProviderName);
                                getNetworkCellId.setText(igetNetworkCellId);
                                showSignalStr.setText(ishowSignalStr + " dBm");
                                showNetworkHint.setText(ishowNetworkHint);
                                getSignalQuality.setText(iSignal_Quality);
                                showNetworkChannelNo.setText(ishowChannelNo);

                    /* For Note Pad */
                                showCmtType.setText(iShowCmtType);
                                showPopupNote.setText(iShowPopupNote);

                    /*For Captured Image*/
                                showCapturedLocation.setText(iShowCapturedLocation);


                                showStoredData.show();

                                iNetworkSelectionMode = "";
                                iuser_name = "";
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
                                iShowCapturedLocation = "";
                                iNetwork_Selection = "";
                            }
                        });

                        btn.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(final View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SelectImage.this);
                                builder.setMessage("Are you Sure Want to Delect the Pointer?");
                                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            id = v.getId();
                                            if (id != 0) {
                                                v.setVisibility(View.GONE);
                                                selectedPicture.removeView(v);
                                            }

                                            dbClass.open();
                                            boolean value = dbClass.deleteRemovePin(imageNewPath, String.valueOf(id));
                                            if (value) {
                                                View myView = findViewById(id);
                                                ViewGroup parent = (ViewGroup) myView.getParent();
                                                parent.removeView(myView);
                                                /*String textValue = String.valueOf(txt.getText());
                                                Message.message(getBaseContext(), "" + textValue, Toast.LENGTH_SHORT);
                                                v.setVisibility(View.GONE);
                                                txt.setVisibility(View.GONE);*/
                                            } else {
                                                Message.message(getBaseContext(), "Pin Not Removed", Toast.LENGTH_SHORT);
                                            }
                                            dbClass.close();
                                        } catch (Exception dbEx) {
                                            dbEx.printStackTrace();
                                        }

                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();

                                return true;
                            }
                        });
                    }
                }
                    return false;
                }

        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        return super.onTouchEvent(e);
    }


    /* Adding the Pin Marker */

    public void saveSignalData() {
        btnFitPin.setOnClickListener(new OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
         dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
         switch (iNetwork_Selection) {
             case "Wifi":
                 try {
                     if (setHint.getText().toString().equals("")) {
                         setHint.setError("Please Enter the Location");
                     } else {
                         txt = new TextView(getBaseContext());
                         getHint = setHint.getText().toString();
                         btn.setId(i);
                         btn.setImageBitmap(bitmap);
                         btn.setLayoutParams(rparams);
                         txt.setLayoutParams(rparams);
                         btnId = btn.getId();

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
//                         txt.setText(ParameterGetSet.pinmark_Hint);
                         txt.setId(i);
                         txt.setTextAppearance(getBaseContext(), R.style.txtViewStyle);
                         txt.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                         txt.setTextColor(Color.BLACK);
                         i++;
                         selectedPicture.addView(txt);
                         selectedPicture.addView(btn);
                         /*x_btn = btn.getLeft();
                         y_btn = btn.getTop();
                         ParameterGetSet.image_xValue = x_btn;
                         ParameterGetSet.image_yValue = y_btn;*/
                         dbClass.open();
                         insertParam();
                         dbClass.insertImagePath();
                         dbClass.close();

                         extractDB();
                         dialog.dismiss();
                         iNetwork_Selection = "";
                     }
                 } catch (Exception e) {
                     e.printStackTrace();
                 }

                 break;
             case "Network":

                 try {
                     if (setNetworkHint.getText().toString().equals("")) {
                         setNetworkHint.setError("Please Enter the Location");
                     } else {
                         txt = new TextView(getBaseContext());
                         getNetworkHint = setNetworkHint.getText().toString();
                         btn.setId(i);
                         btn.setImageBitmap(towerBitmap);
                         btn.setLayoutParams(rparams);
                         txt.setLayoutParams(rparams);
                         btnId = btn.getId();

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
//                         txt.setText(ParameterGetSet.pinmark_Hint);
                         txt.setId(i);
                         txt.setTextAppearance(getBaseContext(), R.style.txtViewStyle);
                         txt.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                         txt.setTextColor(Color.BLACK);
                         i++;
                         selectedPicture.addView(btn);

                        /* x_btn = btn.getLeft();
                         y_btn = btn.getTop();
                         ParameterGetSet.image_xValue = x_btn;
                         ParameterGetSet.image_yValue = y_btn;*/
                         selectedPicture.addView(txt);
                         dbClass.open();
                         insertParam();
                         dbClass.insertImagePath();
                         dbClass.close();

                         extractDB();
                         dialog.dismiss();
                         iNetwork_Selection = "";
                     }
                 } catch (Exception e) {
                     e.printStackTrace();
                 }

                 break;
             case "Notepad":
                 try {
                     if (editNotePad.getText().toString().equals("")) {
                         editNotePad.setError("Please Enter your Comment");
                     } else {
                         txt = new TextView(getBaseContext());
                         btn.setId(i);
                         btn.setImageBitmap(notePadBitmap);
                         btn.setLayoutParams(rparams);
                         txt.setLayoutParams(rparams);
                         btnId = btn.getId();

                         setValues = new ArrayList<String>();
                         setValues.add("Notepad");
                         setValues.add(editNotePad.getText().toString());
                         setValues.add(String.valueOf(btnId));
                         setValues.add(String.valueOf(x));
                         setValues.add(String.valueOf(y));
                         ParameterGetSet.pinmark_Hint = editNotePad.getText().toString();
//                         txt.setText(ParameterGetSet.pinmark_Hint);
                         txt.setId(i);
                         txt.setTextAppearance(getBaseContext(), R.style.txtViewStyle);
                         txt.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                         txt.setTextColor(Color.BLACK);
                         i++;
                         selectedPicture.addView(btn);
                         selectedPicture.addView(txt);

                         /*x_btn = btn.getLeft();
                         y_btn = btn.getTop();
                         ParameterGetSet.image_xValue = x_btn;
                         ParameterGetSet.image_yValue = y_btn;*/
                         dbClass.open();
                         insertParam();
                         dbClass.insertImagePath();
                         dbClass.close();

                         extractDB();
                         dialog.dismiss();
                         iNetwork_Selection = "";
                     }
                     break;
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 break;
             case "Camera":
                 try {
                     if (cameraLocation.getText().toString().equals("")) {
                         cameraLocation.setError("Please Enter your Comment");
                     } else {
                         txt = new TextView(getBaseContext());
                         btn.setId(i);
                         btn.setImageBitmap(cameraBitmap);
                         btn.setLayoutParams(rparams);
                         txt.setLayoutParams(rparams);
                         btnId = btn.getId();

                         setValues = new ArrayList<String>();
                         setValues.add("Camera");
                         setValues.add(cameraLocation.getText().toString());
                         setValues.add(String.valueOf(btnId));
                         setValues.add(String.valueOf(x));
                         setValues.add(String.valueOf(y));
                         ParameterGetSet.pinmark_Hint = cameraLocation.getText().toString();
//                         txt.setText(ParameterGetSet.pinmark_Hint);
                         txt.setId(i);
                         txt.setTextAppearance(getBaseContext(), R.style.txtViewStyle);
                         txt.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                         txt.setTextColor(Color.BLACK);
                         i++;
                         selectedPicture.addView(btn);
                         selectedPicture.addView(txt);

                         /*x_btn = btn.getLeft();
                         y_btn = btn.getTop();
                         ParameterGetSet.image_xValue = x_btn;
                         ParameterGetSet.image_yValue = y_btn;*/
                         dbClass.open();
                         insertParam();
                         dbClass.insertImagePath();
                         extractDB();
                         dbClass.close();
                         dialog.dismiss();
                         iNetwork_Selection = "";
                     }
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 break;
         }
             }
         }

        );
        }
    /* Close adding Pin Marker */


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public Bitmap decode( int width,int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageNewPath,options);
        int imgHeight = options.outHeight;
        int imgWidth = options.outWidth;
        int sampleSize=1;
        if(width< imgWidth || height<imgHeight ){
            int widthRatio=Math.round(imgWidth/(float)width);
            int heightRatio=Math.round(imgHeight/(float)height);
            sampleSize=widthRatio<heightRatio?widthRatio:heightRatio;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize=sampleSize;
        return BitmapFactory.decodeFile(imageNewPath, options);
    }


    /* Copy Selected  Image */
    private void copyNewImage(File imgFile, String getEditName, String imageExtension) {
        byte[] buf = new byte[1024];
        int len;
        try {
            InputStream in = new FileInputStream(imgFile);
            OutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory() + "/mySignalCapture/"+getEditName+"."+imageExtension);
            imageNewPath = Environment.getExternalStorageDirectory() +"/mySignalCapture/"+getEditName+"."+imageExtension;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            dbClass.open();
            Cursor readData = dbClass.checkImage(setUserName);
            readData.moveToFirst();
            if (readData.getCount() == 0) {
                setValues = new ArrayList<>();
                setValues.add("");
                insertParam();
                dbClass.insertImagePath();
                extractDB();
                dbClass.close();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
    /* /Close copy Selected  Image */


    /* class WifiReceiver */

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

            AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(SelectImage.this);
            alertDialogBuilder.setView(viewAlert);

            final AlertDialog alertDialog= alertDialogBuilder.create();
            final ListView wifiPopdetail=(ListView)viewAlert.findViewById(R.id.user_wifi_detail);

            for (int i = 0; i < scanwifilist.size(); i++) {
                availableWifiList[i]= (scanwifilist.get(i).SSID);
            }

            wifiPopdetail.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, availableWifiList));
            if (wifiPopdetail.isEnabled()){
                pdialog.dismiss();
            }

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
        pdialog=new ProgressDialog(this);
        pdialog.setCancelable(true);
        pdialog.setMessage("Loading ....");
        pdialog.show();
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


            scanNetwork.setOnClickListener(new OnClickListener() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*public void getCellID(){
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
    }*/
    private int displayChannelNo() {
        String operatorName = telephonyManager.getNetworkOperatorName();
        dbClass.open();
        Cursor dataCursor = dbClass.readData(operatorName);
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


    private String getphoneType(TelephonyManager telephonyManager) {
        int phoneType = telephonyManager.getPhoneType();
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_GSM:
                return "GSM";
            case TelephonyManager.PHONE_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.PHONE_TYPE_SIP:
                return "LTE";
            case TelephonyManager.PHONE_TYPE_NONE:
                return "None";
            default:
                return "Unknown Phone type";
        }
    }

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


    public class myPhoneStateListener extends PhoneStateListener {

        public int signalStrengthValue;

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            try {
                String[] signalstrength = signalStrength.toString().split(" ");
                LTErsrq = Integer.valueOf(signalstrength[10]);  // LTE - 4G SignalQuality

                if(telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
                    LTErsrp = Integer.valueOf(signalstrength[8]); // LTE - 4G SignalStrength
                    signalStrengthValue = LTErsrp * 2 - 113; // LTE - 4G SignalStrength
                    signalQuality = String.valueOf(LteRsrq(LTErsrq));
                }

                if (signalStrength.isGsm()){
                    if(signalStrength.getGsmSignalStrength() != 99) {
                        signalStrengthValue = -113 + 2 * signalStrength.getGsmSignalStrength(); // 3G SignalStrength

                    } else {
                        signalStrengthValue = signalStrength.getGsmSignalStrength();  // 3G SignalStrength
                    }
                }

//               CDMA SIGNAL
                /*if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_CDMA){
                    signalStrengthValue = signalStrength.getCdmaDbm();  // CDMA SignalStrength
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
                }*/

            } catch (Exception e) {
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

    private String getTechnology(TelephonyManager telephonyManager) {

        try {
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
        } catch (Exception e) {
            Message.msgShort(this, "Error : " + e.getMessage());
        }

        return "Unknown Network";
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void onCellsChanged(List<CellInfo> cellInfoList) {
        for(CellInfo cellInfo : cellInfoList){
            if (cellInfo instanceof CellInfoLte) {
                CellIdentityLte identity1 =  ((CellInfoLte) cellInfo).getCellIdentity();
                pci = identity1.getPci();
            }else if(cellInfo instanceof  CellInfoWcdma){
                CellIdentityWcdma cellInfoWcdma = ((CellInfoWcdma)cellInfo).getCellIdentity();
                psc = cellInfoWcdma.getPsc();
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selectimage, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically hnandle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int flagWifi=0,flagNw=0,flagNotePad=0,flagStick=0;
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.wifiOperators:
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    iNetwork_Selection = "Wifi";
                    wifiManager.setWifiEnabled(true);

                    dialog = new Dialog(SelectImage.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_wifi_store);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                    wifiOperatorName = (TextView) dialog.findViewById(R.id.wifiOperatorName);
                    wifiMacAddress = (TextView) dialog.findViewById(R.id.wifiMacAddress);
                    wifiSignalStr = (TextView) dialog.findViewById(R.id.wifiSignalStr);
                    wifiMode = (TextView) dialog.findViewById(R.id.wifiMode);

                    setHint = (EditText) dialog.findViewById(R.id.getHintName);
                    scaning();
                    flagWifi=1;
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.networkOperators:

                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    dialog = new Dialog(SelectImage.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    dialog.setContentView(R.layout.dialog_network_store);

                    iNetwork_Selection = "Network";

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
                    flagNw=1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.notepad:
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    dialog = new Dialog(SelectImage.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_wifiphone_networks);
                    dialog.setTitle(R.string.notePad);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                    iNetwork_Selection = "Notepad";
                    flagNotePad=1;
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.save:
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    dbClass.open();
                    combineImages(ParameterGetSet.floorPlanBitmap, ParameterGetSet.drawingBitmap);
                    dbClass.updateColumn(ParameterGetSet.image_Name);
                    extractDB();
                    Cursor readData = dbClass.checkImage(setUserName);
                    readData.moveToFirst();
                    if (readData.getCount() == 0) {
                        Message.message(getBaseContext(), "No Pin Head Placed", Toast.LENGTH_LONG);
                        setValues = new ArrayList<>();
                        setValues.add("");
                        insertParam();
                        dbClass.insertImagePath();
                        extractDB();
                        dbClass.close();
                    }
                    onBackPressed();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.pdfSelector:
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent = new Intent(SelectImage.this, PdfScreenShot.class);
                        Helper.imageFilePath = "PdfFileClick";
                        startActivity(intent);
                    } else {
                        Message.message(getBaseContext(), "Pdf Capture Not Support your Device", Toast.LENGTH_SHORT);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.PlansViewer:
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    Intent planViewerIntent = new Intent(SelectImage.this, ResultOfSignalCapture.class);
                    startActivity(planViewerIntent);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.BuildingInfo:
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    Helper.imageFrom = "PathLoadImage";
                    ResultViewBuilding resultViewBuilding = new ResultViewBuilding();
                    resultViewBuilding.show(getFragmentManager(), "My Building View");
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            case R.id.cameraDialog:
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    iNetwork_Selection = "Camera";
                    getPlanPartName = new Dialog(this);
                    dialog = new Dialog(SelectImage.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_camera_store);
                    dialog.setTitle(R.string.floorCapture);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri fileUri = getOutputMediaFileUri(CAMERA_REQUEST);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); //set Image file name
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                }

                return true;
            /*case R.id.customDrawing :
                try {
                    DrawOnFloorPlan.DrawBitmap = null;
                    iNetwork_Selection = "Drawing";
                    dialog = new Dialog(SelectImage.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    CustomDrawing customDrawing = new CustomDrawing();
                    customDrawing.show(getFragmentManager(), "My Custom Drawing");
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;*/
            case R.id.drawing :
                try {
                    sketchLayout.setVisibility(View.VISIBLE);
                    iNetwork_Selection = "Draw";
                    dialog = new Dialog(SelectImage.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_wifiphone_networks);
                    dialog.setTitle(R.string.notePad);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    drawOnFloorPlan = new DrawOnFloorPlan(getBaseContext());
                    selectedPicture.addView(drawOnFloorPlan);
                    drawOnFloorPlan.destroyDrawingCache();
                    flagStick=1;
                }catch (Exception e){
                    e.printStackTrace();
                }

                return true;
            /*case R.id.drawingSave :
                try{
                    DrawOnFloorPlan.DrawBitmap = null;
                    dialog = new Dialog(SelectImage.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_drawimage_paint);
                    iNetwork_Selection = "DrawSave";
                    btnFitPin = (Button)dialog.findViewById(R.id.btnFitPin);
                    btnCancel = (Button)dialog.findViewById(R.id.btnCancel);
                    dialog.show();
                    btnFitPin.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                combineImages(ParameterGetSet.floorPlanBitmap, ParameterGetSet.drawingBitmap);
                                drawOnFloorPlan.clear();
                                drawOnFloorPlan = new DrawOnFloorPlan(getBaseContext());
                                dbClass.open();
                                dbClass.updateColumn(ParameterGetSet.image_Name);
                                extractDB();
                                Display display = getWindowManager().getDefaultDisplay();
                                Point size = new Point();
                                display.getSize(size);
                                btmp = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(ParameterGetSet.floor_Image_Path), size.x, size.y, true);
                                ParameterGetSet.floorPlanBitmap = btmp;
                                Drawable newImageDraw = new BitmapDrawable(btmp);
                                selectedPicture.setBackground(newImageDraw);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    btnCancel.setOnClickListener(new OnClickListener() {
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
//                    DrawOnFloorPlan.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    drawOnFloorPlan.clear();
                    drawErase = 1;
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;*/
           /* case R.id.drawingDelete :
                DrawOnFloorPlan.mPaint.setXfermode(null);
                return true;*/
            case R.id.syncDetail:
                try{
                    dialog = new Dialog(SelectImage.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.layout_sync_detail);
                    dialog.setTitle(R.string.serverSyncTitle);
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    connectServiceSample();
                    /*readFullRowDetail();
                    imageFileUpload();*/
                    EditText ipAddress = (EditText)dialog.findViewById(R.id.txtIpAddress);
                    Button syncDetail = (Button)dialog.findViewById(R.id.btnSync);
                    syncDetail.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpPost httpPost = new HttpPost("http://192.168.1.7/");

//                            httpPost.setEntity(new UrlEncodedFormEntity());
                        }
                    });
//                    dialog.show();

                }catch (Exception e){
                    e.printStackTrace();
                }
            default:
                return super.onOptionsItemSelected(item);

        }
    }




    /*public static String convertStreamToString(InputStream is)
    {
        BufferedReader bs = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String Line = "";

        try
        {
            while((Line = bs.readLine()) != null)
            {
                sb.append(Line  + "/n");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
*/
    public void connectServiceSample(){
        try{
            //System.out.println("method called");
//            jsonObject = new JSONObject();
            Toast.makeText(getBaseContext(), "connectedTo server", Toast.LENGTH_LONG);
            objJSONObjectForuser = new JSONObject();



            objJSONObjectForuser.put("keyID","1");
            objJSONObjectForuser.put("user_Name",   "rajasekar");
            objJSONObjectForuser.put("lastName",   "D://plan2.jpg");
            objJSONObjectForuser.put("contactNo",   "plan2.jpg");
            objJSONObjectForuser.put("emailAddress","wifi");
            objJSONObjectForuser.put("address1",   "1");
            objJSONObjectForuser.put("address2",   "airtel");
            objJSONObjectForuser.put("city",   "55");
            objJSONObjectForuser.put("state",   "63");
            objJSONObjectForuser.put("zip",   "wpf");
            objJSONObjectForuser.put("Floor_Image_Path",   "12");
            objJSONObjectForuser.put("Image_Name",   "3g");
            objJSONObjectForuser.put("Network_Selection",   "12345678");
            objJSONObjectForuser.put("Pin_Id",   "good");
            objJSONObjectForuser.put("Provider_Detail",   "location");
            objJSONObjectForuser.put("Bssi",   "250");
            objJSONObjectForuser.put("Signal_Strength",   "360");

            objJSONObjectForuser.put("Frequency",   "wpf");
            objJSONObjectForuser.put("WiFimode",   "12");
            objJSONObjectForuser.put("Channel_Number",   "3g");
            objJSONObjectForuser.put("Network_Technology",   "12345678");
            objJSONObjectForuser.put("Cell_Id",   "good");
            objJSONObjectForuser.put("Signal_Quality",   "location");
            objJSONObjectForuser.put("PinMark_Hint",   "250");
            objJSONObjectForuser.put("Camera_LocationPath",   "360");

            objJSONObjectForuser.put("Camera_LocationName",   "wpf");
            objJSONObjectForuser.put("XValue",   "12");
            objJSONObjectForuser.put("YValue",   "3g");
            objJSONObjectForuser.put("dateTime",   "12345678");


            HttpClient httpclient=new DefaultHttpClient();
            HttpResponse reponse;
            HttpPost httppost  = new HttpPost("http://192.168.1.11:8080/SignalCapture_Service/rest/signal/shareValue");
            HttpEntity entity;

            Log.i("JSON:", objJSONObjectForuser.toString());

            StringEntity se = new StringEntity(objJSONObjectForuser.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setHeader("content-type", "application/json");
            httppost.setEntity(se);
            reponse = httpclient.execute(httppost);
            entity = reponse.getEntity();

            if(entity != null)
            {
                InputStream is = entity.getContent();
                String result1 = convertStreamToString(is);
                is.close();

                JSONObject objJSONResponse  = new JSONObject(result1);

                if(objJSONResponse.getString("Response").equalsIgnoreCase("Data Uploaded SuccessFully"))
                {
                    Toast.makeText(getBaseContext(), "Server Side Data Uploaded SuccessFully!",Toast.LENGTH_LONG).show();
                }

            }



        }catch(Exception e){
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG);
            e.printStackTrace();
        }

    }
    public static String convertStreamToString(InputStream is)
    {
        BufferedReader bs = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String Line = "";

        try
        {
            while((Line = bs.readLine()) != null)
            {
                sb.append(Line  + "/n");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public void readFullRowDetail(){
        dbClass.open();
        int status = 0;
        Cursor readAllRecord = dbClass.readFullRow(status);

        if (readAllRecord != null){
            readAllRecord.moveToFirst();
            do{
                User_Name = readAllRecord.getString(0);
                XValue = readAllRecord.getString(25);
                if (XValue == null){
                    readAllRecord.moveToNext();
                }else
                    lastName = readAllRecord.getString(1);
                    contactNo = readAllRecord.getString(2);
                    emailAddress= readAllRecord.getString(3);
                    address1 = readAllRecord.getString(4);
                    address2 = readAllRecord.getString(5);
                    city = readAllRecord.getString(6);
                    state = readAllRecord.getString(7);
                    zip = readAllRecord.getString(8);
                    Floor_Image_Path = readAllRecord.getString(9);
                    Image_Name = readAllRecord.getString(10);
                    Network_Selection = readAllRecord.getString(11);
                    Pin_Id = readAllRecord.getString(12);
                    Provider_Detail = readAllRecord.getString(13);
                    Bssi = readAllRecord.getString(14);
                    Signal_Strength = readAllRecord.getString(15);
                    Frequency = readAllRecord.getString(16);
                    WiFimode = readAllRecord.getString(17);
                    Channel_Number = readAllRecord.getString(18);
                    Network_Technology = readAllRecord.getString(19);
                    Cell_Id = readAllRecord.getString(20);
                    Signal_Quality = readAllRecord.getString(21);
                    PinMark_Hint = readAllRecord.getString(22);
                    Camera_LocationPath = readAllRecord.getString(23);
                    Camera_LocationName = readAllRecord.getString(24);
                    XValue = readAllRecord.getString(25);
                    YValue = readAllRecord.getString(26);
                    dateTime = readAllRecord.getString(27);
                    keyID = readAllRecord.getString(28);
                syncDetails();
            }while (readAllRecord.moveToNext());
            readAllRecord.moveToLast();
        }
        readAllRecord.moveToFirst();

        dbClass.close();


    }

    public void syncDetails(){
        jsonObject = new JSONObject();

        try {
            jsonObject.put("keyID",keyID);
            jsonObject.put("User_Name",User_Name);
            jsonObject.put("lastName",lastName);
            jsonObject.put("contactNo",contactNo);
            jsonObject.put("emailAddress",emailAddress);
            jsonObject.put("address1",address1);
            jsonObject.put("address2",address2);
            jsonObject.put("city",city);
            jsonObject.put("state",state);
            jsonObject.put("zip",zip);
            jsonObject.put("Floor_Image_Path",Floor_Image_Path);
            jsonObject.put("Image_Name",Image_Name);
            jsonObject.put("Network_Selection",Network_Selection);
            jsonObject.put("Pin_Id",Pin_Id);
            jsonObject.put("Provider_Detail",Provider_Detail);
            jsonObject.put("Bssi",Bssi);
            jsonObject.put("Signal_Strength",Signal_Strength);
            jsonObject.put("Frequency",Frequency);
            jsonObject.put("WiFimode",WiFimode);
            jsonObject.put("Channel_Number",Channel_Number);
            jsonObject.put("Network_Technology",Network_Technology);
            jsonObject.put("Cell_Id",Cell_Id);
            jsonObject.put("Signal_Quality",Signal_Quality);
            jsonObject.put("PinMark_Hint",PinMark_Hint);
            jsonObject.put("Camera_LocationPath",Camera_LocationPath);
            jsonObject.put("Camera_LocationName",Camera_LocationName);
            jsonObject.put("XValue",XValue);
            jsonObject.put("YValue",YValue);
            jsonObject.put("dateTime",dateTime);


            HttpClient httpclient=new DefaultHttpClient();
            HttpResponse reponse;
            HttpPost httppost  = new HttpPost("http://192.168.1.7:8080/SignalCapture_Service/rest/signal/shareValue");
            HttpEntity entity;

            Log.i("JSON:", jsonObject.toString());

            try {


                StringEntity se = new StringEntity(jsonObject.toString());
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setHeader("content-type", "application/json");
                httppost.setEntity(se);
                reponse = httpclient.execute(httppost);
                entity = reponse.getEntity();

                if (entity != null) {
                    InputStream is = entity.getContent();
                    String result1 = convertStreamToString(is);
                    is.close();

                    JSONObject objJSONResponse = new JSONObject(result1);

                    if (objJSONResponse.getString("Response").equalsIgnoreCase("Data Uploaded SuccessFully")) {
                        Toast.makeText(getBaseContext(), "Server Side Data Uploaded SuccessFully!", Toast.LENGTH_LONG).show();
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
   /* public static String convertStreamToString(InputStream is)
    {
        BufferedReader bs = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String Line = "";

        try
        {
            while((Line = bs.readLine()) != null)
            {
                sb.append(Line  + "/n");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
*/

    public void imageFileUpload(){

        String selectedPath2 = ParameterGetSet.floor_Image_Path;

        Bitmap bitmap = BitmapFactory.decodeFile(ParameterGetSet.floor_Image_Path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte [] byte_arr = stream.toByteArray();

        final File file1 = new File(selectedPath2);

        String urlString = "http://192.168.1.7:8080/TestProjects/rest/signal/multiPartUpload";

        try
        {
            String image_str = new String(byte_arr,"UTF-8");
            ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair(selectedPath2, image_str));

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = client.execute(post);
//            String the_string_response = convertResponseToString(response);


           /* JSONObject objJSONResponse  = new JSONObject(the_string_response);

            if(objJSONResponse.getString("Response").equalsIgnoreCase("Inserted Sucessfully!"))
            {
                //---display response---
                Toast.makeText(getApplicationContext(), "Inserted Sucessfully!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //---display response---
                Toast.makeText(getApplicationContext(), "Not Inserted!", Toast.LENGTH_SHORT).show();
            }
*/
            HttpEntity resEntity = response.getEntity();
            final String response_str = EntityUtils.toString(resEntity);
            if (resEntity != null) {
                Log.i("RESPONSE",response_str);
                runOnUiThread(new Runnable(){
                    public void run() {
                        try {

                            //upload entry
                            JSONObject objJSONRequest = new JSONObject();

                            //request
                            objJSONRequest.put("Sqlbuffer", "INSERT INTO upload");

                            Log.i("JSON:", objJSONRequest.toString());

                            //dispatch to web service to insert user details
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpResponse reponse;

                            //Toast.makeText(getApplicationContext(), "http://192.168.1.6:8080/TestProjects/rest/activityservice/putData", Toast.LENGTH_SHORT).show();

                            HttpPost httppost  = new HttpPost("http://192.168.1.7:8080/TestProjects/rest/activityservice/putData");
                            HttpEntity entity;

                            Log.i("JSON:", objJSONRequest.toString());

                            InputStream objInputStream = null;

                            try
                            {
                                StringEntity se = new StringEntity(objJSONRequest.toString());
                                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                                httppost.setHeader("content-type", "application/json");
                                httppost.setEntity(se);
                                reponse = httpclient.execute(httppost);
                                entity = reponse.getEntity();

                                if(entity != null)
                                {
                                    objInputStream = entity.getContent();
                                    String result = convertStreamToString(objInputStream);
                                    JSONObject objJSONResponse  = new JSONObject(result);

                                    if(objJSONResponse.getString("Response").equalsIgnoreCase("Inserted Sucessfully!"))
                                    {
                                        //---display response---
                                        Toast.makeText(getApplicationContext(), "Inserted Sucessfully!", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        //---display response---
                                        Toast.makeText(getApplicationContext(), "Not Inserted!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();

                                Toast.makeText(getApplicationContext(), "Exception :" + e, Toast.LENGTH_SHORT).show();
                            }
                            finally
                            {
                                objInputStream.close();
                            }

                            Toast.makeText(getApplicationContext(),"Upload Complete. Check the server uploads directory.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    /*        FileBody bin1 = new FileBody(file1);
            MultipartEntity reqEntity = new MultipartEntity();
            reqEntity.addPart("uploadedfile1", bin1);
            reqEntity.addPart("Notes:", new StringBody("fileUploaded Successfully"));
            post.setEntity(reqEntity);
            HttpResponse response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            final String response_str = EntityUtils.toString(resEntity);
            if (resEntity != null) {
                Log.i("RESPONSE",response_str);
                runOnUiThread(new Runnable(){
                    public void run() {
                        try {

                            //upload entry
                            JSONObject objJSONRequest = new JSONObject();

                            //request
                            objJSONRequest.put("Sqlbuffer", "INSERT INTO upload");

                            Log.i("JSON:", objJSONRequest.toString());

                            //dispatch to web service to insert user details
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpResponse reponse;

                            //Toast.makeText(getApplicationContext(), "http://192.168.1.6:8080/TestProjects/rest/activityservice/putData", Toast.LENGTH_SHORT).show();

                            HttpPost httppost  = new HttpPost("http://192.168.1.6:8080/TestProjects/rest/activityservice/putData");
                            HttpEntity entity;

                            Log.i("JSON:", objJSONRequest.toString());

                            InputStream objInputStream = null;

                            try
                            {
                                StringEntity se = new StringEntity(objJSONRequest.toString());
                                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                                httppost.setHeader("content-type", "application/json");
                                httppost.setEntity(se);
                                reponse = httpclient.execute(httppost);
                                entity = reponse.getEntity();

                                if(entity != null)
                                {
                                    objInputStream = entity.getContent();
                                    String result = convertStreamToString(objInputStream);
                                    JSONObject objJSONResponse  = new JSONObject(result);

                                    if(objJSONResponse.getString("Response").equalsIgnoreCase("Inserted Sucessfully!"))
                                    {
                                        //---display response---
                                        Toast.makeText(getApplicationContext(), "Inserted Sucessfully!", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        //---display response---
                                        Toast.makeText(getApplicationContext(), "Not Inserted!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();

                                Toast.makeText(getApplicationContext(), "Exception :" + e, Toast.LENGTH_SHORT).show();
                            }
                            finally
                            {
                                objInputStream.close();
                            }

                            Toast.makeText(getApplicationContext(),"Upload Complete. Check the server uploads directory.", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }*/
        }}
        catch (Exception ex){
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
    }
    public String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException{

        String res = "";
        StringBuffer buffer = new StringBuffer();
        inputStream = response.getEntity().getContent();
        final int contentLength = (int) response.getEntity().getContentLength(); //getting content length…..
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(SelectImage.this, "contentLength : " + contentLength, Toast.LENGTH_LONG).show();
            }
        });

        if (contentLength < 0){
        }
        else{
            byte[] data = new byte[512];
            int len = 0;
            try
            {
                while (-1 != (len = inputStream.read(data)) )
                {
                    buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbuffer…..
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                inputStream.close(); // closing the stream…..
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            res = buffer.toString();     // converting stringbuffer to string…..

            final String finalRes = res;
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(SelectImage.this, "Result : " + finalRes, Toast.LENGTH_LONG).show();
                }
            });
            //System.out.println("Response => " +  EntityUtils.toString(response.getEntity()));
        }
        return res;
    }

    /*Compine Two Bitmap Image*/
    public Bitmap combineImages(Bitmap c, Bitmap s) {

        Bitmap bmOverlay = Bitmap.createBitmap(c.getWidth(), c.getHeight(), c.getConfig());
        Bitmap newResizedBitmap = Bitmap.createScaledBitmap(s,c.getWidth(),c.getHeight(),false);
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(c, new Matrix(), null);
        canvas.drawBitmap(newResizedBitmap, 0, 0, null);
        String root = Environment.getExternalStorageDirectory() + "/mySignalCapture";
        File myDir = new File(root + "/sketchDrawing");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        try {
        String drawingName = ParameterGetSet.building_name +".png";
        File file = new File (myDir, drawingName);
        if (file.exists ()) file.deleteOnExit();

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
    public void invalidateButton_Click (View v)
    {
        selectedPicture.invalidate();
    }
    private File getOutputMediaFile(int type) {
        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory("mySignalCapture"),"CameraApp");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("CameraApp","Failed to create Directory");
                return null;
            }
        }

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);

        if (type == CAMERA_REQUEST) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "cam_" + n + ".jpg");

            String cameraImgName = mediaFile.getName();
            ParameterGetSet.camera_location_name = cameraImgName;
            ParameterGetSet.camera_location_path = mediaFile.getPath();

        } else {
            mediaFile = null;
        }
        return mediaFile;
    }
    private void getImageNameNetworkModeDB() {
        try {
            dbClass.open();
            Cursor readDataDB = dbClass.readNetworkMode(setUserName, pinid);
            if (readDataDB.moveToNext()) {
                network_selection = readDataDB.getString(1);
                switch (network_selection) {
                    case "Wifi":
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
                        break;
                }
            }

            pinid = -1;
            readDataDB.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public class drawonImage extends View {

        boolean freeTouched = false;
        Path freePath;

        Bitmap myCanvasBitmap = null;
        Canvas myCanvas = null;

        Matrix identityMatrix;

        public drawonImage(Context context) {
            super(context);
        }

        public drawonImage(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public drawonImage(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            if(freeTouched){
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.RED);
                paint.setStrokeWidth(10);

                myCanvas.drawPath(freePath, paint);
                canvas.drawBitmap(myCanvasBitmap, identityMatrix, null);

            }
        }

      /*  @Override
        public boolean onTouchEvent(MotionEvent event) {

            switch(event.getAction()){
                case MotionEvent.ACTION_UP:
                    freeTouched = false;
                    break;
                case MotionEvent.ACTION_DOWN:
                    freeTouched = true;
                    freePath = new Path();
                    freePath.moveTo(event.getX(), event.getY());

                    myCanvasBitmap.eraseColor(Color.BLACK);

                    break;
                case MotionEvent.ACTION_MOVE:
                    freePath.lineTo(event.getX(), event.getY());
                    invalidate();
                    break;
            }

            return true;
        }*/

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

            int w = MeasureSpec.getSize(widthMeasureSpec);
            int h = MeasureSpec.getSize(heightMeasureSpec);

            myCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            myCanvas = new Canvas();
            myCanvas.setBitmap(myCanvasBitmap);

            identityMatrix = new Matrix();

            setMeasuredDimension(w, h);
        }

        public Bitmap getCanvasBitmap(){

            return myCanvasBitmap;

        }

    }


    public void insertParam() {
        ParameterGetSet.floor_Image_Path = imageNewPath;
        ParameterGetSet.image_Name = setUserName;
        ParameterGetSet.valuesHash = setValues;
    }

    /* Extract Device Database to local folder */
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
}
