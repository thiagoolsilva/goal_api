package br.lopes.goalapi.goal.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.web.config.EnableSpringDataWebSupport

@SpringBootApplication
@EnableSpringDataWebSupport
class GoalApiApplication

fun main(args: Array<String>) {
	runApplication<GoalApiApplication>(*args)
}
