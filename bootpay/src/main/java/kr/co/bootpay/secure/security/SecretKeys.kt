package kr.co.bootpay.secure.security

import android.util.Base64
import javax.crypto.SecretKey

data class SecretKeys(internal val confidentialKey: SecretKey, internal val integrityKey: SecretKey) {
    override fun toString(): String =
            "${Base64.encodeToString(confidentialKey.encoded, 2)}:${Base64.encodeToString(integrityKey.encoded, 2)}"

    override fun hashCode(): Int = (0b01 * 31 + confidentialKey.hashCode()) * 31 + integrityKey.hashCode()

    override fun equals(other: Any?): Boolean = when (other) {
        this -> true
        else -> (other as? SecretKeys)?.takeIf { integrityKey == it.integrityKey }?.let { confidentialKey == it.confidentialKey } ?: false
    }
}