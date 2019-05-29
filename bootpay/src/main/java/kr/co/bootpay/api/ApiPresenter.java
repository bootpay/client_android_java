package kr.co.bootpay.api;

import com.google.gson.Gson;

import java.util.concurrent.Executors;

import kr.co.bootpay.enums.UX;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.pref.UserInfo;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class ApiPresenter {
    ApiService service;
    Scheduler scheduler;

    public ApiPresenter(ApiService service) {
        this.service = service;
        this.scheduler = Schedulers.from(Executors.newCachedThreadPool());
    }

    public void request(Request request) {
        UX ux = request.getUX();
        request.getBootExtra().setUX(ux.toString());
        if(ux == UX.BOOTPAY_REMOTE_LINK) {
            request_link(request);
        } else if(ux == UX.BOOTPAY_REMOTE_ORDER) {
            request_form(request);
        } else if(ux == UX.BOOTPAY_REMOTE_PRE) {
            request_pre(request);
        }
    }

    public void request_link(Request request) {
        String userJson = request.getBootUser().toGson();
        String extraJson = request.getBootExtra().toGson();
        String sms_payload = request.getSmsPayload().toGson();

        service.getApi().request_link(
                request.getApplicationId(),
                "2", //Android
                request.getMethod(),
                new Gson().toJson(request.getMethods()),
                request.getPG(),
                request.getPrice(),
                request.getTaxFree(),
                request.getName(),
                new Gson().toJson(request.getItems()),
                request.getIsShowAgree(),
                UserInfo.getInstance(service.getContext()).getBootpayUuid(),
                UserInfo.getInstance(service.getContext()).getBootpaySk(),
                System.currentTimeMillis(),
                userJson,
                UserInfo.getInstance(service.getContext()).getBootpayUserId(),
                request.getBootKey(),
                params(request),
                request.getOrderId(),
                request.getUseOrderId(),
                request.getAccountExpireAt(),
                extraJson,
                sms_payload
        );
    }

    public void request_form(Request request) {
        String userJson = request.getBootUser().toGson();
        String extraJson = request.getBootExtra().toGson();
        String sms_payload = request.getSmsPayload().toGson();

        service.getApi().request_form(
                request.getApplicationId(),
                "2", //Android
                request.getMethod(),
                new Gson().toJson(request.getMethods()),
                request.getPG(),
                request.getPrice(),
                request.getTaxFree(),
                request.getName(),
                new Gson().toJson(request.getItems()),
                request.getIsShowAgree(),
                UserInfo.getInstance(service.getContext()).getBootpayUuid(),
                UserInfo.getInstance(service.getContext()).getBootpaySk(),
                System.currentTimeMillis(),
                userJson,
                UserInfo.getInstance(service.getContext()).getBootpayUserId(),
                request.getBootKey(),
                params(request),
                request.getOrderId(),
                request.getUseOrderId(),
                request.getAccountExpireAt(),
                extraJson,
                sms_payload,
                request.getRemoteOrderForm().toGson()
        );
    }

    public void request_pre(Request request) {
        String userJson = request.getBootUser().toGson();
        String extraJson = request.getBootExtra().toGson();
        String sms_payload = request.getSmsPayload().toGson();

        service.getApi().request_pre(
                request.getApplicationId(),
                "2", //Android
                request.getMethod(),
                new Gson().toJson(request.getMethods()),
                request.getPG(),
                request.getPrice(),
                request.getTaxFree(),
                request.getName(),
                new Gson().toJson(request.getItems()),
                request.getIsShowAgree(),
                UserInfo.getInstance(service.getContext()).getBootpayUuid(),
                UserInfo.getInstance(service.getContext()).getBootpaySk(),
                System.currentTimeMillis(),
                userJson,
                UserInfo.getInstance(service.getContext()).getBootpayUserId(),
                request.getBootKey(),
                params(request),
                request.getOrderId(),
                request.getUseOrderId(),
                request.getAccountExpireAt(),
                extraJson,
                sms_payload,
                request.getRemoteOrderPre().toGson()
        );
    }

    private String params(Request request) {
        if(request == null) return "";
        if(request.getParams() == null) return "";
        return new Gson().toJson(request.getParams());
    }
}
