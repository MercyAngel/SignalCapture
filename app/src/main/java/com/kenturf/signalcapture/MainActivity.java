package com.kenturf.signalcapture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Context context = this;
    TextView putUserName;
    Button pdfScrshot,loadImg,loadRslt;
    private static final int RESULT_LOAD_IMG = 1;
    private static final int RESULT_RESULT_IMG = 0;
    final int ACTIVITY_CHOOSE_FILE = 2;
    private String showUserName,str_UserLastName,str_UserContactNumber,str_UserEmailAddress;
    public static final String SELECTED_IMG_PATH = "com.kenturf.signalcapture.IMAGE_PATH";
    public static final String SELECTED_RESULT_IMAGE_PATH = "com.kenturf.signalcapture.IMAGE_PATH";
    static final String USERNAME = "appUsername";
    String floorImageName;
    EditText getName,getLastName,getContactNumber,getEmailAddress;
    public Uri uri;
    String myUserName,myLastName,myContactNumber,myEmailId;
    DBClass myDataBase;
    CircleButton selectPDF,loadPlan,viewPlans,syncButton,serverDetails;
    Button okButton,cancelButton;
    AlertDialog alertDialog;
    DialogInterface dialog;
    private boolean doubleBackToExitPressedOnce = false;
    XmlResourceParser parser;
    ProgressDialog progressDialog;
    JSONObject objJSONObjectForuser;

    ArrayList<String> list;
    HashMap<Integer, ArrayList<String>> syncDataHash;

    String ipaddress;

    String User_Name,lastName,contactNo,emailAddress,address1,address2,city,state,zip,Floor_Image_Path,Image_Name,Network_Selection,Pin_Id,Provider_Detail,Bssi,Signal_Strength, Frequency,
            WiFimode,Channel_Number,Network_Technology,Cell_Id,Signal_Quality, PinMark_Hint,Camera_LocationPath,Camera_LocationName,XValue,YValue,Image_XValue,Image_YValue,dateTime,keyID;
    JSONObject jsonObject;

    EditText txt_ipaddress;
    String ipaddress_db;
    String urlServer;
    private ActionBarDrawerToggle myActionBarDrawerToggle;
    int serverResponseCode = 0;
    String upLoadServerUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        try {

            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            DrawerLayout myDrawerLayout = (DrawerLayout)findViewById(R.id.mDrawer_layout);
            myActionBarDrawerToggle = new ActionBarDrawerToggle(this,myDrawerLayout,R.string.drawer_open,R.string.drawer_close) {

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.drawer_open);
                        invalidateOptionsMenu();
                    }
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.drawer_close);
                        invalidateOptionsMenu();
                    }
                }
            };

            myDrawerLayout.setDrawerListener(myActionBarDrawerToggle);

            selectPDF = (CircleButton)findViewById(R.id.button0);
            loadPlan = (CircleButton)findViewById(R.id.button1);
            viewPlans = (CircleButton)findViewById(R.id.button2);
            syncButton = (CircleButton)findViewById(R.id.btnSync);
            serverDetails = (CircleButton)findViewById(R.id.ipDetail);

            selectPDF.setOnClickListener(this);
            loadPlan.setOnClickListener(this);
            viewPlans.setOnClickListener(this);
            syncButton.setOnClickListener(this);
            serverDetails.setOnClickListener(this);


            myDataBase = new DBClass(this);
            parser = getResources().getXml(R.xml.bands);

            putUserName = (TextView)findViewById(R.id.showUName);

            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.dialogbox, null);

            File dir = new File(Environment.getExternalStorageDirectory() + "/mySignalCapture/");
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0;i > children.length;i++) {
                    new File(dir,children[i]).delete();
                }
            }

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptsView);

            getName = (EditText)promptsView.findViewById(R.id.userName);
            getLastName = (EditText)promptsView.findViewById(R.id.userSecondName);
            getContactNumber = (EditText)promptsView.findViewById(R.id.contactNumber);
            getEmailAddress = (EditText)promptsView.findViewById(R.id.userEmailAddress);
            syncDataHash = new HashMap<>();

            myDataBase.open();

            Cursor appUserName = myDataBase.getUserNameIfExists();
            if (appUserName !=  null) {
                appUserName.moveToFirst();
                if (appUserName.getCount() > 0) {
                    appUserName.moveToLast();
                    myUserName = appUserName.getString(0);
                    myLastName = appUserName.getString(1);
                    myContactNumber = appUserName.getString(2);
                    myEmailId = appUserName.getString(3);
                    showDialog(alertDialogBuilder);
                    okButton = (Button)alertDialog.findViewById(R.id.okBtn);
                    cancelButton = (Button)alertDialog.findViewById(R.id.cancelBtn);
                    getName.setText(myUserName);
                    getLastName.setText(myLastName);
                    getContactNumber.setText(myContactNumber);
                    getEmailAddress.setText(myEmailId);
                    cancelButton.setText("Exit");

                } else {
                    showDialog(alertDialogBuilder);
                }
            } else {
                showDialog(alertDialogBuilder);
            }

            if (savedInstanceState != null) {
                showUserName = savedInstanceState.getString(USERNAME);
                Helper.userName = showUserName;
            }
            progressDialog.setMessage("Please wait ...");
            progressDialog.show();
            insertXmlData(parser);
            progressDialog.dismiss();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void insertXmlData(XmlResourceParser parser) {
        try {
            myDataBase.open();
            while(parser.next() != XmlPullParser.END_TAG) {
                if(parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                if(name.equals("Content")) {
                    String operator = null,band = null;
                    while(parser.next()!= XmlPullParser.END_TAG) {
                        if(parser.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        name = parser.getName();
                        if(name.equals("operator")) {
                            operator = readText(parser);
                        } else if(name.equals("band")) {
                            band = readText(parser);
                        }
                    }
                    myDataBase.insertData(operator,band);
                }
            }

        } catch(XmlPullParserException | IOException e) {
            Message.msgShort(getBaseContext(), "Error : " + e.getMessage());
        } finally {
            if(myDataBase != null) {
                myDataBase.close();
            }
        }
    }

    private String readText(XmlPullParser parser) throws IOException,XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void showDialog(final AlertDialog.Builder alertDialogBuilder) {
        try{
            alertDialogBuilder
                    .setCancelable(false);
            alertDialog = alertDialogBuilder.create();
            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {
                    okButton = (Button)alertDialog.findViewById(R.id.okBtn);
                    cancelButton = (Button)alertDialog.findViewById(R.id.cancelBtn);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showUserName = getName.getText().toString();
                            str_UserLastName = getLastName.getText().toString();
                            str_UserContactNumber = getContactNumber.getText().toString();
                            str_UserEmailAddress = getEmailAddress.getText().toString();

                            ParameterGetSet.setEngineer_lastName(str_UserLastName);
                            ParameterGetSet.setContactNumber(str_UserContactNumber);
                            ParameterGetSet.setEmailAddress(str_UserEmailAddress);

                            boolean check;
                            Pattern p;
                            Matcher m;

                            String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

                            p = Pattern.compile(EMAIL_STRING);

                            m = p.matcher(str_UserEmailAddress);
                            check = m.matches();


                            if (notEmpty(showUserName) || getEmailAddress.getText().length()!= 0 || getContactNumber.getText().length() != 0) {
                                ParameterGetSet.Engineer_Name = showUserName;
                                putUserName.setText("Welcome " + showUserName + " " + str_UserLastName);
                                if (getEmailAddress.getText().length() !=0){
                                if (!check){
                                    getEmailAddress.setError("Not Valid Email");
                                    alertDialog.show();
                                }
                                }else if(getContactNumber.getText().length() !=0) {
                                    if (str_UserContactNumber.length() < 10 || str_UserContactNumber.length() > 11) {
                                        getContactNumber.setError("Not Valid Number");
                                        alertDialog.show();
                                    }else {
                                        dialog.dismiss();
                                    }
                                }else if (getName.getText().length() == 0){
                                    getName.setError("Enter Your Name");
                                }
                                else {
                                    dialog.dismiss();
                                }
                            }else if(getName.getText().length() == 0) {
                                getName.setError("Enter Your Name");
                                alertDialog.show();
                            }
                            if (getName.getText().length() == 0){
                                getName.setError("Enter Your Name");
                                alertDialog.show();
                            }
                            if(notEmpty(showUserName) && check &&
                                    (str_UserContactNumber.length() > 10 || str_UserContactNumber.length() < 11)){
                                dialog.dismiss();
                            }

                            }
                    });
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                }
            });
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            if (showUserName == null) {
                alertDialog.show();
            } else {
                putUserName.setText("Welcome " + showUserName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean notEmpty(String s) {
        return (s != null && s.length() > 0);
    }

    @Override
    public void onClick(View v) {
        try{
            Intent intent;
            switch (v.getId()) {
                case R.id.button0:
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        intent = new Intent(MainActivity.this,PdfScreenShot.class);
                        Helper.imageFilePath="PdfFileClick";
                        startActivityForResult(intent, RESULT_LOAD_IMG);
                    } else {
                        Message.message(getBaseContext(),"Pdf Capture Not Support your Device",Toast.LENGTH_SHORT);
                    }
                    break;
                case R.id.button1:
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    Helper.imageFilePath="LoadImageClick";
                    startActivityForResult(intent, RESULT_LOAD_IMG);
                    break;
                case R.id.button2:
                    intent = new Intent(MainActivity.this,ResultOfSignalCapture.class);
                    startActivity(intent);
                    break;
                case R.id.btnSync:
                    try{
                        connectServiceSample();
                    }catch (Exception ip){
                        ip.printStackTrace();
                    }
                    break;
                case R.id.ipDetail:
                    showPopup(MainActivity.this);
                    break;

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    + Floor_Image_Path);

            return 0;

        }
        else
        {
            try {
                upLoadServerUri = "http://"+ipaddress_db+":8080/TestProjects/rest/signal/multiPartUpload";
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"\" + file.getName() + \"\""+lineEnd);
                /*dos.writeBytes("Content-Disposition: form-data;"+" name="uploaded_file";""filename=""
                                + fileName + "" + lineEnd);
*/
                        dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" http://www.androidexample.com/media/uploads/"
                                    +Image_Name;


                            Toast.makeText(MainActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(MainActivity.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(MainActivity.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
               /* Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);*/
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }


    private void showPopup(final Activity context){
        // Inflate the popup_userdetail.xml
        LinearLayout viewGroup = (LinearLayout)context.findViewById(R.id.layout_sync_ipaddress);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout= layoutInflater.inflate(R.layout.layout_sync_detail,viewGroup);


//        Creating a Popup Window
        final PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setContentView(layout);
        popupWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

//        Display Popup Window
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        txt_ipaddress = (EditText)layout.findViewById(R.id.txtIpAddress);

//        Toast.makeText(context,"Welcome" +showUserName,Toast.LENGTH_LONG).show();
        Button cancelpopup = (Button)layout.findViewById(R.id.btnCancel);
        cancelpopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        Button syncIpaddress = (Button)layout.findViewById(R.id.btnSync);
        syncIpaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ipaddress = txt_ipaddress.getText().toString();
                ParameterGetSet.IpAddress = ipaddress;
                myDataBase.open();
                myDataBase.insertIpaddress();
                myDataBase.close();
                popupWindow.dismiss();
                Message.msgLong(getBaseContext(),"Server Detail Inserted. Now Update Your Details In Server.");

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


                Intent intent = new Intent(MainActivity.this,SelectImage.class);
                intent.putExtra(SELECTED_IMG_PATH, imgDecodableString);
                startActivityForResult(intent,1);

            } else if (requestCode == RESULT_RESULT_IMG && resultCode == RESULT_OK && null != data){
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                floorImageName = imgDecodableString.substring(imgDecodableString.lastIndexOf("/"));

                Intent intent = new Intent(MainActivity.this,ResultOfSignalCapture.class);
                intent.putExtra(SELECTED_IMG_PATH, imgDecodableString);
                startActivityForResult(intent,1);
            } else if (requestCode == ACTIVITY_CHOOSE_FILE && resultCode == RESULT_OK && null != data) {
                uri = data.getData();
                String myimagePath  = getRealPathFromURI(uri);
                Intent intent = new Intent(MainActivity.this,ResultOfSignalCapture.class);
                intent.putExtra(SELECTED_RESULT_IMAGE_PATH, myimagePath);
                startActivityForResult(intent,2);
            }
        } catch (Exception e) {
            Message.message(getBaseContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    protected void onSaveInstanceState(Bundle mySavedInstanceState) {
        mySavedInstanceState.putString(USERNAME, showUserName);
        super.onSaveInstanceState(mySavedInstanceState);
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        showUserName = savedInstanceState.getString(USERNAME);
        super.onRestoreInstanceState(savedInstanceState);
    }


    public void connectServiceSample(){
        try{
            //System.out.println("method called");
//            jsonObject = new JSONObject();
            myDataBase.open();
            Cursor readIpaddress =  myDataBase.getIpaddressIfExists();
            readIpaddress.moveToLast();
            if (readIpaddress.getCount()== 0){
                showPopup(MainActivity.this);
            }
           ipaddress_db = readIpaddress.getString(0);
            myDataBase.close();
            urlServer = "http://"+ipaddress_db+":8080/SignalCapture_Service/rest/signal/shareValue";
            new sampleAsync().execute(urlServer);

        }catch(Exception e){
            showPopup(MainActivity.this);
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }
public class sampleAsync extends AsyncTask<String,Integer,String>{

    @Override
    protected String doInBackground(String... params) {
        try {
                readFullRowDetail();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

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
        myDataBase.open();
        list = new ArrayList<String>();

        int status = 0;
        Cursor readAllRecord = myDataBase.readFullRow(status);

        if (readAllRecord != null) {
            readAllRecord.moveToFirst();
            int countDb = readAllRecord.getCount();
            if (countDb == 0) {
                Message.msgLong(getBaseContext(), "No Records Found to Upload. All details were uploaded in server.");
            } else {
                    for (int i=0;i < readAllRecord.getCount();i++)
                    {
                        setreadAllRecord(readAllRecord);
                    }
                }
            }
        myDataBase.close();
        Message.msgLong(getBaseContext(),"Server Side Data Uploaded SuccessFully!");
    }
    public void setreadAllRecord(Cursor readAllRecord)
    {
            int status=0;

            User_Name = readAllRecord.getString(0);
            lastName = readAllRecord.getString(1);
            if (lastName.equals("")){
                lastName = "";
            }
            contactNo = readAllRecord.getString(2);
            if (contactNo.equals("")){
                contactNo = "";
            }
            emailAddress = readAllRecord.getString(3);
            if (emailAddress.equals("")){
                emailAddress = "";
            }
            address1 = readAllRecord.getString(4);
            if (address1 == null){
                address1 = "";
            }
            address2 = readAllRecord.getString(5);
            if (address2 == null){
                address2 = "";}
            city = readAllRecord.getString(6);
            if (city == null){
                city = "";}
            state = readAllRecord.getString(7);
            if (state == null){
                state = "";}
            zip = readAllRecord.getString(8);
            if (zip == null){
                zip = "";}
            Floor_Image_Path = readAllRecord.getString(9);
            if (Floor_Image_Path == null){
                Floor_Image_Path = "";}
            Image_Name = readAllRecord.getString(10);
            if (Image_Name == null){
                Image_Name = "";}
            Network_Selection = readAllRecord.getString(11);
            if (Network_Selection == null){
                Network_Selection = "";}
            Pin_Id = readAllRecord.getString(12);
            if (Pin_Id == null){
                Pin_Id = "";}
            Provider_Detail = readAllRecord.getString(13);
            if (Provider_Detail == null){
                Provider_Detail = "";}
            Bssi = readAllRecord.getString(14);
            if (Bssi == null){
                Bssi = "";}
            Signal_Strength = readAllRecord.getString(15);
            if (Signal_Strength == null){
                Signal_Strength = "";}
            Frequency = readAllRecord.getString(16);
            if (Frequency == null){
                Frequency = "";}
            WiFimode = readAllRecord.getString(17);
            if (WiFimode == null){
                WiFimode = "";}
            Channel_Number = readAllRecord.getString(18);
            if (Channel_Number == null){
                Channel_Number = "";}
            Network_Technology = readAllRecord.getString(19);
            if (Network_Technology == null){
                Network_Technology = "";}
            Cell_Id = readAllRecord.getString(20);
            if (Cell_Id == null){
                Cell_Id = "";}
            Signal_Quality = readAllRecord.getString(21);
            if (Signal_Quality == null){
                Signal_Quality = "";}
            PinMark_Hint = readAllRecord.getString(22);
            if (PinMark_Hint == null){
                PinMark_Hint = "";}
            Camera_LocationPath = readAllRecord.getString(23);
            if (Camera_LocationPath == null){
                Camera_LocationPath = "";}
            Camera_LocationName = readAllRecord.getString(24);
            if (Camera_LocationName == null){
                Camera_LocationName = "";}
            XValue = readAllRecord.getString(25);
            if (XValue == null){
                XValue = "";}
            YValue = readAllRecord.getString(26);
            if (YValue == null){
                YValue = "";}
            dateTime = readAllRecord.getString(27);
            keyID = readAllRecord.getString(28);
            Image_XValue = readAllRecord.getString(29);
            if (Image_XValue == null){
                Image_XValue = "";}
            Image_YValue = readAllRecord.getString(30);
             if (Image_YValue == null){
                 Image_YValue = "";}
            readAllRecord.moveToNext();
            myDataBase.updateStatus(status);
            syncDetails();
    }
    public void syncDetails(){
        jsonObject = new JSONObject();
        try{
        try {
            jsonObject.put("keyID",keyID);
            jsonObject.put("user_Name",User_Name);
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
            jsonObject.put("dateTime", dateTime);
            jsonObject.put("imagex_Value", Image_XValue);
            jsonObject.put("imagey_Value", Image_YValue);


                HttpClient httpclient=new DefaultHttpClient();
                HttpResponse reponse;
                HttpPost httppost  = new HttpPost("http://"+ipaddress_db+":8080/SignalCapture_Service/rest/signal/shareValue");
                HttpEntity entity;

                Log.i("JSON:", jsonObject.toString());


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

        } catch (JSONException e) {
            e.printStackTrace();
        }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        myActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        myActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return myActionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
