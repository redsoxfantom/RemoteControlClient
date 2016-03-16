package com.csc8570.remotecontrolclient.views;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.net.InetAddress;

/**
 * Created by Tom on 3/15/2016.
 */
public class IpEditText extends LinearLayout
{
    public IpEditText(Context context) {
        super(context);
    }

    public IpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IpEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getAddress()
    {
        return "";
    }
}
