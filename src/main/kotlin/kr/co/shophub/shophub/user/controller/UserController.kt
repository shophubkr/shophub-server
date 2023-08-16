package kr.co.shophub.shophub.user.controller

import kr.co.shophub.shophub.user.controller.dto.SignUpRequest
import kr.co.shophub.shophub.user.domain.User
import kr.co.shophub.shophub.user.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): User = userService.signUp(request)

}