package com.pucetec.geomed.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class MdcLoggingFilter : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(MdcLoggingFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        val sub = if (authentication?.principal is Jwt) {
            (authentication.principal as Jwt).getClaimAsString("sub") ?: "anonimo"
        } else {
            "anonimo"
        }

        MDC.put("sub", sub)

        val uri = request.requestURI
        val method = request.method
        log.info("event=http.request | msg={} {}", method, uri)

        try {
            filterChain.doFilter(request, response)
        } finally {
            val status = response.status
            log.info("event=http.response | msg={} {} {}", status, method, uri)
            MDC.remove("sub")
        }
    }
}