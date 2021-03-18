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

package br.lopes.goalapi.goal.api.controller.config.error.advise

import br.lopes.goalapi.goal.api.controller.config.error.ErrorConstants.ApiError.UNSUPPORTED_MEDIA_TYPE
import br.lopes.goalapi.goal.api.controller.contract.ApiContract
import br.lopes.goalapi.goal.api.controller.contract.ErrorResponseMessage
import br.lopes.goalapi.goal.api.controller.printError
import mu.KLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class InvalidConsumeAdvise {

    @Autowired
    private lateinit var logger: KLogger

    @ExceptionHandler(value = [(HttpMediaTypeNotSupportedException::class)])
    fun handleInvalidHttpMediaTypeNotSupported(
        ex: HttpMediaTypeNotSupportedException,
        request: WebRequest
    ): ResponseEntity<ApiContract<Nothing>> {
        ex.printError(logger)

        val response = ApiContract(null, null)
        response.errorMessage = ErrorResponseMessage(UNSUPPORTED_MEDIA_TYPE)

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response)
    }
}