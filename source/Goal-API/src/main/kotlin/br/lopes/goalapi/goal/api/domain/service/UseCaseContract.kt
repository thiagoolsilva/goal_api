package br.lopes.goalapi.goal.api.domain.service

interface UseCaseContract <In, Out> {
    fun execute(input:In): Out
}