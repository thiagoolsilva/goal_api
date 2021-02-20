package br.lopes.goalapi.goal.api.controller.user.mapper

import br.lopes.goalapi.goal.api.controller.user.contract.UserRequest
import br.lopes.goalapi.goal.api.controller.user.contract.UserResponseDetails
import br.lopes.goalapi.goal.api.domain.service.user.model.UserEntity

fun UserRequest.toUserEntity() = UserEntity(
        id = this.id,
        name = this.name,
        nickname = this.nickname
)

fun UserEntity.toUserResponse() = UserResponseDetails(
        id = this.id,
        name = this.name,
        nickname = this.nickname
)