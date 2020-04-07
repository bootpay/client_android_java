package kr.co.bootpay.api;

import android.util.Log;

import com.google.gson.Gson;

import java.util.concurrent.Executors;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kr.co.bootpay.analytics.LoginResult;
import kr.co.bootpay.enums.UX;
import kr.co.bootpay.model.BootUser;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.rest.BootpayRestImplement;
import kr.co.bootpay.rest.model.ResEasyPayUserToken;
import kr.co.bootpay.rest.model.ResRestToken;
import kr.co.bootpay.pref.UserInfo;
//import rx.Scheduler;
//import rx.schedulers.Schedulers;

public class ApiPresenter {
    ApiService service;
    BootpayRestImplement parent;
    ResEasyPayUserToken easyPayUserToken;
    ResRestToken restToken;

    public ApiPresenter(ApiService service) {
        this(service, null);
    }

    public ApiPresenter(ApiService service, BootpayRestImplement parent) {
        this.service = service;
        if(parent != null) {
            this.parent = parent;
        }
    }



    public void request(Request request) {
        UX ux = request.getUX();
        request.getBoot_extra().setUX(ux.toString());
        if(ux == UX.BOOTPAY_REMOTE_LINK) {
            request_link(request);
        } else if(ux == UX.BOOTPAY_REMOTE_ORDER) {
            request_form(request);
        } else if(ux == UX.BOOTPAY_REMOTE_PRE) {
            request_pre(request);
        }
    }

    public void request_link(Request request) {
        String userJson = request.getBoot_user().toGson();
        String extraJson = request.getBoot_extra().toGson();
        String sms_payload = request.getSms_payload().toGson();

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
        ).retry(3)
        .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
        .subscribe(
                new Observer<LoginResult>() {
                    @Override
                    public void onComplete() {
//                        activity.requestSuccess();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginResult res) {
//                        Log.d("res", res.toString());

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });

    }

    public void request_form(Request request) {
        String userJson = request.getBoot_user().toGson();
        String extraJson = request.getBoot_extra().toGson();
        String sms_payload = request.getSms_payload().toGson();

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
                request.getRemote_order_form().toGson()
        ).retry(3)
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(
                        new Observer<LoginResult>() {
                            @Override
                            public void onComplete() {
//                        activity.requestSuccess();
                            }

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(LoginResult res) {
//                                Log.d("res", res.toString());
//                        activity.requestSuccess();
//                                if(res.getData() != null)
//                                    UserInfo.getInstance(service.getContext()).setBootpayUserId(res.getData().getUserId());
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        });
    }

    public void request_pre(Request request) {
        String userJson = request.getBoot_user().toGson();
        String extraJson = request.getBoot_extra().toGson();
        String sms_payload = request.getSms_payload().toGson();

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
                request.getRemote_order_pre().toGson()
        ).retry(3)
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(
                        new Observer<LoginResult>() {
                            @Override
                            public void onComplete() {
//                        activity.requestSuccess();
                            }

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(LoginResult res) {
                                Log.d("res", res.toString());
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }
                        });
    }

    public void getRestToken(String restApplicationId, String privateKey) {
        final ApiPresenter parentScope = this;

        service.getApi().getRestToken(
                restApplicationId,
                privateKey
        ).retry(3)
        .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
        .subscribe(
            new Observer<ResRestToken>() {
                @Override
                public void onComplete() {
                    if(parentScope.parent != null && parentScope.restToken != null) {
                        parentScope.parent.callbackRestToken(restToken.data);
                        parentScope.restToken = null;
                    }
                }

                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(ResRestToken res) {
                    Log.d("res", res.toString());
                    if(parentScope.parent != null) {
                        parentScope.restToken = res;
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }
            }
        );
    }

    public void getEasyPayUserToken(String restToken, BootUser user) {
        final ApiPresenter parentScope = this;


        service.getApi().getEasyPayUserToken(
                restToken,
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getGender(),
                user.getBirth(),
                user.getPhone()
        ).retry(3)
        .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
        .subscribe(
            new Observer<ResEasyPayUserToken>() {
                @Override
                public void onComplete() {
                    if(parentScope.parent != null && parentScope.easyPayUserToken != null) {
                        parentScope.parent.callbackEasyPayUserToken(easyPayUserToken.data);
                        parentScope.easyPayUserToken = null;
                    }
                }

                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(ResEasyPayUserToken res) {
                    Log.d("res", res.toString());

                    if(parentScope.parent != null) {
                        parentScope.easyPayUserToken = res;
                    }
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }
            }
        );

    }

    private String params(Request request) {
        if(request == null) return "";
        if(request.getParams() == null) return "";
        return new Gson().toJson(request.getParams());
    }
}
