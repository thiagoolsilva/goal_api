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

package br.lopes.goalapi.goal.api.domain.service.goal.di

import br.lopes.goalapi.goal.api.data.repository.GoalRepositoryContract
import br.lopes.goalapi.goal.api.data.repository.HistoryRepositoryContract
import br.lopes.goalapi.goal.api.data.repository.UserRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.goal.GoalService
import br.lopes.goalapi.goal.api.domain.service.goal.GoalServiceContract
import br.lopes.goalapi.goal.api.domain.service.goal.usecase.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class GoalDomainModule {

    @Autowired
    private lateinit var goalRepositoryContract: GoalRepositoryContract

    @Autowired
    private lateinit var userRepositoryContract: UserRepositoryContract

    @Autowired
    private lateinit var historyRepositoryContract: HistoryRepositoryContract

    @Bean
    @Primary
    fun createGoalService(): GoalServiceContract {
        val useCases = mapOf<String, Any>(
                GetGoalByIdUC::class.toString() to createGetGoalById(),
                GetGoalHistoryById::class.toString() to createGetGoalHistoryById(),
                SaveGoalUC::class.toString() to createGoalSaveGoalUC(),
                UpdateGoalUC::class.toString() to createGoalUpdateGoalUC(),
                DeleteGoalUC::class.toString() to createGoalDeleteGoalByIdUC()
        )

        return GoalService(useCases)
    }

    @Bean
    fun createGetGoalById(): GetGoalByIdUC {
        return GetGoalByIdUC(goalRepositoryContract)
    }

    @Bean
    fun createGetGoalHistoryById(): GetGoalHistoryById {
        return GetGoalHistoryById(historyRepositoryContract)
    }

    @Bean
    fun createGoalSaveGoalUC(): SaveGoalUC {
        return SaveGoalUC(goalRepositoryContract, userRepositoryContract)
    }

    @Bean
    fun createGoalUpdateGoalUC(): UpdateGoalUC {
        return UpdateGoalUC(goalRepositoryContract)
    }

    @Bean
    fun createGoalDeleteGoalByIdUC(): DeleteGoalUC {
        return DeleteGoalUC(goalRepositoryContract)
    }

}