package br.lopes.goalapi.goal.api.controller.goal.error.model

class GoalPreConditionFailed constructor(message: String, error: Exception = RuntimeException(message)) :
    RuntimeException(message, error)