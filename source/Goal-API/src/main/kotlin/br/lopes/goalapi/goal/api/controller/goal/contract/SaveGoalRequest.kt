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

package br.lopes.goalapi.goal.api.controller.goal.contract

import br.lopes.goalapi.goal.api.controller.validation.dateformat.DateFormat
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.PositiveOrZero

data class SaveGoalRequest constructor(
        @field:PositiveOrZero
        val userId: Long = 0,

        @field:NotEmpty
        var title:String = "",

        @field:NotEmpty
        var description:String = "",

        @field:PositiveOrZero
        var totalPrice:Double,

        @DateFormat
        @field:NotEmpty
        var dtEndGoal: String = ""
)