package br.lopes.goalapi.goal.api.data.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class History constructor(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,

        @Column(name = "dtevent")
        val dtEvent:LocalDateTime,

        @Column(name = "value")
        var value:Double,

        @ManyToOne(fetch = FetchType.LAZY)
        var goal: Goal
)