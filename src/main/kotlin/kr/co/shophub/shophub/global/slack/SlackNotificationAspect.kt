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

    fun sendSlackMessage(request: HttpServletRequest, e: Exception) {
        val slackMessage = constructSlackMessage(request, e)
        slackApi.call(slackMessage)
    }

    private fun constructSlackMessage(request: HttpServletRequest, e: Exception): SlackMessage {
        val slackAttachment = constructSlackAttachment(request, e)
        val slackMessage = SlackMessage()
        slackMessage.setAttachments(listOf(slackAttachment))
        slackMessage.setText("!!에러 발생!!")
        slackMessage.setUsername("당직병")
        return slackMessage
    }

    private fun constructSlackAttachment(request: HttpServletRequest, e: Exception): SlackAttachment {
        val slackAttachment = SlackAttachment()
        slackAttachment.setFallback("Error")
        slackAttachment.setColor("danger")
        slackAttachment.setFields(listOf(
            constructSlackField("Exception class", e.javaClass.canonicalName),
            constructSlackField("예외 메시지", e.message ?: ""),
            constructSlackField("Request URL", request.requestURL.toString()),
            constructSlackField("Request Method", request.method),
            constructSlackField("요청 시간", currentTime()),
            constructSlackField("Request IP", request.remoteAddr),
            constructSlackField("Profile 정보", environment.activeProfiles.contentToString())
        ))
        return slackAttachment
    }

    private fun constructSlackField(title: String, value: String): SlackField {
        val slackField = SlackField()
        slackField.setTitle(title)
        slackField.setValue(value)
        return slackField
    }

    private fun currentTime(): String {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))
    }
}