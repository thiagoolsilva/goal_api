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

package br.lopes.goalapi.goal.api.controller.goal

import br.lopes.goalapi.goal.api.controller.contract.ApiContract
import br.lopes.goalapi.goal.api.controller.goal.contract.GoalHistoryResponse
import br.lopes.goalapi.goal.api.controller.goal.contract.GoalResponse
import br.lopes.goalapi.goal.api.controller.goal.mapper.toGoalHistoryResponse
import br.lopes.goalapi.goal.api.controller.goal.mapper.toGoalResponse
import br.lopes.goalapi.goal.api.domain.service.goal.GoalConstants
import br.lopes.goalapi.goal.api.domain.service.goal.GoalServiceContract
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class Handler constructor(
       private val goalServiceContract: GoalServiceContract
) {
    fun getGoalById(id: Long): ApiContract<GoalResponse> {
        val response = ApiContract<GoalResponse>(null, null)
        response.body = goalServiceContract.findGoalById(id).toGoalResponse()

        return response
    }

    fun getGoalHistoryById(id: Long, pageable: Pageable): ApiContract<Page<GoalHistoryResponse>> {
        val apiContract = ApiContract<Page<GoalHistoryResponse>>(null, null)

        val params = mapOf(GoalConstants.PARAMS.QUERY_ID_PARAM to id,
                GoalConstants.PARAMS.QUERY_PAGEABLE_PARAM to pageable)
        apiContract.body = goalServiceContract.findGoalHistoryById(params).map { it.toGoalHistoryResponse() }

        return apiContract
    }
}