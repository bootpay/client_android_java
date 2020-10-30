package kr.co.bootpay.bio.pager;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import kr.co.bootpay.bio.BootpayBioDialog;
import kr.co.bootpay.model.bio.BioWallet;
import kr.co.bootpay.model.bio.BioWalletData;

public class CardPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private BioWalletData data;
    private BootpayBioDialog bootpayBioDialog;

    public CardPagerAdapter(FragmentManager fm, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    public void setData(BioWalletData data) {
        this.data = data;
        if(this.data == null) return;
        if(this.data.wallets == null) return;
        if(this.data.wallets.card == null) this.data.wallets.card = new ArrayList<>();
        addWallet(1);
        addWallet(2);
    }

    void addWallet(int walletType) {
        BioWallet bioWallet = new BioWallet();
        bioWallet.wallet_type = walletType;
        data.wallets.card.add(bioWallet);
    }

    public void setDialog(BootpayBioDialog bootpayBioDialog) {
        this.bootpayBioDialog = bootpayBioDialog;
    }

    @Override
    public Fragment getItem(int position) {
        CardFragment obj = CardFragment.newInstance(bootpayBioDialog, data.user, data.wallets.card.get(position), context);
        obj.updateView();
        return obj;
    }

    @Override
    public int getCount() {
        if(data == null) return 0;
        if(data.wallets == null) return 0;
        if(data.wallets.card == null) return 0;
        return data.wallets.card.size();
    }
}
