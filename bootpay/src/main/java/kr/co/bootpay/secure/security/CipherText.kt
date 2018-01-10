package kr.co.bootpay.secure.security

import android.util.Base64
import java.util.*

class CipherText(text: ByteArray,
                 iv: ByteArray,
                 mac: ByteArray) {

    internal val iv = ByteArray(iv.size)
    internal val text = ByteArray(text.size)
    internal val mac = ByteArray(mac.size)

    init {
        System.arraycopy(mac, 0, this.mac, 0, mac.size)
        System.arraycopy(text, 0, this.text, 0, text.size)
        System.arraycopy(iv, 0, this.iv, 0, iv.size)
    }

    companion object {
        fun ivCipherConcat(iv: ByteArray, text: ByteArray): ByteArray = ByteArray(iv.size + text.size).also {
            System.arraycopy(iv, 0, it, 0, iv.size)
            System.arraycopy(text, 0, it, iv.size, text.size)
        }
    }

    override fun toString(): String =
            "${Base64.encodeToString(iv, 2)}:${Base64.encodeToString(mac, 2)}:${Base64.encodeToString(text, 2)}"

    override fun hashCode(): Int = ((0b01 * 31 + Arrays.hashCode(text)) * 31 + Arrays.hashCode(iv)) * 31 + Arrays.hashCode(mac)

    override fun equals(other: Any?): Boolean = when (other) {
        this -> true
        else -> (other as? CipherText)?.
                takeIf { Arrays.equals(text, it.text) }?.
                takeIf { Arrays.equals(iv, it.iv) }?.
                let { Arrays.equals(mac, it.mac) } ?: false
    }
}