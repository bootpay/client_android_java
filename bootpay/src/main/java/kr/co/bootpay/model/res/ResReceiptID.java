package kr.co.bootpay.model.res;

import kr.co.bootpay.model.bio.BioDevice;
import kr.co.bootpay.model.bio.ReceiptID;
import kr.co.bootpay.rest.model.ResDefault;

public class ResReceiptID extends ResDefault {
    public ReceiptID data;

    public ResReceiptID(ResDefault res) {
        this.code = res.code;
        this.message = res.message;
        this.status = res.status;
    }
}
