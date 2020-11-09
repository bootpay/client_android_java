package kr.co.bootpay.bio;

import android.webkit.JavascriptInterface;

import kr.co.bootpay.model.bio.BioDeviceUse;
import kr.co.bootpay.model.bio.BioWallet;


public interface IBioActivityFunction {
    void startBioPay(BioDeviceUse user, BioWallet bioWallet);
    void goNewCardActivity();
    void goOtherActivity();
}
