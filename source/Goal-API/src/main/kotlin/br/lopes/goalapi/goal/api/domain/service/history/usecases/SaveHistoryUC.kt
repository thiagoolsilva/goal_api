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

package br.lopes.goalapi.goal.api.domain.service.history.usecases

import br.lopes.goalapi.goal.api.data.entity.History
import br.lopes.goalapi.goal.api.data.repository.GoalRepositoryContract
import br.lopes.goalapi.goal.api.data.repository.HistoryRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.UseCaseContract
import br.lopes.goalapi.goal.api.domain.service.history.GoalHistoryContsants
import br.lopes.goalapi.goal.api.domain.service.history.mapper.toHistory
import br.lopes.goalapi.goal.api.domain.service.history.model.HistoryEntity
import org.springframework.beans.factory.annotation.Autowired

class SaveHistoryUC @Autowired constructor(
        private val goalRepositoryContract: GoalRepositoryContract,
        private val goalHistoryRepositoryContract: HistoryRepositoryContract
) : UseCaseContract<Map<String, Any>, History> {
    override fun execute(input: Map<String, Any>): History {
        val goalId = input[GoalHistoryContsants.SaveParams.GOAL_ID] as Long
        val historyEntity = input[GoalHistoryContsants.SaveParams.HISTORY_CONTENT] as HistoryEntity

        val goalDb = goalRepositoryContract.getOne(goalId)

        val historyDb = historyEntity.toHistory()
        historyDb.goal = goalDb
        goalDb.history.add(historyDb)

        return goalHistoryRepositoryContract.save(historyDb)
    }

}