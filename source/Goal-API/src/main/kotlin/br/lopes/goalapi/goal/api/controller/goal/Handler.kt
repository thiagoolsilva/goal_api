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
import br.lopes.goalapi.goal.api.controller.goal.contract.*
import br.lopes.goalapi.goal.api.controller.goal.error.model.GoalNotFoundException
import br.lopes.goalapi.goal.api.controller.goal.error.model.InvalidGoalInputException
import br.lopes.goalapi.goal.api.controller.goal.mapper.toGoalEntity
import br.lopes.goalapi.goal.api.controller.goal.mapper.toGoalHistoryResponse
import br.lopes.goalapi.goal.api.controller.goal.mapper.toGoalResponse
import br.lopes.goalapi.goal.api.controller.config.error.handleUserInputErrors
import br.lopes.goalapi.goal.api.controller.config.error.model.IfMatchNotProvided
import br.lopes.goalapi.goal.api.controller.config.error.model.DataNotModified
import br.lopes.goalapi.goal.api.domain.service.goal.GoalConstants
import br.lopes.goalapi.goal.api.domain.service.goal.GoalServiceContract
import br.lopes.goalapi.goal.api.domain.service.history.HistoryServiceContract
import br.lopes.goalapi.goal.api.domain.service.history.mapper.toGoalHistoryResponse
import br.lopes.goalapi.goal.api.domain.service.history.mapper.toHistoryEntity
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.validation.BindingResult
import javax.persistence.EntityNotFoundException

class Handler constructor(
    private val goalServiceContract: GoalServiceContract,
    private val historyService: HistoryServiceContract
) {
    fun getGoalById(id: Long,
                    ifNoneMatch:String?,
          onGoalIdFinished: (body: ApiContract<GoalResponse>, entityVersion:Long) -> Unit)  {
        try {
            val response = ApiContract<GoalResponse>(null, null)
            val goalDb = goalServiceContract.findGoalById(id)

            if (ifNoneMatch != null && ifNoneMatch.isNotEmpty() && goalDb.entityVersion == ifNoneMatch.toLong()) {
                throw DataNotModified("Goal data not modified.")
            }
            response.body = goalDb.toGoalResponse()

            val entityVersion = goalDb.entityVersion ?: 0

            onGoalIdFinished(response, entityVersion)
        } catch (entityNotFoundException: EntityNotFoundException) {
            throw GoalNotFoundException("Goal not found", entityNotFoundException)
        }
    }

    fun getGoalHistoryById(id: Long, pageable: Pageable): ApiContract<Page<GoalHistoryResponse>> {
        val apiContract = ApiContract<Page<GoalHistoryResponse>>(null, null)

        val params = mapOf(
            GoalConstants.PARAMS.QUERY_ID_PARAM to id,
            GoalConstants.PARAMS.QUERY_PAGEABLE_PARAM to pageable
        )
        apiContract.body = goalServiceContract.findGoalHistoryById(params).map { it.toGoalHistoryResponse() }

        return apiContract
    }

    fun createGoalHistoryById(
        id: Long,
        history: SaveGoalHistoryRequest,
        bindingResult: BindingResult,
        onHandleFinished: (body:ApiContract<GoalHistoryResponse>, goalHistoryCreated: Long ) -> Unit
    ) {
        if (bindingResult.hasErrors()) {
            throw InvalidGoalInputException(handleUserInputErrors(bindingResult))
        }
        try {
            val apiContract = ApiContract<GoalHistoryResponse>(null, null)

            val historyEntity = history.toHistoryEntity()
            historyEntity.goalId = id

            val body = historyService.saveGoalHistoryById(historyEntity)
            apiContract.body = body.toGoalHistoryResponse()

            val goalHistoryId = body.id ?: 0

            onHandleFinished(apiContract, goalHistoryId)
        } catch (entityNotFoundException: EntityNotFoundException) {
            throw GoalNotFoundException("Goal not found", entityNotFoundException)
        }
    }

    fun saveGoal(saveGoalRequest: SaveGoalRequest, bindingResult: BindingResult): ApiContract<GoalResponse> {
        if (bindingResult.hasErrors()) {
            throw InvalidGoalInputException(handleUserInputErrors(bindingResult))
        }

        val apiContract = ApiContract<GoalResponse>(null, null)

        val goalEntity = saveGoalRequest.toGoalEntity()
        apiContract.body = goalServiceContract.saveGoal(goalEntity).toGoalResponse()

        return apiContract
    }

    fun updateGoal(goalId:Long,
                   updateGoalRequest: UpdateGoalRequest,
                   entityVersion: String?,
                   bindingResult: BindingResult): ApiContract<GoalResponse> {
        if (bindingResult.hasErrors()) {
            throw InvalidGoalInputException(handleUserInputErrors(bindingResult))
        }

        entityVersion?.let {
            val apiContract = ApiContract<GoalResponse>(null, null)
            val goalEntity = updateGoalRequest.toGoalEntity(entityVersion.toLong(), goalId)
            apiContract.body = goalServiceContract.updateGoal(goalEntity).toGoalResponse()

            return apiContract
        } ?: kotlin.run {
            throw IfMatchNotProvided("The header If-Match was not provided.")
        }
    }

    fun deleteGoalById(id: Long) {
        try {
            goalServiceContract.deleteById(id)
        } catch (emptyResultDataAccessException: EmptyResultDataAccessException) {
            throw GoalNotFoundException("goal not found", emptyResultDataAccessException)
        }
    }
}