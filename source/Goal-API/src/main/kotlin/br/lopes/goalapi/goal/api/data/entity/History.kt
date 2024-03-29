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

package br.lopes.goalapi.goal.api.data.entity

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class History constructor(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column(name = "dtevent")
        val dtEvent: LocalDateTime,

        @Column(name = "value")
        var value: Double,
) {
    @ManyToOne(fetch = FetchType.LAZY)
    lateinit var goal: Goal
}