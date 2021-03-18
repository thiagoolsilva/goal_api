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

import br.lopes.goalapi.goal.api.controller.ApiConstants
import br.lopes.goalapi.goal.api.controller.ErrorConstants
import br.lopes.goalapi.goal.api.controller.contract.ApiContract
import br.lopes.goalapi.goal.api.controller.contract.ErrorResponseMessage
import br.lopes.goalapi.goal.api.controller.goal.contract.*
import br.lopes.goalapi.goal.api.controller.goal.error.GoalApiErrorMessages.ErrorMessage.GOAL_NOT_FOUND
import br.lopes.goalapi.goal.api.controller.goal.error.GoalApiErrorMessages.ErrorMessage.INVALID_GOAL_ENTITY
import br.lopes.goalapi.goal.api.controller.goal.error.model.GoalNotFoundException
import br.lopes.goalapi.goal.api.controller.goal.error.model.InvalidGoalInputException
import br.lopes.goalapi.goal.api.controller.goal.error.model.UpdateGoalNotSupported
import br.lopes.goalapi.goal.api.controller.printError
import mu.KLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.transaction.UnexpectedRollbackException
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
@RequestMapping(ApiConstants.Goal.GOAL_PATH)
class GoalController {

    @Autowired
    private lateinit var handler: Handler

    @Autowired
    private lateinit var logger: KLogger

    @GetMapping(value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getGoalById(
        @PathVariable id: Long
    ): ResponseEntity<ApiContract<GoalResponse>> {
        var apiContract = ApiContract<GoalResponse>(null, null)
        return try {
            apiContract = handler.getGoalById(id)

            ResponseEntity.ok(apiContract)
        } catch (error: Exception) {
            apiContract.errorMessage = ErrorResponseMessage(ErrorConstants.GENERIC_ERROR_MESSAGE)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
        }
    }

    @GetMapping(value = ["/{id}/history"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getGoalHistory(
        @PathVariable id: Long,
        @Valid pageable: Pageable
    ): ResponseEntity<ApiContract<Page<GoalHistoryResponse>>> {
        var apiContract = ApiContract<Page<GoalHistoryResponse>>(null, null)
        return try {
            apiContract = handler.getGoalHistoryById(id, pageable)

            return ResponseEntity.ok(apiContract)
        } catch (error: Exception) {
            apiContract.errorMessage = ErrorResponseMessage(ErrorConstants.GENERIC_ERROR_MESSAGE)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
        }
    }

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun saveGoal(
        @RequestBody @Valid saveGoalRequest: SaveGoalRequest,
        bindingResult: BindingResult,
        uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<ApiContract<GoalResponse>> {
        var apiContract = ApiContract<GoalResponse>(null, null)
        return try {
            apiContract = handler.saveGoal(saveGoalRequest, bindingResult)

            val goalID = apiContract.body?.id
            val uri = uriComponentsBuilder.path(ApiConstants.Goal.GOAL_PATH + "/{id}").buildAndExpand(goalID).toUri()

            ResponseEntity.created(uri).body(apiContract)
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is InvalidGoalInputException -> {
                    apiContract.errorMessage = ErrorResponseMessage(INVALID_GOAL_ENTITY.second)
                    ResponseEntity.status(INVALID_GOAL_ENTITY.first).body(apiContract)
                }
                else -> {
                    apiContract.errorMessage = ErrorResponseMessage(ErrorConstants.GENERIC_ERROR_MESSAGE)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
                }
            }
        }
    }

    @PutMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateGoal(
        @RequestBody @Valid updateGoalRequest: UpdateGoalRequest,
        bindingResult: BindingResult
    ): ResponseEntity<ApiContract<GoalResponse>> {
        var apiContract = ApiContract<GoalResponse>(null, null)
        return try {
            apiContract = handler.updateGoal(updateGoalRequest, bindingResult)

            return ResponseEntity.ok(apiContract)
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is UpdateGoalNotSupported -> {
                    apiContract.errorMessage = ErrorResponseMessage(GOAL_NOT_FOUND.second)
                    ResponseEntity.status(GOAL_NOT_FOUND.first).body(apiContract)
                }
                is InvalidGoalInputException -> {
                    apiContract.errorMessage = ErrorResponseMessage(INVALID_GOAL_ENTITY.second)
                    ResponseEntity.status(INVALID_GOAL_ENTITY.first).body(apiContract)
                }
                else -> {
                    apiContract.errorMessage = ErrorResponseMessage(ErrorConstants.GENERIC_ERROR_MESSAGE)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
                }
            }
        }
    }

    @PostMapping(value = ["/{id}/history"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE])
    fun createGoalHistory(
        @PathVariable id: Long,
        @RequestBody @Valid saveGoalHistoryRequest: SaveGoalHistoryRequest,
        bindingResult: BindingResult
    ): ResponseEntity<ApiContract<GoalHistoryResponse>> {
        var apiContract = ApiContract<GoalHistoryResponse>(null, null)
        return try {
            apiContract = handler.createGoalHistoryById(id, saveGoalHistoryRequest, bindingResult)

            return ResponseEntity.ok(apiContract)
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is GoalNotFoundException -> {
                    apiContract.errorMessage = ErrorResponseMessage(GOAL_NOT_FOUND.second)
                    ResponseEntity.status(GOAL_NOT_FOUND.first).body(apiContract)
                }
                is InvalidGoalInputException -> {
                    apiContract.errorMessage = ErrorResponseMessage(INVALID_GOAL_ENTITY.second)
                    ResponseEntity.status(INVALID_GOAL_ENTITY.first).body(apiContract)
                }
                else -> {
                    apiContract.errorMessage = ErrorResponseMessage(ErrorConstants.GENERIC_ERROR_MESSAGE)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
                }
            }
        }
    }

    @DeleteMapping(value = ["/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteGoal(@PathVariable id: Long): ResponseEntity<Unit> {
        return try {
            handler.deleteGoalById(id)
            ResponseEntity.ok().build()
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is GoalNotFoundException -> ResponseEntity.status(GOAL_NOT_FOUND.first).build()
                else ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }
        }
    }
}