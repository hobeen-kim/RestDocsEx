package restapi.restdocs.restdocsTest.utils;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EnumDocs {
    // 문서화하고 싶은 모든 enum값을 명시
    Map<String,String> authority;
}
