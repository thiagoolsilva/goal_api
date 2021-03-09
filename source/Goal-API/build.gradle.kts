import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
	id("org.springframework.boot") version "2.4.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.21"
	kotlin("plugin.spring") version "1.4.21"
	kotlin("plugin.jpa") version "1.4.21"
	kotlin("plugin.allopen") version "1.3.61"
	kotlin("kapt") version "1.3.61"
}

allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.Embeddable")
	annotation("javax.persistence.MappedSuperclass")
}

group = "br.lopes.goalapi"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
	mavenCentral()
}

dependencies {
	// database
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// validation
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// Rest
	implementation("org.springframework.boot:spring-boot-starter-web")

	// serialize and deserialize class in kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	// build dependencies
	implementation("org.mapstruct:mapstruct:1.4.2.Final")
	kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")

	// mapper
	implementation ("org.mapstruct:mapstruct:1.4.2.Final")
	annotationProcessor ("org.mapstruct:mapstruct-processor:1.4.2.Final")

//	implementation( "org.mapstruct:mapstruct:1.4.2.Final")
//	annotationProcessor( "org.mapstruct:mapstruct-processor:1.4.2.Final")

	// dev dependencies
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// database in memory
	runtimeOnly("com.h2database:h2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
