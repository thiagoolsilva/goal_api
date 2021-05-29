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

package br.lopes.goalapi.goal.api.controller.goal.error

import org.springframework.http.HttpStatus

class GoalApiErrorMessages {
    companion object ErrorMessage {
        val BAD_REQUEST_FOR_INVALID_GOAL_ENTITY = Pair(HttpStatus.BAD_REQUEST, "Invalid provided Goal entity.")
        val BAD_REQUEST_FOR_IF_MATCH_NOT_PROVIDED = Pair(HttpStatus.BAD_REQUEST, "It was not provided the if-match with entity version.")
        val PRE_CONDITION_FAILED_FOR_GOAL_RESOURCE = Pair(HttpStatus.PRECONDITION_FAILED, "The provided if-match value is not the same saved in database.")
        val NOT_FOUND_FOR_GOAL_ID = Pair(HttpStatus.NOT_FOUND, "Invalid provided Goal ID.")
        val NOT_MODIFIED_FOR_GOAL_DATA = Pair(HttpStatus.NOT_MODIFIED, "Goal not changed.")
    }
}