package kr.co.bootpay.model

import com.google.gson.Gson


class RemoteLink {
    var member: String? = null // 부트페이에서 발급한 부계정 고유 키
    var is_receive_member: Boolean? = null // 구매자 이름 입력 허용할지 말지
    var seller_name: String? = null // 보여질 판매자명, 없으면 등록된 상점명이 보여짐
    var memo: String? = null // 판매자 메모, 없으면 보여주지 않음
    var img_url: String? = null // 상품 대표 이미지 URL, 없으면 보여주지 않음
    var desc_html: String? = null // 상품 설명, 없으면 보여주지 않음
    var delivery_area_price_jeju: Double? = null // 도서산간비용 제주
    var delivery_area_price_nonjeju: Double? = null // 도서산간비용 제주 외 지역

    // 링크결제에서는 전화번호는 필수다, user_phone 으로 채워지느냐 마느냐일 뿐이다
    var is_addr: Boolean? = null // 구매자에게 주소를 받을지 말지
    var is_delivery_area: Boolean? = null // 도서산간 지역 비용 추가 할지 말지
    var is_memo: Boolean? = null // 구매자에게 한줄메시지 받을지 말지

    // 세미 결제폼 관련 데이터
    var item_price: Double? = null // 본래 아이템 판매금액
    var promotion_price: Double? = null // 본래 아이템 판매금액
    var delivery_price: Double? = null // 배달 금액
    var push_policy_type: Int? = null

    private fun remoteLink(vararg etcs: String) = "{${etcs.filter(String::isNotEmpty).joinToString()}}"
    private fun member() = member?.takeIf(String::isNotEmpty)?.let { "member: '$it'" } ?: ""
    private fun isReceiveMember() = is_receive_member?.let { "is_receive_member: '$it'" } ?: ""
    private fun sellerName() = seller_name?.takeIf(String::isNotEmpty)?.let { "seller_name: '$it'" } ?: ""
    private fun memo() = memo?.takeIf(String::isNotEmpty)?.let { "memo: '$it'" } ?: ""
    private fun imgUrl() = img_url?.takeIf(String::isNotEmpty)?.let { "img_url: '$it'" } ?: ""
    private fun descHtml() = desc_html?.takeIf(String::isNotEmpty)?.let { "desc_html: '$it'" } ?: ""
    private fun deliveryAreaPriceJeju() = delivery_area_price_jeju?.let { "delivery_area_price_jeju: '$it'" } ?: ""
    private fun deliveryAreaPriceNonjeju() = delivery_area_price_nonjeju?.let { "delivery_area_price_nonjeju: '$it'" } ?: ""

    private fun itemPrice() = item_price?.let { "ip: $it" } ?: ""
    private fun promotionPrice() = promotion_price?.let { "pp: $it" } ?: ""
    private fun deliveryPrice() = delivery_price?.let { "dp: $it" } ?: ""
    private fun pushPolicyType() = push_policy_type?.let { "ppt: $it" } ?: ""

    private fun isAddr() = is_addr?.let { "is_addr: '$it'" } ?: ""
    private fun isDeliveryArea() = is_delivery_area?.let { "is_delivery_area: '$it'" } ?: ""
    private fun isMemo() = is_memo?.let { "is_memo: '$it'" } ?: ""
    fun toJson() = remoteLink(
            member(),
            isReceiveMember(),
            sellerName(),
            memo(),
            imgUrl(),
            descHtml(),
            deliveryAreaPriceJeju(),
            deliveryAreaPriceNonjeju(),
            isAddr(),
            isDeliveryArea(),
            isMemo(),
            itemPrice(),
            promotionPrice(),
            deliveryPrice(),
            pushPolicyType()
    )
    fun toGson() = Gson().toJson(this)
}
