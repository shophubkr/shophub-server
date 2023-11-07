package kr.co.shophub.shophub.user.service

import kr.co.shophub.shophub.global.jwt.service.JwtService
import kr.co.shophub.shophub.user.dto.MailRequest
import kr.co.shophub.shophub.user.repository.UserRepository
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MailService(
    private val javaMailSender: JavaMailSender,
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
) {

    fun sendAuthorizeMail(mailRequest: MailRequest) {
        val message = SimpleMailMessage()
        val token = jwtService.createTokenForOAuth2(mailRequest.email)
        //이 부분은 도메인이 결정되면 환경변수 처리해서 사용하면 될 것 같습니다!
        val baseUrl = "http//localhost:8081"
        val link = "$baseUrl/api/v1/auth/authorize?token=$token"
        message.setTo(mailRequest.email)
        message.subject = "인증 이메일 입니다."
        message.text = "다음 링크를 눌러 메일 인증을 완료 해 주세요. 링크: $link"
        javaMailSender.send(message)
    }

    fun sendPasswordMail(mailRequest: MailRequest) {
        require(userRepository.existsByEmail(mailRequest.email)) { "가입 정보와 일치하지 않습니다." }
        val message = SimpleMailMessage()
        val link = "비밀번호 재설정용 링크"
        message.setTo(mailRequest.email)
        message.subject = "비밀번호 재설정 이메일 입니다."
        message.text = "다음 링크를 눌러 메일 인증을 완료 해 주세요. 링크: $link"
        javaMailSender.send(message)
    }

}