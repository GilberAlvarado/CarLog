package com.carlog.gilberto.carlog.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.activity.MyActivity;


/**
 * Created by Gilberto on 20/06/2015.
 */
public class SimpleDataView extends RelativeLayout {
    private String mTitle;
    private String mValue;
    private String mEdit;
    private Drawable mImage;

    public SimpleDataView(Context context) {
        super(context);
    }

    public SimpleDataView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //get the attributes specified in attrs.xml using the name we included
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SimpleDataView,
                0, 0);

        try {
            //get the text and colors specified using the names in attrs.xml
            mTitle = a.getString(R.styleable.SimpleDataView_mytitle);
            mValue = a.getString(R.styleable.SimpleDataView_myvalue);
            mEdit = a.getString(R.styleable.SimpleDataView_myEdit);
            mImage = a.getDrawable(R.styleable.SimpleDataView_image);
        } finally {
            a.recycle();
        }

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.simpledataview, this);

        setTitle(mTitle);
        setValue(mValue);
        setEdit(mEdit);
        if (mImage != null) {
            setImage(mImage);
        }
    }

    public void setImage(Drawable mImage) {
        this.mImage = mImage;

        ((ImageView)this.findViewById(R.id.view_image)).setImageDrawable(mImage);
    }

    public SimpleDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTitle(String title) {
        this.mTitle = title;

        ((TextView)this.findViewById(R.id.view_title)).setText(mTitle);
    }

    public void setValue(String value) {
        this.mValue = value;

        ((TextView)this.findViewById(R.id.view_value)).setText(mValue);
    }

    public void setEdit(String edit) {
        this.mEdit = edit;

        ((EditText)this.findViewById(R.id.view_value_edit)).setText(mEdit);
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getValue() {
        return this.mValue;
    }

    public String getmEdit() {
        return this.mEdit;
    }

    public void setEditVisible() {
        EditText et = (EditText)this.findViewById(R.id.view_value_edit);
        et.setVisibility(View.VISIBLE);

        TextView tv = (TextView)this.findViewById(R.id.view_value);
        tv.setVisibility(View.GONE);
    }

    public void setEditInvisible() {
        EditText et = (EditText)this.findViewById(R.id.view_value_edit);
        et.setVisibility(View.GONE);

        TextView tv = (TextView)this.findViewById(R.id.view_value);
        tv.setVisibility(View.VISIBLE);
    }

    public void setLos2Invisible() {
        EditText et = (EditText)this.findViewById(R.id.view_value_edit);
        et.setVisibility(View.GONE);

        TextView tv = (TextView)this.findViewById(R.id.view_value);
        tv.setVisibility(View.GONE);
    }

    public void setEditHint(String hint) {
        EditText et = (EditText)this.findViewById(R.id.view_value_edit);
        et.setHint(hint);
    }

    public EditText getTextEditLeerPantalla() {
        return ((EditText)this.findViewById(R.id.view_value_edit));
    }
}
