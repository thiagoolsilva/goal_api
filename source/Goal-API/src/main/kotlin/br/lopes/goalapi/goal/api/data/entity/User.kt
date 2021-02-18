package br.lopes.goalapi.goal.api.data.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class User constructor(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,

        @Column(name = "name")
        var name:String,

        @Column(name = "nickname")
        var nickname:String,

        @Column(name = "dtcreate")
        val dtCreate: LocalDateTime,

        @Column(name = "dtupdate")
        var dtUpdate:LocalDateTime?,

        @OneToMany(mappedBy = "user", targetEntity = Goal::class)
        var goals:List<Goal> = mutableListOf()
)