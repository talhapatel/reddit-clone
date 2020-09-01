package com.talha.springredditclone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.talha.springredditclone.dto.PostRequest;
import com.talha.springredditclone.dto.PostResponse;
import com.talha.springredditclone.model.Post;
import com.talha.springredditclone.model.Subreddit;
import com.talha.springredditclone.model.User;
import com.talha.springredditclone.repository.CommentRepository;
import com.talha.springredditclone.repository.VoteRepository;
import com.talha.springredditclone.service.AuthService;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

	 @Autowired
	    private CommentRepository commentRepository;
	    @Autowired
	    private VoteRepository voteRepository;
	    @Autowired
	    private AuthService authService;


    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "voteCount", constant = "0")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
}
