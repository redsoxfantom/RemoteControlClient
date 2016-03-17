package com.csc8570.remotecontrolclient.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.csc8570.remotecontrolclient.R;

import java.net.InetAddress;

/**
 * Text box that encapsulates a segment of an IP address
 *
 * Created by Tom on 3/15/2016.
 */
public class IpEditText extends EditText
{
    int nextBoxId;

    public IpEditText(Context context) {
        super(context);
        addTextChangedListener(new IpInputWatcher(this));
    }

    public IpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        nextBoxId = getNextBoxId(context,attrs);
        addTextChangedListener(new IpInputWatcher(this));
    }

    public IpEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        nextBoxId = getNextBoxId(context,attrs);
        addTextChangedListener(new IpInputWatcher(this));
    }

    //  Get the resource Id of the next box
    private int getNextBoxId(Context ctx, AttributeSet attrs)
    {
        TypedArray arry = ctx.obtainStyledAttributes(attrs, R.styleable.IpEditText, 0, 0);
        int resourceId = -1;

        try {
            int idx = arry.getIndex(R.styleable.IpEditText_nextBox);
            resourceId = arry.getResourceId(idx, -1);
        }
        finally {
            arry.recycle();
        }

        return resourceId;
    }

    private class IpInputWatcher implements TextWatcher
    {
        private IpEditText view;
        private IpEditText nextBox;

        public IpInputWatcher(IpEditText view)
        {
            this.view = view;
            nextBox = null;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }

        @Override
        public void afterTextChanged(Editable s)
        {
            view.removeTextChangedListener(this);
            getNextBox();

            String currentText = s.toString();
            if(currentText.length() >= 3)
            {
                s.clear();
                currentText = currentText.substring(0,3);
                int currVal = Integer.parseInt(currentText);
                if(currVal > 255)
                {
                    currentText = "255";
                    Toast.makeText(view.getContext(),"IP Address segment cannot be greater than 255",Toast.LENGTH_LONG).show();
                }

                s.append(currentText);

                nextBox.requestFocus();
            }

            view.addTextChangedListener(this);
        }

        // Get the reference to the next box
        private void getNextBox() {
            if (nextBox == null) {
                nextBox = (IpEditText) ((View) view.getParent()).findViewById(nextBoxId);

            }
        }
    }
}
