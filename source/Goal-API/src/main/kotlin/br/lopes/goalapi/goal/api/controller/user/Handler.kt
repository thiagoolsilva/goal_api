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
import br.lopes.goalapi.goal.api.controller.user.contract.UserRequest
import br.lopes.goalapi.goal.api.controller.user.contract.UserResponseDetails
import br.lopes.goalapi.goal.api.controller.user.mapper.toUserEntity
import br.lopes.goalapi.goal.api.controller.user.mapper.toUserResponse
import br.lopes.goalapi.goal.api.domain.service.user.UserServiceContract
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


class Handler @Autowired constructor(
        private val userService: UserServiceContract
){

    fun getUserById(userId:Long): ApiContract<UserResponseDetails> {
        val response = ApiContract<UserResponseDetails>(null, null)

        val findUser = userService.getUserById(userId).toUserResponse()
        response.body = findUser

        return response
    }

    fun getUserByQuery(pageable: Pageable): ApiContract<Page<UserResponseDetails>> {
        val apiContract = ApiContract<Page<UserResponseDetails>>(null, null)

        val userPage = userService.findUserByQuery(pageable).map { it.toUserResponse() }
        apiContract.body = userPage

        return apiContract
    }

    fun createOrUpdateUser(userRequest: UserRequest): ApiContract<UserResponseDetails> {
        val response = ApiContract<UserResponseDetails>(null, null)

        val userEntity = userRequest.toUserEntity()
        val savedUser =  userService.saveUser(userEntity).toUserResponse()
        response.body = savedUser

        return response
    }

    fun updateUser(userRequest: UserRequest): ApiContract<UserResponseDetails> {
        val apiContract = ApiContract<UserResponseDetails>(null, null)

        val userEntity = userRequest.toUserEntity()
        val response = userService.saveUser(userEntity).toUserResponse()
        apiContract.body = response

        return apiContract
    }

}