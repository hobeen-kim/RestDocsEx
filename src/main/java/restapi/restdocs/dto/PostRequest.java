package restapi.restdocs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import restapi.restdocs.entity.Post;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    private String title;
    private String content;

    public static PostRequest of(Post post) {
        return new PostRequest(post.getTitle(), post.getContent());
    }

    public Post toEntity() {
        return new Post(null, title, content);
    }
}
