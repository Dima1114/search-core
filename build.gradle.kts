import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val kotlinVersion = "1.3.61"
	kotlin("plugin.jpa") version kotlinVersion
//	id("org.springframework.boot") version "2.1.6.RELEASE"
//	id("io.spring.dependency-management") version "1.0.7.RELEASE"
	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	kotlin("kapt") version kotlinVersion
	`maven-publish`
	maven
}

group = "venus"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
    mavenLocal()
	jcenter()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.1.6.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-data-rest:2.1.6.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-web:2.1.6.RELEASE")
	implementation("org.springframework.boot:spring-boot-starter-security:2.1.6.RELEASE")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//	kapt("com.querydsl:querydsl-apt:4.2.1:jpa")
	implementation("com.querydsl:querydsl-jpa:4.2.1")
	implementation("org.reflections:reflections:0.9.11")
	implementation("io.github.microutils:kotlin-logging:1.6.10")

	testImplementation("org.springframework.boot:spring-boot-starter-test:2.1.6.RELEASE")
	testImplementation("com.h2database:h2:1.4.199")
	testImplementation("org.amshove.kluent:kluent:1.49")
	testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
	testImplementation("org.jetbrains.kotlin:kotlin-test:1.3.41")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.3.41")
	testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
	kaptTest("com.querydsl:querydsl-apt:4.2.1:jpa")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

publishing {
	publications {
		create<MavenPublication>("maven") {
			groupId = "venus"
			artifactId = "search-core"
			version = "1.0"

			from(components["java"])
		}
	}
}
