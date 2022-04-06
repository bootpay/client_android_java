package kr.co.bootpay.bio.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executors;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kr.co.bootpay.analytics.LoginResult;
import kr.co.bootpay.api.ApiService;
import kr.co.bootpay.model.Payload;
import kr.co.bootpay.model.req.ReqBioPayload;
import kr.co.bootpay.model.res.ResEasyBiometric;
import kr.co.bootpay.model.res.ResReceiptID;
import kr.co.bootpay.model.res.ResWalletList;
import kr.co.bootpay.rest.BootpayBioRestImplement;
import kr.co.bootpay.rest.BootpayRestImplement;
import kr.co.bootpay.rest.model.ResDefault;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class BioApiPresenter {
    ApiService service;
    BootpayBioRestImplement parent;
    ResEasyBiometric easyBiometric;
    ResWalletList walletList;
    ResReceiptID receiptID;
    ResponseBody easyConfirm;
    ResDefault deleteWalletID;

    public BioApiPresenter(ApiService service, BootpayBioRestImplement callback) {
        this.service = service;
        if(callback != null) {
            this.parent = callback;
        }
    }

    public void postEasyBiometric(String deviceUUID, String userToken, String passwordToken) {
        final BioApiPresenter scope = this;

        service.getApi().postEasyBiometric(deviceUUID, userToken, passwordToken, "android")
                .retry(1)
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(
                        new Observer<ResEasyBiometric>() {
                            @Override
                            public void onComplete() {
                                scope.parent.callbackEasyBiometric(easyBiometric);
                            }

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResEasyBiometric res) {
                                scope.easyBiometric = res;
                            }

                            @Override
                            public void onError(Throwable e) {
                                try {
                                    HttpException httpException = (HttpException) e;
                                    ResDefault res = new Gson().fromJson(httpException.response().errorBody().charStream(), ResDefault.class);
                                    scope.parent.callbackEasyBiometric(new ResEasyBiometric(res));
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                            }
                        });


    }

    public void postEasyBiometricRegister(String deviceUUID, String userToken, String otp) {
        final BioApiPresenter scope = this;

        service.getApi().postEasyBiometricRegister(deviceUUID, userToken, otp)
                .retry(1)
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(
                        new Observer<ResEasyBiometric>() {
                            @Override
                            public void onComplete() {
                                scope.parent.callbacktEasyBiometricRegister(easyBiometric);
//                        activity.requestSuccess();
                            }

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResEasyBiometric res) {
                                scope.easyBiometric = res;
//                        Log.d("res", res.toString());

                            }

                            @Override
                            public void onError(Throwable e) {
                                try {
                                    HttpException httpException = (HttpException) e;
                                    ResDefault res = new Gson().fromJson(httpException.response().errorBody().charStream(), ResDefault.class);
                                    scope.parent.callbacktEasyBiometricRegister(new ResEasyBiometric(res));
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                            }
                        });

    }

    public void getEasyCardWallet(String deviceUUID, String userToken) {
        final BioApiPresenter scope = this;

        service.getApi().getEasyCardWallet(deviceUUID, userToken)
                .retry(1)
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(
                        new Observer<ResWalletList>() {
                            @Override
                            public void onComplete() {
                                scope.parent.callbackEasyCardWallet(walletList);
                            }

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResWalletList res) {
                                scope.walletList = res;
                            }

                            @Override
                            public void onError(Throwable e) {
                                try {
                                    HttpException httpException = (HttpException) e;
                                    ResDefault res = new Gson().fromJson(httpException.response().errorBody().charStream(), ResDefault.class);
                                    scope.parent.callbackEasyCardWallet(new ResWalletList(res));
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                            }
                        });

    }

    public void postEasyCardRequest(String deviceUUID, String userToken, String otp,
                                    String password_token, String wallet_id, String quota, Payload payload) {
        final BioApiPresenter scope = this;

        ReqBioPayload req = new ReqBioPayload();
        req.otp = otp;
        req.password_token = password_token;
        req.quota = quota;
        req.wallet_id = wallet_id;
        req.request_data = payload;

        service.getApi().postEasyCardRequest(deviceUUID, userToken, req)
                .retry(1)
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(
                        new Observer<ResReceiptID>() {
                            @Override
                            public void onComplete() {
                                scope.parent.callbackEasyCardRequest(receiptID);
                            }

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResReceiptID res) {
                                scope.receiptID = res;
                            }

                            @Override
                            public void onError(Throwable e) {
                                try {
                                    HttpException httpException = (HttpException) e;
                                    ResDefault res = new Gson().fromJson(httpException.response().errorBody().charStream(), ResDefault.class);
                                    scope.parent.callbackEasyCardRequest(new ResReceiptID(res));
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                            }
                        });
    }

    public void postEasyConfirm(String deviceUUID, String userToken, String receipt_id) {
        final BioApiPresenter scope = this;

        service.getApi().postEasyConfirm(deviceUUID, userToken, receipt_id)
                .retry(1)
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(
                        new Observer<ResponseBody>() {
                            @Override
                            public void onComplete() {
                                try {
                                    JSONObject jsonObject = new JSONObject(easyConfirm.toString());
                                    scope.parent.callbackEasyTransaction(jsonObject.getString("data"));
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                            }

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResponseBody res) {
                                easyConfirm = res;
                            }

                            @Override
                            public void onError(Throwable e) {
                                try {
                                    scope.parent.callbackEasyTransaction(easyConfirm.string());
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                            }
                        });
    }

    public void deleteCardWalletID(String deviceUUID, String userToken, String wallet_id) {
        final BioApiPresenter scope = this;

        service.getApi().deleteCardWalletID(deviceUUID, userToken, wallet_id)
                .retry(1)
                .subscribeOn(Schedulers.from(Executors.newCachedThreadPool()))
                .subscribe(
                        new Observer<ResDefault>() {
                            @Override
                            public void onComplete() {
                                try {
                                    scope.parent.callbackDeleteWalletID(deleteWalletID);
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                            }

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(ResDefault res) {
                                deleteWalletID = res;
                            }

                            @Override
                            public void onError(Throwable e) {
                                try {
                                    scope.parent.callbackDeleteWalletID(deleteWalletID);
                                } catch (Exception ee) {
                                    ee.printStackTrace();
                                }
                            }
                        });
    }
}
