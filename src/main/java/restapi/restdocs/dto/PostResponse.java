package restapi.restdocs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.restdocs.entity.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String content;

    public static PostResponse of(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent());
    }
}
