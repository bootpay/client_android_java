package kr.co.bootpay.bio;

import kr.co.bootpay.model.bio.BioDeviceUse;
import kr.co.bootpay.model.bio.BioWallet;


public interface IBioPayFunction {
    void transactionConfirm(String data);
    void activityFinish();
}
