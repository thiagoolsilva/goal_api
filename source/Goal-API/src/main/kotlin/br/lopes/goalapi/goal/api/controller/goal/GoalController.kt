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
import br.lopes.goalapi.goal.api.controller.config.error.ErrorConstants
import br.lopes.goalapi.goal.api.controller.config.error.model.DataNotModified
import br.lopes.goalapi.goal.api.controller.config.error.model.IfMatchNotProvided
import br.lopes.goalapi.goal.api.controller.contract.ApiContract
import br.lopes.goalapi.goal.api.controller.contract.ErrorResponseMessage
import br.lopes.goalapi.goal.api.controller.goal.contract.*
import br.lopes.goalapi.goal.api.controller.goal.error.GoalApiErrorMessages.ErrorMessage.BAD_REQUEST_FOR_IF_MATCH_NOT_PROVIDED
import br.lopes.goalapi.goal.api.controller.goal.error.GoalApiErrorMessages.ErrorMessage.NOT_FOUND_FOR_GOAL_ID
import br.lopes.goalapi.goal.api.controller.goal.error.GoalApiErrorMessages.ErrorMessage.BAD_REQUEST_FOR_INVALID_GOAL_ENTITY
import br.lopes.goalapi.goal.api.controller.goal.error.GoalApiErrorMessages.ErrorMessage.NOT_MODIFIED_FOR_GOAL_DATA
import br.lopes.goalapi.goal.api.controller.goal.error.GoalApiErrorMessages.ErrorMessage.PRE_CONDITION_FAILED_FOR_GOAL_RESOURCE
import br.lopes.goalapi.goal.api.controller.goal.error.model.GoalNotFoundException
import br.lopes.goalapi.goal.api.controller.goal.error.model.GoalPreConditionFailed
import br.lopes.goalapi.goal.api.controller.goal.error.model.InvalidGoalInputException
import br.lopes.goalapi.goal.api.controller.goal.error.model.UpdateGoalNotSupported
import br.lopes.goalapi.goal.api.controller.printError
import mu.KLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping(ApiConstants.Goal.GOAL_PATH)
class GoalController {

    @Autowired
    private lateinit var handler: Handler

    @Autowired
    private lateinit var logger: KLogger

    @GetMapping(
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getGoalById(
        @PathVariable id: Long,
        @RequestHeader(HttpHeaders.IF_NONE_MATCH) ifNoneMatch: String?,
    ): ResponseEntity<ApiContract<GoalResponse>> {
        var apiContract = ApiContract<GoalResponse>(null, null)
        var entityVersion = 0L
        return try {
            handler.getGoalById(id, ifNoneMatch) { body, version ->
                apiContract = body
                entityVersion = version
            }

            ResponseEntity
                .ok()
                .eTag(entityVersion.toString())
                .body(apiContract)
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is DataNotModified -> {
                    apiContract.errorMessage = ErrorResponseMessage(NOT_MODIFIED_FOR_GOAL_DATA.second)
                    ResponseEntity.status(NOT_MODIFIED_FOR_GOAL_DATA.first).body(apiContract)
                }
                else -> {
                    apiContract.errorMessage = ErrorResponseMessage(ErrorConstants.GENERIC_ERROR_MESSAGE)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
                }
            }
        }
    }

    @GetMapping(
        value = ["/{id}/history"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
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
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun saveGoal(
        @RequestBody @Valid saveGoalRequest: SaveGoalRequest,
        bindingResult: BindingResult,
        uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<ApiContract<GoalResponse>> {
        var apiContract = ApiContract<GoalResponse>(null, null)
        return try {
            apiContract = handler.saveGoal(saveGoalRequest, bindingResult)

            val goalID = apiContract.body?.id
            val uri = ApiConstants.Goal.GOAL_PATH
                .plus("/{id}")
                .let {
                    uriComponentsBuilder
                        .path(it)
                        .buildAndExpand(goalID).toUri()
                }
            ResponseEntity.created(uri).body(apiContract)
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is InvalidGoalInputException -> {
                    apiContract.errorMessage = ErrorResponseMessage(BAD_REQUEST_FOR_INVALID_GOAL_ENTITY.second)
                    ResponseEntity.status(BAD_REQUEST_FOR_INVALID_GOAL_ENTITY.first).body(apiContract)
                }
                else -> {
                    apiContract.errorMessage = ErrorResponseMessage(ErrorConstants.GENERIC_ERROR_MESSAGE)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
                }
            }
        }
    }

    @PutMapping(
        value = ["/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateGoal(
        @PathVariable id: Long,
        @RequestBody @Valid updateGoalRequest: UpdateGoalRequest,
        bindingResult: BindingResult,
        @RequestHeader(HttpHeaders.IF_MATCH) ifMatch: String?,
    ): ResponseEntity<ApiContract<GoalResponse>> {
        var apiContract = ApiContract<GoalResponse>(null, null)
        return try {
            apiContract = handler.updateGoal(id, updateGoalRequest, ifMatch, bindingResult)

            return ResponseEntity.ok(apiContract)
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is UpdateGoalNotSupported -> {
                    apiContract.errorMessage = ErrorResponseMessage(NOT_FOUND_FOR_GOAL_ID.second)
                    ResponseEntity.status(NOT_FOUND_FOR_GOAL_ID.first).body(apiContract)
                }
                is InvalidGoalInputException -> {
                    apiContract.errorMessage = ErrorResponseMessage(BAD_REQUEST_FOR_INVALID_GOAL_ENTITY.second)
                    ResponseEntity.status(BAD_REQUEST_FOR_INVALID_GOAL_ENTITY.first).body(apiContract)
                }
                is IfMatchNotProvided -> {
                    apiContract.errorMessage = ErrorResponseMessage(BAD_REQUEST_FOR_IF_MATCH_NOT_PROVIDED.second)
                    ResponseEntity.status(BAD_REQUEST_FOR_IF_MATCH_NOT_PROVIDED.first).body(apiContract)
                }
                is GoalPreConditionFailed -> {
                    apiContract.errorMessage = ErrorResponseMessage(PRE_CONDITION_FAILED_FOR_GOAL_RESOURCE.second)
                    ResponseEntity.status(PRE_CONDITION_FAILED_FOR_GOAL_RESOURCE.first).body(apiContract)
                }
                else -> {
                    apiContract.errorMessage = ErrorResponseMessage(ErrorConstants.GENERIC_ERROR_MESSAGE)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
                }
            }
        }
    }

    @PostMapping(
        value = ["/{id}/history"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun saveGoalHistory(
        @PathVariable id: Long,
        @RequestBody @Valid saveGoalHistoryRequest: SaveGoalHistoryRequest,
        bindingResult: BindingResult,
        uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<ApiContract<GoalHistoryResponse>> {
        var apiContract = ApiContract<GoalHistoryResponse>(null, null)
        var goalHistoryID = 0L
        return try {
            handler.createGoalHistoryById(id, saveGoalHistoryRequest, bindingResult) { body, goalHistoryCreated ->
                apiContract = body
                goalHistoryID = goalHistoryCreated
            }
            val uri = ApiConstants.Goal.GOAL_PATH
                .plus("/$id")
                .plus(ApiConstants.GoalHistory.GOAL_HISTORY_PATH)
                .plus("/{id}")
                .let {
                    uriComponentsBuilder.path(it)
                        .buildAndExpand(goalHistoryID).toUri()
                }

            ResponseEntity.created(uri).body(apiContract)
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is GoalNotFoundException -> {
                    apiContract.errorMessage = ErrorResponseMessage(NOT_FOUND_FOR_GOAL_ID.second)
                    ResponseEntity.status(NOT_FOUND_FOR_GOAL_ID.first).body(apiContract)
                }
                is InvalidGoalInputException -> {
                    apiContract.errorMessage = ErrorResponseMessage(BAD_REQUEST_FOR_INVALID_GOAL_ENTITY.second)
                    ResponseEntity.status(BAD_REQUEST_FOR_INVALID_GOAL_ENTITY.first).body(apiContract)
                }
                else -> {
                    apiContract.errorMessage = ErrorResponseMessage(ErrorConstants.GENERIC_ERROR_MESSAGE)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
                }
            }
        }
    }

    @DeleteMapping(
        value = ["/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun deleteGoal(@PathVariable id: Long): ResponseEntity<Unit> {
        return try {
            handler.deleteGoalById(id)
            ResponseEntity.ok().build()
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is GoalNotFoundException -> ResponseEntity.status(NOT_FOUND_FOR_GOAL_ID.first).build()
                else ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }
        }
    }
}