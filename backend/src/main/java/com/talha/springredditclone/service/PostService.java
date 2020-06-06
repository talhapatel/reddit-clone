package com.talha.springredditclone.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talha.springredditclone.dto.PostRequest;
import com.talha.springredditclone.dto.PostResponse;
import com.talha.springredditclone.exception.PostNotFoundException;
import com.talha.springredditclone.exception.SubredditNotFoundException;
import com.talha.springredditclone.mapper.PostMapper;
import com.talha.springredditclone.model.Post;
import com.talha.springredditclone.model.Subreddit;
import com.talha.springredditclone.model.User;
import com.talha.springredditclone.repository.PostRepository;
import com.talha.springredditclone.repository.SubredditRepository;
import com.talha.springredditclone.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toList;
@Service

@Slf4j
@Transactional
public class PostService {

	@Autowired
    private  PostRepository postRepository;
	@Autowired
    private  SubredditRepository subredditRepository;
	@Autowired
    private  UserRepository userRepository;
    @Autowired
    private  AuthService authService;
    @Autowired
    private  PostMapper postMapper;

    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());

}
}