package kr.co.bootpay.secure.security

import android.os.Build
import android.os.Process
import android.util.Log
import java.io.*
import java.security.SecureRandomSpi
import java.util.concurrent.atomic.AtomicBoolean

internal object LinuxPRNGSecureRandom : SecureRandomSpi() {

    private val URANDOM_FILE = File("/dev/urandom")
    private val urandomInputStream: DataInputStream by lazy { DataInputStream(FileInputStream(URANDOM_FILE)) }
    private val urandomOutputStream: OutputStream by lazy { FileOutputStream(URANDOM_FILE) }
    private val isSeeded = AtomicBoolean(false)
    private val FINGERPRINT_AND_SERIAL = getBuildFingerprintAndDeviceSerial()

    override fun engineSetSeed(b: ByteArray?) {
        try {
            urandomOutputStream.write(b)
            urandomOutputStream.flush()
        } catch (e: IOException) {
            Log.w("PRNG", "Failed to mix seed into ${URANDOM_FILE}")
        } finally {
            isSeeded.set(true)
        }
    }

    override fun engineNextBytes(b: ByteArray?) {
        if (!isSeeded.get()) engineSetSeed(generateSeed())
        try {
            urandomInputStream.readFully(b)
        } catch (e: IOException) {
            throw SecurityException("Failed to read from ${URANDOM_FILE}", e)
        }
    }

    override fun engineGenerateSeed(i: Int): ByteArray =
            ByteArray(i).also { engineNextBytes(it) }

    private fun generateSeed(): ByteArray {
        try {
            return ByteArrayOutputStream().also {
                DataOutputStream(it).use {
                    it.writeLong(System.currentTimeMillis())
                    it.writeLong(System.nanoTime())
                    it.writeInt(Process.myPid())
                    it.writeInt(Process.myUid())
                    it.write(FINGERPRINT_AND_SERIAL)
                    it.flush()
                }
            }.toByteArray()
        } catch (e: IOException) {
            throw SecurityException("Failed to generate seed", e)
        }
    }

    private fun getBuildFingerprintAndDeviceSerial(): ByteArray =
            "${Build.FINGERPRINT ?: ""}${Build.SERIAL ?: ""}".toByteArray()
}