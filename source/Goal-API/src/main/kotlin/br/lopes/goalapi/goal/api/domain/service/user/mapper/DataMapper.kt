package br.lopes.goalapi.goal.api.domain.service.user.mapper

import br.lopes.goalapi.goal.api.data.entity.User
import br.lopes.goalapi.goal.api.domain.service.user.model.UserEntity
import java.time.LocalDateTime

fun UserEntity.toUserDb() = User(
        id = this.id,
        name = this.name,
        nickname = this.nickname,
        dtCreate = LocalDateTime.now(),
        dtUpdate = null
)

fun User.toUserEntity() = UserEntity(
        id = this.id,
        name = this.name,
        nickname = this.nickname
)