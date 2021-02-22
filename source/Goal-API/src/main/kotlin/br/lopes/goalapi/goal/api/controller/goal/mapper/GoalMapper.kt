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
import br.lopes.goalapi.goal.api.domain.service.goal.model.GoalEntity
import br.lopes.goalapi.goal.api.domain.service.goal.model.HistoryEntity

fun GoalEntity.toGoalResponse() = GoalResponse(
        title = this.title,
        description = this.description,
        totalPrice = this.totalPrice,
        dtEndGoal = this.dtEndGoal
)

fun HistoryEntity.toGoalHistoryResponse() = GoalHistoryResponse(
        dtEvent = this.dtEvent,
        value = this.value
)