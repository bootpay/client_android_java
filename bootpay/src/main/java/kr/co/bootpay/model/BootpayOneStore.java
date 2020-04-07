package kr.co.bootpay.model;

import java.util.Locale;

public class BootpayOneStore {
    public String ad_id;
    public String sim_operator;
    public String installer_package_name;

    private String oneStore(String... etcs) {
        StringBuilder sb = new StringBuilder();
        for(String str : etcs) {
            if(str.length() == 0) continue;
            if(sb.toString().length() > 0) sb.append(",");
            sb.append(str);
        }
        return String.format(Locale.KOREA,"{%s}", sb.toString());
    }

    private String adId() {
        if(this.ad_id == null) return "";
        return String.format(Locale.KOREA, "ad_id: '%s'", this.ad_id);
    }

    private String simOperator() {
        if(this.sim_operator == null) return "";
        return String.format(Locale.KOREA, "sim_operator: '%s'", this.sim_operator);
    }

    private String installerPackageName() {
        if(this.installer_package_name == null) return "";
        return String.format(Locale.KOREA, "installer_package_name: '%s'", this.installer_package_name);
    }

    public final  String toJson() {
        return oneStore(
                adId(),
                simOperator(),
                installerPackageName()
        );
    }
}
