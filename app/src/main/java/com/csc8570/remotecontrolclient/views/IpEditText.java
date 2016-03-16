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
import android.widget.Toast;

import java.net.InetAddress;

/**
 * Created by Tom on 3/15/2016.
 */
public class IpEditText extends EditText
{
    public IpEditText(Context context) {
        super(context);
        addTextChangedListener(new IpInputWatcher(this));
    }

    public IpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(new IpInputWatcher(this));
    }

    public IpEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTextChangedListener(new IpInputWatcher(this));
    }

    private class IpInputWatcher implements TextWatcher
    {
        private IpEditText view;

        public IpInputWatcher(IpEditText view)
        {
            this.view = view;
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

            String currentText = s.toString();
            if(currentText.length() > 3)
            {
                int currPos = view.getSelectionEnd();

                s.clear();
                currentText = currentText.substring(0,3);
                s.append(currentText);
                view.setSelection(3);
            }

            view.addTextChangedListener(this);
        }
    }
}
