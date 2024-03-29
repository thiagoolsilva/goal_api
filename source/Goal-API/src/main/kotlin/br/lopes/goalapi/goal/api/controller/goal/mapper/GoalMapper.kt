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

package br.lopes.goalapi.goal.api.controller.goal.mapper

import br.lopes.goalapi.goal.api.controller.goal.contract.GoalHistoryResponse
import br.lopes.goalapi.goal.api.controller.goal.contract.GoalResponse
import br.lopes.goalapi.goal.api.controller.goal.contract.SaveGoalRequest
import br.lopes.goalapi.goal.api.controller.goal.contract.UpdateGoalRequest
import br.lopes.goalapi.goal.api.domain.service.goal.model.GoalEntity
import br.lopes.goalapi.goal.api.domain.service.goal.model.GoalHistoryEntity
import java.time.LocalDateTime

fun GoalEntity.toGoalResponse() = GoalResponse(
    id = this.id ?: 0,
    title = this.title,
    description = this.description,
    totalPrice = this.totalPrice,
    amount = this.amount,
    dtEndGoal = this.dtEndGoal
)

fun GoalHistoryEntity.toGoalHistoryResponse() = GoalHistoryResponse(
    dtEvent = this.dtEvent,
    value = this.value
)

fun SaveGoalRequest.toGoalEntity() = GoalEntity(
    title = this.title,
    description = this.description,
    totalPrice = this.totalPrice,
    dtEndGoal = LocalDateTime.parse(this.dtEndGoal),
    id = null,
    userId = this.userId,
    amount = 0.0,
    entityVersion = null
)

fun UpdateGoalRequest.toGoalEntity(entityVersion:Long, goalId:Long) = GoalEntity(
    title = this.title,
    description = this.description,
    totalPrice = 0.0,
    dtEndGoal = LocalDateTime.parse(this.dtEndGoal),
    id = goalId,
    userId = 0,
    amount = 0.0,
    entityVersion = entityVersion
)