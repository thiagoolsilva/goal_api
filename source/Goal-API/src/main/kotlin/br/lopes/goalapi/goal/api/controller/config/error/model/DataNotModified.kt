package br.lopes.goalapi.goal.api.controller.config.error.model

class DataNotModified  constructor(message: String, error: Exception = RuntimeException(message)) :
    RuntimeException(message, error)