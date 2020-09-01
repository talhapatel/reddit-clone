package com.talha.springredditclone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.talha.springredditclone.dto.CommentsDto;
import com.talha.springredditclone.model.Comment;
import com.talha.springredditclone.model.Post;
import com.talha.springredditclone.model.User;


@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    Comment map(CommentsDto commentsDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    CommentsDto mapToDto(Comment comment);
}