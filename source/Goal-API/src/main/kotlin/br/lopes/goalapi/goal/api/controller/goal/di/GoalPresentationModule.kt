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

package br.lopes.goalapi.goal.api.controller.goal.di

import br.lopes.goalapi.goal.api.controller.goal.Handler
import br.lopes.goalapi.goal.api.data.repository.GoalRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.goal.GoalServiceContract
import br.lopes.goalapi.goal.api.domain.service.history.HistoryServiceContract
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GoalPresentationModule {

    @Autowired
    private lateinit var goalServiceContract:GoalServiceContract

    @Autowired
    private lateinit var historyServiceContract: HistoryServiceContract

    @Bean
    fun createGoalHandler(): Handler {
        return Handler(goalServiceContract, historyServiceContract)
    }
}