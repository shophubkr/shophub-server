package kr.co.shophub.shophub.user.controller

import kr.co.shophub.shophub.user.controller.dto.reqeust.SignUpRequest
import kr.co.shophub.shophub.user.controller.dto.response.UserResponse
import kr.co.shophub.shophub.user.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/join")
    fun signUp(@RequestBody request: SignUpRequest): UserResponse = userService.join(request)

}