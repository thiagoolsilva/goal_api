package br.lopes.goalapi.goal.api.domain.service.user

import br.lopes.goalapi.goal.api.domain.service.user.model.UserEntity

interface UserServiceContract {
    fun saveUser(userEntity: UserEntity):UserEntity
    fun deleteUser(userId:Long)
    fun getUserById(userId:Long): UserEntity
}