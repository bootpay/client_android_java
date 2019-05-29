package kr.co.bootpay.unity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.gson.Gson;
import com.unity3d.player.UnityPlayer;

import java.util.List;

import kr.co.bootpay.BootpayInnerActivity;
import kr.co.bootpay.api.ApiPresenter;
import kr.co.bootpay.api.ApiService;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.listner.CancelListener;
import kr.co.bootpay.listner.CloseListener;
import kr.co.bootpay.listner.ConfirmListener;
import kr.co.bootpay.listner.DoneListener;
import kr.co.bootpay.listner.ErrorListener;
import kr.co.bootpay.listner.EventListener;
import kr.co.bootpay.listner.ReadyListener;
import kr.co.bootpay.model.BootExtra;
import kr.co.bootpay.model.BootUser;
import kr.co.bootpay.model.Item;
import kr.co.bootpay.model.RemoteOrderForm;
import kr.co.bootpay.model.RemoteOrderPre;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.model.SMSPayload;
import kr.co.bootpay.pref.UserInfo;
import kr.co.bootpay.valid.PGAvailable;
import kr.co.bootpay.valid.ValidRequest;

public class BootpayUnity {
    private static Context context;
    private FragmentManager fragmentManager;
    protected Request request = new Request();
    protected EventListener listener;
//    private ErrorListener error;
//    private ReadyListener ready;
//    private CloseListener close;
//    private DoneListener done;
//    private CancelListener cancel;
//    private ConfirmListener confirm;

    private UnityView unityView;
    private ApiPresenter presenter;

    public BootpayUnity() {
        final Activity currentActivity = UnityPlayer.currentActivity;
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BootpayUnity.context = currentActivity;
            }
        });
    }

    public void SetApplicationId(String applicationId) {
        request.setApplicationId(applicationId);
    }

    public void SetPrice(String price) {
        request.setPrice(new Double(price));
    }

    public void SetPrice(int price) {
        request.setPrice(new Double(price));
    }

    public void SetPrice(Double price) {
        request.setPrice(price);
    }


    public void SetPG(String pg) {
        request.setPG(pg);
    }

    public void SetItemName(String name) {
        request.setName(name);
    }

    public void AddItem(String name, int quantity, String primaryKey, int price) {
        request.addItem(new Item(name, quantity, primaryKey, new Double(price), "", "", ""));
    }

    public void AddItem(String name, int quantity, String primaryKey, int price, String cat1, String cat2, String cat3) {
        request.addItem(new Item(name, quantity, primaryKey, new Double(price), cat1, cat2, cat3));
    }

    public void AddItem(String name, int quantity, String primaryKey, Double price) {
        request.addItem(new Item(name, quantity, primaryKey, price, "", "", ""));
    }

    public void AddItem(String name, int quantity, String primaryKey, Double price, String cat1, String cat2, String cat3) {
        request.addItem(new Item(name, quantity, primaryKey, price, cat1, cat2, cat3));
    }

    public void AddItem(Item item) {
        request.addItem(item);
    }

    public void AddItems(List<Item> items) {
        request.addItems(items);
    }

    public void SetItems(List<Item> items) {
        request.setItems(items);
    }

    public void SetIsShowAgree(Boolean isShow) {
        request.setIsShowAgree(isShow);
    }

    public void SetOrderId(String orderId) {
        request.setOrderId(orderId);
    }

    public void SetUseOrderId(boolean use_order_id) {
        request.setUseOrderId(use_order_id);
    }

    public void SetBootUser(BootUser bootUser) {
        request.setBootUser(bootUser);
    }

    public void SetBootExtra(BootExtra bootExtra) {
        request.setBootExtra(bootExtra);
    }

    public void SetRemoteOrderForm(RemoteOrderForm remoteForm) {
        request.setRemoteOrderForm(remoteForm);
    }

    public void SetRemoteOrderPre(RemoteOrderPre remotePre) {
        request.setRemoteOrderPre(remotePre);
    }


    public void SetSMSPayload(SMSPayload smsPayload) {
        request.setSmsPayload(smsPayload);
    }

    public void SetMethod(String method) {
        request.setMethod(method);
    }

    public void SetMethods(List<String> methods) {
        request.setMethods(methods);
    }

    public void SetAccountExpireAt(String account_expire_at) {
        request.setAccountExpireAt(account_expire_at);
    }

    public void SetRequest(Request request) {
        this.request = request;
    }

//    public void SetEventListener(EventListener eventListener) {
//        this.request = request;
//    }

    public void SetParams(Object params) {
        request.setParams(new Gson().toJson(params));
    }

    public void SetParams(String params) {
        request.setParams(params);
    }

    public void SetUX(String ux) {
        request.setUX(UX.valueOf(ux));
    }

    private boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }
    private void error(String message) {
        throw new RuntimeException(message);
    }



    public static void UseOneStoreApi() {
        UseOneStoreApi(true);
    }

    public static void UseOneStoreApi(Boolean enable) {
        if(enable == false) {
            UserInfo.getInstance(context).setEnableOneStore(enable);
            return;
        }

        UserInfo.getInstance(context).update();
        UserInfo.getInstance(context).setEnableOneStore(enable);
        UserInfo.getInstance(context).setInstallPackageMarket(getInstallerPackageName(context));
        UserInfo.getInstance(context).setSimOperator(getSimOperator(context));
        new GoogleAppIdTask().execute();

//        final Activity currentActivity = UnityPlayer.currentActivity;
//        currentActivity.runOnUiThread(new Runnable() {public void run() {
//            if(enable == false) {
//                UserInfo.getInstance(context).setEnableOneStore(enable);
//                return;
//            }
//
//            UserInfo.getInstance(context).update();
//            UserInfo.getInstance(context).setEnableOneStore(enable);
//            UserInfo.getInstance(context).setInstallPackageMarket(getInstallerPackageName(context));
//            UserInfo.getInstance(context).setSimOperator(getSimOperator(context));
//            new GoogleAppIdTask().execute();
//        }});

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

                return AdvertisingIdClient.getAdvertisingIdInfo(context).getId();
//                Logger.logDebug("adid : " + adId);
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
//    public static void init() {
//        final Activity currentActivity = UnityPlayer.currentActivity;
//        currentActivity.runOnUiThread(new Runnable() {public void run() {
//            Context context = currentActivity;
//            builder =  new BootpayUnityBuilder(context);
//        }});
//    }

    public void Request(final String gameObject) {
        if(context == null) throw new IllegalStateException("context cannot be null from " + request.getUX().toString());

//        validCheck();
        UserInfo.getInstance(context).update();

        this.request = ValidRequest.validUXAvailablePG(this.request);
        UX ux = request.getUX();
        if(PGAvailable.isUXPGDefault(ux)) requestDialog(gameObject);
        else if(PGAvailable.isUXPGSubscript(ux)) {
            this.request.setMethod("card_rebill");
            requestDialog(gameObject);
        }
        else if(PGAvailable.isUXBootpayApi(ux)) requestApi();
        else if(PGAvailable.isUXApp2App(ux)) requestApp2App();
        else throw new IllegalStateException(ux.toString() + " is not supported!");
    }

    private void requestApi() {
        if(context == null) throw new IllegalStateException("context cannot be null from " + request.getUX());
        if (presenter == null) presenter = new ApiPresenter(new ApiService(context));
        presenter.request(request);
    }

    private void requestApp2App() {
        Intent intent = new Intent(context, BootpayInnerActivity.class);
        context.startActivity(intent);
    }

    public void TransactionConfirm(String data) {
        if (unityView != null)
            unityView.transactionConfirm(data);
    }

    public void Dismiss() {
        RemovePaymentWindow();
    }

    public void RemovePaymentWindow() {
        if (unityView != null)
            unityView.removePaymentWindow();
    }

    private void callbackUnity(String gameObject, String method, String message) {
        final Activity a = UnityPlayer.currentActivity;
        a.runOnUiThread(new Runnable() {public void run() {
            if (unityView.IsInitialized()) {
                UnityPlayer.UnitySendMessage(gameObject, method, message);
            }
        }});
    }

    private void requestDialog(String gameObject) {
        EventListener listener = new EventListener() {
            @Override
            public void onError(String message) {
                Log.d("bootpay", "error: " + message);
                callbackUnity(gameObject, "OnError", message);
            }

            @Override
            public void onCancel(String message) {
                UserInfo.getInstance(context).finish();
                Log.d("bootpay", "cancel: " + message);
                unityView.removePaymentWindow();
                callbackUnity(gameObject, "OnCancel", message);
            }

            @Override
            public void onClose(String message) {
                unityView.removePaymentWindow();
                Log.d("bootpay", "close: " + message);
                callbackUnity(gameObject, "OnClose", message);
            }

            @Override
            public void onReady(String message) {
                Log.d("bootpay", "ready: " + message);
                callbackUnity(gameObject, "OnReady", message);
            }

            @Override
            public void onConfirm(String message) {
                Log.d("bootpay", "confirm: " + message);
                callbackUnity(gameObject, "OnConfirm", message);
            }

            @Override
            public void onDone(String message) {
                Log.d("bootpay", "done: " + message);
                callbackUnity(gameObject, "OnDone", message);
            }
        };
        unityView = new UnityView(request);
        unityView.init(gameObject, listener);
        unityView.show();
    }

//    public static void finish() {
//        unityView = null;
//        builder = null;
//    }
//
//    public static void confirm(String data) {
//        if (builder != null) builder.transactionConfirm(data);
//    }
//
//    public static void removePaymentWindow() {
//        if (builder != null) builder.removePaymentWindow();
//    }
}