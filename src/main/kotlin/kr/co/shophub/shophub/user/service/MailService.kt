package kr.co.shophub.shophub.user.service

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
    private val userRepository: UserRepository,
) {

    fun sendPasswordMail(mailRequest: MailRequest) {
        if (!userRepository.existsByEmail(mailRequest.email)) {
            throw IllegalStateException("가입 정보와 일치하지 않습니다.")
        }
        val message = SimpleMailMessage()
        val link = "비밀번호 재설정용 링크"
        message.setTo(mailRequest.email)
        message.subject = "비밀번호 재설정 이메일 입니다."
        message.text = "다음 링크를 눌러 메일 인증을 완료 해 주세요. 링크: $link"
        javaMailSender.send(message)
    }

}