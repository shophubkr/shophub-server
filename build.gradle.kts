import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.2"
	id("io.spring.dependency-management") version "1.1.2"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
	kotlin("kapt") version "1.8.22"
}

configurations {
	all {
		exclude(group = "commons-logging", module = "commons-logging")
	}
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

group = "kr.co.shophub"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}
repositories {
	mavenCentral()
}

val queryDslVersion = "5.0.0"

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.3")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation ("com.auth0:java-jwt:4.2.1")
	implementation("io.github.oshai:kotlin-logging:5.0.0")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.mysql:mysql-connector-j")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("net.gpedro.integrations.slack:slack-webhook:1.4.0")
	implementation("io.awspring.cloud:spring-cloud-starter-aws:2.4.4")

	//kotest + mockk
	testImplementation("io.kotest:kotest-runner-junit5-jvm:4.6.0")
	testImplementation("io.kotest:kotest-assertions-core-jvm:4.6.0")
	testImplementation("io.mockk:mockk:1.13.7")

	//oauth2
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

	// queryDSL
	implementation ("com.querydsl:querydsl-jpa:${queryDslVersion}:jakarta")
	kapt("com.querydsl:querydsl-apt:${queryDslVersion}:jakarta")
	annotationProcessor("jakarta.annotation:jakarta.annotation-api")
	annotationProcessor("jakarta.persistence:jakarta.persistence-api")

	// mail
	implementation ("org.springframework.boot:spring-boot-starter-mail")

	//feign Client
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}
tasks.named<Jar>("jar") {
	enabled = false
}
tasks.withType<Test>().configureEach  {
	useJUnitPlatform()
}