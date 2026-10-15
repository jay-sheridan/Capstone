package ca.cars365;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable(named = "NEON_TEST_DATABASE_URL", matches = ".+")
@org.springframework.test.context.TestPropertySource(properties = {
    "spring.datasource.url=${NEON_TEST_DATABASE_URL}",
    "spring.datasource.username=${NEON_TEST_DATABASE_USERNAME}",
    "spring.datasource.password=${NEON_TEST_DATABASE_PASSWORD}",
    "JWT_SECRET_KEY=AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
})
class Cars365BackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
