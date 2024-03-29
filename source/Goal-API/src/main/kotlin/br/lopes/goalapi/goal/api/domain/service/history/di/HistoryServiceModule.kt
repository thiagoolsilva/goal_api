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

package br.lopes.goalapi.goal.api.domain.service.history.di

import br.lopes.goalapi.goal.api.data.repository.GoalRepositoryContract
import br.lopes.goalapi.goal.api.data.repository.HistoryRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.history.HistoryService
import br.lopes.goalapi.goal.api.domain.service.history.HistoryServiceContract
import br.lopes.goalapi.goal.api.domain.service.history.usecases.SaveHistoryUC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class HistoryServiceModule {

    @Autowired
    private lateinit var  historyRepositoryContract: HistoryRepositoryContract

    @Autowired
    private lateinit var goalRepositoryContract: GoalRepositoryContract

    @Bean
    @Primary
    fun createHistoryService(): HistoryServiceContract {
        val useCases = mapOf<String, Any>(
                SaveHistoryUC::class.toString() to createSaveHistoryUc()
        )
        return HistoryService(useCases)
    }

    @Bean
    fun createSaveHistoryUc() : SaveHistoryUC {
        return SaveHistoryUC(goalRepositoryContract, historyRepositoryContract)
    }

}