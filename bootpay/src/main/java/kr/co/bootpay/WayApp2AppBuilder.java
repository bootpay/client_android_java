package kr.co.bootpay;

public class WayApp2AppBuilder {
//    protected Request request = new Request();
////    protected AppEventListener listener;
//    private ErrorListener error;
//    private CloseListener close;
//    private DoneListener done;
//    private CancelListener cancel;
//    private Context context;
//
//    private WayApp2AppBuilder() {
//        // do nothing
//    }
//
//    public WayApp2AppBuilder(Context context) {
//        this.context = context;
//    }
//
//    public WayApp2AppBuilder setApplicationId(String id) {
//        request.setApplication_id(id);
//        return this;
//    }
//
//    public WayApp2AppBuilder setPrice(@IntRange(from = 0) int price) {
//        request.setPrice(price);
//        return this;
//    }
//
//    public WayApp2AppBuilder setPG(String pg) {
//        request.setPg(pg);
//        return this;
//    }
//
//    public WayApp2AppBuilder setPG(PG pg) {
//        switch (pg) {
////                case BOOTPAY:
////                    request.setPg("bootpay");
////                    break;
//            case PAYAPP:
//                request.setPg("payapp");
//                break;
////                case DANAL:
////                    request.setPg("danal");
////                    break;
////                case KCP:
////                    request.setPg("kcp");
////                    break;
////                case INICIS:
////                    request.setPg("inicis");
////                    break;
////                case LGUP:
////                    request.setPg("lgup");
////                    break;
////                case KAKAO:
////                    request.setPg("kakao");
////                    break;
////                case JTNET:
////                    request.setPg("jtnet");
////                    break;
////                case NICEPAY:
////                    request.setPg("nicepay");
////                    break;
////                case PAYCO:
////                    request.setPg("payco");
////                    break;
//        }
//        return this;
//    }
//
//    public WayApp2AppBuilder setName(String name) {
//        request.setName(name);
//        return this;
//    }
//
//    public WayApp2AppBuilder addItem(String name, @IntRange(from = 1) int quantity, String primaryKey, int price) {
//        request.addItem(new Item(name, quantity, primaryKey, price, "", "", ""));
//        return this;
//    }
//
//    public WayApp2AppBuilder addItem(String name, @IntRange(from = 1) int quantity, String primaryKey, int price, String cat1, String cat2, String cat3) {
//        request.addItem(new Item(name, quantity, primaryKey, price, cat1, cat2, cat3));
//        return this;
//    }
//
//    public WayApp2AppBuilder addItem(Item item) {
//        request.addItem(item);
//        return this;
//    }
//
//    public WayApp2AppBuilder addItems(Collection<Item> items) {
//        request.addItems(items);
//        return this;
//    }
//
//    public WayApp2AppBuilder setItems(List<Item> items) {
//        request.setItems(items);
//        return this;
//    }
//
//    public WayApp2AppBuilder is_show_agree(Boolean isShow) {
//        request.set_show_agree(isShow);
//        return this;
//    }
//
//    public WayApp2AppBuilder setOrderId(String orderId) {
//        request.setOrder_id(orderId);
//        return this;
//    }
//
//    public WayApp2AppBuilder setUseOrderId(Integer use_order_id) {
//        request.setUse_order_id(use_order_id);
//        return this;
//    }
//
//    public WayApp2AppBuilder setExpireMonth(int expireMonth) {
//        request.setExtra_expire_month(expireMonth);
//        return this;
//    }
//
//    public WayApp2AppBuilder setVBankResult(int vbankResult) {
//        request.setExtra_vbank_result(vbankResult);
//        return this;
//    }
//
//    public WayApp2AppBuilder setQuotas(int[] quotas) {
//        request.setExtra_quotas(quotas);
//        return this;
//    }
//
//    public WayApp2AppBuilder setAppScheme(String appScheme) {
//        request.setExtra_app_scheme(appScheme);
//        return this;
//    }
//
//    public WayApp2AppBuilder setAppSchemeHost(String appSchemeHost) {
//        request.setExtra_app_scheme_host(appSchemeHost);
//        return this;
//    }
//
//    public WayApp2AppBuilder setMethod(Method method) {
//        switch (method) {
//            case CARD:
//                request.setMethod("card");
//                break;
//            case CARD_SIMPLE:
//                request.setMethod("card_simple");
//                break;
//            case BANK:
//                request.setMethod("bank");
//                break;
//            case VBANK:
//                request.setMethod("vbank");
//                break;
//            case PHONE:
//                request.setMethod("phone");
//                break;
//            case SELECT:
//                request.setMethod("");
//                break;
//        }
//        return this;
//    }
//
//    public WayApp2AppBuilder setUX(UX UX) {
//        request.setUX(UX);
//        return this;
//    }
//
//    public WayApp2AppBuilder setAccountExpireAt(String account_expire_at) {
//        request.setAccountExpireAt(account_expire_at);
//        return this;
//    }
//
//    public WayApp2AppBuilder setModel(Request request) {
//        this.request = request;
//        return this;
//    }
//
//    public WayApp2AppBuilder setEventListener(AppEventListener eventListener) {
//        listener = eventListener;
//        return this;
//    }
//
//    public WayApp2AppBuilder setParams(Object params) {
//        request.setParams(params);
//        return this;
//    }
//
//    public WayApp2AppBuilder setParams(String params) {
//        request.setParams(params);
//        return this;
//    }
//
//    public WayApp2AppBuilder setParams(JSONObject params) {
//        request.setParams(params);
//        return this;
//    }
//
//    public WayApp2AppBuilder onCancel(CancelListener listener) {
//        cancel = listener;
//        return this;
//    }
//
//    public WayApp2AppBuilder onError(ErrorListener listener) {
//        error = listener;
//        return this;
//    }
//
//    public WayApp2AppBuilder onDone(DoneListener listener) {
//        done = listener;
//        return this;
//    }
//
//    public WayApp2AppBuilder onClose(CloseListener listener) {
//        close = listener;
//        return this;
//    }
//
//    public WayApp2AppBuilder setUser_email(String email) {
//        request.setUser_email(email);
//        return this;
//    }
//
//    public WayApp2AppBuilder setUser_name(String name) {
//        request.setUser_name(name);
//        return this;
//    }
//
//    public WayApp2AppBuilder setUser_addr(String addr) {
//        request.setUser_addr(addr);
//        return this;
//    }
//
//    public WayApp2AppBuilder setUser_phone(String phone) {
//        request.setUser_phone(phone);
//        return this;
//    }
//
//    public WayApp2AppBuilder setOnResponseListener(AppEventListener listener) {
//        this.listener = listener;
//        return this;
//    }
//
//
//    public void show() {
//        if (isEmpty(request.getApplication_id()))
//            error("Application id is not configured.");
////
//        if (isEmpty(request.getPg()))
//            error("PG is not configured.");
////
//        if (request.getPrice() < 0)
//            error("Price is not configured.");
////
//        if (isEmpty(request.getOrder_id()))
//            error("Order id is not configured.");
////
//        if (listener == null && (error == null || cancel == null || done == null))
//            error("Must to be required to handel events.");
//
//        Intent intent = new Intent(context, BootpayActivity.class);
//        context.startActivity(intent);
//    }
//
//    private boolean isEmpty(String value) {
//        return value == null || value.length() == 0;
//    }
//
//    private void error(String message) {
//        throw new RuntimeException(message);
//    }
}
