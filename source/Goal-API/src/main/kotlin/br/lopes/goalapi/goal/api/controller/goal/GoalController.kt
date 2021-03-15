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
import br.lopes.goalapi.goal.api.controller.goal.error.GoalApiErrorMessages.ErrorMessage.INVALID_GOAL_ENTITY
import br.lopes.goalapi.goal.api.controller.goal.error.model.InvalidGoalInputException
import br.lopes.goalapi.goal.api.controller.printError
import mu.KLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
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

    @GetMapping("/{id}")
    fun getGoalById(
            @PathVariable id: Long
    ): ResponseEntity<ApiContract<GoalResponse>> {
        var apiContract = ApiContract<GoalResponse>(null, null)
        try {
            apiContract = handler.getGoalById(id)

            return ResponseEntity.ok(apiContract)
        } catch (error: Exception) {
            apiContract.errorMessage = ErrorResponseMessage("unexpected error")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
        }
    }

    @GetMapping("/{id}/history")
    fun getGoalHistory(
            @PathVariable id: Long,
            @Valid pageable: Pageable
    ): ResponseEntity<ApiContract<Page<GoalHistoryResponse>>> {
        var apiContract = ApiContract<Page<GoalHistoryResponse>>(null, null)
        try {
            apiContract = handler.getGoalHistoryById(id, pageable)

            return ResponseEntity.ok(apiContract)
        } catch (error: Exception) {
            apiContract.errorMessage = ErrorResponseMessage("unexpected error")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
        }
    }

    @PostMapping
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
        } catch (error:Exception) {
            error.printError(logger)

            when(error) {
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

    @PutMapping
    fun updateGoal(
        @RequestBody @Valid updateGoalRequest: UpdateGoalRequest,
        bindingResult: BindingResult,
    ): ResponseEntity<ApiContract<GoalResponse>> {
        var apiContract = ApiContract<GoalResponse>(null, null)
        return try {
            apiContract = handler.updateGoal(updateGoalRequest, bindingResult)

            return ResponseEntity.ok(apiContract)
        }  catch (error:Exception) {
            when(error) {
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

    @PostMapping("/{id}/history")
    @Transactional
    fun createGoalHistory(
            @PathVariable id: Long,
            @RequestBody @Valid saveGoalHistoryRequest: SaveGoalHistoryRequest
    ): ResponseEntity<ApiContract<GoalHistoryResponse>> {
        var apiContract = ApiContract<GoalHistoryResponse>(null, null)
        try {
            apiContract = handler.createGoalHistoryById(id, saveGoalHistoryRequest)

            return ResponseEntity.ok(apiContract)
        } catch (error: Exception) {
            when(error) {
                is EntityNotFoundException ->  apiContract.errorMessage = ErrorResponseMessage("")
                else -> apiContract.errorMessage = ErrorResponseMessage("unexpected error")
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    fun deleteGoal(@PathVariable id: Long) :ResponseEntity<Unit> {
        try {
            handler.deleteGoalById(id)
            return ResponseEntity.ok().build()
        } catch (error: Exception) {
            error.printError(logger)

            return when(error) {
                is UnexpectedRollbackException,
                is EmptyResultDataAccessException -> ResponseEntity.notFound().build()
                else -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
            }
        }
    }
}