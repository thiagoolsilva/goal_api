package br.lopes.goalapi.goal.api.controller.contract

data class ApiContract<T> constructor(
        var body: T?,
        var  error: ErrorResponse?
)