package com.smartcity.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.smartcity.R;


public class TextViewRoboto extends TextView {
    private Context c;
    public TextViewRoboto(Context context) {
        super(context);
        if(!isInEditMode()){
            this.c = context;
            Typeface tfs = Typeface.createFromAsset(c.getAssets(),c.getString(R.string.font_name));
            setTypeface(tfs);

        }

    }

    public TextViewRoboto(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()){
            this.c = context;
            Typeface tfs = Typeface.createFromAsset(c.getAssets(),c.getString(R.string.font_name));
            setTypeface(tfs);

        }
    }

    public TextViewRoboto(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(!isInEditMode()){
            this.c = context;
            Typeface tfs = Typeface.createFromAsset(c.getAssets(),c.getString(R.string.font_name));
            setTypeface(tfs);

        }
    }


}
