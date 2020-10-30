package kr.co.bootpay.bio;

import android.webkit.JavascriptInterface;


public interface IBioFunction {
    @JavascriptInterface
    void easyCancel(String data);

    @JavascriptInterface
    void easyError(String data);

    @JavascriptInterface
    void easySuccess(String data);
}
