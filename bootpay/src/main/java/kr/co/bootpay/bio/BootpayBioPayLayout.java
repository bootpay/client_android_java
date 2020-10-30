package kr.co.bootpay.bio;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import java.text.DecimalFormat;

import kr.co.bootpay.R;
import kr.co.bootpay.api.ApiPresenter;
import kr.co.bootpay.bio.pager.CardPagerAdapter;
import kr.co.bootpay.bio.pager.CardViewPager;
import kr.co.bootpay.model.bio.BioPayload;
import kr.co.bootpay.model.bio.BioPrice;
import kr.co.bootpay.model.bio.BioWalletData;

public class BootpayBioPayLayout extends LinearLayout {
    HorizontalScrollView scrollView;
    private BootpayBioDialog bootpayBioDialog;
    private BioPayload bioPayload;
    TextView pg;
    TextView msg;
    LinearLayout names;
    LinearLayout prices;
    BioWalletData data;
    CardViewPager card_pager;
    CardPagerAdapter cardPagerAdapter;
    Context context;
    int currentIndex = 0;

    public BootpayBioPayLayout(Context context) {
        super(context);
        this.context = context;
    }

    public BootpayBioPayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public BootpayBioPayLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void initView(final BootpayBioDialog bootpayBioDialog, FragmentManager fm) {
        this.bootpayBioDialog = bootpayBioDialog;

        View layout = inflate(getContext(), R.layout.layout_bio_pay, this);
        scrollView = layout.findViewById(R.id.scrollView);
        pg = layout.findViewById(R.id.pg);
        names = layout.findViewById(R.id.names);
        prices = layout.findViewById(R.id.prices);
        msg = layout.findViewById(R.id.msg);
        card_pager = layout.findViewById(R.id.card_pager);
        cardPagerAdapter = new CardPagerAdapter(fm, this.context);
        cardPagerAdapter.setDialog(bootpayBioDialog);
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
        final BootpayBioPayLayout scope = this;
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(scope.bootpayBioDialog != null) scope.bootpayBioDialog.dismiss();
            }
        });
        findViewById(R.id.barcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(data == null || data.wallets == null || data.wallets.card == null || data.wallets.card.size() <= currentIndex) return;
                if(data.wallets.card.get(currentIndex).wallet_type == -1) {
//                    msg.setText("이 카드로 결제합니다");
//                    if(bootpayBioDialog != null) bootpayBioDialog.
                    if(bootpayBioDialog != null) bootpayBioDialog.startBioPay(data.user, data.wallets.card.get(currentIndex));
                } else if(data.wallets.card.get(currentIndex).wallet_type == 1) {
                    if(bootpayBioDialog != null) bootpayBioDialog.goNewCardActivity();
                } else if(data.wallets.card.get(currentIndex).wallet_type == 2) {
                    if(bootpayBioDialog != null) bootpayBioDialog.goOtherActivity();
                }
            }
        });
//        getFra
        card_pager.setAnimationEnabled(true);
        card_pager.setFadeEnabled(true);
        card_pager.setFadeFactor(0.6f);

        setNameViews();
        setPriceViews();
    }


    private void setNameViews() {
        if(bioPayload == null) return;
        for(String name : bioPayload.getNames()) {
            TextView text = new TextView(getContext());
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
            LinearLayout layout = new LinearLayout(getContext());
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(params);


            TextView left = new TextView(getContext());
            LayoutParams params1 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
            left.setLayoutParams(params1);
            left.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            left.setText(bioPrice.getName());
            left.setTextColor(getResources().getColor(R.color.black, null));
            layout.addView(left);

            TextView right = new TextView(getContext());
            LayoutParams params2 = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
            right.setLayoutParams(params2);
            right.setTextAlignment(TEXT_ALIGNMENT_TEXT_END);
            right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            right.setText(getComma(bioPrice.getPrice()));
            right.setTextColor(getResources().getColor(R.color.black, null));
            layout.addView(right);
            prices.addView(layout);
        }
    }

    private String getComma(double value) {
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        return myFormatter.format(value);
    }

    public void setBioPayload(BioPayload bioPayload) {
        this.bioPayload = bioPayload;
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
}
