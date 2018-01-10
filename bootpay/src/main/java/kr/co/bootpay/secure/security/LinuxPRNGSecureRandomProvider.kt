package kr.co.bootpay.secure.security

import java.security.Provider

internal object LinuxPRNGSecureRandomProvider : Provider("LinuxPRNG", 1.0, "A Linux-specific random number provider that uses /dev/urandom") {
    init {
        put("SecureRandom.SHA1PRNG", "LinuxPRNGSecureRandom")
        put("SecureRandom.SHA1PRNG ImplementedIn", "Software")
    }
}
