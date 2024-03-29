package top.e404.media.module.common.test.controller

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import top.e404.media.module.common.entity.auth.LoginDto
import top.e404.media.module.common.service.system.AuthService

@AutoConfigureMockMvc
@SpringBootTest
class UserTest {
    @set:Autowired
    lateinit var mockMvc: MockMvc

    @set:Autowired
    lateinit var authService: AuthService

    @set:Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    private val tokens = ThreadLocal<String>()

    @BeforeEach
    fun beforeEach() {
        val (_, token) = authService.login(LoginDto("admin", "123456"))
        tokens.set(token)
    }

    @AfterEach
    fun afterEach() {
        jdbcTemplate.execute("DELETE FROM sys_user_token WHERE TRUE")
        tokens.remove()
    }

    @Test
    fun testGet() {
        mockMvc.get("/api/admin/users/1") {
            header(HttpHeaders.AUTHORIZATION, tokens.get())
        }.andExpect {
            status().isOk()
            content {
                contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.name") {
                    value("admin")
                }
            }
        }
    }
}
