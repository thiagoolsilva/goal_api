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

import br.lopes.goalapi.goal.api.controller.Constants
import br.lopes.goalapi.goal.api.controller.contract.ApiContract
import br.lopes.goalapi.goal.api.controller.goal.contract.GoalHistoryResponse
import br.lopes.goalapi.goal.api.controller.goal.contract.GoalResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(Constants.Goal.GOAL_PATH)
class GoalController {

    @Autowired
    private lateinit var handler: Handler

    @GetMapping("/{id}")
    fun getGoalById(
            @PathVariable id: Long
    ): ResponseEntity<ApiContract<GoalResponse>> {
        val goal = handler.getGoalById(id)

        return ResponseEntity.ok(goal)
    }

    @GetMapping("/{id}/history")
    fun getGoalHistory(
            @PathVariable id: Long,
            @Valid pageable: Pageable
    ) : ResponseEntity<ApiContract<Page<GoalHistoryResponse>>> {
        val response = handler.getGoalHistoryById(id, pageable)

        return ResponseEntity.ok(response)
    }
}