package kr.co.bootpay.rest;

import kr.co.bootpay.rest.model.RestEasyPayUserTokenData;
import kr.co.bootpay.rest.model.RestTokenData;

//이  로직은 서버와 통신함에 있어서 참조용 코드일 뿐입니다.
@Deprecated
public interface BootpayRestImplement {
    void callbackRestToken(RestTokenData acessToken);
    void callbackEasyPayUserToken(RestEasyPayUserTokenData userToken);
}
