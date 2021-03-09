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

package br.lopes.goalapi.goal.api.controller.user.error

import org.springframework.http.HttpStatus

class UserApiErrorMessages {

    companion object ErrorMessge {
         val USER_NOT_FOUND = Pair(HttpStatus.NO_CONTENT, "User not found")
         val USER_NOT_UPDATED = Pair(HttpStatus.FORBIDDEN, "user not authorized to update the provided id")
         val INVALID_USER_ENTITY = Pair(HttpStatus.BAD_REQUEST, "invalid provided User entity")
    }
}