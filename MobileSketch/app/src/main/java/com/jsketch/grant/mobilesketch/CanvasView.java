package com.jsketch.grant.mobilesketch;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import java.util.Calendar;

public class CanvasView extends View {

    Context context;
    private Paint cpaint;
    DataModel model;
    int inselect, re_type, re_color, re_fill, re_stroke;
    private TextView t2, t3;


    Point start, end, re_start, re_end;
    public final int SELECTION = 1;
    public final int ERASE = 2;
    public final int LINE = 3;
    public final int CIRCLE = 4;
    public final int RECT = 5;
    public final int FILL = 6;
    int operation = SELECTION;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        cpaint = new Paint();
        cpaint.setAntiAlias(true);
        cpaint.setColor(Color.BLACK);
        cpaint.setStyle(Paint.Style.STROKE);
        cpaint.setStrokeJoin(Paint.Join.ROUND);
        cpaint.setStrokeWidth(4f);
    }

    public void setTextView(TextView a, TextView b){
        t2 = a;
        t3 = b;
    }


    public void setOperation(int n){
        operation = n;
    }

    public void setModel(DataModel m){
        model = m;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (model.current_color == Color.BLUE){
            t2.setText(" blue");
        } else if (model.current_color == Color.BLACK) {
            t2.setText(" black");
        } else if (model.current_color == Color.GREEN) {
            t2.setText(" green");
        } else if (model.current_color == Color.YELLOW) {
            t2.setText(" yellow");
        }

        if (model.cur_stroke == 3){
            t3.setText(" L");
        } else if (model.cur_stroke == 2) {
            t3.setText(" M");
        } else if (model.cur_stroke == 1) {
            t3.setText(" S");
        }

        for (int i = 0; i < model.startp.size(); i++)
        {
            if (model.shape_fill.get(i) != null){
                cpaint.setStyle(Paint.Style.FILL);
                cpaint.setColor(model.shape_fill.get(i));
                draw_shape(model.startp.get(i).x, model.startp.get(i).y, model.endp.get(i).x, model.endp.get(i).y, model.shape_type.get(i), canvas);
            }
            cpaint.setStyle(Paint.Style.STROKE);
            if (model.shape_stroke.get(i) == 3){
                cpaint.setStrokeWidth(12f);
            } else if (model.shape_stroke.get(i) == 2) {
                cpaint.setStrokeWidth(8f);
            } else {
                cpaint.setStrokeWidth(4f);
            }

            if(i == model.select) cpaint.setColor(Color.RED);
            else cpaint.setColor(model.shape_color.get(i));
            draw_shape(model.startp.get(i).x, model.startp.get(i).y, model.endp.get(i).x, model.endp.get(i).y, model.shape_type.get(i), canvas);
        }
        if (start != null && end != null) {
            cpaint.setStrokeWidth(4f);
            cpaint.setColor(Color.GRAY);
            draw_shape(start.x, start.y, end.x, end.y, operation, canvas);
            if (inselect == 1) {
                draw_shape(re_start.x, re_start.y, re_end.x, re_end.y, re_type, canvas);
            }
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start = new Point(x, y);
                end = start;

                if (operation == SELECTION && model.select != -1){
                    re_start = model.startp.get(model.select);
                    re_end = model.endp.get(model.select);
                    re_type = model.shape_type.get(model.select);
                    re_color = model.shape_color.get(model.select);
                    re_stroke = model.shape_stroke.get(model.select);
                    if (model.shape_fill.get(model.select) == null) re_fill = -1;
                    else re_fill = model.shape_fill.get(model.select);

                    if (inside(re_start, re_end, x, y, re_type)) {
                        model.remove(model.select);
                        model.select = -1;
                        inselect = 1;
                    }

                }
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                end = new Point(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                if (operation == RECT)
                {
                    model.add(start, new Point(x, y), RECT);
                } else if (operation == CIRCLE) {
                    model.add(start, new Point(x, y), CIRCLE);
                } else if (operation == LINE) {
                    model.add(start, new Point(x, y), LINE);
                } else if (operation == ERASE) {
                    for (int i = model.startp.size() - 1; i >= 0; i--)
                    {
                        if (inside(model.startp.get(i), model.endp.get(i), x, y, model.shape_type.get(i)))
                        {
                            model.remove(i);
                            break;
                        }
                    }
                } else if (operation == FILL) {
                    for (int i = model.startp.size() - 1; i >= 0; i--)
                    {
                        if (inside(model.startp.get(i), model.endp.get(i), x, y, model.shape_type.get(i)))
                        {
                            model.shape_fill.set(i, model.current_color);
                            break;
                        }
                    }
                } else if (operation == SELECTION) {
                    if (inselect == 0){
                        for (int i = model.startp.size() - 1; i >= 0; i--)
                        {
                            if (inside(model.startp.get(i), model.endp.get(i), x, y, model.shape_type.get(i)))
                            {
                                model.select = i;
                                if (model.shape_fill.get(i) == null)
                                {
                                    model.current_color = model.shape_color.get(i);
                                } else {
                                    model.current_color = model.shape_fill.get(i);
                                }
                                model.cur_stroke = model.shape_stroke.get(i);
                                break;
                            }
                        }
                    } else if (inselect == 1) {
                        int x_change = x - start.x;
                        int y_change = y - start.y;
                        re_start.x += x_change;
                        re_start.y += y_change;
                        re_end.x += x_change;
                        re_end.y += y_change;
                        model.select = model.startp.size();
                        model.add(re_start, re_end, re_type);
                        model.shape_color.set(model.select,re_color);
                        model.shape_stroke.set(model.select, re_stroke);
                        if (re_fill == -1) model.shape_fill.set(model.select, null);
                        else model.shape_fill.set(model.select,re_fill);
                        inselect = 0;
                    }
                }
                start = null;
                end = null;
                invalidate();
                break;
        }
        return true;
    }

    private void draw_shape(int x1, int y1, int x2, int y2, int type, Canvas canvas)
    {
        int sx = Math.min(x1, x2);
        int sy = Math.min(y1, y2);
        int ex = Math.max(x1, x2);
        int ey = Math.max(y1, y2);
        if (type == RECT)
        {
            canvas.drawRect(sx, sy, ex, ey, cpaint);
        } else if (type == LINE) {
            canvas.drawLine(x1, y1, x2, y2, cpaint);
        } else if (type == CIRCLE) {
            int cx = (x1 + x2) / 2;
            int cy = (y1 + y2) / 2;
            int high = Math.abs(x1 - x2);
            int wight = Math.abs(y1 - y2);
            int d = (int) Math.sqrt(Math.pow(high, 2) + Math.pow(wight,2));
            RectF c = new RectF(cx - d/2, cy - d/2, cx + d/2, cy + d/2);
            canvas.drawOval(c, cpaint);
        }

    }

    private boolean inside(Point st, Point en, int x, int y, int type){
        if (type == CIRCLE) {
            int cx = (st.x + en.x) / 2;
            int cy = (st.y + en.y) / 2;
            int high = Math.abs(st.x - en.x);
            int wight = Math.abs(st.y - en.y);
            int d = (int) Math.sqrt(Math.pow(high, 2) + Math.pow(wight, 2));
            int dis = 2 * (int) Math.sqrt(Math.pow(Math.abs(x - cx), 2) + Math.pow(Math.abs(y - cy), 2));
            return d >= dis;
        } else if (type == RECT) {
            RectF r = new RectF(st.x, st.y, en.x, en.y);
            return r.contains(x, y);
        } else {
            int dis1 = (int) Math.sqrt(Math.pow(Math.abs(st.x - x), 2) + Math.pow(Math.abs(st.y - y), 2));
            int dis2 = (int) Math.sqrt(Math.pow(Math.abs(en.x - x), 2) + Math.pow(Math.abs(en.y - y), 2));
            int disline = (int) Math.sqrt(Math.pow(Math.abs(st.x - en.x), 2) + Math.pow(Math.abs(st.y - en.y), 2));
            return (dis1 + dis2 - disline < 5);
        }
    }
}