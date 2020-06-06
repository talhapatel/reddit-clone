package com.talha.springredditclone.model;

import javax.persistence.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static javax.persistence.GenerationType.IDENTITY;

import java.time.Instant;

import static javax.persistence.FetchType.LAZY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class VerificationToken {
	  @Id
	    @GeneratedValue(strategy = IDENTITY)
	    private Long id;
	    private String token;
	    @OneToOne(fetch = LAZY)
	    private User user;
	    private Instant expiryDate;
}