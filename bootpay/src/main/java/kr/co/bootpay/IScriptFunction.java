package kr.co.bootpay;

import android.webkit.JavascriptInterface;


public interface  IScriptFunction {
    @JavascriptInterface
    void error(String String);

    @JavascriptInterface
    void close(String var1);

    @JavascriptInterface
    void cancel(String var1);

    @JavascriptInterface
    void ready(String var1);

    @JavascriptInterface
    void confirm(String var1);

    @JavascriptInterface
    void done(String var1);
}
