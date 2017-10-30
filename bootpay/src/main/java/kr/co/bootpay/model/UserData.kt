package kr.co.bootpay.model

import android.support.annotation.IntRange

class UserData {
    private var user_id: String? = null

    private var user_name: String? = null

    private var user_birth: String? = null

    private var user_email: String? = null

    @IntRange(from = 0, to = 1)
    private var user_gender = -1

    private var user_area: String? = null

    fun setUserID(id: String?): UserData {
        user_id = id
        return this
    }

    fun setUserName(name: String?): UserData {
        user_name = name
        return this
    }

    fun setUserBirth(birth: String?): UserData {
        user_birth = birth
        return this
    }

    fun setUserEmail(email: String?): UserData {
        user_email = email
        return this
    }

    fun setUserGender(@IntRange(from = 0, to = 1) gender: Int): UserData {
        user_gender = gender
        return this
    }

    fun setUserArea(area: String?): UserData {
        user_area = area
        return this
    }
}
