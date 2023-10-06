package kr.co.shophub.shophub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class ShopHubApplication

fun main(args: Array<String>) {
	runApplication<ShopHubApplication>(*args)
}
