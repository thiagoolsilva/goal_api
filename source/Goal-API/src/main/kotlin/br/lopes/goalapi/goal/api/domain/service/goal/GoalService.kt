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

package br.lopes.goalapi.goal.api.domain.service.goal

import br.lopes.goalapi.goal.api.domain.service.goal.mapper.toHistoryEntity
import br.lopes.goalapi.goal.api.domain.service.goal.model.GoalEntity
import br.lopes.goalapi.goal.api.domain.service.goal.model.HistoryEntity
import br.lopes.goalapi.goal.api.domain.service.goal.usecase.GetGoalByIdUC
import br.lopes.goalapi.goal.api.domain.service.goal.usecase.GetGoalHistoryById
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class GoalService  @Autowired constructor(
        private val getGoalByIdUC: GetGoalByIdUC,
        private val getGoalHistoryById: GetGoalHistoryById
): GoalServiceContract {

    override fun findGoalById(id: Long): GoalEntity {
        return getGoalByIdUC.execute(id)
    }

    override fun findGoalHistoryById(params: Map<String, Any>): Page<HistoryEntity> {
        return getGoalHistoryById.execute(params).map { it.toHistoryEntity() }
    }
}