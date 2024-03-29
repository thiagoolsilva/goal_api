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

package br.lopes.goalapi.goal.api.controller.config.validation.dateformat

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext


class DateFormatImpl : ConstraintValidator<DateFormat, String> {

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        var result = false
        val datetimeRegexPatter = "[1-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]T[0-9][0-9]:[0-9][0-9]:[0-9][0-9]"
        val regex = Regex(datetimeRegexPatter)

        value?.let {
            result = regex.matches(it)
        }
        return result
    }
}