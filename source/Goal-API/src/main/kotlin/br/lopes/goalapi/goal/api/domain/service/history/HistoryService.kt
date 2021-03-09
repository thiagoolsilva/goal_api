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

package br.lopes.goalapi.goal.api.domain.service.history

import br.lopes.goalapi.goal.api.domain.service.goal.mapper.toHistoryEntity
import br.lopes.goalapi.goal.api.domain.service.history.model.HistoryEntity
import br.lopes.goalapi.goal.api.domain.service.history.usecases.SaveHistoryUC
import org.springframework.stereotype.Service

@Service
class HistoryService constructor(
        private val useCases: Map<String, Any>
): HistoryServiceContract {
    override fun saveGoalHistoryById(historyEntity: HistoryEntity): HistoryEntity {
        val saveHistoryUC = useCases[SaveHistoryUC::class.toString()] as SaveHistoryUC
        val content = mapOf(
                GoalHistoryContsants.SaveParams.GOAL_ID to historyEntity.goalId,
                GoalHistoryContsants.SaveParams.HISTORY_CONTENT to historyEntity
        )
        return saveHistoryUC.execute(content).toHistoryEntity()
    }
}