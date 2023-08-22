package kr.co.shophub.shophub.global.slack

import jakarta.servlet.http.HttpServletRequest
import net.gpedro.integrations.slack.SlackApi
import net.gpedro.integrations.slack.SlackAttachment
import net.gpedro.integrations.slack.SlackField
import net.gpedro.integrations.slack.SlackMessage
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Aspect
@Component
@Profile("ec2-dev")
class SlackNotificationAspect(
    private val slackApi: SlackApi,
    private val threadPoolTaskExecutor: ThreadPoolTaskExecutor,
    private val environment: Environment,
) {

    @Around("@annotation(kr.co.shophub.shophub.global.slack.SlackNotification) && args(request, e)")
    fun slackNotification(
        proceedingJoinPoint: ProceedingJoinPoint,
        request: HttpServletRequest,
        e: Exception
    ) {
        proceedingJoinPoint.proceed()
        val context = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        val requestCopy = context.request
        threadPoolTaskExecutor.execute { sendSlackMessage(requestCopy, e) }
    }

    private fun sendSlackMessage(request: HttpServletRequest, e: Exception) {
        val slackAttachment = SlackAttachment()
        slackAttachment.setFallback("Error")
        slackAttachment.setColor("danger")
        slackAttachment.setFields(
            listOf<SlackField>(
                SlackField().setTitle("Exception class").setValue(e.javaClass.getCanonicalName()),
                SlackField().setTitle("예외 메시지").setValue(e.message),
                SlackField().setTitle("Request URL").setValue(request.requestURL.toString()),
                SlackField().setTitle("Request Method").setValue(request.method),
                SlackField().setTitle("요청 시간")
                    .setValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))),
                SlackField().setTitle("Request IP").setValue(request.remoteAddr),
                SlackField().setTitle("Profile 정보").setValue(environment.activeProfiles.contentToString())
            )
        )
        val slackMessage = SlackMessage()
        slackMessage.setAttachments(listOf(slackAttachment))
        slackMessage.setText("!!에러 발생!!")
        slackMessage.setUsername("당직병")
        slackApi.call(slackMessage)
    }
}