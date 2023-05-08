package restapi.restdocs.controller;


import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import restapi.restdocs.dto.PostResponse;
import restapi.restdocs.service.PostService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class PostControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @MockBean
    private PostService postService;

    @Test
    void create() throws Exception {
        final PostResponse postResponse = new PostResponse(1L, "title", "content");
        when(postService.create(any())).thenReturn(postResponse);

        this.mockMvc.perform(post("/posts") // 1
                        .content("{\"title\": \"title\", \n\"content\": \"content\"}") // 2
                        .contentType(MediaType.APPLICATION_JSON)) // 3
                .andExpect(status().isCreated()) // 4
                .andDo(document("post-create", // 5
                        requestFields( // 6
                                fieldWithPath("title").description("Post 제목"), // 7
                                fieldWithPath("content").description("Post 내용").optional() // 8
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

        this.mockMvc.perform(get("/posts")
                        .accept(MediaType.APPLICATION_JSON)) // 1
                .andExpect(status().isOk())
                .andDo(document("post-get-all",
                        responseFields( // 2
                                fieldWithPath("[].id").description("Post Id"), // 3
                                fieldWithPath("[].title").description("Post 제목"),
                                fieldWithPath("[].content").description("Post 내용")
                        )
                ));
    }

    @Test
    void findById() throws Exception {
        PostResponse postResponse = new PostResponse(1L, "title", "content");
        when(postService.findById(anyLong())).thenReturn(postResponse);

        this.mockMvc.perform(get("/posts/{postId}", postResponse.getId()) // 4
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("post-get-one",
                        pathParameters( // 5
                                parameterWithName("postId").description("Post Id") // 6
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
        this.mockMvc.perform(put("/posts/{postId}", 1L)
                        .content("{\"title\": \"turtle\", \n\"content\": \"context\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("post-update",
                        pathParameters(
                                parameterWithName("postId").description("Post Id")
                        ),
                        requestFields(
                                fieldWithPath("title").description("Post 제목"),
                                fieldWithPath("content").description("Post 내용")
                        )
                ));
    }

    @Test
    void remove() throws Exception {
        this.mockMvc.perform(delete("/posts/{postId}", 1L))
                .andExpect(status().isNoContent())
                .andDo(document("post-delete",
                        pathParameters(
                                parameterWithName("postId").description("Post Id")
                        )
                ));
    }




}