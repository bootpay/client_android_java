[![](https://jitpack.io/v/bootpay/client_android_java.svg)](https://jitpack.io/#bootpay/client_android_java)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![](logo/logo_bootpay.png)

## PG Analytics - 결제데이터 분석서비스
* 기존 PG 사를 이용 중이신 사업자도 별도의 계약없이 부트페이를 통해 결제 연동과 통계를 무료로 이용하실 수 있습니다.
* 한줄의 소스코드로 인사이트를 얻어 매출을 극대화하세요.


# Java 
## build.gradle (Project):
```gradle
buildscript {
    repositories {
        ...
    }
    dependencies {
        ...
        classpath 'com.github.dcendents:android-maven-gradle-plugin:2.1' // 비공식 해결 방법, gradle build error 가 발생시에만 추가
    }
}

allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

## build.gradle (Modlue):setAccountExpireAt
```gradle
dependencies {
    ...
    implementation 'com.github.bootpay:client_android_java:2.1.1'
}
```

## manifests.xml
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

## 샘플 코드
```java
//        결제호출
Bootpay.init(getFragmentManager())
                .setApplicationId("59a7e647396fa64fcad4a8c2") // 해당 프로젝트(안드로이드)의 application id 값
                .setPG(PG.DANAL) // 결제할 PG 사
                .setUser_phone("010-1234-5678") // 구매자 전화번호
                .setMethod(Method.PHONE) // 결제수단
                .setName("맥북프로임다") // 결제할 상품명
                .setOrderId("1234") // 결제 고유번호
                .setPrice(1000) // 결제할 금액
                .addItem("마우스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
                .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
                .onConfirm(new ConfirmListener() { // 결제가 진행되기 바로 직전 호출되는 함수로, 주로 재고처리 등의 로직이 수행
                    @Override
                    public void onConfirm(@Nullable String message) {
                        if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                        Log.d("confirm", message);
                    }
                })
                .onDone(new DoneListener() { // 결제완료시 호출, 아이템 지급 등 데이터 동기화 로직을 수행합니다
                    @Override
                    public void onDone(@Nullable String message) {
                        Log.d("done", message);
                    }
                })
                .onReady(new ReadyListener() { // 가상계좌 입금 계좌번호가 발급되면 호출되는 함수입니다.
                    @Override
                    public void onReady(@Nullable String message) {
                        Log.d("ready", message);
                    }
                })
                .onCancel(new CancelListener() { // 결제 취소시 호출
                    @Override
                    public void onCancel(@Nullable String message) {
                        Log.d("cancel", message);
                    }
                })
                .onError(new ErrorListener() { // 에러가 났을때 호출되는 부분
                    @Override
                    public void onError(@Nullable String message) {
                        Log.d("error", message);
                    }
                })
                .onClose(new CloseListener() { //결제창이 닫힐때 실행되는 부분
                    @Override
                    public void onClose(String message) {
                        Log.d("close", "close");
                    }
                })
                .show();
```

<hr/>
 
