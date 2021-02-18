package br.lopes.goalapi.goal.api.data.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Goal constructor(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,

        @Column(name = "title")
        var title:String,

        @Column(name = "description")
        var description:String,

        @Column(name = "totalprice")
        var totalPrice:Double,

        @Column(name = "dtendgoal")
        var dtEndGoal:LocalDateTime,

        @Column(name = "dtcreate")
        val dtCreate:LocalDateTime,

        @Column(name = "dtupdate")
        var dtUpdate:LocalDateTime?,

        @OneToMany(mappedBy = "goal", targetEntity = History::class)
        var history: List<History> = mutableListOf(),

        @ManyToOne(fetch = FetchType.LAZY)
        var user:User
)