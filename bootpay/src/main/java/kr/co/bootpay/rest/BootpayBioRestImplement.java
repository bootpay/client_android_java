package kr.co.bootpay.rest;

import kr.co.bootpay.model.res.ResEasyBiometric;
import kr.co.bootpay.model.res.ResReceiptID;
import kr.co.bootpay.model.res.ResWalletList;
import kr.co.bootpay.rest.model.ResDefault;
import kr.co.bootpay.rest.model.RestEasyPayUserTokenData;
import kr.co.bootpay.rest.model.RestTokenData;

public interface BootpayBioRestImplement {
    void callbackEasyBiometric(ResEasyBiometric res);
    void callbacktEasyBiometricRegister(ResEasyBiometric res);
    void callbackEasyCardWallet(ResWalletList res);
    void callbackEasyCardRequest(ResReceiptID res);
    void callbackEasyTransaction(String data);
    void callbackDeleteWalletID(ResDefault res);
}
