package kr.co.bootpay.model

class StatCallKotlin(
        val ver: String,
        val application_id: String,
        val uuid: String,
        val url: String,
        val page_type: String,
        val items: List<StatItem>,
        val sk: String,
        val user_id: String,
        val referer: String
)
