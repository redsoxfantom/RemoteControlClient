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

import java.net.InetAddress;

/**
 * Created by Tom on 3/15/2016.
 */
public class IpEditText extends EditText
{
    public IpEditText(Context context) {
        super(context);
        setInputType(InputType.TYPE_CLASS_PHONE);
        addTextChangedListener(new IpAddressTextWatcher(this));
    }

    public IpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInputType(InputType.TYPE_CLASS_PHONE);
        addTextChangedListener(new IpAddressTextWatcher(this));
    }

    public IpEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setInputType(InputType.TYPE_CLASS_PHONE);
        addTextChangedListener(new IpAddressTextWatcher(this));
    }

    public String getAddress()
    {
        String text = this.getText().toString();
        return text;
    }

    // Handles displaying a well formed IP address to the user
    private class IpAddressTextWatcher implements TextWatcher
    {
        private IpEditText textView;

        public IpAddressTextWatcher(IpEditText textView)
        {
            this.textView = textView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            textView.removeTextChangedListener(this);
            int selectionPos = textView.getSelectionEnd();

            String ip = convertToIP(s.toString());
            s.clear();
            s.append(ip);

            textView.setSelection(selectionPos);
            textView.addTextChangedListener(this);
        }

        // Takes a string and inserts periods where necessary
        private String convertToIP(String input)
        {
            input = input.replace(".","");
            String retString = String.format("%s.%s.%s.%s",
                    getSubSequence(input,0,3),
                    getSubSequence(input,3,6),
                    getSubSequence(input,6,9),
                    getSubSequence(input,9,12));

            return retString;
        }

        // Returns a subsequence from a char sequence, or the max characters that could be extracted
        private String getSubSequence(String input, int start, int end)
        {
            if(start > input.length())
            {
                return "";
            }
            if(end > input.length())
            {
                return input.substring(start);
            }
            return input.substring(start,end);
        }
    }
}
