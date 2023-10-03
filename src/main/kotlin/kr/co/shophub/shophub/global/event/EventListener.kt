package kr.co.shophub.shophub.global.event

import kr.co.shophub.shophub.user.dto.MailRequest
import kr.co.shophub.shophub.user.service.MailService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class EventListener(
    private val mailService: MailService,
) {

    @EventListener
    fun sendJoinMail(event: JoinEvent) {
        val mailRequest = MailRequest(event.email)
        mailService.sendMail(mailRequest)
    }

}