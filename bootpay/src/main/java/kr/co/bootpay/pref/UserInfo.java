package kr.co.bootpay.pref;

import android.content.Context;

import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import java.util.Locale;
import java.util.UUID;

public class UserInfo {
    private final String encryptPassword = "1q2w3e4r";
    private static UserInfo instance;
    private EncryptedPreferences encryptedPreferences;
    private UserInfo() {

    }
    public static UserInfo getInstance(Context context) {
        if(instance == null) {
            instance = new UserInfo();
            instance.encryptedPreferences = new EncryptedPreferences
                    .Builder(context)
                    .withEncryptionPassword(instance.encryptPassword).build();
        }

        return instance;
    }


//    private String bootpay_uuid;
//    private Long bootpay_last_time = System.currentTimeMillis();
//    private String bootpay_sk;
//    private String bootpay_application_id;
//    private String bootpay_user_id;
//    private String bootpay_receipt_id;
//    private String developerPayload;
//
//    private Boolean enable_onstore;
//    private String sim_operator;
//    private String install_package_market;
//    private String ad_id;

    public String getBootpayUuid() {
        return instance.encryptedPreferences.getString("bootpay_uuid", "");
    }

    public void setBootpayUuid(String bootpay_uuid) {
        instance.encryptedPreferences.edit()
                .putString("bootpay_uuid", bootpay_uuid)
                .apply();
    }

    public Long getBootpayLastTime() {
        return instance.encryptedPreferences.getLong("bootpay_last_time", System.currentTimeMillis());
    }

    public void setBootpayLastTime(Long bootpay_last_time) {
        instance.encryptedPreferences.edit()
                .putLong("bootpay_last_time", bootpay_last_time)
                .apply();
    }

    public String getBootpaySk() {
        return instance.encryptedPreferences.getString("bootpay_sk", "");
    }

    public void setBootpaySk(String bootpay_sk) {
        instance.encryptedPreferences.edit()
                .putString("bootpay_sk", bootpay_sk)
                .apply();
    }

    public void newSk(Long time) {
        instance.encryptedPreferences.edit()
                .putString("bootpay_sk", String.format(Locale.KOREA, "%s_%d", getBootpayUuid(), time))
                .apply();
    }

    public String getBootpayApplicationId() {
        return instance.encryptedPreferences.getString("bootpay_application_id", "");
    }

    public void setBootpayApplicationId(String bootpay_application_id) {
        instance.encryptedPreferences.edit()
                .putString("bootpay_application_id", bootpay_application_id)
                .apply();
    }

    public String getBootpayUserId() {
        return instance.encryptedPreferences.getString("bootpay_user_id", "");
    }

    public void setBootpayUserId(String bootpay_user_id) {
        instance.encryptedPreferences.edit()
                .putString("bootpay_user_id", bootpay_user_id)
                .apply();
    }

    public String getBootpay_receipt_id() {
        return instance.encryptedPreferences.getString("bootpay_receipt_id", "");
    }

    public void setBootpay_receipt_id(String bootpay_receipt_id) {
        instance.encryptedPreferences.edit()
                .putString("bootpay_receipt_id", bootpay_receipt_id)
                .apply();
    }

    public String getDeveloperPayload() {
        return instance.encryptedPreferences.getString("developerPayload", "");
    }

    public void setDeveloperPayload(String developerPayload) {
        instance.encryptedPreferences.edit()
                .putString("developerPayload", developerPayload)
                .apply();
    }

    public Boolean getEnableOneStore() {
        return instance.encryptedPreferences.getBoolean("enable_one_store", false);
    }

    public void setEnableOneStore(Boolean enable_onstore) {
        instance.encryptedPreferences.edit()
                .putBoolean("enable_onstore", enable_onstore)
                .apply();
    }

    public String getSimOperator() {
        return instance.encryptedPreferences.getString("sim_operator", "");
    }

    public void setSimOperator(String sim_operator) {
        instance.encryptedPreferences.edit()
                .putString("sim_operator", sim_operator)
                .apply();
    }

    public String getInstallPackageMarket() {
        return instance.encryptedPreferences.getString("install_package_market", "");
    }

    public void setInstallPackageMarket(String install_package_market) {
        instance.encryptedPreferences.edit()
                .putString("install_package_market", install_package_market)
                .apply();
    }

    public String getAdId() {
        return instance.encryptedPreferences.getString("ad_id", "");
    }

    public void setAdId(String ad_id) {
        instance.encryptedPreferences.edit()
                .putString("ad_id", ad_id)
                .apply();
    }

    public void update() {
        if(getBootpayUuid().isEmpty()) setBootpayUuid(UUID.randomUUID().toString());
        if(getBootpaySk().isEmpty()) setBootpaySk(String.format(Locale.KOREA, "%s_%d", getBootpayUuid(), getBootpayLastTime()));

        Long current = System.currentTimeMillis();

        boolean isExipred = current - getBootpayLastTime() > 30 * 60 * 1000l;
        if(isExipred) newSk(current);
        setBootpayLastTime(current);
    }

    public void finish() {
        setBootpayLastTime(System.currentTimeMillis());
    }
}
