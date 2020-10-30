package kr.co.bootpay.model.res;

import kr.co.bootpay.model.bio.BioDevice;
import kr.co.bootpay.rest.model.ResDefault;

public class ResEasyBiometric extends ResDefault {
    public BioDevice data;

    public ResEasyBiometric(ResDefault res) {
        this.code = res.code;
        this.message = res.message;
        this.status = res.status;
    }
}
