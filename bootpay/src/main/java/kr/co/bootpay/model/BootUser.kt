package kr.co.bootpay.model

// 결제할 사용자 정보
class BootUser {
    private var id: String? = null //개발사가 발급한 고유 아이디
    private var username: String? = null
    private var birth: String? = null
    private var email: String? = null
    private var gender: Int? = -1
    private var addr: String? = null
    private var phone: String? = null

    fun setID(value: String?): BootUser {
        id = value
        return this
    }

    fun setUsername(value: String?): BootUser {
        username = value
        return this
    }

    fun setBirth(value: String?): BootUser {
        birth = value
        return this
    }

    fun setEmail(value: String?): BootUser {
        email = value
        return this
    }

    fun setGender(value: Int): BootUser {
        gender = value
        return this
    }

    fun setAddr(value: String?): BootUser {
        addr = value
        return this
    }

    fun setPhone(value: String?): BootUser {
        phone = value
        return this
    }

    private fun user(vararg etcs: String) = "{${etcs.filter(String::isNotEmpty).joinToString()}}"
    private fun id() = id?.takeIf(String::isNotEmpty)?.let { "id: '$it'" } ?: ""
    private fun username() = username?.takeIf(String::isNotEmpty)?.let { "username: '$it'" } ?: ""
    private fun birth() = addr?.takeIf(String::isNotEmpty)?.let { "birth: '$it'" } ?: ""
    fun phone() = phone?.takeIf(String::isNotEmpty)?.let { "phone: '$it'" } ?: ""
    private fun email() = email?.takeIf(String::isNotEmpty)?.let { "email: '$it'" } ?: ""
    private fun gender() = gender?.let { "gender: $it" } ?: ""
    private fun addr() = addr?.takeIf(String::isNotEmpty)?.let { "addr: '$it'" } ?: ""
    fun toJson() = user(
            id(),
            username(),
            birth(),
            phone(),
            email(),
            gender(),
            addr()
    )
    fun getPhone(): String {
        return phone ?: ""
    }
}
