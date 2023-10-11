package kr.co.shophub.shophub.business.repository

import kr.co.shophub.shophub.business.model.Business
import org.springframework.data.jpa.repository.JpaRepository

interface BusinessRepository : JpaRepository<Business, Long>