package kr.co.bootpay.bio.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.exceptions.CodeGenerationException;
import kr.co.bootpay.R;
import kr.co.bootpay.api.ApiService;
import kr.co.bootpay.bio.BootpayBioPayLayout;
import kr.co.bootpay.bio.api.BioApiPresenter;
import kr.co.bootpay.bio.memory.CurrentBioRequest;
import kr.co.bootpay.bio.pager.CardPagerAdapter;
import kr.co.bootpay.bio.pager.CardViewPager;
import kr.co.bootpay.listener.ConfirmListener;
import kr.co.bootpay.listener.DoneListener;
import kr.co.bootpay.listener.EventListener;
import kr.co.bootpay.model.Request;
import kr.co.bootpay.model.bio.BioDeviceUse;
import kr.co.bootpay.model.bio.BioPayload;
import kr.co.bootpay.model.bio.BioPrice;
import kr.co.bootpay.model.bio.BioWallet;
import kr.co.bootpay.model.bio.BioWalletData;
import kr.co.bootpay.model.res.ResEasyBiometric;
import kr.co.bootpay.model.res.ResReceiptID;
import kr.co.bootpay.model.res.ResWalletList;
import kr.co.bootpay.pref.UserInfo;
import kr.co.bootpay.rest.BootpayBioRestImplement;

import static android.view.View.TEXT_ALIGNMENT_TEXT_END;

public class BootpayBioActivity extends FragmentActivity implements BootpayBioRestImplement {

    private Context context;
    private Request request;
    private BioApiPresenter presenter;

    // data
    ResEasyBiometric easyBiometric;
    ResWalletList walletList;
    ResReceiptID receiptID;
    long server_unixtime = 0;
    int bioFailCount = 0;
    BioWallet bioWallet;
    boolean isBioFailPopUp = false;

    // view
    HorizontalScrollView scrollView;
    private BioPayload bioPayload;
    TextView pg;
    TextView msg;
    LinearLayout names;
    LinearLayout prices;
    BioWalletData data;
    CardViewPager card_pager;
    CardPagerAdapter cardPagerAdapter;
    PowerSpinnerView quota_spinner;
    LinearLayout quota_layout;
    View quota_line;
    int currentIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bio_pay);
        overridePendingTransition(R.anim.open, R.anim.close);

        this.context = this;
        this.presenter = new BioApiPresenter(new ApiService(this.context), this);
        request = CurrentBioRequest.getInstance().request;
        if(request != null) bioPayload = request.getBioPayload();

        CurrentBioRequest.getInstance().bioActivity = this;

        initView();
        getEasyCardWalletList();
        initBiometricAuth();
        setNameViews();
        setPriceViews();
        setQuotaValue();
    }

    void setQuotaValue() {
        if(request == null) return;
        quota_layout.setVisibility(request.getPrice() < 50000 ? View.GONE : View.VISIBLE);
        quota_line.setVisibility(request.getPrice() < 50000 ? View.GONE : View.VISIBLE);
        if(request.getPrice() < 50000) return;
        List<String> array = getQuotaList();
        if(array == null) return;
        quota_spinner.setItems(array);
        quota_spinner.selectItemByIndex(0);
    }

    List<String> getQuotaList() {
        List<String> result = new ArrayList<>();
        if(request.getBootExtra(this) == null || request.getBootExtra(this).getQuotas() == null) return  null;
        for(Integer i : request.getBootExtra(this).getQuotas()) {
            if(i == 0) result.add("일시불");
            else result.add((i+1) + "개월");
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        CurrentBioRequest.getInstance().bioActivity = null;
        super.onDestroy();
    }

    @Override
    public void finish() {
        CurrentBioRequest.getInstance().bioActivity = null;
        super.finish();
        overridePendingTransition(R.anim.open, R.anim.close);
    }

    void initView() {
        scrollView = findViewById(R.id.scrollView);
        pg = findViewById(R.id.pg);
        names = findViewById(R.id.names);
        prices = findViewById(R.id.prices);
        msg = findViewById(R.id.msg);
        card_pager = findViewById(R.id.card_pager);
        quota_layout = findViewById(R.id.quota_layout);
        quota_spinner = findViewById(R.id.quota_spinner);
        quota_line = findViewById(R.id.quota_line);
        cardPagerAdapter = new CardPagerAdapter(getSupportFragmentManager(), this.context);
        cardPagerAdapter.setParent(this);
//        cardPagerAdapter.setDialog(bootpayBioDialog);
        card_pager.setAdapter(cardPagerAdapter);
        card_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(data == null || data.wallets == null || data.wallets.card == null || data.wallets.card.size() <= position) return;
                currentIndex = position;
                if(data.wallets.card.get(position).wallet_type == -1) {
                    msg.setText("이 카드로 결제합니다");
                } else if(data.wallets.card.get(position).wallet_type == 1) {
                    msg.setText("새로운 카드를 등록합니다");
                } else if(data.wallets.card.get(position).wallet_type == 2) {
                    msg.setText("다른 결제수단으로 결제합니다");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        final BootpayBioActivity scope = this;
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                if(scope.bootpayBioDialog != null) scope.bootpayBioDialog.dismiss();
            }
        });
        findViewById(R.id.barcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data == null || data.wallets == null || data.wallets.card == null || data.wallets.card.size() <= currentIndex) return;
                if(data.wallets.card.get(currentIndex).wallet_type == -1) {
                    startBioPay(data.user, data.wallets.card.get(currentIndex));
                } else if(data.wallets.card.get(currentIndex).wallet_type == 1) {
                    goNewCardActivity();
                } else if(data.wallets.card.get(currentIndex).wallet_type == 2) {
                    goOtherActivity();
                }
            }
        });
//        getFra
        card_pager.setAnimationEnabled(true);
        card_pager.setFadeEnabled(true);
        card_pager.setFadeFactor(0.6f);
    }

    private void setNameViews() {
        if(bioPayload == null) return;
        for(String name : bioPayload.getNames()) {
            TextView text = new TextView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            text.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
            text.setLayoutParams(params);
            text.setText(name);
            text.setTextColor(getResources().getColor(R.color.black, null));
            names.addView(text);
        }
    }

    private void setPriceViews() {
        if(bioPayload == null) return;
        for(BioPrice bioPrice : bioPayload.getPrices()) {
            LinearLayout layout = new LinearLayout(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(params);


            TextView left = new TextView(context);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            left.setLayoutParams(params1);
            left.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            left.setText(bioPrice.getName());
            left.setTextColor(getResources().getColor(R.color.black, null));
            layout.addView(left);

            TextView right = new TextView(context);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            right.setLayoutParams(params2);
            right.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
            right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            right.setText(getComma(bioPrice.getPrice()));
            right.setTextColor(getResources().getColor(R.color.black, null));
            layout.addView(right);
            prices.addView(layout);
        }

        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(params);


        TextView left = new TextView(context);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        left.setLayoutParams(params1);
        left.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        left.setTypeface(left.getTypeface(), Typeface.BOLD);
        left.setText("총 결제금액");
        left.setTextColor(getResources().getColor(R.color.black, null));
        layout.addView(left);

        TextView right = new TextView(context);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        right.setLayoutParams(params2);
        right.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
        right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        right.setTypeface(left.getTypeface(), Typeface.BOLD);
        right.setText(getComma(request.getPrice()));
        right.setTextColor(getResources().getColor(R.color.black, null));
        layout.addView(right);
        prices.addView(layout);
    }

    private String getComma(double value) {
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        return myFormatter.format(value) + "원";
    }

    public void setCardPager(final BioWalletData data) {
        this.data = data;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                cardPagerAdapter.setData(data);
                cardPagerAdapter.notifyDataSetChanged();
            }
        });

    }

    //    @Override
//    protected void onStart() {
//        super.onStart();
//        getWindow().setWindowAnimations(R.style.AnimationPopupStyle);
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
        presenter.postEasyCardRequest(uuid, userToken, otp, passwordToken, bioWallet.wallet_id, "", CurrentBioRequest.getInstance().request.getPayload());
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
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
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
        setCardPager(res.data);
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
        ConfirmListener confirm = CurrentBioRequest.getInstance().confirm;
        if(confirm != null) confirm.onConfirm(new Gson().toJson(receiptID));
    }

    @Override
    public void callbackEasyTransaction(String data) {
        DoneListener done = CurrentBioRequest.getInstance().done;
        if(done != null) done.onDone(data);
    }

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

    public void goVeiryPassword() {
        if(CurrentBioRequest.getInstance().type == CurrentBioRequest.REQUEST_TYPE_NONE) {
            CurrentBioRequest.getInstance().type = CurrentBioRequest.REQUEST_TYPE_VERIFY_PASSWORD;
        }

        Intent intent = new Intent(this, BootpayBioWebviewActivity.class);
        startActivityForResult(intent, 9999);
    }

    public void goNewCardActivity() {
        CurrentBioRequest.getInstance().type = CurrentBioRequest.REQUEST_TYPE_REGISTER_CARD;
        Intent intent = new Intent(this, BootpayBioWebviewActivity.class);
        startActivityForResult(intent, 9999);
    }

    public void goOtherActivity() {
        CurrentBioRequest.getInstance().type = CurrentBioRequest.REQUEST_TYPE_OTHER;
        Intent intent = new Intent(this, BootpayBioWebviewActivity.class);
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
}
