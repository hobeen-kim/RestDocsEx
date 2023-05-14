package restapi.restdocs.restdocsTest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;
import restapi.restdocs.dto.PostRequest;
import restapi.restdocs.restdocsTest.config.RestDocsTestSupport;
import restapi.restdocs.restdocsTest.utils.ApiResponseDto;
import restapi.restdocs.restdocsTest.utils.CommonDocController;
import restapi.restdocs.restdocsTest.utils.CustomResponseFieldsSnippet;
import restapi.restdocs.restdocsTest.utils.EnumDocs;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static restapi.restdocs.restdocsTest.config.RestDocsConfig.field;

class CommonDocControllerTest extends RestDocsTestSupport{


    @Test
    public void errorSample() throws Exception {
        CommonDocController.SampleRequest sampleRequest = new CommonDocController.SampleRequest("name","hhh.naver");

        ResultActions result =
                mockMvc.perform(
                    post("/test/error")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest))
                )
                .andExpect(status().isBadRequest());

        FieldDescriptor[] fields = new FieldDescriptor[]{
                fieldWithPath("message").description("에러 메시지"),
                fieldWithPath("code").description("Error Code"),
                fieldWithPath("errors").description("Error 값 배열 값"),
                fieldWithPath("errors[].field").description("문제 있는 필드"),
                fieldWithPath("errors[].value").description("문제가 있는 값"),
                fieldWithPath("errors[].reason").description("문재가 있는 이유")
        };

        result.andDo(documentHandler.document(
                    customResponseFields("exception-response",
                            attributes(field("title", "Exception")), fields)


                    )
                );
    }

    @Test
    public void enums() throws Exception {
        // 요청
        ResultActions result = this.mockMvc.perform(
                get("/test/enums")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        System.out.println(result.andReturn().getResponse().getContentAsString());

        // 결과값
        MvcResult mvcResult = result.andReturn();

        // 데이터 파싱
        EnumDocs enumDocs = getData(mvcResult);

        // 문서화 진행
        result.andExpect(status().isOk())
                .andDo(documentHandler.document(
                        customResponseFields("custom-response", beneathPath("data.authority").withSubsectionId("authority"),
                                attributes(field("title", "authority")),
                                enumConvertFieldDescriptor(enumDocs.getAuthority())
                        )
                ));
    }

    // 커스텀 템플릿 사용을 위한 함수
    public static CustomResponseFieldsSnippet customResponseFields(
            String type,
            PayloadSubsectionExtractor<?> subsectionExtractor,
            Map<String, Object> attributes, FieldDescriptor... descriptors) {
        return new CustomResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes, true);
    }

    public static CustomResponseFieldsSnippet customResponseFields(
            String type,
            Map<String, Object> attributes, FieldDescriptor... descriptors) {
        return new CustomResponseFieldsSnippet(type, Arrays.asList(descriptors), attributes, true);
    }

    // Map으로 넘어온 enumValue를 fieldWithPath로 변경하여 리턴
    private static FieldDescriptor[] enumConvertFieldDescriptor(Map<String, String> enumValues) {
        return enumValues.entrySet().stream()
                .map(x -> fieldWithPath(x.getKey()).description(x.getValue()))
                .toArray(FieldDescriptor[]::new);
    }

    // mvc result 데이터 파싱
    private EnumDocs getData(MvcResult result) throws IOException {
        ApiResponseDto<EnumDocs> apiResponseDto = objectMapper.readValue(result.getResponse().getContentAsByteArray(), new TypeReference<>(){});
        return apiResponseDto.getData();
    }
}
