package br.lopes.goalapi.goal.api.controller.user.contract

data class UserResponseDetails constructor(
        val id:Long?,
        val name:String,
        val nickname:String
)