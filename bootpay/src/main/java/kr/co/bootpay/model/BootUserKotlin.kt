package kr.co.bootpay.model

import com.google.gson.Gson

// 결제할 사용자 정보
class BootUserKotlin {
    private var id: String? = null //개발사가 발급한 고유 아이디
    private var username: String? = null
    private var birth: String? = null
    private var email: String? = null
    private var gender: Int? = -1
    private var area: String? = null
    private var phone: String? = null

    fun setID(value: String?): BootUserKotlin {
        id = value
        return this
    }

    fun setUsername(value: String?): BootUserKotlin {
        username = value
        return this
    }

    fun setBirth(value: String?): BootUserKotlin {
        birth = value
        return this
    }

    fun setEmail(value: String?): BootUserKotlin {
        email = value
        return this
    }

    fun setGender(value: Int): BootUserKotlin {
        gender = value
        return this
    }

    fun setAddr(value: String?): BootUserKotlin {
        area = value
        return this
    }

    fun setPhone(value: String?): BootUserKotlin {
        phone = value
        return this
    }

    private fun user(vararg etcs: String) = "{${etcs.filter(String::isNotEmpty).joinToString()}}"
    private fun id() = id?.takeIf(String::isNotEmpty)?.let { "id: '$it'" } ?: ""
    private fun username() = username?.takeIf(String::isNotEmpty)?.let { "username: '$it'" } ?: ""
    private fun birth() = birth?.takeIf(String::isNotEmpty)?.let { "birth: '$it'" } ?: ""
    fun phone() = phone?.takeIf(String::isNotEmpty)?.let { "phone: '$it'" } ?: ""
    private fun email() = email?.takeIf(String::isNotEmpty)?.let { "email: '$it'" } ?: ""
    private fun gender() = gender?.let { "gender: $it" } ?: ""
    private fun area() = area?.takeIf(String::isNotEmpty)?.let { "area: '$it'" } ?: ""

    // javascript
    fun toJson() = user(
            id(),
            username(),
            birth(),
            phone(),
            email(),
            gender(),
            area()
    )

    fun toGson() = Gson().toJson(this)

    fun getPhone(): String {
        return phone ?: ""
    }
}
