package kr.co.shophub.shophub.user.service

import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.user.dto.MailAuthRequest
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class MailService(
    private val javaMailSender: JavaMailSender,
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
) {

    fun sendMail(mailAuthRequest: MailAuthRequest) {
        val message = SimpleMailMessage()
        val token = jwtService.createTokenForOAuth2(mailAuthRequest.email)
        val link = "http://localhost:8081/api/v1/auth/authorize?token=$token"
        message.setTo(mailAuthRequest.email)
        message.subject = "인증 이메일 입니다."
        message.text = "다음 링크를 눌러 메일 인증을 완료 해 주세요. 링크: $link"
        javaMailSender.send(message)
    }

    fun sendMailForPassword(email: String) {
        if (!userRepository.existsByEmail(email)) {
            throw IllegalStateException("가입 정보와 일치하지 않습니다.")
        }
        val message = SimpleMailMessage()
        val link = "비밀번호 재설정용 링크"
        message.setTo(email)
        message.subject = "비밀번호 재설정 이메일 입니다."
        message.text = "다음 링크를 눌러 메일 인증을 완료 해 주세요. 링크: $link"
        javaMailSender.send(message)
    }

}