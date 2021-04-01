package br.lopes.goalapi.goal.api.controller.user.error.model

class IfMatchNotProvided constructor(message: String, error: Exception = RuntimeException(message)) :
    RuntimeException(message, error)