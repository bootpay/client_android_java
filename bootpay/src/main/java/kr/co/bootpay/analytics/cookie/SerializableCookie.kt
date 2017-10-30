package kr.co.bootpay.analytics.cookie

import android.util.Log
import okhttp3.Cookie
import java.io.*
import java.util.*

internal object SerializableCookie : Serializable {

    private val NON_VALID_EXPIRES_AT = -1L

    @Transient
    var cookie: Cookie? = null

    fun encode(cookie: Cookie): String? {
        SerializableCookie.cookie = cookie
        val bos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(bos)
        return try {
            oos.writeObject(this)
            bos.toByteArray().toHex()
        } catch (e: IOException) {
            Log.e(SerializableCookie::class.java.simpleName, e.message ?: "IOException", e)
            null
        } finally {
            oos.close()
            bos.close()
        }
    }


    fun decode(encodedCookie: String): Cookie? {
        val bytes = encodedCookie.hexToByteArray()
        val bis = ByteArrayInputStream(bytes)
        val ois = ObjectInputStream(bis)
        return try {
            SerializableCookie.cookie
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            ois.close()
            bis.close()
        }
    }


    @Throws(IOException::class)
    fun writeObject(oos: ObjectOutputStream) {
        cookie?.let {
            oos.writeObject(it.name())
            oos.writeObject(it.value())
            oos.writeLong(if (it.persistent()) it.expiresAt() else NON_VALID_EXPIRES_AT)
            oos.writeObject(it.domain())
            oos.writeObject(it.path())
            oos.writeBoolean(it.secure())
            oos.writeBoolean(it.httpOnly())
            oos.writeBoolean(it.hostOnly())
        }
    }

    @Throws(IOException::class)
    fun readObject(ois: ObjectInputStream) {
        val name = ois.readObject() as String
        val value = ois.readObject() as String
        val expire = ois.readLong()
        val domain = ois.readObject() as String
        val path = ois.readObject() as String
        val secure = ois.readBoolean()
        val httpOnly = ois.readBoolean()
        val hostOnly = ois.readBoolean()

        val builder = Cookie.Builder()
                .name(name)
                .value(value)
                .domain(domain)
                .path(path)

        if (expire != NON_VALID_EXPIRES_AT) builder.expiresAt(expire)
        if (secure) builder.secure()
        if (httpOnly) builder.httpOnly()
        if (hostOnly) builder.hostOnlyDomain(domain)
        cookie = builder.build()
    }

    private fun String.hexToByteArray(): ByteArray {
        val data = ByteArray(length shr 1)
        forEachIndexed { i, s ->
            val d = HEX_HASH[s] ?: 0
            data[i shr 1] = if ((i % 2) == 0) (data[i shr 1].plus(d shl 4)).toByte() else data[i shr 1].plus(d).toByte()
        }
        return data
    }

    private fun Byte.toHex(): String {
        val octet = toInt()
        val first: Int = (octet and 0xF0) shr 4
        val second: Int = (octet and 0x0F)
        return "${HEX_CHAR[first]}${HEX_CHAR[second]}"
    }

    private fun ByteArray.toHex(): String {
        return map { it.toHex() }
                .joinToString("")
    }

    private val HEX_CHAR by lazy { "0123456789ABCDEF".toCharArray() }
    private val HEX_HASH by lazy { hexHash(HashMap()) }

    private fun hexHash(hash: HashMap<Char, Int>): HashMap<Char, Int> {
        hash['0'] = 0
        hash['1'] = 1
        hash['2'] = 2
        hash['3'] = 3
        hash['4'] = 4
        hash['5'] = 5
        hash['6'] = 6
        hash['7'] = 7
        hash['8'] = 8
        hash['9'] = 9
        hash['a'] = 10
        hash['b'] = 11
        hash['c'] = 12
        hash['d'] = 13
        hash['e'] = 14
        hash['f'] = 15
        hash['A'] = 10
        hash['B'] = 11
        hash['C'] = 12
        hash['D'] = 13
        hash['E'] = 14
        hash['F'] = 15
        return hash
    }
}