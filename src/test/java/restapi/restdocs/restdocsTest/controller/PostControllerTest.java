package restapi.restdocs.restdocsTest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import restapi.restdocs.dto.MemberSignUpRequest;
import restapi.restdocs.dto.PostRequest;
import restapi.restdocs.dto.PostResponse;
import restapi.restdocs.restdocsTest.config.RestDocsConfig;
import restapi.restdocs.restdocsTest.config.RestDocsTestSupport;
import restapi.restdocs.restdocsTest.utils.ConstraintFields;
import restapi.restdocs.service.PostService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static restapi.restdocs.restdocsTest.config.RestDocsConfig.field;

class PostControllerTest extends RestDocsTestSupport {

    @MockBean
    private PostService postService;

    ConstraintFields<PostRequest> fields = new ConstraintFields<>(PostRequest.class);

    @Test
    void create() throws Exception {
        final PostResponse postResponse = new PostResponse(1L, "title", "content");
        when(postService.create(any())).thenReturn(postResponse);

        this.mockMvc.perform(post("/posts")
                        .content("{\"title\": \"title\", \n\"content\": \"content\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(documentHandler.document(
                        requestFields(
                                fields.withPath("title").description("Post 제목"),
                                fieldWithPath("content").description("Post 내용").optional()
                        )
                ));
    }

    @Test
    void findAll() throws Exception {
        List<PostResponse> postResponses = Lists.newArrayList(
                new PostResponse(1L, "title1", "content1"),
                new PostResponse(2L, "title2", "content2")
        );

        when(postService.findAll()).thenReturn(postResponses);

        String expectedJson = objectMapper.writeValueAsString(postResponses);

        this.mockMvc.perform(get("/posts")
                        .accept(MediaType.APPLICATION_JSON)) // 1
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andDo(documentHandler.document(
                        responseFields( // 2
                                fieldWithPath("[].id").description("Post Id"),
                                fieldWithPath("[].title").description("Post 제목"),
                                fieldWithPath("[].content").description("Post 내용")
                        )
                ));
    }

    @Test
    void findById() throws Exception {
        PostResponse postResponse = new PostResponse(1L, "title", "content");
        when(postService.findById(anyLong())).thenReturn(postResponse);

        String expectedJson = objectMapper.writeValueAsString(postResponse);

        this.mockMvc.perform(get("/posts/{postId}", postResponse.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andDo(documentHandler.document(
                        pathParameters(
                                parameterWithName("postId").description("Post Id")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Post Id"),
                                fieldWithPath("title").description("Post 제목"),
                                fieldWithPath("content").description("Post 내용")
                        )
                ));
    }

    @Test
    void update() throws Exception {

        PostResponse postResponse = new PostResponse(1L, "title", "content");
        when(postService.update(anyLong(), any())).thenReturn(postResponse);

        String expectedJson = objectMapper.writeValueAsString(postResponse);

        this.mockMvc.perform(put("/posts/{postId}", 1L)
                        .content("{\"title\": \"title\", \n\"content\": \"context\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson))
                .andDo(documentHandler.document(
                        pathParameters(
                                parameterWithName("postId").description("Post Id")
                        ),
                        requestFields(
                                fields.withPath("title").description("Post 제목"),
                                fieldWithPath("content").description("Post 내용")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Post Id"),
                                fieldWithPath("title").description("Post 제목"),
                                fieldWithPath("content").description("Post 내용")
                        )
                ));
    }

    @Test
    void remove() throws Exception {
        this.mockMvc.perform(delete("/posts/{postId}", 1L))
                .andExpect(status().isNoContent())
                .andDo(documentHandler.document(
                        pathParameters(
                                parameterWithName("postId").description("Post Id")
                        )
                ));
    }




}