package br.lopes.goalapi.goal.api.domain.service.user.usecase

import br.lopes.goalapi.goal.api.data.repository.UserRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.UseCaseContract
import br.lopes.goalapi.goal.api.domain.service.user.mapper.toUserDb
import br.lopes.goalapi.goal.api.domain.service.user.model.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import javax.transaction.Transactional

class SaveUserUC @Autowired constructor(
        private val userRepositoryContract: UserRepositoryContract
): UseCaseContract<UserEntity, Long> {

    override fun execute(input: UserEntity): Long {
        val userDb = input.toUserDb()
        val resultSet = userRepositoryContract.save(userDb)

        return resultSet.id ?: -1
    }
}