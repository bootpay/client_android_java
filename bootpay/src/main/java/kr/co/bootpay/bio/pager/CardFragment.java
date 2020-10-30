package kr.co.bootpay.bio.pager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import kr.co.bootpay.R;
import kr.co.bootpay.bio.BootpayBioDialog;
import kr.co.bootpay.bio.CardCode;
import kr.co.bootpay.bio.activity.BootpayBioActivity;
import kr.co.bootpay.bio.memory.CurrentBioRequest;
import kr.co.bootpay.model.bio.BioDeviceUse;
import kr.co.bootpay.model.bio.BioWallet;

public class CardFragment extends Fragment {
    BioWallet bioWallet;
    BioDeviceUse user;
    Context context;

    LinearLayout card;
    TextView bank_name;
    ImageView card_chip;
    TextView bank_num;

    ImageView new_card;
    TextView other;
    BootpayBioActivity parent;

    public static CardFragment newInstance(BootpayBioActivity parent, BioDeviceUse user, BioWallet bioWallet, Context context) {
        CardFragment fragment = new CardFragment();
        fragment.bioWallet = bioWallet;
        fragment.context = context;
        fragment.user = user;
        fragment.parent = parent;
        return fragment;
    }

    private void updateVisible() {
        if(bioWallet == null) return;
        if(bank_name == null) return;
        bank_name.setVisibility(bioWallet.wallet_type == -1 ? View.VISIBLE : View.GONE);
        bank_num.setVisibility(bioWallet.wallet_type == -1 ? View.VISIBLE : View.GONE);
        card_chip.setVisibility(bioWallet.wallet_type == -1 ? View.VISIBLE : View.GONE);
        new_card.setVisibility(bioWallet.wallet_type == 1 ? View.VISIBLE : View.GONE);
        other.setVisibility(bioWallet.wallet_type == 2 ? View.VISIBLE : View.GONE);
    }

    private void updateBackground() {
        if(bioWallet == null) return;
        if(card == null) return;
        if(bioWallet.wallet_type == -1) {
            card.setBackground(getResources().getDrawable(R.drawable.rounded_gray, null));
        } else if(bioWallet.wallet_type == 1) {
            card.setBackground(getResources().getDrawable(R.drawable.rounded_white, null));
        } else if(bioWallet.wallet_type == 2) {
            card.setBackground(getResources().getDrawable(R.drawable.rounded_blue, null));
        }
    }

    private void updateCardValue() {
        if(bioWallet == null || bioWallet.d == null) return;
        if(card == null) return;
        bank_name.setText(bioWallet.d.card_name);
        bank_num.setText(bioWallet.d.card_no);
    }

    private void updateCardStyle() {
        if(bioWallet == null || bioWallet.d == null) return;
        if(card == null) return;
        bank_name.setTextColor(CardCode.getColorText(bioWallet.d.card_code));
        bank_num.setTextColor(CardCode.getColorText(bioWallet.d.card_code));
        card.setBackground(getResources().getDrawable(CardCode.getColorBackground(bioWallet.d.card_code), null));
    }

    public void updateView() {
        updateVisible();
        updateBackground();
        if(bioWallet != null || bioWallet.wallet_type == -1) {
            updateCardValue();
            updateCardStyle();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup cardView = (ViewGroup) inflater.inflate(R.layout.layout_card, container, false);
        card = cardView.findViewById(R.id.card);
        card_chip =  cardView.findViewById(R.id.card_chip);
        bank_name = cardView.findViewById(R.id.bank_name);
        bank_num = cardView.findViewById(R.id.bank_num);
        new_card = cardView.findViewById(R.id.new_card);
        other = cardView.findViewById(R.id.other);
        updateView();

//        bank_num.setTextColor(R.);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickView(view);
            }
        });
        return cardView;
    }

    public void clickView(View v) {
        if(bioWallet.wallet_type == -1) {
            goCardClick();
        } else if(bioWallet.wallet_type == 1) {
            goNewCard();
        } else if(bioWallet.wallet_type == 2) {
            goOther();
        }
    }

    void goCardClick() {
        if(parent != null) parent.startBioPay(user, bioWallet);
    }

//    void goVerify

    void verifyPassword () {
        if(parent != null) parent.goVeiryPassword();
    }

    void goNewCard() {
        if(parent != null) parent.goNewCardActivity();
    }

    void goOther() {
        if(parent != null) parent.goOtherActivity();
    }
}
