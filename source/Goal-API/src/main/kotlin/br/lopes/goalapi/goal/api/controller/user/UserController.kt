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
import br.lopes.goalapi.goal.api.controller.config.error.ErrorConstants.ApiError.GENERIC_ERROR_MESSAGE
import br.lopes.goalapi.goal.api.controller.contract.ApiContract
import br.lopes.goalapi.goal.api.controller.contract.ErrorResponseMessage
import br.lopes.goalapi.goal.api.controller.printError
import br.lopes.goalapi.goal.api.controller.user.contract.UpdateUserRequest
import br.lopes.goalapi.goal.api.controller.user.contract.UserRequest
import br.lopes.goalapi.goal.api.controller.user.contract.UserResponseDetails
import br.lopes.goalapi.goal.api.controller.user.error.UserApiErrorMessages.ErrorMessage.BAD_REQUEST_FOR_IF_MATCH_NOT_PROVIDED
import br.lopes.goalapi.goal.api.controller.user.error.UserApiErrorMessages.ErrorMessage.BAD_REQUEST_FOR_INVALID_USER_ENTITY
import br.lopes.goalapi.goal.api.controller.user.error.UserApiErrorMessages.ErrorMessage.FORBIDDEN_FOR_DUPLICATED_USER_ENTITY
import br.lopes.goalapi.goal.api.controller.user.error.UserApiErrorMessages.ErrorMessage.FORBIDDEN_FOR_USER_NOT_UPDATED
import br.lopes.goalapi.goal.api.controller.user.error.UserApiErrorMessages.ErrorMessage.NOT_MODIFIED_FOR_USER_DATA
import br.lopes.goalapi.goal.api.controller.user.error.UserApiErrorMessages.ErrorMessage.NO_CONTENT_FOR_USER_NOT_FOUND
import br.lopes.goalapi.goal.api.controller.user.error.UserApiErrorMessages.ErrorMessage.PRE_CONDITION_FAILED_FOR_USER_RESOURCE
import br.lopes.goalapi.goal.api.controller.user.error.model.*
import mu.KLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders.IF_MATCH
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
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
                    apiContract.errorMessage = ErrorResponseMessage(BAD_REQUEST_FOR_INVALID_USER_ENTITY.second)
                    ResponseEntity.status(BAD_REQUEST_FOR_INVALID_USER_ENTITY.first).body(apiContract)
                }
                is DuplicatedUserException -> {
                    apiContract.errorMessage = ErrorResponseMessage(FORBIDDEN_FOR_DUPLICATED_USER_ENTITY.second)
                    ResponseEntity.status(FORBIDDEN_FOR_DUPLICATED_USER_ENTITY.first).body(apiContract)
                }
                else -> {
                    apiContract.errorMessage = ErrorResponseMessage(GENERIC_ERROR_MESSAGE)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
                }
            }
        }
    }

    @PutMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateUser(
        @Valid @RequestBody userRequest: UpdateUserRequest,
        bindingResult: BindingResult,
        @RequestHeader(IF_MATCH) ifMatch:String?,
    ): ResponseEntity<ApiContract<UserResponseDetails>> {
        var apiContract = ApiContract<UserResponseDetails>(null, null)
        return try {
            apiContract = handler.updateUser(userRequest, ifMatch, bindingResult)

            ResponseEntity
                .ok(apiContract)
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is UserNotFound -> {
                    apiContract.errorMessage = ErrorResponseMessage(FORBIDDEN_FOR_USER_NOT_UPDATED.second)
                    ResponseEntity.status(FORBIDDEN_FOR_USER_NOT_UPDATED.first).body(apiContract)
                }
                is UserInputNotValid -> {
                    apiContract.errorMessage = ErrorResponseMessage(BAD_REQUEST_FOR_INVALID_USER_ENTITY.second)
                    ResponseEntity.status(BAD_REQUEST_FOR_INVALID_USER_ENTITY.first).body(apiContract)
                }
                is IfMatchNotProvided -> {
                    apiContract.errorMessage = ErrorResponseMessage(BAD_REQUEST_FOR_IF_MATCH_NOT_PROVIDED.second)
                    ResponseEntity.status(BAD_REQUEST_FOR_IF_MATCH_NOT_PROVIDED.first).body(apiContract)
                }
                is UserPreConditionFailed -> {
                    apiContract.errorMessage = ErrorResponseMessage(PRE_CONDITION_FAILED_FOR_USER_RESOURCE.second)
                    ResponseEntity.status(PRE_CONDITION_FAILED_FOR_USER_RESOURCE.first).body(apiContract)
                }
                else -> {
                    apiContract.errorMessage = ErrorResponseMessage(GENERIC_ERROR_MESSAGE)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
                }
            }
        }
    }

    @GetMapping(
        value = ["/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getUserById(
        @PathVariable id: Long,
        @RequestHeader headers: Map<String,String>
    ): ResponseEntity<ApiContract<UserResponseDetails>> {
        var apiContract = ApiContract<UserResponseDetails>(null, null)
        return try {
            val handledUser = handler.getUserById(id, headers)

            val userEtagVersion = handledUser.first.toString()
            apiContract = handledUser.second

            ResponseEntity
                .ok()
                .eTag(userEtagVersion)
                .body(apiContract)
        } catch (error: Exception) {
            error.printError(logger)

            when (error) {
                is UserNotFound -> {
                    apiContract.errorMessage = ErrorResponseMessage(NO_CONTENT_FOR_USER_NOT_FOUND.second)
                    ResponseEntity.status(NO_CONTENT_FOR_USER_NOT_FOUND.first).body(apiContract)
                }
                is UserDataNotModified -> {
                    apiContract.errorMessage = ErrorResponseMessage(NOT_MODIFIED_FOR_USER_DATA.second)
                    ResponseEntity.status(NOT_MODIFIED_FOR_USER_DATA.first).body(apiContract)
                }
                else -> {
                    apiContract.errorMessage = ErrorResponseMessage(GENERIC_ERROR_MESSAGE)
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
                }
            }
        }
    }

    @GetMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
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