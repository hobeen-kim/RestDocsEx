package restapi.restdocs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestdocsApplicationTests {

	@Test
	void contextLoads(TestInfo testInfo) {
		System.out.println(testInfo.getTestClass().orElseThrow().getSimpleName());
		System.out.println(testInfo.getTestMethod().orElseThrow().getName());
	}

}
