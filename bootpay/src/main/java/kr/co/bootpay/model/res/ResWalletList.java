package kr.co.bootpay.model.res;

import kr.co.bootpay.model.bio.BioWalletData;
import kr.co.bootpay.rest.model.ResDefault;

public class ResWalletList extends ResDefault {
    public BioWalletData data;

    public ResWalletList(ResDefault res) {
        this.code = res.code;
        this.message = res.message;
        this.status = res.status;
    }
}
