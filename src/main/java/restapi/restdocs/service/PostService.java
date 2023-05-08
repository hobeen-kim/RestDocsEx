package restapi.restdocs.service;

import org.springframework.stereotype.Service;
import restapi.restdocs.dto.PostRequest;
import restapi.restdocs.dto.PostResponse;
import restapi.restdocs.entity.Post;
import restapi.restdocs.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponse findById(Long postId) {
        return PostResponse.of(postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. postId=" + postId)));
    }

    public List<PostResponse> findAll() {
        List<PostResponse> postResponses = new ArrayList<>();
        postRepository.findAll().forEach(post -> postResponses.add(PostResponse.of(post)));
        return postResponses;
    }

    public PostResponse create(PostRequest postRequest) {
        return PostResponse.of(postRepository.save(postRequest.toEntity()));
    }

    public void update(Long postId, PostRequest postRequest) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. postId=" + postId));
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        postRepository.save(post);
    }

    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }
}
