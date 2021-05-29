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

package br.lopes.goalapi.goal.api.domain.service.user.di

import br.lopes.goalapi.goal.api.data.repository.UserRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.user.usecase.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserDomainModule {

    @Autowired
    private lateinit var userRepositoryContract: UserRepositoryContract

    @Bean
    fun createSaveUseCase(): SaveUserUC {
        return SaveUserUC(userRepositoryContract)
    }

    @Bean
    fun createDeleteUseCase() : DeleteUserUC {
        return DeleteUserUC(userRepositoryContract)
    }

    @Bean
    fun createFindUserByIdUseCase() : FindUserByIdUC {
        return FindUserByIdUC(userRepositoryContract)
    }

    @Bean
    fun creteUserByQueryUseCase(): FindUserByQueryUC {
        return FindUserByQueryUC(userRepositoryContract)
    }

    @Bean
    fun createUpdateUserUseCase() : UpdateUserUC {
        return UpdateUserUC(userRepositoryContract)
    }
}