package kr.co.bootpay.secure.security

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.GeneralSecurityException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.or
import kotlin.experimental.xor

/**
 * Simple library for the "right" defaults for AES key generation, encryption,
 * and decryption using 256-bit AES, CBC, PKCS5 padding,
 * and a random 16-byte IV with SHA1PRNG. Integrity with HmacSHA256
 */
internal object AesCbc {

    private const val CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val CIPHER = "AES"
    private const val AES_KEY_LENGTH_BITS = 256
    private const val HMAC_ALGORITHM = "HmacSHA256"
    private const val HMAC_KEY_LENGTH_BITS = 256
    private const val IV_LENGTH_BYTES = 16
    private const val PBE_ITERATION_COUNT = 10000
    private const val PBE_SALT_LENGTH_BITS = AES_KEY_LENGTH_BITS
    private const val PBE_ALGORITHM = "PBKDF2WithHmacSHA1"

    private val RANDOM = SecureRandom()

    private const val ZERO: Byte = 0

    private const val BASE64_FLAGS = Base64.NO_WRAP

    /**
     * @see SecretKeys.toString
     * @return confidentialKey:integrityKey
     */
    internal fun keyString(keys: SecretKeys) = keys.toString()

    /**
     * @param key : Base64 encoded AES key / hmac key as base64(aesKey) : base64(hmacKey)
     */
    @Throws(InvalidKeyException::class, IllegalArgumentException::class)
    internal fun keys(key: String): SecretKeys {
        val arr = key.split(":")
        return if (arr.size != 2) throw IllegalArgumentException("Cannot parse aesKey:hmacKey")
        else {
            val confidentialKey = Base64.decode(arr[0], BASE64_FLAGS)
            val integrityKey = Base64.decode(arr[1], BASE64_FLAGS)
            if (confidentialKey.size != AES_KEY_LENGTH_BITS / 8) throw (InvalidKeyException("Base64 decoded key is not ${AES_KEY_LENGTH_BITS} bytes"))
            if (integrityKey.size != AES_KEY_LENGTH_BITS / 8) throw (InvalidKeyException("Base64 decoded key is not ${HMAC_KEY_LENGTH_BITS} bytes"))
            SecretKeys(
                    SecretKeySpec(confidentialKey, 0, confidentialKey.size, CIPHER),
                    SecretKeySpec(integrityKey, HMAC_ALGORITHM)
            )
        }
    }

    @Throws(GeneralSecurityException::class)
    internal fun generateKey(password: String, salt: ByteArray): SecretKeys {
        val key = SecretKeyFactory.getInstance(PBE_ALGORITHM)
                .generateSecret(PBEKeySpec(password.toCharArray(), salt, PBE_ITERATION_COUNT, AES_KEY_LENGTH_BITS + HMAC_KEY_LENGTH_BITS)).encoded
        val confidentialityKey = copyOfRange(key, 0, AES_KEY_LENGTH_BITS / 8)
        val integrityKeyBytes = copyOfRange(key, AES_KEY_LENGTH_BITS / 8, AES_KEY_LENGTH_BITS / 8 + HMAC_KEY_LENGTH_BITS / 8)
        return SecretKeys(SecretKeySpec(confidentialityKey, CIPHER), SecretKeySpec(integrityKeyBytes, HMAC_ALGORITHM))
    }

    @Throws(GeneralSecurityException::class)
    fun generateKey(): SecretKeys = SecretKeys(
            KeyGenerator.getInstance(CIPHER).apply { init(AES_KEY_LENGTH_BITS) }.generateKey(),
            SecretKeySpec(randomBytes(HMAC_KEY_LENGTH_BITS / 8), HMAC_ALGORITHM))

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    private fun generateMac(cipherText: ByteArray, integrityKey: SecretKey): ByteArray = Mac.getInstance(HMAC_ALGORITHM)
            .apply { init(integrityKey) }
            .doFinal(cipherText)

    private fun constantTimeEQ(x: ByteArray, y: ByteArray): Boolean =
            if (x.size != y.size) false
            else x
                    .withIndex()
                    .map { y[it.index] xor it.value }
                    .reduce { a, b -> a or b } == ZERO

    private fun copyOfRange(from: ByteArray, start: Int, end: Int): ByteArray =
            ByteArray(end - start).also { System.arraycopy(from, start, it, 0, end - start) }

    @Throws(GeneralSecurityException::class)
    internal fun generateSalt(): ByteArray = randomBytes(PBE_SALT_LENGTH_BITS)

    internal fun saltString(salt: ByteArray): String = Base64.encodeToString(salt, BASE64_FLAGS)

    @Throws(GeneralSecurityException::class)
    private fun generateIV(): ByteArray = randomBytes(IV_LENGTH_BYTES)

    private fun randomBytes(length: Int): ByteArray = ByteArray(length)
            .also { RANDOM.nextBytes(it) }

    @Throws(UnsupportedEncodingException::class, GeneralSecurityException::class)
    internal fun encrypt(text: String, keys: SecretKeys, encoding: Charset = Charsets.UTF_8): CipherText =
            encrypt(text.toByteArray(encoding), keys)

    @Throws(GeneralSecurityException::class)
    private fun encrypt(text: ByteArray, keys: SecretKeys): CipherText {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
                .apply { init(Cipher.ENCRYPT_MODE, keys.confidentialKey, IvParameterSpec(generateIV())) }
        val iv = cipher.iv
        val cipherText = cipher.doFinal(text)
        val ivCipherConcat = CipherText.ivCipherConcat(iv, cipherText)
        val integrityMac = generateMac(ivCipherConcat, keys.integrityKey)
        return CipherText(cipherText, iv, integrityMac)
    }

    @Throws(GeneralSecurityException::class)
    private fun decrypt(civ: CipherText, keys: SecretKeys): ByteArray =
            if (constantTimeEQ(generateMac(CipherText.ivCipherConcat(civ.iv, civ.text), keys.integrityKey), civ.mac))
                Cipher.getInstance(CIPHER_TRANSFORMATION)
                        .apply { init(Cipher.DECRYPT_MODE, keys.confidentialKey, IvParameterSpec(civ.iv)) }
                        .doFinal(civ.text)
            else throw GeneralSecurityException("MAC stored in civ does not match computed MAC.")

    @Throws(UnsupportedEncodingException::class, GeneralSecurityException::class)
    internal fun decryptString(civ: CipherText, keys: SecretKeys, encoding: Charset = Charsets.UTF_8): String =
            String(decrypt(civ, keys), encoding)

    @Throws(UnsupportedEncodingException::class, GeneralSecurityException::class, IllegalArgumentException::class)
    internal fun decryptString(text: String, keys: SecretKeys, encoding: Charset = Charsets.UTF_8): String =
            text.split(":").takeIf { it.size == 3 }?.map { Base64.decode(it, BASE64_FLAGS) }?.let { String(decrypt(CipherText(it[2], it[0], it[1]), keys), encoding) } ?: throw IllegalArgumentException("Cannot parse iv:text:mac")

}