package top.e404.media.test

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import top.e404.media.MediaManagerApplication

@ExtendWith(SpringExtension::class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [MediaManagerApplication::class]
)
class SpringKotlinExampleApplicationTests {
    @set:Autowired
    lateinit var webTestClient: WebTestClient

    // @Test
    // fun testGet() {
    //     webTestClient
    //         .get()
    //         .uri("/api/user/1")
    //         .accept(MediaType.APPLICATION_JSON)
    //         .exchange()
    //         .expectStatus().isOk()
    //         .expectBody(UserDo::class.java)
    //         .value { assertThat(it.name).isEqualTo("awa") }
    // }
    //
    // @Test
    // fun testSave() {
    //     webTestClient
    //         .post()
    //         .uri("/api/user")
    //         .accept(MediaType.APPLICATION_JSON)
    //         .body(UserDto(name = "qaq"), UserDto::class.java)
    //         .exchange()
    //         .expectStatus().isOk()
    //         .expectBody(UserDo::class.java)
    //         .value { assertThat(it.name).isEqualTo("qaq") }
    // }
    //
    // @Test
    // fun testUpdate() {
    //     webTestClient
    //         .patch()
    //         .uri("/api/user")
    //         .accept(MediaType.APPLICATION_JSON)
    //         .body(UserDto(id = 2, name = "qaq"), UserDto::class.java)
    //         .exchange()
    //         .expectStatus().isOk()
    //         .expectBody(UserDo::class.java)
    //         .value { assertThat(it.name).isEqualTo("qaq") }
    // }

}
