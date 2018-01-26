package kr.co.bootpay.inapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import kr.co.bootpay.pref.UserInfo;

abstract class InAppPurchaseActivity extends Activity {
    protected final static String TYPE_PURCHASE = "inapp";
    protected final static String TYPE_SUBSCRIBE = "subs";
    private final static int API_VERSION = 3;
    private final static int REQUEST_CODE = 1001;
    private InAppPresenter presenter;

    private IInAppBillingService billingService;
    private final Intent billingIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND")
            .setPackage("com.android.vending");

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            billingService = IInAppBillingService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            billingService = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService(billingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        if (presenter == null)
            presenter = new InAppPresenter(this);
    }

    @Override
    protected void onDestroy() {
        if (billingService != null) unbindService(serviceConnection);
        super.onDestroy();
    }

    protected final void purchaseItem(String itemId) {
        purchaseItem(itemId, UUID.randomUUID().toString());
    }

    protected final void purchaseItem(String itemId, @NonNull String payload) {
        if (billingService == null) return;
        UserInfo.INSTANCE.setDeveloperPayload(payload);

        Bundle bundle = null;

        try {
            bundle = billingService.getBuyIntent(API_VERSION, getPackageName(), itemId, TYPE_PURCHASE, payload);
        } catch (RemoteException e) {
            onPurchaseException(e);
        }

        if (bundle == null) return;

        PendingIntent pendingIntent = bundle.getParcelable("BUY_INTENT");

        if (pendingIntent == null) return;

        try {
            startIntentSenderForResult(pendingIntent.getIntentSender(), REQUEST_CODE, new Intent(), 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            onPurchaseException(e);
        }
    }

    protected final void subscribeItem(String itemId) {
        subscribeItem(itemId, UUID.randomUUID().toString());
    }

    protected final void subscribeItem(String itemId, @NonNull String payload) {
        if (billingService == null) return;
        UserInfo.INSTANCE.setDeveloperPayload(payload);

        Bundle bundle = null;

        try {
            bundle = billingService.getBuyIntent(API_VERSION, getPackageName(), itemId, TYPE_SUBSCRIBE, payload);
        } catch (RemoteException e) {
            onPurchaseException(e);
        }

        if (bundle == null) return;

        PendingIntent pendingIntent = bundle.getParcelable("BUY_INTENT");

        if (pendingIntent == null) return;

        try {
            startIntentSenderForResult(pendingIntent.getIntentSender(), REQUEST_CODE, new Intent(), 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
            onPurchaseException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE) return;
        int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
        String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
        String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
        if (resultCode == RESULT_OK && responseCode == 0) try {
            JSONObject jo = new JSONObject(purchaseData);
            String itemId = jo.getString("productId");
            String token = jo.getString("purchaseToken");
            String order_id = jo.getString("order_id");
            String payload = jo.getString("developerPayload");
            String auth = UserInfo.INSTANCE.getDeveloperPayload();
            if (!auth.isEmpty() && auth.equals(payload)) {
                onPurchaseSuccess(itemId, token, purchaseData);
                JSONObject j = new JSONObject(searchItemDetails(itemId));
                String price = j.getString("price");
                String itemName = j.getString("title");
                presenter.purchase(application_id(), order_id, itemName, price);
            } else onPurchaseFailed(itemId, token, "Developer Payload 문제");
        } catch (JSONException e) {
            onPurchaseException(e);
        }
    }

    protected final void consumeByToken(String token) {
        new Consume().execute(token);
    }

    protected final void consume(String itemId) {
        consumeByToken(searchItemToken(itemId));
    }

    protected final ArrayList<String> getInAppItems() {
        return getItems(TYPE_PURCHASE);
    }

    protected final ArrayList<String> getSubsItems() {
        return getItems(TYPE_SUBSCRIBE);
    }

    protected final ArrayList<String> getItems(String type) {
        if (billingService != null) try {
            Bundle bundle = billingService.getPurchases(API_VERSION, getPackageName(), type, null);
            if (bundle != null) {
                int response = bundle.getInt("RESPONSE_CODE");
                if (response == 0) return bundle.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            }
        } catch (RemoteException e) {
            onPurchaseException(e);
        }
        return null;
    }

    protected final Map<String, String> getItemTokens() {
        ArrayList<String> data = getInAppItems();
        if (data != null && !data.isEmpty()) {
            int size = data.size();
            HashMap<String, String> result = new HashMap<>(size % 31);
            for (int i = 0; i < size; i++)
                try {
                    JSONObject jo = new JSONObject(data.get(i));
                    String id = jo.getString("productId");
                    String token = jo.getString("purchaseToken");
                    result.put(id, token);
                } catch (JSONException e) {
                    onPurchaseException(e);
                }
            return result;
        }
        return null;
    }

    protected final String searchItemDetails(String itemId) {
        Map<String, String> map = getItemDetails();
        return map != null ? map.get(itemId) : null;
    }

    protected final Map<String, String> getItemDetails() {
        ArrayList<String> details = getItemDetailList();
        if (details != null && !details.isEmpty()) {
            int size = details.size();
            Map<String, String> result = new HashMap<>(size % 31);
            for (int i = 0; i < size; i++)
                try {
                    String data = details.get(i);
                    JSONObject jo = new JSONObject(data);
                    String id = jo.getString("productId");
                    result.put(id, data);
                } catch (JSONException e) {
                    onPurchaseException(e);
                }
            return result;
        }
        return null;
    }

    protected final String searchItemToken(String itemId) {
        Map<String, String> tokens = getItemTokens();
        return tokens != null ? tokens.get(itemId) : null;
    }

    protected final ArrayList<String> getItemDetailList() {
        if (billingService != null) try {
            Bundle bundle = billingService.getSkuDetails(API_VERSION, getPackageName(), TYPE_PURCHASE, null);
            return bundle.getStringArrayList("DETAILS_LIST");
        } catch (RemoteException e) {
            onPurchaseException(e);
        }
        return null;
    }

    protected final ArrayList<String> getPurchasableItems() {
        if (billingService != null) try {
            Bundle bundle = billingService.getSkuDetails(API_VERSION, getPackageName(), TYPE_PURCHASE, null);
            return bundle.getStringArrayList("ITEM_ID_LIST");
        } catch (RemoteException e) {
            onPurchaseException(e);
        }
        return null;
    }

    protected abstract String application_id();

    protected abstract void onPurchaseSuccess(String itemId, String token, String dataSignature);

    protected abstract void onPurchaseFailed(String itemId, String token, String errorMessage);

    protected abstract void onConsumeSuccess();

    protected abstract void onConsumeFailed(int errorCode);

    protected void onPurchaseException(Exception e) {
        // optional override
    }

    private class Consume extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            int result = -1;
            if (strings.length > 0) try {
                result = billingService.consumePurchase(API_VERSION, getPackageName(), strings[0]);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer response) {
            if (response == 0) onConsumeSuccess();
            else onConsumeFailed(response);
        }
    }
}

