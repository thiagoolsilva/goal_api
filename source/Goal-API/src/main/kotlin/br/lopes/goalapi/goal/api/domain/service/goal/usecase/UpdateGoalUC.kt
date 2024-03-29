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

import br.lopes.goalapi.goal.api.controller.goal.error.model.GoalNotFoundException
import br.lopes.goalapi.goal.api.controller.goal.error.model.GoalPreConditionFailed
import br.lopes.goalapi.goal.api.data.entity.Goal
import br.lopes.goalapi.goal.api.data.repository.GoalRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.UseCaseContract
import br.lopes.goalapi.goal.api.domain.service.goal.model.GoalEntity
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

class UpdateGoalUC @Autowired constructor(
    private val goalRepositoryContract: GoalRepositoryContract
) : UseCaseContract<GoalEntity, Goal> {

    override fun execute(input: GoalEntity): Goal {
        val goalEntityId = input.id ?: 0
        val goalId = goalRepositoryContract.findById(goalEntityId)
        if (goalId.isPresent) {
            val goalDb = goalId.get()

            if(input.entityVersion != goalDb.version) {
                throw GoalPreConditionFailed("the provided if-match is not the same as expected in database.")
            }

            goalDb.description = input.description
            goalDb.dtEndGoal = input.dtEndGoal
            goalDb.title = input.title
            goalDb.dtUpdate = LocalDateTime.now()

            return goalRepositoryContract.save(goalDb)
        } else {
            throw GoalNotFoundException("Goal id not found")
        }
    }
}