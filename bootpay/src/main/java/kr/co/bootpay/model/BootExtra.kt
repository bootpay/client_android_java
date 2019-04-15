package kr.co.bootpay.model

import com.google.gson.Gson

class BootExtra {
    private var start_at: String? = null // 정기 결제 시작일 - 시작일을 지정하지 않으면 그 날 당일로부터 결제가 가능한 Billing key 지급
    private var end_at: String? = null // 정기결제 만료일 -  기간 없음 - 무제한
    private var expire_month: Int? = null //정기결제가 적용되는 개월 수 (정기결제 사용시)
    private var vbank_result = false //가상계좌 결과창을 볼지 말지 (가상계좌 사용시)
    private var quotas: IntArray? = null //할부허용 범위 (5만원 이상 구매시)
    var app_scheme: String? = null //app2app 결제시 return 받을 intent scheme
    var app_scheme_host: String? = null //app2app 결제시 return 받을 intent scheme host
    var ux: String? = null //로켓페이 옵션 등 다양한 용도로 사용이 가능하다

    fun setStartAt(value: String?): BootExtra {
        start_at = value
        return this
    }

    fun setEndAt(value: String?): BootExtra {
        end_at = value
        return this
    }

    fun setExpireMonth(value: Int?): BootExtra {
        expire_month = value
        return this
    }

    fun setVbankResult(value: Boolean): BootExtra {
        vbank_result = value
        return this
    }

    fun setQuotas(value: IntArray?): BootExtra {
        quotas = value
        return this
    }

    fun setAppScheme(value: String?): BootExtra {
        app_scheme = value
        return this
    }

    fun setAppSchemeHost(value: String?): BootExtra {
        app_scheme_host = value
        return this
    }

    fun setUX(value: String): BootExtra {
        ux = value
        return this
    }

    private fun extra(vararg etcs: String) = "{${etcs.filter(String::isNotEmpty).joinToString()}}"
    private fun startAt() = start_at?.takeIf(String::isNotEmpty)?.let { "start_at: '$it'" } ?: ""
    private fun endAt() = end_at?.takeIf(String::isNotEmpty)?.let { "end_at: '$it'" } ?: ""
    private fun expireMonth() = expire_month?.let { "expire_month: '$it'" } ?: ""
    private fun vbankResult() = vbank_result?.let { "vbank_result: '$it'" } ?: ""
    private fun quotas() = quotas?.let { "quotas: '${it.joinToString()}'" } ?: ""
    private fun appScheme() = app_scheme?.let { "app_scheme: '${it}://${appSchemeHost()}'" } ?: ""
    private fun appSchemeHost() = app_scheme_host?.let { "${it}" } ?: ""
    private fun ux() = ux?.let { "ux: '${it}'" } ?: ""

    fun toJson() = extra(
            startAt(),
            endAt(),
            expireMonth(),
            vbankResult(),
            quotas(),
            ux(),
            appScheme()
    )

    fun toGson() = Gson().toJson(this)
}


