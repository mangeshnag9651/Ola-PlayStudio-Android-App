package com.example.keshavaggarwal.olaplaystudios.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.example.keshavaggarwal.olaplaystudios.utils.AppConstants;
import com.example.keshavaggarwal.olaplaystudios.R;
import com.example.keshavaggarwal.olaplaystudios.utils.Utils;

/**
 * Created by KeshavAggarwal on 17/12/17.
 */

public class CustomTextView extends AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context, attrs);

    }

    private void setFont(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.customView,0,0);
        try{
            int fontName = typedArray.getInt(R.styleable.customView_font,0);
            setTextFontStyle(context, fontName);
        }finally {
            typedArray.recycle();
        }


    }

    private void setTextFontStyle(Context context, int fontName) {
        setTypeface(Utils.getThisTypeFace(context, fontName));
    }

}
