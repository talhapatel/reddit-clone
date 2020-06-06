package com.talha.springredditclone.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talha.springredditclone.dto.SubredditDto;
import com.talha.springredditclone.exception.SpringRedditException;
import com.talha.springredditclone.exception.SubredditNotFoundException;
import com.talha.springredditclone.mapper.SubredditMapper;
import com.talha.springredditclone.model.Subreddit;
import com.talha.springredditclone.repository.SubredditRepository;

import lombok.AllArgsConstructor;

import static java.time.Instant.now;
import static java.util.stream.Collectors.toList;


@Service
	@AllArgsConstructor
	public class SubredditService {

	    private final SubredditRepository subredditRepository;
	    private final AuthService authService;
	    private final SubredditMapper subredditMapper;

	    @Transactional(readOnly = true)
	    public List<SubredditDto> getAll() {
	        return subredditRepository.findAll()
	                .stream()
	                .map(subredditMapper::mapSubredditToDto)
	                .collect(toList());
	    }

	    @Transactional
	    public SubredditDto save(SubredditDto subredditDto) {
	        Subreddit subreddit = subredditRepository.save(mapToSubreddit(subredditDto));
	        subredditDto.setId(subreddit.getId());
	        return subredditDto;
	    }

	    @Transactional(readOnly = true)
	    public SubredditDto getSubreddit(Long id) {
	    	 Subreddit subreddit = subredditRepository.findById(id)
	                 .orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
	         return subredditMapper.mapSubredditToDto(subreddit);
	     }

	    private Subreddit mapToSubreddit(SubredditDto subredditDto) {
	        return Subreddit.builder().name("/r/" + subredditDto.getName())
	                .description(subredditDto.getDescription())
	                .user(authService.getCurrentUser())
	                .createdDate(now()).build();
	    }
	}
