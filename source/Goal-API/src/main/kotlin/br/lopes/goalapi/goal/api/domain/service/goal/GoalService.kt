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

import br.lopes.goalapi.goal.api.domain.service.goal.mapper.toGoalEntity
import br.lopes.goalapi.goal.api.domain.service.goal.mapper.toGoalHistoryEntity
import br.lopes.goalapi.goal.api.domain.service.goal.model.GoalEntity
import br.lopes.goalapi.goal.api.domain.service.goal.model.GoalHistoryEntity
import br.lopes.goalapi.goal.api.domain.service.goal.usecase.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class GoalService @Autowired constructor(
        private val useCases: Map<String, Any>
) : GoalServiceContract {

    override fun findGoalById(id: Long): GoalEntity {
        val getGoalByIdUC = useCases[GetGoalByIdUC::class.toString()] as GetGoalByIdUC
        return getGoalByIdUC.execute(id)
    }

    override fun findGoalHistoryById(params: Map<String, Any>): Page<GoalHistoryEntity> {
        val getGoalHistoryById = useCases[GetGoalHistoryById::class.toString()] as GetGoalHistoryById
        return getGoalHistoryById.execute(params).map { it.toGoalHistoryEntity() }
    }

    @Transactional
    override fun updateGoal(goalEntity: GoalEntity): GoalEntity {
        val updateGoalUC = useCases[UpdateGoalUC::class.toString()] as UpdateGoalUC
        return updateGoalUC.execute(goalEntity).toGoalEntity()
    }

    @Transactional
    override fun saveGoal(goalEntity: GoalEntity): GoalEntity {
        val saveGoalUC = useCases[SaveGoalUC::class.toString()] as SaveGoalUC
        return saveGoalUC.execute(goalEntity).toGoalEntity()
    }

    override fun deleteById(id: Long) {
        val deleteGoalUC = useCases[DeleteGoalUC::class.toString()] as DeleteGoalUC
        return deleteGoalUC.execute(id)
    }
}