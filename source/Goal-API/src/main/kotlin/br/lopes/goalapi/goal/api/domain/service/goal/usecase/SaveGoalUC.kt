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

package br.lopes.goalapi.goal.api.domain.service.goal.usecase

import br.lopes.goalapi.goal.api.data.entity.Goal
import br.lopes.goalapi.goal.api.data.repository.GoalRepositoryContract
import br.lopes.goalapi.goal.api.data.repository.UserRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.UseCaseContract
import br.lopes.goalapi.goal.api.domain.service.goal.mapper.toGoalDb
import br.lopes.goalapi.goal.api.domain.service.goal.model.GoalEntity
import org.springframework.beans.factory.annotation.Autowired
import java.lang.IllegalArgumentException

class SaveGoalUC @Autowired constructor(
        private val goalRepositoryContract: GoalRepositoryContract,
        private val userRepositoryContract: UserRepositoryContract
): UseCaseContract<GoalEntity, Goal> {

    override fun execute(input: GoalEntity): Goal {
        val goalDb:Goal
        if(input.id == null) {
            val userDb = userRepositoryContract.getOne(input.userId)

            goalDb = input.toGoalDb()
            goalDb.customer = userDb

            return goalRepositoryContract.save(goalDb)
        } else {
            throw IllegalArgumentException("This operation update is not supported on SaveGoalUC")
        }
    }
}