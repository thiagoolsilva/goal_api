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

package br.lopes.goalapi.goal.api.domain.service.goal.mapper

import br.lopes.goalapi.goal.api.data.entity.Goal
import br.lopes.goalapi.goal.api.data.entity.History
import br.lopes.goalapi.goal.api.data.entity.User
import br.lopes.goalapi.goal.api.domain.service.goal.model.GoalEntity
import br.lopes.goalapi.goal.api.domain.service.goal.model.GoalHistoryEntity
import br.lopes.goalapi.goal.api.domain.service.history.model.HistoryEntity
import java.time.LocalDateTime

fun Goal.toGoalEntity() = GoalEntity(
        id = this.id,
        title = this.title,
        description = this.description,
        totalPrice = this.totalPrice,
        dtEndGoal = this.dtEndGoal,
        userId = this.user.id ?: -1
)

fun History.toHistoryEntity() = GoalHistoryEntity(
        goalId = this.goal.id,
        id = this.id,
        dtEvent = this.dtEvent,
        value = this.value
)

fun GoalHistoryEntity.toHistoryDb(goal:Goal) = History(
        id = this.id,
        dtEvent = this.dtEvent,
        value = this.value,
        goal = goal
)

fun GoalEntity.toGoalDb(user: User) = Goal(
        user = user,
        id = this.id,
        title = this.title,
        description = this.description,
        totalPrice = this.totalPrice,
        dtEndGoal = this.dtEndGoal,
        dtCreate = LocalDateTime.now(),
        dtUpdate = LocalDateTime.now()
)

fun HistoryEntity.toGoalHistory() = GoalHistoryEntity(
        goalId = this.goalId,
        id = this.id,
        dtEvent = this.dtEvent,
        value = this.value
)

