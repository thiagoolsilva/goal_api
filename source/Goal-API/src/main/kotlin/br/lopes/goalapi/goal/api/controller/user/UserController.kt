package br.lopes.goalapi.goal.api.controller.user

import br.lopes.goalapi.goal.api.controller.Constants
import br.lopes.goalapi.goal.api.controller.contract.ApiContract
import br.lopes.goalapi.goal.api.controller.contract.ErrorResponse
import br.lopes.goalapi.goal.api.controller.user.contract.UserRequest
import br.lopes.goalapi.goal.api.controller.user.contract.UserResponseDetails
import br.lopes.goalapi.goal.api.controller.user.mapper.toUserEntity
import br.lopes.goalapi.goal.api.controller.user.mapper.toUserResponse
import br.lopes.goalapi.goal.api.domain.service.user.UserServiceContract
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.lang.Exception
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
@RequestMapping(Constants.USER_PATH)
class UserController {

    @Autowired
    private lateinit var userService: UserServiceContract

    @PostMapping
    @Transactional
    fun saveUser(
            @RequestBody @Valid userRequest: UserRequest,
            uriComponentsBuilder: UriComponentsBuilder
    ): ResponseEntity<ApiContract<UserResponseDetails>> {
        try {
            val apiContract = ApiContract<UserResponseDetails>(null,null)

            val userEntity = userRequest.toUserEntity()
            val savedUser = userService.saveUser(userEntity).toUserResponse()
            val uri = uriComponentsBuilder.path(Constants.USER_PATH + "/{id}").buildAndExpand(savedUser.id).toUri()

            apiContract.body = savedUser

            return ResponseEntity.created(uri).body(apiContract)
        } catch (error: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

    }

    @PutMapping
    fun updateUser(
            @RequestBody @Valid userRequest: UserRequest,
    ): ResponseEntity<ApiContract<UserResponseDetails>> {
        try {
            val apiContract = ApiContract<UserResponseDetails>(null,null)

            if (userRequest.id != null) {
                val response = userService.saveUser(userRequest.toUserEntity()).toUserResponse()
                apiContract.body = response

                return ResponseEntity.ok(apiContract)
            } else {
                apiContract.error = ErrorResponse("Invalid user id")
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiContract)
            }
        } catch (error: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @GetMapping("/{id}")
    fun getUserById(
            @PathVariable id: Long
    ): ResponseEntity<ApiContract<UserResponseDetails>> {
        val apiContract = ApiContract<UserResponseDetails>(null,null)
        try {
            val findUser = userService.getUserById(id).toUserResponse()
            apiContract.body = findUser

            return ResponseEntity.ok(apiContract)
        } catch (EntityNotFoundException: Exception) {
            apiContract.error = ErrorResponse("User not found")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiContract)
        } catch (error: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

}