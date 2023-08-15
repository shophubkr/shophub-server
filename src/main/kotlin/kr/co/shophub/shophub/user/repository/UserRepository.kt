package kr.co.shophub.shophub.user.repository

import kr.co.shophub.shophub.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>