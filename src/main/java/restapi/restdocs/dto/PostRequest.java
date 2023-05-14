package restapi.restdocs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.restdocs.entity.Post;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하입니다.")
    private String title;
    private String content;

    public static PostRequest of(Post post) {
        return new PostRequest(post.getTitle(), post.getContent());
    }

    public Post toEntity() {
        return new Post(null, title, content);
    }
}
