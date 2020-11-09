package kr.co.bootpay.bio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.gson.Gson;

import java.util.concurrent.Executor;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import kr.co.bootpay.R;
import kr.co.bootpay.api.ApiService;
import kr.co.bootpay.bio.activity.BootpayBioWebviewActivity;
import kr.co.bootpay.bio.api.BioApiPresenter;
import kr.co.bootpay.bio.memory.CurrentBioRequest;
import kr.co.bootpay.listener.EventListener;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.model.bio.BioDeviceUse;
import kr.co.bootpay.model.bio.BioWallet;
import kr.co.bootpay.model.res.ResEasyBiometric;
import kr.co.bootpay.model.res.ResReceiptID;
import kr.co.bootpay.model.res.ResWalletList;
import kr.co.bootpay.pref.UserInfo;
import kr.co.bootpay.rest.BootpayBioRestImplement;
import kr.co.bootpay.rest.model.ResDefault;

@Deprecated
public class BootpayBioDialog extends androidx.fragment.app.DialogFragment implements BootpayBioRestImplement {

    private Context context;
    private Request request;
    protected BootpayBioPayLayout bioPayLayout;
    private EventListener listener;
    private BioApiPresenter presenter;

    ResEasyBiometric easyBiometric;
    ResWalletList walletList;
    ResReceiptID receiptID;
    long server_unixtime = 0;
    int bioFailCount = 0;
    BioWallet bioWallet;
    boolean isBioFailPopUp = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_FRAME, android.R);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent);
        this.context = getContext();
        this.presenter = new BioApiPresenter(new ApiService(this.context), this);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setWindowAnimations(R.style.AnimationPopupStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bioPayLayout = new BootpayBioPayLayout(inflater.getContext());
        afterViewInit();
        bioPayLayout.initView(this, getChildFragmentManager());
        getEasyCardWalletList();
        initBiometricAuth();

        CurrentBioRequest.getInstance().request = request;
        return bioPayLayout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void afterViewInit() {
        if (bioPayLayout != null) {
            if(request != null) bioPayLayout.setBioPayload(request.getBioPayload());

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
        }
    }

    public BootpayBioDialog setOnResponseListener(EventListener listener) {
        this.listener = listener;
        return this;
    }

    public BootpayBioDialog setRequest(Request request) {
        this.request = request;
        if(bioPayLayout != null) bioPayLayout.setBioPayload(request.getBioPayload());
        return this;
    }

//    public BootpayBioDialog setUserToken(String userToken) {
//        this.userToken = userToken;
//        return this;
//    }

    void saveBiometricKey() {
        if(easyBiometric == null || easyBiometric.data == null) return;
//        server_unixtime = easyBiometric.data.server_unixtime;
        UserInfo.getInstance(context).setBiometricDeviceId(easyBiometric.data.biometric_device_id);
        UserInfo.getInstance(context).setBiometricSecretKey(easyBiometric.data.biometric_secret_key);
    }

    void getEasyCardWalletList() {
        if(request == null) return;
        String uuid = UserInfo.getInstance(context).getBootpayUuid();
        String userToken = request.getEasyPayUserToken();
        if(uuid == null || "".equals(uuid)) { Log.d("bootpay", "uuid 값이 없습니다"); return; }
        if(userToken == null || "".equals(userToken)) { Log.d("bootpay", "userToken 값이 없습니다"); return; }

        presenter.getEasyCardWallet(uuid, userToken);
    }

    @Override
    public void callbackEasyBiometric(ResEasyBiometric res) {
        if(res.code != 0) {
            goPopUpError(res.message);
            return;
        }
        easyBiometric = res;
        server_unixtime = res.data.server_unixtime;
        saveBiometricKey();
        goAuthRegisterBiometricOTP();
    }

    @Override
    public void callbacktEasyBiometricRegister(ResEasyBiometric res) {
        if(res.code != 0) {
            goPopUpError(res.message);
            return;
        }
        easyBiometric = res;
        saveBiometricKey();
        getEasyCardWalletList();
    }

    @Override
    public void callbackEasyCardWallet(ResWalletList res) {
        if(res.code != 0) {
            goPopUpError(res.message);
            return;
        }
        walletList = res;
        server_unixtime = res.data.user.server_unixtime;
        if(bioPayLayout != null) bioPayLayout.setCardPager(res.data);
        if(CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_REGISTER_CARD) {
            goBiometricAuth();
        }
    }

    @Override
    public void callbackEasyCardRequest(ResReceiptID res) {
        if(res.code != 0) {
            goPopUpError(res.message);
            return;
        }
        receiptID = res;
        if(listener != null) listener.onConfirm(new Gson().toJson(receiptID));
    }

    @Override
    public void callbackEasyTransaction(String data) {
        if(listener != null) listener.onDone(data);
    }

    @Override
    public void callbackDeleteWalletID(ResDefault res) {

    }

    public void transactionConfirm(String data) {
        try {
            ResReceiptID res = new Gson().fromJson(data, ResReceiptID.class);

            String uuid = UserInfo.getInstance(context).getBootpayUuid();
            String userToken = request.getEasyPayUserToken();
            presenter.postEasyConfirm(uuid, userToken, res.data.receipt_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void goPopUpError(final String msg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
                builder.setMessage(msg);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    public void goVeiryPassword() {
        if(getActivity() == null) return;
        if(CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_NONE) {
            CurrentBioRequest.getInstance().type = CurrentBioRequest.REQUEST_TYPE_VERIFY_PASSWORD;
        }

        Intent intent = new Intent(getActivity(), BootpayBioWebviewActivity.class);
        startActivityForResult(intent, 9999);
    }

    public void goNewCardActivity() {
        if(getActivity() == null) return;
        CurrentBioRequest.getInstance().type = CurrentBioRequest.REQUEST_TYPE_REGISTER_CARD;
        Intent intent = new Intent(getActivity(), BootpayBioWebviewActivity.class);
        startActivityForResult(intent, 9999);
    }

    public void goOtherActivity() {
        if(getActivity() == null) return;
        CurrentBioRequest.getInstance().type = CurrentBioRequest.REQUEST_TYPE_OTHER;
        Intent intent = new Intent(getActivity(), BootpayBioWebviewActivity.class);
        startActivityForResult(intent, 9999);
    }

    public void startBioPay(BioDeviceUse user, BioWallet bioWallet) {
        if(user == null) return;
        if(user.use_biometric == 0 || user.use_device_biometric == 0) {
            //등록해야함
            goPopup("이 기기에서 결제할 수 있도록 설정합니다. (최초 1회)");
        } else {
            //지문인식 후 api를 넘김
            this.bioWallet = bioWallet;
            goBiometricAuth();
        }
    }


    void goPopup(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
//        builder.se
        builder.setMessage(msg);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CurrentBioRequest.getInstance().type = CurrentBioRequest.REQUEST_TYPE_ENABLE_DEVICE;
                goVeiryPassword();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    void goBioPayRequest(String passwordToken) {
        String uuid = UserInfo.getInstance(context).getBootpayUuid();
        String userToken = request.getEasyPayUserToken();

        String otp = "";

        if(CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_VERIFY_PASSWORD_FOR_PAY) {
            if("".equals(passwordToken)) return;
        } else {
            String key = UserInfo.getInstance(context).getBiometricSecretKey();
            if("".equals(key)) { // 다시 등록해야함
                goVeiryPassword();
                return;
            }
            otp = getOTPValue(key);
        }

//        String key = UserInfo.getInstance(context).getBiometricSecretKey();
//        if("".equals(key)) { // 다시 등록해야함
//            goVeiryPassword();
//            return;
//        }
//        String otp = getOTPValue(key);
        presenter.postEasyCardRequest(uuid, userToken, otp, passwordToken, bioWallet.wallet_id, "", CurrentBioRequest.getInstance().request.getPayload());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 9999 && resultCode > 0) {
            //wallet 재갱신
            if(CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_VERIFY_PASSWORD) {
                //지문인식 후
                goRegisterBiometricRequest();
            } else if(CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_ENABLE_DEVICE) {
                //지문인식 후
                goRegisterBiometricRequest();
                //otp 등록해야함
            } else if(CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_VERIFY_PASSWORD_FOR_PAY) {
                //비밀번호로 결제
                if(CurrentBioRequest.getInstance().token != null) goBioPayRequest(CurrentBioRequest.getInstance().token);
            } else if(CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_REGISTER_CARD) {
                //카드를 등록 했음
                getEasyCardWalletList();
            } else if(CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_PASSWORD_CHANGE) {
                //카드를 등록 했음
                getEasyCardWalletList();
            } else if(CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_OTHER) {
                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(10);
                dismiss();
            }
        }
    }

    void goRegisterBiometricRequest() {
        if(CurrentBioRequest.getInstance().token == null) return;
        String uuid = UserInfo.getInstance(context).getBootpayUuid();
        String userToken = request.getEasyPayUserToken();
        String passwordToken = CurrentBioRequest.getInstance().token;
        if(uuid == null || "".equals(uuid)) { Log.d("bootpay", "uuid 값이 없습니다"); return; }
        if(userToken == null || "".equals(userToken)) { Log.d("bootpay", "userToken 값이 없습니다"); return; }

        presenter.postEasyBiometric(uuid, userToken, passwordToken);
    }

    void goAuthRegisterBiometricOTP() {
//        if(easyBiometric == null || easyBiometric.data == null) return;
        String key = UserInfo.getInstance(context).getBiometricSecretKey();
        if("".equals(key)) { // 다시 등록해야함
            goVeiryPassword();
            return;
        }
        String otp = getOTPValue(key);
        if("".equals(otp)) return;
        String uuid = UserInfo.getInstance(context).getBootpayUuid();
        String userToken = request.getEasyPayUserToken();
        presenter.postEasyBiometricRegister(uuid, userToken, otp);
    }

    String getOTPValue(String key) {
        CodeGenerator generator = new DefaultCodeGenerator(HashingAlgorithm.SHA512, 8);
        long time = Long.valueOf(server_unixtime) / 30;

        try {
            return generator.generate(key, time);
        } catch (CodeGenerationException e) {
            e.printStackTrace();
            return "";
        }
    }
//
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    void initBiometricAuth() {
        executor = ContextCompat.getMainExecutor(context);
        biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                if(bioFailCount > 3 || errorCode != 13 ) { //9
                    if(biometricPrompt != null) biometricPrompt.cancelAuthentication();
                    goPopUpVerifyForPay();
                }
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(10); // 0.5초간 진동
                goBioPayRequest("");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                bioFailCount++;
                if(bioFailCount > 3) {
                    //팝업 물어보고 OK시 verify password for pay
                    if(biometricPrompt != null) biometricPrompt.cancelAuthentication();
                    goPopUpVerifyForPay();
                } else {
                    Toast.makeText(context, "지문인식에 인식에 실패하였습니다. (" + bioFailCount + "/3)", Toast.LENGTH_SHORT).show();
                }
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("지문 인증")
                .setSubtitle("결제를 진행하시려면")
                .setNegativeButtonText("취소")
                .setDeviceCredentialAllowed(false)
                .build();
    }

    void goBiometricAuth() {
        biometricPrompt.authenticate(promptInfo);
    }

    void goPopUpVerifyForPay() {
        if(isBioFailPopUp == true) return;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(getActivity() == null || context == null) return;
                isBioFailPopUp = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
                builder.setMessage("지문인식에 여러 번 실패하여, 비밀번호로 결제합니다.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CurrentBioRequest.getInstance().type = CurrentBioRequest.REQUEST_TYPE_VERIFY_PASSWORD_FOR_PAY;
                        CurrentBioRequest.getInstance().token = null;
                        goVeiryPassword();
                        isBioFailPopUp = false;
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

}
