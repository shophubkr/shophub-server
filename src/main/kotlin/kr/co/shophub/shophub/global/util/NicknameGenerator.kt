package kr.co.shophub.shophub.global.util

class NicknameGenerator {

    companion object {
        private val adjectiveWords = listOf(
            "행복한", "따듯한", "무서운", "부드러운",
            "재미있는", "매력적인", "친절한", "우아한",
            "특별한", "용감한", "아름다운", "편안한",
            "맑은", "다정한", "즐거운", "강한"
        )
        private val nounWords = listOf(
            "바다", "하늘", "우주", "구름",
            "태양", "산", "바위", "나무",
            "별", "동굴", "호수", "파도",
            "낙타", "고양이", "강아지", "사자",
            "고래", "상어", "사슴", "기린"
        )
        fun makeNickname(): String {
            val adjective = adjectiveWords.random()
            val noun = nounWords.random()
            val randomNumber = (1..9999).random()
            return adjective + noun + randomNumber
        }
    }

}