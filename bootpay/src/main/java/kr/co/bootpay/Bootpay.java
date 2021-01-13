package kr.co.bootpay;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;


import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import kr.co.bootpay.enums.PG;
import kr.co.bootpay.model.BootUser;
import kr.co.bootpay.pref.UserInfo;

public class Bootpay {
    private static Context context;
    protected  static BootpayBuilder builder;

    public static void setContext(Context context) {
        Bootpay.context = context;
    }


    public static void useOnestoreApi(Context context) {
        useOnestoreApi(context, true);
    }




    public static void useOnestoreApi(Context context, Boolean enable) {
        if(enable == false) {
            UserInfo.getInstance(context).setEnableOneStore(enable);
            return;
        }

        UserInfo.getInstance(context).update();
        UserInfo.getInstance(context).setEnableOneStore(enable);
        UserInfo.getInstance(context).setInstallPackageMarket(getInstallerPackageName(context));
        UserInfo.getInstance(context).setSimOperator(getSimOperator(context));
        new GoogleAppIdTask().execute();
    }

    private static String getInstallerPackageName(Context context) {
        if (context != null) {

            Context applicationContext = context.getApplicationContext();
            PackageManager pm = applicationContext.getPackageManager();
            final String installPackageName =
                    pm.getInstallerPackageName(applicationContext.getPackageName());

            if (!TextUtils.isEmpty(installPackageName)) {
                return installPackageName;
            }
        }
        return "UNKNOWN_INSTALLER";
    }

    private static String getSimOperator(Context context) {
        if (context != null) {

            Context applicationContext = context.getApplicationContext();
            TelephonyManager telephonyManager =
                    (TelephonyManager)
                            applicationContext.getSystemService(Context.TELEPHONY_SERVICE);

            if (telephonyManager != null && telephonyManager.getSimState()
                    == TelephonyManager.SIM_STATE_READY) {
                return telephonyManager.getSimOperator();
            }
        }
        return "UNKNOWN_SIM_OPERATOR";
    }

    private static class GoogleAppIdTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(final Void... params) {

            try {

//                Log.d("intent", e.getMessage());
                return AdvertisingIdClient.getAdvertisingIdInfo(context).getId();
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
//                Logger.logError("IllegalStateException");
            }  catch (Exception ex) {
                ex.printStackTrace();
            }
            return "UNKNOWN_ADID";
        }

        protected void onPostExecute(String adId) {
            UserInfo.getInstance(context).setAdId(adId);
        }
    }


    /******************
     * 부트페이 앱투앱 관련 로직
     ******************/
    public static BootpayBuilder init(Context context) {
        Bootpay.context = context;
        return builder = new BootpayBuilder(context);
    }


    /******************
     * 부트페이 일반결제 관련 로직
     ******************/
    public static BootpayBuilder init(Activity activity) {
        return init(activity.getFragmentManager());
    }

    public static BootpayBuilder init(Fragment fragment) {
        return init(fragment.getFragmentManager());
    }

    public static BootpayBuilder init(FragmentManager fragmentManager) {
//        fragmentManager.c
        return builder = new BootpayBuilder(fragmentManager);
    }

    public static BootpayBuilder init(androidx.fragment.app.FragmentManager fragmentManager) {
//        fragmentManager.c
        return builder = new BootpayBuilder(fragmentManager);
    }

    public static void finish() {
        builder = null;
    }

    public static void confirm(String data) {
        if (builder != null) builder.transactionConfirm(data);
    }

    public static void removePaymentWindow() {
        if (builder != null) builder.removePaymentWindow();
    }

    public static void dismiss() {
        if (builder != null) builder.dismiss();
    }

    public static String getPG(PG pg) {
        switch (pg) {
            case BOOTPAY:
                return "bootpay";
            case PAYAPP:
                return "payapp";
            case DANAL:
                return "danal";
            case KCP:
                return "kcp";
            case INICIS:
                return "inicis";
            case UDPAY:
                return "udpay";
            case LGUP:
                return "lgup";
            case KAKAO:
                return "kakao";
            case EASYPAY:
            case KICC:
                return "easypay";
            case TPAY:
            case JTNET:
                return "tpay";
            case MOBILIANS:
                return "mobilians";
            case PAYLETTER:
                return "payletter";
            case NICEPAY:
                return "nicepay";
            case PAYCO:
                return "payco";
            case ONESTORE:
                return "onestore";
            case WELCOME:
                return "welcome";
            case TOSS:
                return "toss";
            default:
                return "";
        }
    }
}