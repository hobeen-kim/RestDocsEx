package restapi.restdocs.restdocsTest.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import restapi.restdocs.dto.MemberResponse;
import restapi.restdocs.dto.MemberSignUpRequest;
import restapi.restdocs.entity.Authority;
import restapi.restdocs.restdocsTest.config.RestDocsTestSupport;
import restapi.restdocs.restdocsTest.utils.ConstraintFields;
import restapi.restdocs.service.MemberService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends RestDocsTestSupport {

    @MockBean
    private MemberService memberService;

    ConstraintFields<MemberSignUpRequest> fields = new ConstraintFields<>(MemberSignUpRequest.class);

    @Test
    void signUp() throws Exception {
        final MemberResponse memberResponse = new MemberResponse("memberName", "title", "content", Authority.USER);
        when(memberService.signUp(any())).thenReturn(memberResponse);

        this.mockMvc.perform(post("/members/signup")
                        .content("{\"memberName\": \"memberName\", \"password\": \"password\", \"email\": \"email@example.com\"}") // Updated JSON payload
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(documentHandler.document(
                        requestFields(
                                fields.withPath("memberName").description("Member memberName"),
                                fields.withPath("password").description("Member password"),
                                fields.withPath("email").description("Member email")
                        )
                ));
    }

    @Test
    void login() throws Exception {
        final MemberResponse memberResponse = new MemberResponse("memberName", "title", "content", Authority.USER);
        when(memberService.login(any())).thenReturn(memberResponse);

        this.mockMvc.perform(post("/members/login")
                        .content("{\"memberName\": \"memberName\", \"password\": \"password\"}") // Updated JSON payload
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(documentHandler.document(
                        requestFields(
                                fieldWithPath("memberName").description("Member memberName"),
                                fieldWithPath("password").description("Member password")
                        ),
                        responseFields(
                                fieldWithPath("memberName").description("Member memberName"),
                                fieldWithPath("password").description("Member password"),
                                fieldWithPath("email").description("Member email"),
                                fieldWithPath("authority").description(generateLinkCode(Authority.class))
                        )
                ));
    }

    @Test
    void remove() throws Exception {

        this.mockMvc.perform(delete("/members/{memberName}", "memberName"))
                .andExpect(status().isNoContent())
                .andDo(documentHandler.document(
                        pathParameters(
                                parameterWithName("memberName").description("member memberName")
                        )
                ));
    }

}