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

import br.lopes.goalapi.goal.api.controller.contract.ApiContract
import br.lopes.goalapi.goal.api.controller.handleUserInputErrors
import br.lopes.goalapi.goal.api.controller.user.contract.UpdateUserRequest
import br.lopes.goalapi.goal.api.controller.user.contract.UserRequest
import br.lopes.goalapi.goal.api.controller.user.contract.UserResponseDetails
import br.lopes.goalapi.goal.api.controller.user.error.UserApiErrorMessages.ErrorMessage.USER_NOT_FOUND
import br.lopes.goalapi.goal.api.controller.user.error.model.DuplicatedUserException
import br.lopes.goalapi.goal.api.controller.user.error.model.UserInputNotValid
import br.lopes.goalapi.goal.api.controller.user.error.model.UserNotFound
import br.lopes.goalapi.goal.api.controller.user.mapper.toUserEntity
import br.lopes.goalapi.goal.api.controller.user.mapper.toUserResponse
import br.lopes.goalapi.goal.api.domain.service.user.UserServiceConstants
import br.lopes.goalapi.goal.api.domain.service.user.UserServiceContract
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.validation.BindingResult
import javax.persistence.EntityNotFoundException


class Handler @Autowired constructor(
    private val userService: UserServiceContract
) {

    fun getUserById(userId: Long): ApiContract<UserResponseDetails> {
        return try {
            val response = ApiContract<UserResponseDetails>(null, null)

            val findUser = userService.getUserById(userId).toUserResponse()
            response.body = findUser

            response
        } catch (entityNotFoundException: EntityNotFoundException) {
            throw UserNotFound(USER_NOT_FOUND.second, entityNotFoundException)
        }
    }

    fun getUserByQuery(nickname: String?, pageable: Pageable): ApiContract<Page<UserResponseDetails>> {
        val apiContract = ApiContract<Page<UserResponseDetails>>(null, null)
        val params = mutableMapOf<String, Any>()

        if (nickname != null) {
            params[UserServiceConstants.QUERY_NICKNAME_PARAM] = nickname
        }
        params[UserServiceConstants.QUERY_PAGEABLE_PARAM] = pageable

        val userPage = userService.findUserByQuery(params).map { it.toUserResponse() }
        apiContract.body = userPage

        return apiContract
    }

    fun saveUser(userRequest: UserRequest, bindingResult: BindingResult): ApiContract<UserResponseDetails> {
        if (bindingResult.hasErrors()) {
            throw UserInputNotValid(handleUserInputErrors(bindingResult))
        }

        return try {
            val response = ApiContract<UserResponseDetails>(null, null)

            val userEntity = userRequest.toUserEntity()
            val savedUser = userService.saveUser(userEntity).toUserResponse()
            response.body = savedUser

            response
        } catch (duplicatedUserException: DataIntegrityViolationException) {
            throw DuplicatedUserException("duplicated user", duplicatedUserException)
        }
    }

    fun updateUser(
        updateUserRequest: UpdateUserRequest,
        bindingResult: BindingResult
    ): ApiContract<UserResponseDetails> {
        return try {
            if (bindingResult.hasErrors()) {
                throw UserInputNotValid(handleUserInputErrors(bindingResult))
            }

            val apiContract = ApiContract<UserResponseDetails>(null, null)

            val userEntity = updateUserRequest.toUserEntity()
            val response = userService.updateUser(userEntity).toUserResponse()
            apiContract.body = response

            apiContract
        } catch (dataIntegrityViolationException: DataIntegrityViolationException) {
            throw UserNotFound("Id not found", dataIntegrityViolationException)
        }
    }
}