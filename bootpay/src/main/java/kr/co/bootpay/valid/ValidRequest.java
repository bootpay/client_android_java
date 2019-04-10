package kr.co.bootpay.valid;

import android.os.Build;

import java.util.Arrays;
import java.util.List;

import kr.co.bootpay.enums.Method;
import kr.co.bootpay.enums.PG;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.model.Request;

public class ValidRequest {
    public static Request validUXAvailablePG(Request request) {
        UX ux = request.getUX();
        if(PGAvailable.isUXPGDefault(ux)) return validPGDialog(request);
        if(PGAvailable.isUXPGSubscript(ux)) return validPGSubscript(request);
        else if(PGAvailable.isUXBootpayApi(ux)) return validPGDialog(request);
        else if(PGAvailable.isUXApp2App(ux)) return validBootpayUX(request);
        return request;
    }

    private static Request validPGDialog(Request request) {
        if(request.getPg().length() == 0) return request; // 통합결제창
        if(request.getMethods() != null || request.getMethods().size() > 0) return request; // 통합결제창

        List<Method> methodList = PGAvailable.getDefaultMethods(request);
        if(methodList.size() == 1) request.setMethod(PGAvailable.methodToString(methodList.get(0)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            boolean contain = false;
            for(Method method : methodList) {
                if(PGAvailable.methodToString(method).equals(request.getMethod())) {
                    contain = true;
                    break;
                }
            }
            if(!contain) throw new IllegalStateException(request.getPg() + "'s " + request.getMethod() + " is not supported");
        }
        return request;
    }

    private static Request validPGSubscript(Request request) {
        List<String> rebill = Arrays.asList("card_rebill", "phone_rebill");
        if(!rebill.contains(request.getMethod())) throw new IllegalStateException(request.getMethod() + " is not supported in " + request.getUx());
        List<Method> methodList = PGAvailable.getDefaultMethods(request);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            boolean contain = false;
            for(Method method : methodList) {
                if(method.name().toLowerCase() == request.getMethod()) {
                    contain = true;
                    break;
                }
            }
            if(!contain) throw new IllegalStateException(request.getPg() + "'s " + request.getMethod() + " is not supported");
        }
        return request;
    }

    private static Request validBootpayUX(Request request) {
        List<PG> pgList = PGAvailable.getBootpayUX(request);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            boolean contain = false;
            for(PG pg : pgList) {
                if(pg.name().toLowerCase() == request.getPg()) {
                    contain = true;
                    break;
                }
            }
            if(!contain) throw new IllegalStateException(request.getUx() + "'s " + request.getPg() + " is not supported");
        }
        return request;
    }
}
