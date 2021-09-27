import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.31"
	kotlin("plugin.spring") version "1.5.31"
}

group = "com.nilmani"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-reactor-netty
	implementation("org.springframework.boot:spring-boot-starter-reactor-netty:2.5.5")
	// https://mvnrepository.com/artifact/org.webjars/webjars-locator-core
	implementation("org.webjars:webjars-locator-core:0.48")
	// https://mvnrepository.com/artifact/org.webjars/sockjs-client
	implementation("org.webjars:sockjs-client:1.5.1")
	// https://mvnrepository.com/artifact/org.webjars/stomp-websocket
	implementation("org.webjars:stomp-websocket:2.3.4")
	// https://mvnrepository.com/artifact/org.webjars/bootstrap
	implementation("org.webjars:bootstrap:4.6.0")
	// https://mvnrepository.com/artifact/org.webjars.bower/jquery
	implementation("org.webjars.bower:jquery:3.5.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
