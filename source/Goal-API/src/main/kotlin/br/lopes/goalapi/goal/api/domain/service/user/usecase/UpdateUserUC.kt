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

import br.lopes.goalapi.goal.api.controller.user.error.model.UserPreConditionFailed
import br.lopes.goalapi.goal.api.controller.user.error.model.UserNotFound
import br.lopes.goalapi.goal.api.data.entity.Customer
import br.lopes.goalapi.goal.api.data.repository.UserRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.UseCaseContract
import br.lopes.goalapi.goal.api.domain.service.user.model.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

class UpdateUserUC @Autowired constructor(
        private val userRepositoryContract: UserRepositoryContract
): UseCaseContract<UserEntity, Customer> {

    override fun execute(input: UserEntity): Customer {
        val userId = input.id ?: 0
        val userDb = userRepositoryContract.findById(userId)
        if(userDb.isPresent) {
            val updateUserInfo = userDb.get()

            if(input.entityVersion != updateUserInfo.version) {
                throw UserPreConditionFailed("the provided if-match is not the same as expected in database.")
            }

            updateUserInfo.dtUpdate = LocalDateTime.now()
            updateUserInfo.name = input.name
            updateUserInfo.nickname = input.nickname

            return userRepositoryContract.save(updateUserInfo)
        } else {
            throw UserNotFound("invalid user id")
        }
    }
}