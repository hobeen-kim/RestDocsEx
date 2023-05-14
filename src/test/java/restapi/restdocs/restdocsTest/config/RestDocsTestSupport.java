package restapi.restdocs.restdocsTest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import restapi.restdocs.entity.EnumType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@Disabled
@Import(RestDocsConfig.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class RestDocsTestSupport {

    protected RestDocumentationResultHandler documentHandler;

    @Autowired protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void setUp(final WebApplicationContext webApplicationContext,
               final RestDocumentationContextProvider restDocumentation,
               TestInfo testInfo) {


        String className = testInfo.getTestClass().orElseThrow().getSimpleName().replace("ControllerTest", "");
        String methodName = testInfo.getTestMethod().orElseThrow().getName();

        documentHandler = document(
                className + "/" + methodName,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())


        );

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation
                        .documentationConfiguration(restDocumentation)
                )
                .alwaysDo(documentHandler)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    protected static String generateLinkCode(Class<?> clazz) {
        return String.format("link:common/%s.html[Enum,role=\"popup\"]", clazz.getSimpleName().toLowerCase());
    }
}
