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

package br.lopes.goalapi.goal.api.controller.user.mapper

import br.lopes.goalapi.goal.api.controller.user.contract.UpdateUserRequest
import br.lopes.goalapi.goal.api.controller.user.contract.UserRequest
import br.lopes.goalapi.goal.api.controller.user.contract.UserResponseDetails
import br.lopes.goalapi.goal.api.domain.service.user.model.UserEntity

fun UserRequest.toUserEntity() = UserEntity(
    id = null,
    name = this.name,
    nickname = this.nickname,
    entityVersion = null
)

fun UpdateUserRequest.toUserEntity(entityVersion:Long) = UserEntity(
    id = this.id,
    name = this.name,
    nickname = this.nickname,
    entityVersion = entityVersion
)

fun UserEntity.toUserResponse() = UserResponseDetails(
    id = this.id,
    name = this.name,
    nickname = this.nickname
)