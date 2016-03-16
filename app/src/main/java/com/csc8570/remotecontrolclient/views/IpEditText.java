package com.csc8570.remotecontrolclient.views;

import android.content.Context;
import android.graphics.Rect;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import java.net.InetAddress;

/**
 * Created by Tom on 3/15/2016.
 */
public class IpEditText extends EditText
{
    public IpEditText(Context context) {
        super(context);
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setTransformationMethod(new IpAddressTransformation());

    }

    public IpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setTransformationMethod(new IpAddressTransformation());
    }

    public IpEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setTransformationMethod(new IpAddressTransformation());
    }

    public String getAddress()
    {
        String text = this.getText().toString();
        return text;
    }

    // Handles displaying a well formed IP address to the user
    private class IpAddressTransformation implements TransformationMethod
    {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            CharSequence[] sequences = new CharSequence[4];
            sequences[0] = getSubSequence(source,0,3);
            sequences[1] = getSubSequence(source,3,6);
            sequences[2] = getSubSequence(source,6,9);
            sequences[3] = getSubSequence(source,9,12);

            return TextUtils.join(".",sequences);
        }

        // Returns a subsequence from a char sequence, or the max characters that could be extraced
        private CharSequence getSubSequence(CharSequence input, int start, int end)
        {
            if(start > input.length())
            {
                return "";
            }
            if(end > input.length())
            {
                return input.subSequence(start,input.length());
            }
            return input.subSequence(start,end);
        }

        @Override
        public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {

        }
    }
}
