// package top.e404.media.config
//
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.context.annotation.Bean
// import org.springframework.context.annotation.Configuration
// import org.springframework.security.authentication.AuthenticationProvider
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
// import org.springframework.security.config.annotation.web.builders.HttpSecurity
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
// import org.springframework.security.core.Authentication
// import org.springframework.security.core.AuthenticationException
// import org.springframework.security.core.authority.SimpleGrantedAuthority
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
// import org.springframework.security.web.SecurityFilterChain
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher
// import org.springframework.stereotype.Component
// import top.e404.media.service.RoleService
// import top.e404.media.service.UserService
// import java.util.*
//
// @Configuration
// @EnableWebSecurity
// class SpringSecurity {
//
//     // @Bean
//     // fun filterChain(http: HttpSecurity): SecurityFilterChain {
//     //     http
//     //         .csrf { csrf ->
//     //             csrf.disable()
//     //         }
//     //         .authorizeHttpRequests { registry ->
//     //             registry.requestMatchers("/register/**").permitAll()
//     //             registry.requestMatchers("/index").permitAll()
//     //         }
//     //         .formLogin { form ->
//     //             form.loginPage("/login")
//     //             form.loginProcessingUrl("/api/login")
//     //             form.defaultSuccessUrl("/users")
//     //             form.permitAll()
//     //         }
//     //         .logout { logout ->
//     //             logout.logoutRequestMatcher(AntPathRequestMatcher("/logout"))
//     //             logout.permitAll()
//     //         }
//     //     return http.build()
//     // }
//
//     @get:Bean
//     val passwordEncoder by lazy { BCryptPasswordEncoder() }
// }
//
// @Component
// class AuthProvider : AuthenticationProvider {
//     @Autowired
//     lateinit var userService: UserService
//
//     @Autowired
//     lateinit var roleService: RoleService
//
//     @Autowired
//     lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder
//
//     @Throws(AuthenticationException::class)
//     override fun authenticate(authentication: Authentication): Authentication {
//         val id = authentication.name.toLong()
//         val password = authentication.credentials.toString()
//         val user = userService.getById(id) ?: throw AuthenticationFailException
//         if (!bCryptPasswordEncoder.matches(password, user.password)) throw AuthenticationFailException
//         val authorities = roleService.getRoleById(id)
//             .map { SimpleGrantedAuthority(it.name!!) }
//             .let(Collections::unmodifiableList)
//         return UsernamePasswordAuthenticationToken(user, password, authorities)
//     }
//
//     override fun supports(aClass: Class<*>?) = true
// }
//
// object AuthenticationFailException : AuthenticationException("用户名或密码错误") {
//     private fun readResolve(): Any = AuthenticationFailException
// }
