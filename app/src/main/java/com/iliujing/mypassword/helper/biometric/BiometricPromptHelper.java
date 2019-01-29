package com.iliujing.mypassword.helper.biometric;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.Toast;

import com.iliujing.mypassword.R;
import com.iliujing.mypassword.helper.LocalAndroidKeyStore;
import com.iliujing.mypassword.helper.LocalSharedPreference;
import com.iliujing.mypassword.helper.SimpleAuthenticationCallback;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

@TargetApi(Build.VERSION_CODES.P)
public class BiometricPromptHelper extends BiometricPrompt.AuthenticationCallback {

    private BiometricPrompt manager;
    private CancellationSignal mCancellationSignal;
    private SimpleAuthenticationCallback callback;
    private LocalSharedPreference mLocalSharedPreference;
    private LocalAndroidKeyStore mLocalAndroidKeyStore;

    private Context mContext;


    //PURPOSE_ENCRYPT,则表示生成token，否则为取出token
    private int purpose = KeyProperties.PURPOSE_ENCRYPT;
    private String data;

    public BiometricPromptHelper(Context context) {
        this.mContext = context;
        manager = new BiometricPrompt.Builder(context)
                .setTitle(context.getString(R.string.biometric_title))
                .setDescription(context.getString(R.string.biometric_content))
                .setSubtitle(context.getString(R.string.biometric_subtitle))
                .setNegativeButton(context.getString(R.string.biometric_button), context.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).build();
        mLocalSharedPreference = new LocalSharedPreference(context);
        mLocalAndroidKeyStore = new LocalAndroidKeyStore();
    }

    public void generateKey() {
        //在keystore中生成加密密钥
        mLocalAndroidKeyStore.generateKey(LocalAndroidKeyStore.keyName);
        setPurpose(KeyProperties.PURPOSE_ENCRYPT);
    }

    public boolean isKeyProtectedEnforcedBySecureHardware() {
        return mLocalAndroidKeyStore.isKeyProtectedEnforcedBySecureHardware();
    }


    public boolean containsToken() {
        return mLocalSharedPreference.containsKey(mLocalSharedPreference.dataKeyName);
    }

    public void setCallback(SimpleAuthenticationCallback callback) {
        this.callback = callback;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    public void setData(String data){
        this.data = data;
    }

    public boolean authenticate() {
        try {
            BiometricPrompt.CryptoObject object;
            if (purpose == KeyProperties.PURPOSE_DECRYPT) {
                String IV = mLocalSharedPreference.getData(mLocalSharedPreference.IVKeyName);
                object = mLocalAndroidKeyStore.getBiometricCryptoObject(Cipher.DECRYPT_MODE, Base64.decode(IV, Base64.URL_SAFE));
                if (object == null) {
                    return false;
                }
            } else {
                object = mLocalAndroidKeyStore.getBiometricCryptoObject(Cipher.ENCRYPT_MODE, null);
            }
            mCancellationSignal = new CancellationSignal();
            manager.authenticate(object, mCancellationSignal, mContext.getMainExecutor(), this);
            return true;
        } catch (SecurityException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void stopAuthenticate() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
        callback = null;
    }

    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        if (callback == null) {
            return;
        }
        if (result.getCryptoObject() == null) {
            callback.onAuthenticationFail();
            return;
        }
        final Cipher cipher = result.getCryptoObject().getCipher();
        if (purpose == KeyProperties.PURPOSE_DECRYPT) {
            //取出secret key并返回
            String data = mLocalSharedPreference.getData(mLocalSharedPreference.dataKeyName);
            if (TextUtils.isEmpty(data)) {
                callback.onAuthenticationFail();
                return;
            }
            try {
                byte[] decrypted = cipher.doFinal(Base64.decode(data, Base64.URL_SAFE));
                callback.onAuthenticationSucceeded(new String(decrypted));
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
                callback.onAuthenticationFail();
            }
        } else {
            //将前面生成的data包装成secret key，存入沙盒
            try {
                byte[] encrypted = cipher.doFinal(data.getBytes());
                byte[] IV = cipher.getIV();
                String se = Base64.encodeToString(encrypted, Base64.URL_SAFE);
                String siv = Base64.encodeToString(IV, Base64.URL_SAFE);
                if (mLocalSharedPreference.storeData(mLocalSharedPreference.dataKeyName, se) &&
                        mLocalSharedPreference.storeData(mLocalSharedPreference.IVKeyName, siv)) {
                    callback.onAuthenticationSucceeded(null);
                }else{
                    callback.onAuthenticationFail();
                }
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
                callback.onAuthenticationFail();
            }
        }
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        if (callback != null) {
            callback.onAuthenticationFail();
        }
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
    }

    @Override
    public void onAuthenticationFailed() {
    }

}
