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

package br.lopes.goalapi.goal.api.controller.user

import br.lopes.goalapi.goal.api.controller.ApiConstants
import br.lopes.goalapi.goal.api.controller.ErrorConstants.ApiError.GENERIC_ERROR_MESSAGE
import br.lopes.goalapi.goal.api.controller.contract.ApiContract
import br.lopes.goalapi.goal.api.controller.contract.ErrorResponseMessage
import br.lopes.goalapi.goal.api.controller.printError
import br.lopes.goalapi.goal.api.controller.user.contract.UpdateUserRequest
import br.lopes.goalapi.goal.api.controller.user.contract.UserRequest
import br.lopes.goalapi.goal.api.controller.user.contract.UserResponseDetails
import br.lopes.goalapi.goal.api.controller.user.error.UserApiErrorMessages.ErrorMessage.DUPLICATED_USER_ENTITY
import br.lopes.goalapi.goal.api.controller.user.error.UserApiErrorMessages.ErrorMessage.INVALID_USER_ENTITY
import br.lopes.goalapi.goal.api.controller.user.error.UserApiErrorMessages.ErrorMessage.USER_NOT_FOUND
import br.lopes.goalapi.goal.api.controller.user.error.UserApiErrorMessages.ErrorMessage.USER_NOT_UPDATED
import br.lopes.goalapi.goal.api.controller.user.error.model.DuplicatedUserException
import br.lopes.goalapi.goal.api.controller.user.error.model.UserInputNotValid
import br.lopes.goalapi.goal.api.controller.user.error.model.UserNotFound
import mu.KLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping(ApiConstants.User.USER_PATH)
class UserController {

    @Autowired
    private lateinit var handler: Handler

    @Autowired
    private lateinit var logger: KLogger

    @PostMapping
    fun saveUser(
        @Valid @RequestBody userRequest: UserRequest,
        bindingResult: BindingResult,
        uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<ApiContract<UserResponseDetails>> {
        var apiContract = ApiContract<UserResponseDetails>(null, null)
        return try {
            apiContract = handler.saveUser(userRequest, bindingResult)
            val userId = apiContract.body?.id
            val uri = uriComponentsBuilder.path(ApiConstants.User.USER_PATH + "/{id}").buildAndExpand(userId).toUri()

            ResponseEntity.created(uri).body(apiContract)
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is UserInputNotValid -> {
                    apiContract.errorMessage = ErrorResponseMessage(INVALID_USER_ENTITY.second)
                    ResponseEntity.status(INVALID_USER_ENTITY.first).body(apiContract)
                }
                is DuplicatedUserException -> {
                    apiContract.errorMessage = ErrorResponseMessage(DUPLICATED_USER_ENTITY.second)
                    ResponseEntity.status(DUPLICATED_USER_ENTITY.first).body(apiContract)
                }
                else -> {
                    apiContract.errorMessage = ErrorResponseMessage(GENERIC_ERROR_MESSAGE)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
                }
            }
        }
    }

    @PutMapping
    fun updateUser(
        @Valid @RequestBody userRequest: UpdateUserRequest,
        bindingResult: BindingResult
    ): ResponseEntity<ApiContract<UserResponseDetails>> {
        var apiContract = ApiContract<UserResponseDetails>(null, null)
        return try {
            apiContract = handler.updateUser(userRequest, bindingResult)

            ResponseEntity.ok(apiContract)
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is UserNotFound -> {
                    apiContract.errorMessage = ErrorResponseMessage(USER_NOT_UPDATED.second)
                    ResponseEntity.status(USER_NOT_UPDATED.first).body(apiContract)
                }
                is UserInputNotValid -> {
                    apiContract.errorMessage = ErrorResponseMessage(INVALID_USER_ENTITY.second)
                    ResponseEntity.status(INVALID_USER_ENTITY.first).body(apiContract)
                }
                else -> {
                    apiContract.errorMessage = ErrorResponseMessage(GENERIC_ERROR_MESSAGE)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
                }
            }
        }
    }

    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: Long,
    ): ResponseEntity<ApiContract<UserResponseDetails>> {
        var apiContract = ApiContract<UserResponseDetails>(null, null)
        return try {
            apiContract = handler.getUserById(id)

            ResponseEntity.ok(apiContract)
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is UserNotFound -> {
                    apiContract.errorMessage = ErrorResponseMessage(USER_NOT_FOUND.second)
                    ResponseEntity.status(USER_NOT_FOUND.first).body(apiContract)
                }
                else -> {
                    apiContract.errorMessage = ErrorResponseMessage(GENERIC_ERROR_MESSAGE)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
                }
            }
        }
    }

    @GetMapping
    fun getUsers(
        @RequestParam(required = false) nickname: String?,
        @Valid pageable: Pageable,
    ): ResponseEntity<ApiContract<Page<UserResponseDetails>>> {
        var apiContract = ApiContract<Page<UserResponseDetails>>(null, null)
        return try {
            apiContract = handler.getUserByQuery(nickname, pageable)

            ResponseEntity.ok().body(apiContract)
        } catch (error: Exception) {
            error.printError(logger)

            apiContract.errorMessage = ErrorResponseMessage(GENERIC_ERROR_MESSAGE)
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
        }
    }
}