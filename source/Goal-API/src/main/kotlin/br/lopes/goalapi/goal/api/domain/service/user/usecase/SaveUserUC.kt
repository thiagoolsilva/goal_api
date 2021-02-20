package br.lopes.goalapi.goal.api.domain.service.user.usecase

import br.lopes.goalapi.goal.api.data.entity.User
import br.lopes.goalapi.goal.api.data.repository.UserRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.UseCaseContract
import br.lopes.goalapi.goal.api.domain.service.user.mapper.toUserDb
import br.lopes.goalapi.goal.api.domain.service.user.model.UserEntity
import org.springframework.beans.factory.annotation.Autowired

class SaveUserUC @Autowired constructor(
        private val userRepositoryContract: UserRepositoryContract
): UseCaseContract<UserEntity, User> {

    override fun execute(input: UserEntity): User {
        val userDb = input.toUserDb()

        return userRepositoryContract.save(userDb)
    }
}