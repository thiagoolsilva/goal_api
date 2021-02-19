package br.lopes.goalapi.goal.api.data.repository

import br.lopes.goalapi.goal.api.data.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepositoryContract: JpaRepository<User, Long>