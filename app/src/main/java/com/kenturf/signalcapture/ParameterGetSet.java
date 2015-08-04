package com.kenturf.signalcapture;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by DELL on 11-May-15.
 */
public class ParameterGetSet {

    public static String floor_Image_Path;
    public static String image_Name;
    public static String Engineer_Name;
    public static String Engineer_lastName;
    public static String contactNumber;
    public static String emailAddress;
    public static String Building_Address1;
    public static String Building_Address2;
    public static String Building_State;
    public static String Building_City;
    public static String Building_ZipCode;

    /*Building Info*/
    public static String building_name;
    public static String Address1;
    public static String Address2;
    public static String State;
    public static String City;
    public static String ZipCode;

    public static String pinmark_Hint;
    public static LinkedHashMap hashMapValue;
    public static ArrayList<String> valuesHash;
    public static String savedImagePath;
    public static ArrayList<String> fileResultPath;
    public static String camera_location_path;
    public static String camera_location_name;

    public static String customDrawingPath;

    public static int image_xValue;
    public static int image_yValue;

    public static String IpAddress;

    public static Bitmap floorPlanBitmap;
    public static Bitmap drawingBitmap;

    public  static int onStickFlagReturn = 0;


    public static void setEngineer_lastName(String engineer_lastName) {
        Engineer_lastName = engineer_lastName;
    }

    public static String getEngineer_lastName() {
        return Engineer_lastName;
    }

    public static void setContactNumber(String contactNumber) {
        ParameterGetSet.contactNumber = contactNumber;
    }

    public static String getContactNumber() {
        return contactNumber;
    }

    public static void setEmailAddress(String emailAddress) {
        ParameterGetSet.emailAddress = emailAddress;
    }

    public static String getEmailAddress() {
        return emailAddress;
    }

    public static void setBuilding_Address1(String building_Address1) {
        Building_Address1 = building_Address1;
    }

    public static void setBuilding_Address2(String building_Address2) {
        Building_Address2 = building_Address2;
    }

    public static String getBuilding_Address1() {
        return Building_Address1;
    }

    public static String getBuilding_Address2() {
        return Building_Address2;
    }

    public static void setBuilding_State(String building_State) {
        Building_State = building_State;
    }

    public static String getBuilding_State() {
        return Building_State;
    }

    public static void setBuilding_City(String building_City) {
        Building_City = building_City;
    }

    public static String getBuilding_City() {
        return Building_City;
    }

    public static void setBuilding_ZipCode(String building_ZipCode) {
        Building_ZipCode = building_ZipCode;
    }

    public static String getBuilding_ZipCode() {
        return Building_ZipCode;
    }

    public static String getCustomDrawingPath() {
        return customDrawingPath;
    }

    public static void setCustomDrawingPath(String customDrawingPath) {
        ParameterGetSet.customDrawingPath = customDrawingPath;
    }
}

