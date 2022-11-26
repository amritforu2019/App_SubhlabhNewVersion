package com.vastu.shubhlabhvastu.Task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import com.vastu.shubhlabhvastu.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;


public class CommonTask {
    static int count = 0;
    static boolean exit = false;

    public static void backButtonPressed(Activity activity) {
        Toasts.makeText(activity, "Click Again to Exit", ToastType.ERROR);
        count += 1;
        exit = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                count = 0;
                exit = false;
            }
        }, 1000);

        if (count >= 2 && exit == true) {
            activity.finishAffinity();
            System.exit(0);
        }
    }

    public static void redirectActivity(Activity activity, Class classx) {
        activity.startActivity(new Intent(activity, classx));
        activity.overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
    }

    public static void redirectFinishActivity(Activity activity, Class classx) {
        activity.startActivity(new Intent(activity, classx));
        activity.overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
        activity.finish();
    }

    public static void redirectActivity(Activity activity, Class classx, Bundle bundle) {
        activity.startActivity(new Intent(activity, classx).putExtras(bundle));
        activity.overridePendingTransition(R.transition.fade_in, R.transition.fade_out);
    }

    public static void redirectFinishActivity(Activity activity, Class classx, Bundle bundle) {
        activity.startActivity(new Intent(activity, classx).putExtras(bundle));
        activity.overridePendingTransition(R.transition.fade_in, R.transition.fade_out);

        activity.finish();
    }
    public static String getminbysec(final int val)
    {

        int sec = val % 60;
        int min = (val / 60)%60;
        int hours = (val/60)/60;
        String retval=String.valueOf(min)+":"+sec;
        return retval;
    }

     public static SSLSocketFactory getSocketFactory(Context con) {
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = con.getResources().openRawResource(R.raw.anywherelearning_in);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                Log.e("CERT", "ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    Log.e("CipherUsed", session.getCipherSuite());
                    return hostname.compareTo("devindia.in") == 0; //The Hostname of your server.
                }
            };


            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            SSLContext context = null;
            context = SSLContext.getInstance("TLS");

            context.init(null, tmf.getTrustManagers(), null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            SSLSocketFactory sf = context.getSocketFactory();
            return sf;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }

        return null;
    }


}
