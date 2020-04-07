package kr.co.bootpay.rest.model;

//이  로직은 서버사이드에서 수행되길 추천합니다. rest_application_id와 private_key는 노출되어서는 안되는 값입니다.
//사용시 키 값들이 노출될 환경에 있으니, 피치 못할 경우가 아니라면 사용을 자제해 주시기 바랍니다.
//결제수단 등록 후 발급받은 빌링키로 간편 정기결제하는 용도에 해당하는데, 꼭 서버사이드에서 수행되어야 합니다.
//개발자의 부주의로 키가 노출되어 고객의 결제가 무단으로 사용될 경우, 부트페이는 책임이 없음을 밝힙니다.
@Deprecated
public class RestTokenData {
    public String token;
    public long server_time;
    public long expired_at;
}
