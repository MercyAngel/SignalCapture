package com.kenturf.signalcapture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by reegan on 09-Apr-15.
 */


public class PdfScreenShot extends ActionBarActivity {

    public ImageView imageView;
    private int currentPage = 0;
    private Button next,previous;

    public Bitmap myBitmap;
    final int ACTIVITY_CHOOSE_FILE = 1;
    public Uri uri;
    private String filepath;
    public final static String IMAGE_URL = "com.kenturf.signalcapture.imgUrl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pdfcapture);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        next = (Button)findViewById(R.id.next);
        previous = (Button)findViewById(R.id.previous);

        pdfPageNavigator();
    }

    private void pdfPageNavigator() {
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage++;
                render();
            }
        });
        previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage--;
                render();
            }
        });
    }

    private void render() {
        try {
            imageView = (ImageView)findViewById(R.id.pdfImage);
            int REQ_WIDTH = imageView.getWidth();
            int REQ_HEIGHT = imageView.getHeight();


            if (filepath == null) {
                Message.message(getBaseContext(), "Please Select a PDF", Toast.LENGTH_LONG);
                return;
            }
            File file = new File(filepath);

            /* custom comment */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
                if (currentPage < 0) {
                    previous.setEnabled(false);
                    next.setEnabled(true);
                } else if (currentPage == renderer.getPageCount()) {
                    next.setEnabled(false);
                    previous.setEnabled(true);
                } else if (currentPage > renderer.getPageCount()) {
                    currentPage = renderer.getPageCount() - 1;
                }

                Matrix m = imageView.getImageMatrix();
                Rect rect = new Rect(0, 0, REQ_WIDTH, REQ_HEIGHT);


                Bitmap bitmap = Bitmap.createBitmap(REQ_WIDTH, REQ_HEIGHT, Bitmap.Config.ARGB_4444);

            /* custom comment */

                renderer.openPage(currentPage).render(bitmap, rect, m, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);


                imageView.setImageMatrix(m);
                imageView.setImageBitmap(bitmap);
                imageView.invalidate();

            }
        } catch (Exception e) {
            Message.message(getBaseContext(),"Error : " + e.getMessage(),Toast.LENGTH_LONG);
            e.printStackTrace();
        }
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
            Intent intent = new Intent(PdfScreenShot.this,SelectImage.class);
            intent.putExtra(IMAGE_URL,abImgPath);
            startActivity(intent);

        } catch (FileNotFoundException e) {
            Message.message(getBaseContext()," File Not Found : " + e.getMessage(),Toast.LENGTH_LONG);
        } catch (IOException e) {
            Message.message(getBaseContext(),"Error : " +e.getMessage(),Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVITY_CHOOSE_FILE:
            {
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    filepath = uri.getPath();
                    render();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pdfshot,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.screen_shot:
                View view = getWindow().getDecorView().getRootView();
                view.setDrawingCacheEnabled(true);
                myBitmap = view.getDrawingCache();
                saveBitmap(myBitmap);
                return true;
            case R.id.selectPdf:
                Intent chooseFile;
                Intent intent;
                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("file/*");
                intent = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
