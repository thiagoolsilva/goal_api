package br.lopes.goalapi.goal.api.controller.config.error.model

class IfMatchNotProvided constructor(message: String, error: Exception = RuntimeException(message)) :
    RuntimeException(message, error)