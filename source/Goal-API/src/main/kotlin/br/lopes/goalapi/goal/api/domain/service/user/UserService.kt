package br.lopes.goalapi.goal.api.domain.service.user

import br.lopes.goalapi.goal.api.domain.service.user.model.UserEntity
import br.lopes.goalapi.goal.api.domain.service.user.usecase.SaveUserUC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
        private val saveUserUC: SaveUserUC
): UserServiceContract {
    override fun createUser(userEntity: UserEntity): Long {
        return saveUserUC.execute(userEntity)
    }



}