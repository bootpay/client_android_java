package kr.co.bootpay.valid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

import java.util.Arrays;
import java.util.List;

import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.model.Request;

public class ValidRequest {
    public static Request validUXAvailablePG(Context context, Request request) {
        UX ux = request.getUX();
        if(PGAvailable.isUXPGDefault(ux)) return validPGDialog(context, request);
        if(PGAvailable.isUXPGSubscript(ux)) return validPGSubscript(context, request);
        else if(PGAvailable.isUXBootpayApi(ux)) return validPGDialog(context, request);
        else if(PGAvailable.isUXApp2App(ux)) return validBootpayUX(context, request);
        return request;
    }

    private static Request validPGDialog(Context context, Request request) {
        if(request.getPG().length() == 0) return request; // 통합결제창
        if(request.getMethods() != null && request.getMethods().size() > 0) return request; // 통합결제창

        List<Method> methodList = PGAvailable.getDefaultMethods(request);
        if(methodList == null) return request;
        if(methodList.size() == 1) request.setMethod(PGAvailable.methodToString(methodList.get(0)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            boolean contain = false;
            for(Method method : methodList) {
                String strMethod = PGAvailable.methodToString(method).toLowerCase();
                if(strMethod.equals(request.getMethod().toLowerCase())) {
                    contain = true;
                    break;
                }
            }
            if(!contain) {
                final String string = request.getPG() + "'s " + request.getMethod() + " is not supported";
                errorDialog(context, string);
            }
        }
        return request;
    }

    private static Request validPGSubscript(Context context, Request request) {
        if("nicepay".equals(request.getPG().toLowerCase())) throw new IllegalStateException(request.getPG() + " 정기결제는 클라이언트 UI 연동방식이 아닌, REST API를 통해 진행해주셔야 합니다.");
        List<String> rebill = Arrays.asList("card_rebill", "phone_rebill");
        if(!rebill.contains(request.getMethod())) {
            final String string = request.getMethod() + " is not supported in " + request.getUX() + ". select in " + rebill.toString();
            errorDialog(context, string);
        }

        List<Method> methodList = PGAvailable.getDefaultMethods(request);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            boolean contain = false;
            for(Method method : methodList) {
                String strMethod = PGAvailable.methodToString(method).toLowerCase();
                if(strMethod.equals(request.getMethod().toLowerCase())) {
                    contain = true;
                    break;
                }
            }
            if(!contain) {
                final String string = request.getPG() + "'s " + request.getMethod() + " is not supported";
                errorDialog(context, string);
            }
        }
        return request;
    }

    private static Request validBootpayUX(Context context, Request request) {
        List<PG> pgList = PGAvailable.getBootpayUX(request);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            boolean contain = false;
            for(PG pg : pgList) {
                if(pg.name().toLowerCase().equals(request.getPG())) {
                    contain = true;
                    break;
                }
            }

            if(!contain) {
                final String string = request.getPG() + "'s " + request.getMethod() + " is not supported";
                errorDialog(context, string);
            }
        }
        return request;
    }

    private static void errorDialog(Context context, final String msg) {
        // 친절히 알려주자
        new AlertDialog.Builder(context)
                .setTitle("Bootpay Android Dev Error")
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("종료",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                // 프로그램을 종료한다
                                throw new IllegalStateException(msg);
                            }
                        }).create().show();
    }
}
