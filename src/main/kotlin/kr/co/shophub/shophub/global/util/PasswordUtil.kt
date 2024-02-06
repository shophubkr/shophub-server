package kr.co.shophub.shophub.global.util

import java.util.*

class PasswordUtil {

    companion object {
        fun generateRandomPassword(): String {
            var index: Int
            val charSet = charArrayOf(
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
            )
            val password = StringBuffer()
            val random = Random()
            for (i in 0..15) {
                val rd = random.nextDouble()
                index = (charSet.size * rd).toInt()
                password.append(charSet[index])
            }
            return password.toString()
        }
    }
}