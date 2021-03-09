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

package br.lopes.goalapi.goal.api.domain.service.user.usecase

import br.lopes.goalapi.goal.api.controller.user.error.model.UserNotFound
import br.lopes.goalapi.goal.api.data.entity.User
import br.lopes.goalapi.goal.api.data.repository.UserRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.UseCaseContract
import br.lopes.goalapi.goal.api.domain.service.user.mapper.toUserDb
import br.lopes.goalapi.goal.api.domain.service.user.model.UserEntity
import org.springframework.beans.factory.annotation.Autowired

class UpdateUserUC @Autowired constructor(
        private val userRepositoryContract: UserRepositoryContract
): UseCaseContract<UserEntity, User> {

    override fun execute(input: UserEntity): User {
        val userDb = input.toUserDb()

        if(!userRepositoryContract.existsById(userDb.id)) {
           throw UserNotFound("invalid user id")
        }
        return userRepositoryContract.save(userDb)
    }
}