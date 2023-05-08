package restapi.restdocs.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import restapi.restdocs.dto.MemberResponse;
import restapi.restdocs.dto.PostResponse;
import restapi.restdocs.service.MemberService;
import restapi.restdocs.service.PostService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class MemberControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @MockBean
    private MemberService memberService;

    @Test
    void signUp() throws Exception {
        final MemberResponse memberResponse = new MemberResponse("memberName", "title", "content");
        when(memberService.signUp(any())).thenReturn(memberResponse);

        this.mockMvc.perform(post("/members/signup")
                        .content("{\"memberName\": \"memberName\", \"password\": \"password\", \"email\": \"email@example.com\"}") // Updated JSON payload
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("member-signup",
                        requestFields(
                                fieldWithPath("memberName").description("Member memberName"),
                                fieldWithPath("password").description("Member password"),
                                fieldWithPath("email").description("Member email")
                        )
                ));
    }

    @Test
    void login() throws Exception {
        final MemberResponse memberResponse = new MemberResponse("memberName", "title", "content");
        when(memberService.login(any())).thenReturn(memberResponse);

        this.mockMvc.perform(post("/members/login")
                        .content("{\"memberName\": \"memberName\", \"password\": \"password\"}") // Updated JSON payload
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-login",
                        requestFields(
                                fieldWithPath("memberName").description("Member memberName"),
                                fieldWithPath("password").description("Member password")
                        )
                ));
    }

    @Test
    void remove() throws Exception {

        this.mockMvc.perform(delete("/members/{memberName}", "memberName"))
                .andExpect(status().isNoContent())
                .andDo(document("member-delete",
                        pathParameters(
                                parameterWithName("memberName").description("member memberName")
                        )
                ));
    }

}