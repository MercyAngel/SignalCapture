package com.kenturf.signalcapture;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by DELL on 04-May-15.
 */
public class DBClass {

    //    DATABASE NAME
    public static final String DATABASE_NAME = "signalCapture.db";
    public static final int DATABASE_VERSION = 1;

    //    TABLE NAME
    public static final String TABLE_NAMEFLOORIMG = "floorImage";

    public static final String KEY_id = "imgId";
    public static final String User_Name = "user_Name";
    public static final String Floor_Image_Path = "floor_Image_Path";


    public static final String Image_Name = "image_Name";
    public static final String Network_Selection = "network_Selection";
    public static final String Pin_Id = "pin_Id";
    public static final String Provider_Detail = "provider_detail";

    public static final String Bssi = "bssi";
    public static final String Signal_Strength = "signal_Strength";
    public static final String Frequency = "frequency";
    public static final String WiFimode = "wifi_mode";
    public static final String Channel_Number = "channel_Number";
    public static final String Network_Technology = "network_Technology";
    public static final String Cell_Id = "cell_Id";
    public static final String Signal_Quality = "signal_Quality";
    public static final String PinMark_Hint = "pinMark_Hint";
    public static final String XValue = "x_Value";
    public static final String YValue = "y_Value";
    public static final String ImageXValue = "imagex_Value";
    public static final String ImageYValue = "imagey_Value";


    public static final String lastName = "LastName";
    public static final String contactNo = "ContactNumber";
    public static final String emailAddress = "Email";

    public static final String address1 = "Address1";
    public static final String address2 = "Address2";
    public static final String city = "City";
    public static final String state = "State";
    public static final String zip = "Zip";

    public static final String dateTime = "date_Time";
    public static final String Camera_LocationPath = "camera_location_path";
    public static final String Camera_LocationName = "camera_image_name";

    public static final String Status = "status";

    /* bands Table */
    public static final String TABLE_NAME = "bands";
    public static final String _ID = "id";
    public static final String OPERATOR = "operator";
    public static final String BAND = "band";

    /* /band Table */

    /*IP Address Table*/
    public static final String TABLE_IPADDRESS = "ipaddressDetail";
    public static final String Id = "ipId";
    public static final String Ipaddress = "ipaddress";
    public static final String Ip_UserName = "userName";
    public static final String Ip_Password = "password";


    ParameterGetSet settingValue = new ParameterGetSet();
    Databasehelper dbhelper;
    public SQLiteDatabase db;
    public static Context ctx;
    ContentValues content = new ContentValues();
    ContentValues contenthash = new ContentValues();
    //    ParameterGetSet gettingValue= new ParameterGetSet();
    Iterator<Map.Entry<Integer, ArrayList<String>>> iteratorMap;
    Set<Map.Entry<Integer, ArrayList<String>>> setMap;
    Map.Entry<Integer, ArrayList<String>> entry;
    ArrayList<String> values;
    LinkedHashMap<Integer,ArrayList<String >> hashMapDb = new LinkedHashMap<>();
    private static final String ALTER_USER_TABLE_ADD_USER_SOCIETY =
            "ALTER TABLE "+TABLE_NAMEFLOORIMG +" ADD " +XValue +" TEXT;";
    private static final String ALTER_USER_TABLE_ADD_USER_STREET1 =
            "ALTER TABLE "+TABLE_NAMEFLOORIMG +" ADD " +YValue +" TEXT;";

    String conditionCheck;
    String[] wifiValue;
    Cursor imageNameCu;




    public DBClass(Context ctx){
        this.ctx = ctx;
        dbhelper = new Databasehelper(ctx);
    }
    public DBClass open(){
        db = dbhelper.getWritableDatabase();
        return this;
    }
    public void close(){
        db.close();
    }

    public Boolean checkImageNameExists(String getEditName) {
        Cursor cursor = null;
        try {
            String sql = "SELECT "+ Image_Name +" FROM " + TABLE_NAMEFLOORIMG + " WHERE "+ Image_Name+" = "+"'"+ getEditName +"'";
            cursor = db.rawQuery(sql, null);
        }catch (Exception e){
            e.printStackTrace();
        }
            return cursor.getCount() > 0;
    }

    private class Databasehelper extends SQLiteOpenHelper{
        public Databasehelper(Context ctx){
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                String TABLE_FLOORIMG = "CREATE TABLE " + TABLE_NAMEFLOORIMG + "("+KEY_id + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
                        + User_Name +" text,"+ lastName +" text," + contactNo +" text,"+ emailAddress +" text," + address1 +" text," + address2 +" text,"
                        + city +" text," + state +" text," + zip +" text," +Floor_Image_Path +" text," +Image_Name+" text,"+Network_Selection+" text,"
                        + Pin_Id+" text," + Provider_Detail+" text,"+Bssi+" text,"+Signal_Strength+" text,"+Frequency+" text,"+WiFimode+" text,"+ Channel_Number+" text,"
                        + Network_Technology +" text,"+Cell_Id+" text,"+Signal_Quality+" text,"+PinMark_Hint+" text,"+Camera_LocationPath +" text,"+Camera_LocationName +" text," +XValue +" text," +YValue+" text," +ImageXValue+" text,"+ImageYValue+" text," + Status +" integer,"+ dateTime + " DATETIME DEFAULT CURRENT_TIMESTAMP);";
                String CREATE_TABLE = "CREATE TABLE "
                        + TABLE_NAME + " ("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + OPERATOR + " TEXT,"
                        + BAND + " INTEGER);";
                String TABLE_SYNCADDRESS = "CREATE TABLE " +TABLE_IPADDRESS + "("+Id + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
                        + Ipaddress + " TEXT,"+Ip_UserName+" TEXT," + Ip_Password +" TEXT);";

                db.execSQL(TABLE_FLOORIMG);
                db.execSQL(CREATE_TABLE);
                db.execSQL(TABLE_SYNCADDRESS);
                Log.e("Bands","Data inserted Successfull");

                } catch(Exception e) {
                Message.msgShort(ctx, " DBError : " + e.getMessage());
            }
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                // Drop older table if existed
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMEFLOORIMG);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_IPADDRESS);

                onCreate(db);

            } catch (Exception e) {
                Message.msgShort(ctx," DBError : " + e.getMessage());
            }
        }
    }

    public void insertData(String operator2, String band2) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(OPERATOR, operator2);
        contentValues.put(BAND, band2);
        db.insert(TABLE_NAME,null,contentValues);
    }

    public void insertIpaddress(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Ipaddress, ParameterGetSet.IpAddress);
        db.insert(TABLE_IPADDRESS,null,contentValues);

    }
    public Cursor readData(String operator) {
        String[] projection = {OPERATOR,BAND};
        String selection = OPERATOR + " LIKE ?";
        String[] selectionArgs = { String.valueOf(operator)};
        return db.query(TABLE_NAME,projection,selection,selectionArgs,null,null,null);
    }

    public Cursor readBuildingInfo(String imgName){
        try {
            String[] buildingInfo = new String[]{Image_Name, address1, address2, city, state, zip};
            conditionCheck = Image_Name +" = " + "'" +imgName + "'";
            imageNameCu = db.query(TABLE_NAMEFLOORIMG, buildingInfo, conditionCheck, null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return imageNameCu;
    }
    public Cursor readBuilding(){
        try {
            String[] buildingInfo = new String[]{Image_Name, address1, address2, city, state, zip};
            imageNameCu = db.query(TABLE_NAMEFLOORIMG, buildingInfo, null, null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return imageNameCu;
    }
    public  Cursor readBuildingCheck(String buildingName){
        try {
            String buildingNames = "select " +Image_Name +"from "+TABLE_NAMEFLOORIMG +" where "+Image_Name +"like '"
                    +buildingName +"'";
            imageNameCu = db.rawQuery(buildingNames, null);

        }catch (Exception e){
            e.printStackTrace();
        }
        return imageNameCu;
    }
    public Cursor readImageName(String imgName){
        try {
            String[] getImgName = new String[]{Image_Name,Network_Selection,Pin_Id,Provider_Detail, Frequency,
                    WiFimode,Bssi, Signal_Strength, Signal_Quality, Network_Technology, PinMark_Hint,XValue,YValue,Cell_Id,Floor_Image_Path,Camera_LocationPath,Camera_LocationName,Channel_Number,User_Name};
            conditionCheck = Image_Name +" = " + "'" +imgName + "'";
            imageNameCu = db.query(TABLE_NAMEFLOORIMG, getImgName, conditionCheck, null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return imageNameCu;
    }
    public Cursor readFullRow(Integer syncStatus){
        try {
            String[] getStatusSync = new String[]{User_Name,lastName,contactNo,emailAddress,address1,address2,city,state,zip,Floor_Image_Path,Image_Name,Network_Selection,Pin_Id,Provider_Detail,Bssi,Signal_Strength, Frequency,
                    WiFimode,Channel_Number,Network_Technology,Cell_Id,Signal_Quality, PinMark_Hint,Camera_LocationPath,Camera_LocationName,XValue,YValue,dateTime,KEY_id,ImageXValue,ImageYValue};
            conditionCheck = Status +" = "+syncStatus;
            imageNameCu = db.query(TABLE_NAMEFLOORIMG, getStatusSync, conditionCheck, null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return imageNameCu;
    }

    public Cursor getUserNameIfExists() {
        try {
            String[] getUserName = new String[]{User_Name,lastName,contactNo,emailAddress};
            conditionCheck = User_Name + " > 0";
            imageNameCu = db.query(TABLE_NAMEFLOORIMG,getUserName,conditionCheck,null,null,null,null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return imageNameCu;
    }

    public Cursor getIpaddressIfExists() {
        Cursor ipAddressCu = null;
        try {
            String[] getIpaddress = new String[]{Ipaddress,Ip_UserName,Ip_Password};
            conditionCheck = Ipaddress + " > 0";
           ipAddressCu = db.query(TABLE_IPADDRESS,getIpaddress,conditionCheck,null,null,null,null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ipAddressCu;
    }
    public Cursor readNetworkMode(String imgName,int btnID) {
        try {
            String[] getImgName = new String[]{Image_Name,Network_Selection,Pin_Id,Provider_Detail, Frequency,
                    WiFimode,Channel_Number,Bssi, Signal_Strength, Signal_Quality, Network_Technology, PinMark_Hint,XValue,YValue,Cell_Id,Camera_LocationPath
            };
            conditionCheck = Image_Name +" = " + "'" +imgName + "'"+ " AND " + Pin_Id + " = " + "'" + btnID + "'";
            imageNameCu = db.query(TABLE_NAMEFLOORIMG,getImgName,conditionCheck,null,null,null,null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return imageNameCu;
    }
    public Cursor checkImage(String imgName){
        try {
            String[] getImgName = new String[]{Image_Name};
            conditionCheck = Image_Name +" = " + "'" +imgName + "'";
            imageNameCu = db.query(TABLE_NAMEFLOORIMG,getImgName,conditionCheck,null,null,null,null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return imageNameCu;
    }
    public boolean deleteExitsImageName(String imgName){
        return db.delete(TABLE_NAMEFLOORIMG,Image_Name + " = " +"'"+imgName +"'",null)>0;
    }
    public boolean deleteRemovePin(String newImagePath, String pinId){
        String Condition = Floor_Image_Path + " = ? AND " + Pin_Id + " = ?";
        return db.delete(TABLE_NAMEFLOORIMG,Condition,new String[] {newImagePath,pinId}) > 0;
//        String delete = "DELETE FROM " +TABLE_NAMEFLOORIMG +" WHERE " +Image_Name +"="+imgName +" AND " +Pin_Id +"="  +pinId;
        //        String remove_re = ""+Image_Name +"="+"'"+imgName +"'" +" AND " +Pin_Id +"=" +"'" +pinId +"'";
//        return db.delete(TABLE_NAMEFLOORIMG,"Image_Name = ? AND Pin_Id = ?",new String[] {imgName,pinId})>0;
    }
    public void updateColumn(String imgName){
        String query = "UPDATE "+TABLE_NAMEFLOORIMG+" SET " +Floor_Image_Path +"='" +ParameterGetSet.floor_Image_Path  +"' WHERE "+Image_Name+"='"+imgName+"'";
        db.execSQL(query);
    }
    public void updateStatus(Integer sync_status){
        String query = "UPDATE "+TABLE_NAMEFLOORIMG+" SET " +Status +"='" +1 +"' WHERE "+Status+"="+sync_status+";";
        db.execSQL(query);
    }
    public void insertImagePath(){
        values = ParameterGetSet.valuesHash;
        try {
            if (values.get(0).equals( "Wifi")) {
                contenthash.put(Network_Selection, values.get(0));
                contenthash.put(Pin_Id, values.get(1));
                contenthash.put(Provider_Detail, values.get(2));
                contenthash.put(Bssi, values.get(3));
                contenthash.put(Signal_Strength, values.get(4));
                contenthash.put(Frequency, values.get(5));
                contenthash.put(WiFimode, values.get(6));
                contenthash.put(Channel_Number, values.get(7));
                contenthash.put(Network_Technology, values.get(8));
                contenthash.put(Cell_Id, values.get(9));
                contenthash.put(Signal_Quality, values.get(10));
                contenthash.put(PinMark_Hint, values.get(11));
                contenthash.put(User_Name, ParameterGetSet.Engineer_Name);
                contenthash.put(Floor_Image_Path, ParameterGetSet.floor_Image_Path);
                contenthash.put(Image_Name, ParameterGetSet.image_Name);
                contenthash.put(XValue, values.get(12));
                contenthash.put(YValue, values.get(13));
                contenthash.put(ImageXValue,ParameterGetSet.image_xValue );
                contenthash.put(ImageYValue,ParameterGetSet.image_yValue );
                contenthash.put(lastName,ParameterGetSet.getEngineer_lastName());
                contenthash.put(contactNo,ParameterGetSet.getContactNumber());
                contenthash.put(emailAddress,ParameterGetSet.getEmailAddress());
                contenthash.put(address1,ParameterGetSet.getBuilding_Address1());
                contenthash.put(address2,ParameterGetSet.getBuilding_Address2());
                contenthash.put(city,ParameterGetSet.getBuilding_City());
                contenthash.put(state,ParameterGetSet.getBuilding_State());
                contenthash.put(zip,ParameterGetSet.getBuilding_ZipCode());
                contenthash.put(Status,0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (values.get(0).equals("Network")){
                contenthash.put(Network_Selection, values.get(0));
                contenthash.put(Pin_Id,values.get(1));
                contenthash.put(Provider_Detail, values.get(2));
                contenthash.put(PinMark_Hint, values.get(3));
                contenthash.put(Network_Technology, values.get(4));
                contenthash.put(Signal_Strength, values.get(5));
                contenthash.put(Cell_Id,values.get(6));
                contenthash.put(Channel_Number,values.get(7));
                contenthash.put(User_Name, ParameterGetSet.Engineer_Name);
                contenthash.put(Floor_Image_Path, ParameterGetSet.floor_Image_Path);
                contenthash.put(Image_Name, ParameterGetSet.image_Name);
                contenthash.put(XValue, values.get(9));
                contenthash.put(YValue, values.get(10));
                contenthash.put(ImageXValue,ParameterGetSet.image_xValue );
                contenthash.put(ImageYValue,ParameterGetSet.image_yValue );
                contenthash.put(lastName,ParameterGetSet.getEngineer_lastName());
                contenthash.put(contactNo,ParameterGetSet.getContactNumber());
                contenthash.put(emailAddress,ParameterGetSet.getEmailAddress());
                contenthash.put(address1,ParameterGetSet.getBuilding_Address1());
                contenthash.put(address2,ParameterGetSet.getBuilding_Address2());
                contenthash.put(city,ParameterGetSet.getBuilding_City());
                contenthash.put(state,ParameterGetSet.getBuilding_State());
                contenthash.put(zip,ParameterGetSet.getBuilding_ZipCode());
                contenthash.put(Status,0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (values.get(0).equals("Notepad")) {
                contenthash.put(Network_Selection,values.get(0));
                contenthash.put(PinMark_Hint,values.get(1));
                contenthash.put(User_Name, ParameterGetSet.Engineer_Name);
                contenthash.put(Floor_Image_Path, ParameterGetSet.floor_Image_Path);
                contenthash.put(Image_Name, ParameterGetSet.image_Name);
                contenthash.put(Pin_Id,values.get(2));
                contenthash.put(XValue, values.get(3));
                contenthash.put(YValue, values.get(4));
                contenthash.put(ImageXValue,ParameterGetSet.image_xValue );
                contenthash.put(ImageYValue,ParameterGetSet.image_yValue );
                contenthash.put(lastName,ParameterGetSet.getEngineer_lastName());
                contenthash.put(contactNo,ParameterGetSet.getContactNumber());
                contenthash.put(emailAddress,ParameterGetSet.getEmailAddress());
                contenthash.put(address1,ParameterGetSet.getBuilding_Address1());
                contenthash.put(address2,ParameterGetSet.getBuilding_Address2());
                contenthash.put(city,ParameterGetSet.getBuilding_City());
                contenthash.put(state,ParameterGetSet.getBuilding_State());
                contenthash.put(zip,ParameterGetSet.getBuilding_ZipCode());
                contenthash.put(Status,0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (values.get(0).equals("Camera")){
                contenthash.put(Network_Selection,values.get(0));
                contenthash.put(PinMark_Hint,values.get(1));
                contenthash.put(User_Name, ParameterGetSet.Engineer_Name);
                contenthash.put(Floor_Image_Path, ParameterGetSet.floor_Image_Path);
                contenthash.put(Image_Name, ParameterGetSet.image_Name);
                contenthash.put(Camera_LocationPath,ParameterGetSet.camera_location_path);
                contenthash.put(Camera_LocationName,ParameterGetSet.camera_location_name);
                contenthash.put(Pin_Id,values.get(2));
                contenthash.put(XValue, values.get(3));
                contenthash.put(YValue, values.get(4));
                contenthash.put(ImageXValue,ParameterGetSet.image_xValue );
                contenthash.put(ImageYValue,ParameterGetSet.image_yValue );
                contenthash.put(lastName,ParameterGetSet.getEngineer_lastName());
                contenthash.put(contactNo,ParameterGetSet.getContactNumber());
                contenthash.put(emailAddress,ParameterGetSet.getEmailAddress());
                contenthash.put(address1,ParameterGetSet.getBuilding_Address1());
                contenthash.put(address2,ParameterGetSet.getBuilding_Address2());
                contenthash.put(city,ParameterGetSet.getBuilding_City());
                contenthash.put(state,ParameterGetSet.getBuilding_State());
                contenthash.put(zip,ParameterGetSet.getBuilding_ZipCode());
                contenthash.put(Status,0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (values.get(0).equals("Drawing")){
                contenthash.put(Network_Selection,values.get(0));
                contenthash.put(User_Name, ParameterGetSet.Engineer_Name);
                contenthash.put(Floor_Image_Path, ParameterGetSet.floor_Image_Path);
                contenthash.put(Image_Name, ParameterGetSet.image_Name);
                contenthash.put(Camera_LocationPath,ParameterGetSet.getCustomDrawingPath());
                contenthash.put(Pin_Id,values.get(1));
                contenthash.put(XValue, values.get(2));
                contenthash.put(YValue, values.get(3));
                contenthash.put(ImageXValue,ParameterGetSet.image_xValue );
                contenthash.put(ImageYValue,ParameterGetSet.image_yValue );
                contenthash.put(lastName,ParameterGetSet.getEngineer_lastName());
                contenthash.put(contactNo,ParameterGetSet.getContactNumber());
                contenthash.put(emailAddress,ParameterGetSet.getEmailAddress());
                contenthash.put(address1,ParameterGetSet.getBuilding_Address1());
                contenthash.put(address2,ParameterGetSet.getBuilding_Address2());
                contenthash.put(city,ParameterGetSet.getBuilding_City());
                contenthash.put(state,ParameterGetSet.getBuilding_State());
                contenthash.put(zip,ParameterGetSet.getBuilding_ZipCode());
                contenthash.put(Status,0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (values.get(0).equals("")){
                contenthash.put(User_Name, ParameterGetSet.Engineer_Name);
                contenthash.put(Floor_Image_Path, ParameterGetSet.floor_Image_Path);
                contenthash.put(Image_Name, ParameterGetSet.image_Name);
                contenthash.put(ImageXValue,ParameterGetSet.image_xValue );
                contenthash.put(ImageYValue,ParameterGetSet.image_yValue );
                contenthash.put(lastName,ParameterGetSet.getEngineer_lastName());
                contenthash.put(contactNo,ParameterGetSet.getContactNumber());
                contenthash.put(emailAddress,ParameterGetSet.getEmailAddress());
                contenthash.put(address1,ParameterGetSet.getBuilding_Address1());
                contenthash.put(address2,ParameterGetSet.getBuilding_Address2());
                contenthash.put(city,ParameterGetSet.getBuilding_City());
                contenthash.put(state,ParameterGetSet.getBuilding_State());
                contenthash.put(zip,ParameterGetSet.getBuilding_ZipCode());
                contenthash.put(Status,0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        db.insert(TABLE_NAMEFLOORIMG, null, contenthash);
        contenthash.clear();
    }
}