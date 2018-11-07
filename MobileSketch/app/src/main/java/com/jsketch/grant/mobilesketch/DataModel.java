package com.jsketch.grant.mobilesketch;

import android.graphics.*;
import android.graphics.drawable.shapes.*;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.*;
/**
 * Created by Grant on 16-07-10.
 */
public class DataModel implements Parcelable {
    //CanvasView view;
    int current_color = Color.BLACK;
    Integer cur_stroke = 3;
    int select = -1;
    ArrayList<Integer> shape_color = new ArrayList<Integer>();
    ArrayList<Integer> shape_fill = new ArrayList<Integer>();
    ArrayList<Integer> shape_stroke = new ArrayList<Integer>();
    ArrayList<Point> startp = new ArrayList<Point>();
    ArrayList<Point> endp = new ArrayList<Point>();
    ArrayList<Integer> shape_type = new ArrayList<Integer>();

    protected DataModel(){

    }

    public void add(Point st, Point en, int type)
    {
        shape_color.add(current_color);
        shape_fill.add(null);
        shape_stroke.add(cur_stroke);
        startp.add(st);
        endp.add(en);
        shape_type.add(type);
    }

    public void remove(int i){
        shape_color.remove(i);
        shape_fill.remove(i);
        shape_stroke.remove(i);
        startp.remove(i);
        endp.remove(i);
        shape_type.remove(i);
    }

    public void clean(){
        shape_color = new ArrayList<Integer>();
        shape_fill = new ArrayList<Integer>();
        shape_stroke = new ArrayList<Integer>();
        startp = new ArrayList<Point>();
        endp = new ArrayList<Point>();
        shape_type = new ArrayList<Integer>();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //dest.writeParcelable(this.view, flags);
        dest.writeInt(this.current_color);
        dest.writeValue(this.cur_stroke);
        dest.writeInt(this.select);
        dest.writeList(this.shape_color);
        dest.writeList(this.shape_fill);
        dest.writeList(this.shape_stroke);
        dest.writeTypedList(this.startp);
        dest.writeTypedList(this.endp);
        dest.writeList(this.shape_type);
    }

    protected DataModel(Parcel in) {

        this.current_color = in.readInt();
        this.cur_stroke = (Integer) in.readValue(Integer.class.getClassLoader());
        this.select = in.readInt();
        this.shape_color = new ArrayList<Integer>();
        in.readList(this.shape_color, Integer.class.getClassLoader());
        this.shape_fill = new ArrayList<Integer>();
        in.readList(this.shape_fill, Integer.class.getClassLoader());
        this.shape_stroke = new ArrayList<Integer>();
        in.readList(this.shape_stroke, Integer.class.getClassLoader());
        this.startp = in.createTypedArrayList(Point.CREATOR);
        this.endp = in.createTypedArrayList(Point.CREATOR);
        this.shape_type = new ArrayList<Integer>();
        in.readList(this.shape_type, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<DataModel> CREATOR = new Parcelable.Creator<DataModel>() {
        @Override
        public DataModel createFromParcel(Parcel source) {
            return new DataModel(source);
        }

        @Override
        public DataModel[] newArray(int size) {
            return new DataModel[size];
        }
    };
}
