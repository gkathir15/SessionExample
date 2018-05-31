package com.msf.sessionexample;

import android.os.Build;
import android.provider.Settings;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sunnic.e2ee.client.SncE2EEClient;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView resultTxtTv;
    EditText usernameEv, passwordEv;
    Button login;
    Retrofit retrofit;
    String mAlias = "AndroidKeyStore";



    PreSessionData preSessionData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTxtTv = findViewById(R.id.resulttxt);
        usernameEv = findViewById(R.id.username);
        passwordEv = findViewById(R.id.password);
        login = findViewById(R.id.login);

        OkHttpClient builder = new OkHttpClient.Builder().addInterceptor(getLoggingInterceptor()).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder)
                .build();




        Log.d("devID ", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        Log.d("osVersion ", String.valueOf(Build.VERSION.CODENAME));



        Constants.DEVICE_ID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Constants.OS_VERSION = String.valueOf(Build.VERSION.RELEASE);

        preSession();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setLogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private Interceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }


    void preSession()
    {







        PreSessionClient presessionClient = retrofit.create(PreSessionClient.class);

        Call<PreSessionData>call = presessionClient.getPreSessionData("8.1.1",Constants.LANG,Constants.DEVICE_ID,Constants.API_KEY,Constants.VER_No);

        call.enqueue(new Callback<PreSessionData>() {
            @Override
            public void onResponse(Call<PreSessionData> call, Response<PreSessionData> response) {

                Log.d("response",""+response.body().getSessionID()+" message"+response.body().msg);

                preSessionData = response.body();

                resultTxtTv.setText(("session id:  "+response.body().getSessionID()+"\n"));







            }

            @Override
            public void onFailure(Call<PreSessionData> call, Throwable t) {

                Log.d("\n  \n  MainActivity","network call Failed for presession \n ");

            }
        });
    }


    void setLogin() throws Exception {

        String password =  passwordEv.getText().toString();
        Log.d("pass",passwordEv.getText().toString());

        PreSessionClient presessionClient = retrofit.create(PreSessionClient.class);
        String encyrptedpin = new SncE2EEClient().encryptPIN1(preSessionData.publicKey, preSessionData.randomNo,password);

        Call<LoginData> call = presessionClient.getLoginData("8.1.1",Constants.LANG,Constants.DEVICE_ID,Constants.API_KEY,Constants.VER_No, usernameEv.getText().toString(),preSessionData.sessionID,encyrptedpin);
        call.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {

                resultTxtTv.append(("\nLogin message: ")+response.body().getMsg()+"\n");
                //resultTxtTv.setText(("session id:  "+response.body().getLastLoginTime()));

                try {
                    resultTxtTv.append(("\nencrypted text:  "+getSignedKeystore(preSessionData.sessionID)));
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnrecoverableEntryException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (SignatureException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {

                Log.d("MainActivity","network call Failed for login");

            }
        });



    }

    public String makePrivateKey() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        end.add(Calendar.MONTH, 3);

        KeyPairGenerator kpGenerator = KeyPairGenerator
                .getInstance("RSA",
                        "AndroidKeyStore");

        AlgorithmParameterSpec spec;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {


            spec = new KeyPairGeneratorSpec.Builder(MainActivity.this)

                    .setAlias(mAlias)

                    .setSubject(new X500Principal("CN=" + mAlias))

                    .setSerialNumber(BigInteger.valueOf(1337))

                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();


        } else {

            spec = new KeyGenParameterSpec.Builder(mAlias, KeyProperties.PURPOSE_SIGN)
                    .setCertificateSubject(new X500Principal("CN=" + mAlias))
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .setCertificateSerialNumber(BigInteger.valueOf(1337))
                    .setCertificateNotBefore(start.getTime())
                    .setCertificateNotAfter(end.getTime())
                    .build();
        }

        kpGenerator.initialize(spec);

         KeyPair kp = kpGenerator.generateKeyPair();
        Log.d("key generator","Public Key is: " + kp.getPublic().toString());
     return kp.getPublic().toString();
    }




    public String getSignedKeystore(String sessionId) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableEntryException, InvalidKeyException, SignatureException, NoSuchProviderException, InvalidAlgorithmParameterException {

        makePrivateKey();

        byte[] data = sessionId.getBytes();
        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
        ks.load(null);

        KeyStore.Entry entry = ks.getEntry(mAlias, null);

        Signature s = Signature.getInstance("SHA256withRSA");


        s.initSign(((KeyStore.PrivateKeyEntry) entry).getPrivateKey());



        s.update(data);
        byte[] signature = s.sign();
        String result = Base64.encodeToString(signature, Base64.DEFAULT);
        s.initVerify(((KeyStore.PrivateKeyEntry) entry).getCertificate());
        s.update(data);
        boolean valid = s.verify(signature);

        resultTxtTv.append(("\nkeyStore verification : "+String.valueOf(valid)+"\n"));



        return result;
    }

//    public String decryptText() throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, UnrecoverableEntryException, InvalidKeyException {
//        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
//        ks.load(null);
//
//        KeyStore.Entry entry = ks.getEntry(mAlias, null);
//
//        Signature s = Signature.getInstance("SHA256withRSA");
//        return  null;
//    }



}
