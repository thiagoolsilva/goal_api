package br.lopes.goalapi.goal.api.domain.service.user

import br.lopes.goalapi.goal.api.domain.service.user.model.UserEntity

interface UserServiceContract {
    fun createUser(userEntity: UserEntity):Long
}