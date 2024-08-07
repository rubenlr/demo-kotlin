
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class MyPostgresConfiguration {

    @Bean
    @ServiceConnection(name = "postgres")
    fun postgresContainer(): PostgreSQLContainer<*> {
        return PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"))
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
    }
}

//class PostgresTestContainerExtension : BeforeEachCallback, AfterEachCallback {
//
//    private lateinit var postgresContainer: PostgreSQLContainer<*>
//
//    override fun beforeEach(context: ExtensionContext?) {
//        postgresContainer = PostgreSQLContainer<Nothing>("postgres:16-alpine").apply {
//            withDatabaseName("testdb")
//            withUsername("user")
//            withPassword("password")
//            start()
//        }
//
//        System.setProperty("spring.datasource.url", postgresContainer.jdbcUrl)
//    }
//
//    @Bean
//    @Primary
//    fun postgresContainer(): PostgreSQLContainer<*> = postgresContainer
//
//    override fun afterEach(context: ExtensionContext?) {
//        postgresContainer.stop()
//    }
//}