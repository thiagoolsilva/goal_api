package br.lopes.goalapi.goal.api.domain.service.user.usecase

import br.lopes.goalapi.goal.api.data.entity.User
import br.lopes.goalapi.goal.api.data.repository.UserRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.UseCaseContract
import org.springframework.beans.factory.annotation.Autowired

class FindUserByIdUC @Autowired constructor(
        private val userRepositoryContract: UserRepositoryContract
): UseCaseContract<Long, User> {

    override fun execute(input: Long): User {
        return userRepositoryContract.getOne(input)
    }
}