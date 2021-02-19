package br.lopes.goalapi.goal.api.controller.user

import br.lopes.goalapi.goal.api.controller.user.contract.UserRequest
import br.lopes.goalapi.goal.api.controller.user.contract.UserResponse
import br.lopes.goalapi.goal.api.controller.user.mapper.toUserEntity
import br.lopes.goalapi.goal.api.domain.service.user.UserServiceContract
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController {

    @Autowired
    private lateinit var userService: UserServiceContract

    @PostMapping
    @Transactional
    fun saveUser(
            @RequestBody @Valid userRequest: UserRequest
    ): ResponseEntity<UserResponse> {
        val userEntity = userRequest.toUserEntity()
        val userId = userService.createUser(userEntity)
        val response = UserResponse(userId)

        return ResponseEntity.ok(response)

    }

}