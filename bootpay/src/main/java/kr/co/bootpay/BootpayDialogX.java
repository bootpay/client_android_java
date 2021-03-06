package kr.co.bootpay;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import kr.co.bootpay.listener.EventListener;
import kr.co.bootpay.model.Request;

public class BootpayDialogX extends DialogFragment {

    private Request request;
    protected BootpayWebView bootpay;
    private EventListener listener;

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new Dialog(getActivity(), getTheme()){
//            @Override
//            public void onBackPressed() {
//                //do your stuff
//            }
//        };
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bootpay = new BootpayWebView(inflater.getContext());



        afterViewInit();
//        bootpay.getSettings().setUserAgentString(userAgent + "/뭥미");
        return bootpay;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void afterViewInit() {
        if (bootpay != null) {
            Dialog dialog = getDialog();
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                    if(keyCode == KeyEvent.KEYCODE_BACK) {
                        if(listener != null)
                            listener.onClose("android backbutton clicked");
                    }
                    return false;
                }
            });
            bootpay.setRequest(request)
                    .setDialog(dialog)
                    .setOnResponseListener(listener);
        }



//        setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(android.content.DialogInterface dialog, int keyCode, android.view.KeyEvent event) {
//
//                if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
//
//                }
//            }
//        }
    }

    public BootpayDialogX setOnResponseListener(EventListener listener) {
        this.listener = listener;
        return this;
    }

    public BootpayDialogX setRequest(Request request) {
        this.request = request;
        return this;
    }

    protected void transactionConfirm(String data) {
        if (bootpay != null)
            bootpay.transactionConfirm(data);
    }

    protected void removePaymentWindow() {
        if (bootpay != null)
            bootpay.removePaymentWindow();
    }
}
