package com.iliujing.mypassword.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.iliujing.mypassword.R;

public class BiometricPromptDialog extends Dialog implements View.OnClickListener{
    private Context mContext;
    private View.OnClickListener listener;

    public BiometricPromptDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public BiometricPromptDialog(Context context,View.OnClickListener listener){
        super(context);
        this.listener = listener;
    }

    public BiometricPromptDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BiometricPromptDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_biometric_prompt_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        findViewById(R.id.cancel_btn).setOnClickListener(listener);
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
        dismiss();
    }
}
