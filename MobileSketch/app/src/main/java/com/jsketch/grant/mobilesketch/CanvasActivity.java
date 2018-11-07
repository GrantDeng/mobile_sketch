package com.jsketch.grant.mobilesketch;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import java.io.*;
import android.graphics.Bitmap.*;
import android.os.Environment;
import android.Manifest;
import android.support.v4.app.*;
import android.content.pm.*;
/**
 * Created by Grant on 16-07-09.
 */
public class CanvasActivity extends Activity {

    private CanvasView canvas;
    private DataModel model;
    private TextView t1, t2, t3;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(this);

        if (savedInstanceState != null) {
            model = savedInstanceState.getParcelable("model");

        } else { // savedInstanceState has saved values
            model = new DataModel();
        }

        if (getResources().getConfiguration().orientation == 1) setContentView(R.layout.canvas);
        else setContentView(R.layout.canvas_land);

        canvas = (CanvasView) findViewById(R.id.signature_canvas);
        canvas.setModel(model);
        if (getResources().getConfiguration().orientation == 1) {
            t1 = (TextView) findViewById(R.id.textView);
            t2 = (TextView) findViewById(R.id.textView2);
            t3 = (TextView) findViewById(R.id.textView3);
        }
        else {
            t1 = (TextView) findViewById(R.id.textView4);
            t2 = (TextView) findViewById(R.id.textView5);
            t3 = (TextView) findViewById(R.id.textView6);
        }
        t1.setText(" no action");
        t2.setText(" black");
        t3.setText(" L");
        canvas.setTextView(t2, t3);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable("model", model);
        super.onSaveInstanceState(savedInstanceState);
    }



    public void selection(View v) {
        canvas.setOperation(1);
        canvas.invalidate();
        t1.setText(" select");
    }

    public void erase(View v){
        canvas.setOperation(2);
        model.select = -1;
        canvas.invalidate();
        t1.setText(" erase");
    }

    public void line(View v){
        canvas.setOperation(3);
        model.select = -1;
        canvas.invalidate();
        t1.setText(" line");
    }

    public void circle(View v){
        canvas.setOperation(4);
        model.select = -1;
        canvas.invalidate();
        t1.setText(" circle");
    }

    public void rect(View v){
        canvas.setOperation(5);
        model.select = -1;
        canvas.invalidate();
        t1.setText(" rect");
    }

    public void fill(View v){
        canvas.setOperation(6);
        model.select = -1;
        canvas.invalidate();
        t1.setText(" fill");
    }

    public void color1(View v) {
        model.current_color = Color.BLUE;
        t2.setText(" blue");
    }

    public void color2(View v) {
        model.current_color = Color.BLACK;
        t2.setText(" black");
    }

    public void color3(View v) {
        model.current_color = Color.GREEN;
        t2.setText(" green");
    }

    public void color4(View v) {
        model.current_color = Color.YELLOW;
        t2.setText(" yellow");
    }

    public void size1(View v) {
        model.cur_stroke = 1;
        t3.setText(" S");
    }

    public void size2(View v) {
        model.cur_stroke = 2;
        t3.setText(" M");
    }

    public void size3(View v) {
        model.cur_stroke = 3;
        t3.setText(" L");
    }

    public void clean(View v) {
        model.clean();
        canvas.invalidate();
    }


    public void save(View v){
        savefile();
    }

    public void savefile() {

        Bitmap bitmap = getViewBitmap(canvas);
        File f = new File(Environment.getExternalStorageDirectory().toString() + "/img.jpeg");
        try {
            f.createNewFile();

            FileOutputStream fos = new FileOutputStream(f);

            int size = bitmap.getWidth() * bitmap.getHeight() * 4;
            System.err.println("size is " + size);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();

            fos.close();

        }catch (IOException e){

            e.printStackTrace();
        }
    }
    Bitmap getViewBitmap(View view)
    {
        int width = view.getWidth();
        int height = view.getHeight();
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint p = new Paint();
        Canvas c = new Canvas(b);
        for (int i = 0; i < model.startp.size(); i++)
        {
            if (model.shape_fill.get(i) != null){
                p.setStyle(Paint.Style.FILL);
                p.setColor(model.shape_fill.get(i));
                draw_c(model.startp.get(i).x, model.startp.get(i).y, model.endp.get(i).x, model.endp.get(i).y, model.shape_type.get(i), c,p);
            }
            p.setStyle(Paint.Style.STROKE);
            if (model.shape_stroke.get(i) == 3){
                p.setStrokeWidth(12f);
            } else if (model.shape_stroke.get(i) == 2) {
                p.setStrokeWidth(8f);
            } else {
                p.setStrokeWidth(4f);
            }

            draw_c(model.startp.get(i).x, model.startp.get(i).y, model.endp.get(i).x, model.endp.get(i).y, model.shape_type.get(i), c, p);
        }
        return b;
    }

    private void draw_c(int x1, int y1, int x2, int y2, int type, Canvas canvas, Paint p)
    {
        canvas.drawColor(Color.WHITE);
        int sx = Math.min(x1, x2);
        int sy = Math.min(y1, y2);
        int ex = Math.max(x1, x2);
        int ey = Math.max(y1, y2);
        if (type == 5)
        {
            canvas.drawRect(sx, sy, ex, ey, p);
        } else if (type == 3) {
            canvas.drawLine(x1, y1, x2, y2, p);
        } else if (type == 4) {
            int cx = (x1 + x2) / 2;
            int cy = (y1 + y2) / 2;
            int high = Math.abs(x1 - x2);
            int wight = Math.abs(y1 - y2);
            int d = (int) Math.sqrt(Math.pow(high, 2) + Math.pow(wight,2));
            RectF c = new RectF(cx - d/2, cy - d/2, cx + d/2, cy + d/2);
            canvas.drawOval(c, p);
        }

    }
}
