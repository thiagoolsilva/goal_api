package br.lopes.goalapi.goal.api.domain.service.user.di

import br.lopes.goalapi.goal.api.data.repository.UserRepositoryContract
import br.lopes.goalapi.goal.api.domain.service.user.usecase.SaveUserUC
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserServiceConfig {

    @Autowired
    private lateinit var userRepositoryContract: UserRepositoryContract

    @Bean
    fun createSaveUseCaseBean(): SaveUserUC {
        return SaveUserUC(userRepositoryContract)
    }
}