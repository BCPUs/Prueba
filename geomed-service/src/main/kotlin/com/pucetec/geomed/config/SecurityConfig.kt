package com.pucetec.geomed.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val mdcLoggingFilter: MdcLoggingFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                // Reglas para el módulo de doctores
                auth.requestMatchers(HttpMethod.GET, "/api/doctors/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                auth.requestMatchers(HttpMethod.POST, "/api/doctors/**").hasRole("ADMIN")

                // Reglas para el módulo de citas
                auth.requestMatchers("/api/appointments/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")

                // Reglas para el módulo de pacientes
                auth.requestMatchers(HttpMethod.GET, "/api/patients/**").hasAnyRole("ADMIN", "DOCTOR", "PATIENT")
                auth.requestMatchers("/api/patients/**").hasAnyRole("ADMIN", "DOCTOR")

                // Cualquier otra solicitud requiere autenticación
                auth.anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(cognitoGroupsConverter())
                }
            }
            .addFilterAfter(mdcLoggingFilter, BearerTokenAuthenticationFilter::class.java)

        return http.build()
    }

    private fun cognitoGroupsConverter(): JwtAuthenticationConverter {
        val converter = JwtAuthenticationConverter()
        converter.setJwtGrantedAuthoritiesConverter { jwt ->
            val groups = jwt.getClaimAsStringList("cognito:groups") ?: emptyList()
            groups.map { SimpleGrantedAuthority("ROLE_$it") }
        }
        return converter
    }
}