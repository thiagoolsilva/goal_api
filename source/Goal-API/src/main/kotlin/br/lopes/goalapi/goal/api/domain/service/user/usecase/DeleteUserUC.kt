package br.lopes.goalapi.goal.api.domain.service.user.usecase

import br.lopes.goalapi.goal.api.data.repository.UserRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.UseCaseContract
import org.springframework.beans.factory.annotation.Autowired

class DeleteUserUC @Autowired constructor(
        private val userRepositoryContract: UserRepositoryContract
): UseCaseContract<Long, Unit> {

    override fun execute(input: Long):Unit {
        userRepositoryContract.deleteById(input)
    }
}