package com.talha.springredditclone.mapper;

import javax.annotation.Generated;

import org.springframework.stereotype.Component;

import com.talha.springredditclone.dto.SubredditDto;
import com.talha.springredditclone.dto.SubredditDto.SubredditDtoBuilder;
import com.talha.springredditclone.model.Subreddit;
import com.talha.springredditclone.model.Subreddit.SubredditBuilder;

@Generated(
	    value = "org.mapstruct.ap.MappingProcessor",
	    date = "2019-12-12T05:34:58+0100",
	    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_191 (Oracle Corporation)"
	)
@Component
public class SubredditMapperImpl  implements SubredditMapper{

	@Override
	public SubredditDto mapSubredditToDto(Subreddit subreddit) {
		// TODO Auto-generated method stub
		if(subreddit == null) {
			
			return null;
		}
		
		SubredditDtoBuilder subredditDto = SubredditDto.builder();

        subredditDto.id( subreddit.getId() );
        subredditDto.name( subreddit.getName() );
        subredditDto.description( subreddit.getDescription() );

        subredditDto.numberOfPosts( mapPosts(subreddit.getPosts()) );

        return subredditDto.build();
	}

	@Override
	public Subreddit mapDtoToSubreddit(SubredditDto subreddit) {
	    if ( subreddit == null ) {
            return null;
        }

        SubredditBuilder subreddit1 = Subreddit.builder();

        subreddit1.id( subreddit.getId() );
        subreddit1.name( subreddit.getName() );
        subreddit1.description( subreddit.getDescription() );

        return subreddit1.build();
	}

}
