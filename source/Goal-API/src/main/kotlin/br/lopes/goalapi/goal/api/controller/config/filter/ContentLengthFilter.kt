package br.lopes.goalapi.goal.api.controller.config.filter

import org.springframework.stereotype.Component
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

@Component
class ContentLengthFilter : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val responseWrapper = ContentCachingResponseWrapper((response as HttpServletResponse?)!!)

        chain!!.doFilter(request, responseWrapper)

        responseWrapper.copyBodyToResponse()
    }
}