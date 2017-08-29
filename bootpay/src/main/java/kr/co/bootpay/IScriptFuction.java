package kr.co.bootpay;

import android.webkit.JavascriptInterface;

public interface IScriptFuction {
    @JavascriptInterface
    void error(String data);

    @JavascriptInterface
    void cancel(String data);

    @JavascriptInterface
    void confirm(String data);

    @JavascriptInterface
    void done(String data);
}
