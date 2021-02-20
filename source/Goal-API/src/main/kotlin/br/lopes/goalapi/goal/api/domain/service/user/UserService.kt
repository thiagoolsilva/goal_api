package br.lopes.goalapi.goal.api.domain.service.user

import br.lopes.goalapi.goal.api.domain.service.user.mapper.toUserEntity
import br.lopes.goalapi.goal.api.domain.service.user.model.UserEntity
import br.lopes.goalapi.goal.api.domain.service.user.usecase.DeleteUserUC
import br.lopes.goalapi.goal.api.domain.service.user.usecase.FindUserByIdUC
import br.lopes.goalapi.goal.api.domain.service.user.usecase.SaveUserUC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService @Autowired constructor(
        private val saveUserUC: SaveUserUC,
        private val deleteUserUC: DeleteUserUC,
        private val findUserByIdUC: FindUserByIdUC
): UserServiceContract {

    override fun saveUser(userEntity: UserEntity): UserEntity {
        return saveUserUC.execute(userEntity).toUserEntity()
    }

    override fun deleteUser(userId: Long) {
        deleteUserUC.execute(userId)
    }

    override fun getUserById(userId: Long):UserEntity {
        return findUserByIdUC.execute(userId).toUserEntity()
    }

}