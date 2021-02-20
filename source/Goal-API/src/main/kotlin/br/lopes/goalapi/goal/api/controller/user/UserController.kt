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

import br.lopes.goalapi.goal.api.controller.Constants
import br.lopes.goalapi.goal.api.controller.contract.ApiContract
import br.lopes.goalapi.goal.api.controller.contract.ErrorResponse
import br.lopes.goalapi.goal.api.controller.user.contract.UserRequest
import br.lopes.goalapi.goal.api.controller.user.contract.UserResponseDetails
import br.lopes.goalapi.goal.api.domain.service.user.UserServiceContract
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
@RequestMapping(Constants.USER_PATH)
class UserController {

    @Autowired
    private lateinit var userService: UserServiceContract

    @Autowired
    private lateinit var handler: Handler

    @PostMapping
    @Transactional
    fun saveUser(
            @RequestBody @Valid userRequest: UserRequest,
//            bindingResult: BindingResult,
            uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<ApiContract<UserResponseDetails>> {
        var apiContract = ApiContract<UserResponseDetails>(null, null)
        try {
            apiContract = handler.createOrUpdateUser(userRequest)
            val userId = apiContract.body?.id
            val uri = uriComponentsBuilder.path(Constants.USER_PATH + "/{id}").buildAndExpand(userId).toUri()

            return ResponseEntity.created(uri).body(apiContract)
        } catch (error: Exception) {
            apiContract.error = ErrorResponse("unexpected error")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
        }
    }

    @PutMapping
    @Transactional
    fun updateUser(
            @RequestBody @Valid userRequest: UserRequest
    ): ResponseEntity<ApiContract<UserResponseDetails>> {
        var apiContract = ApiContract<UserResponseDetails>(null, null)
        try {
            if (userRequest.id != null) {
                apiContract = handler.updateUser(userRequest)

                return ResponseEntity.ok(apiContract)
            } else {
                apiContract.error = ErrorResponse("Invalid user id")
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiContract)
            }
        } catch (error: Exception) {
            apiContract.error = ErrorResponse("unexpected error")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiContract)
        }
    }

    @GetMapping("/{id}")
    fun getUserById(
            @PathVariable id: Long
    ): ResponseEntity<ApiContract<UserResponseDetails>> {
        var apiContract = ApiContract<UserResponseDetails>(null, null)
        try {
            apiContract = handler.getUserById(id)

            return ResponseEntity.ok(apiContract)
        } catch (EntityNotFoundException: Exception) {
            apiContract.error = ErrorResponse("User not found")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiContract)
        } catch (error: Exception) {
            apiContract.error = ErrorResponse("unexpected error")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping
    fun getUsers(
            @Valid pageable: Pageable
    ): ResponseEntity<ApiContract<Page<UserResponseDetails>>> {
        var apiContract = ApiContract<Page<UserResponseDetails>>(null, null)
        try {
            apiContract = handler.getUserByQuery(pageable)

            return ResponseEntity.ok().body(apiContract)
        } catch (error: Exception) {
            apiContract.error = ErrorResponse("unexpected error")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}