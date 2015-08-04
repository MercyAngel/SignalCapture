package com.kenturf.signalcapture;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by DELL on 10-Jul-15.
 */
public class DrawOnFloorPlan extends View {
    public static Bitmap DrawBitmap;
    private Canvas mCanvas;
    private Path mPath;
    public static Paint DrawBitmapPaint;
    public static Paint mPaint;
    CustomDrawing customDrawing;

    @SuppressWarnings("deprecation")
    public DrawOnFloorPlan(Context c) {

        super(c);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(getResources().getColor(android.R.color.holo_green_dark));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);


        mPath = new Path();
        DrawBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        super.onCreateContextMenu(menu);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        DrawBitmap = Bitmap.createBitmap(w, h,
                Bitmap.Config.ARGB_4444);
        mCanvas = new Canvas(DrawBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setDrawingCacheEnabled(true);
        canvas.drawBitmap(DrawBitmap, 0, 0, DrawBitmapPaint);
        canvas.drawPath(mPath, mPaint);
        canvas.drawRect(mY, 0, mY, 0, DrawBitmapPaint);
    }

    private float mX;
    private float mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
    }
    public void clear()
    {
        int x = (int)mX;
        int y = (int)mY;
        DrawBitmap = Bitmap.createBitmap(x,y ,
                Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(DrawBitmap);
        mPath = new Path();
        mPaint = new Paint(Paint.DITHER_FLAG);

        //Added later..
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(getResources().getColor(android.R.color.holo_green_dark));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(10);
        invalidate();
    }
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if(DrawBitmap == null) {
//            dispatchDraw(mCanvas);
          setVisibility(INVISIBLE);
        }else {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
//                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        }
        ParameterGetSet.drawingBitmap = DrawBitmap;
        return true;
    }

}

/*extends View {
    Paint mPaint;
    //MaskFilter  mEmboss;
    //MaskFilter  mBlur;
    Bitmap mBitmap;
    Canvas mCanvas;
    Path mPath;
    Paint mBitmapPaint;
    Bitmap bmDest;
    Dialog dialogSave;
    Button save,cancel;

    private Context context;

    public DrawOnFloorPlan(Context context) {
        super(context);

        this.context = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);

        mPath = new Path();
        mBitmapPaint = new Paint();
        mBitmapPaint.setColor(Color.RED);
        bmDest = Bitmap.createBitmap(1, 2, Bitmap.Config.RGB_565);
        Canvas mycanvas = new Canvas(bmDest);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        if (mBitmap != null){
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
        canvas.drawBitmap(bmDest, 0, 0, null);
        }
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        //mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }

    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        //mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
        // kill this so we don't double draw
        mPath.reset();
        // mPath= new Path();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

            return getEndProcess(event);
    }

    public boolean getEndProcess(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        //int eID = event.getPointerCount();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                Message.msgShort(getContext(), "Dialog");

                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        // dialogSave.dismiss();
        ParameterGetSet.drawingBitmap = mBitmap;
        ParameterGetSet.onStickFlagReturn = 1;

        return true;
    }

    public void getDrawnResult(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(context);
        builder.setTitle("Confirm ");
        builder.setMessage("Are you sure want to Proceed?");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();

    }

}
*/