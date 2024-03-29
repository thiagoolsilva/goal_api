/*
 *   Copyright (c) 2020  Thiago Lopes da Silva
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package br.lopes.goalapi.goal.api.domain.service.user

import br.lopes.goalapi.goal.api.domain.service.user.mapper.toUserEntity
import br.lopes.goalapi.goal.api.domain.service.user.model.UserEntity
import br.lopes.goalapi.goal.api.domain.service.user.usecase.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService @Autowired constructor(
        private val saveUserUC: SaveUserUC,
        private val deleteUserUC: DeleteUserUC,
        private val findUserByIdUC: FindUserByIdUC,
        private val findUserByQueryUC: FindUserByQueryUC,
        private val updateUserUC:UpdateUserUC
): UserServiceContract {
    @Transactional
    override fun updateUser(userEntity: UserEntity): UserEntity {
        return updateUserUC.execute(userEntity).toUserEntity()
    }

    @Transactional
    override fun saveUser(userEntity: UserEntity): UserEntity {
        return saveUserUC.execute(userEntity).toUserEntity()
    }

    override fun deleteUser(userId: Long) {
        deleteUserUC.execute(userId)
    }

    override fun getUserById(userId: Long):UserEntity {
        return findUserByIdUC.execute(userId).toUserEntity()
    }

    override fun findUserByQuery(params: Map<String, Any>): Page<UserEntity> {
        return findUserByQueryUC.execute(params).map {
            it.toUserEntity()
        }
    }
}