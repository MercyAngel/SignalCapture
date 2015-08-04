package com.kenturf.signalcapture;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by reegan on 31-Mar-15.
 */
public class Message {
    public static void message(Context ctx,String msg,int Duration) {
        Toast.makeText(ctx,msg,Duration).show();
    }
    public static void msgShort(Context context,String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
    public static void msgLong(Context context,String message) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
