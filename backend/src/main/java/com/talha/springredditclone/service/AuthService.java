package com.talha.springredditclone.service;

import static java.time.Instant.now;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.talha.springredditclone.dto.AuthenticationResponse;
import com.talha.springredditclone.dto.LoginRequest;
import com.talha.springredditclone.dto.RegisterRequest;
import com.talha.springredditclone.exception.SpringRedditException;
import com.talha.springredditclone.model.NotificationEmail;
import com.talha.springredditclone.model.User;
import com.talha.springredditclone.model.VerificationToken;
import com.talha.springredditclone.repository.UserRepository;
import com.talha.springredditclone.repository.VerificationTokenRepository;
import com.talha.springredditclone.security.JwtProvider;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j; 


import static com.talha.springredditclone.util.Constants.ACTIVATION_EMAIL;



@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
private final VerificationTokenRepository verificationTokenRepository;
private final MailContentBuilder mailContentBuilder;
private final MailService mailService;

private final AuthenticationManager authenticationManager;
private final JwtProvider jwtProvider;
@Transactional
public void signup(RegisterRequest registerRequest) {
User user = new User();
user.setUsername(registerRequest.getUsername());
user.setEmail(registerRequest.getEmail());
user.setPassword(encodePassword(registerRequest.getPassword()));
user.setCreated(now());
user.setEnabled(false);
userRepository.save(user);
String token = generateVerificationToken(user);
String message = mailContentBuilder.build("Thank you for signing up to Spring Reddit, please click on the below url to activate your account : "
+ ACTIVATION_EMAIL + "/" + token);
mailService.sendMail(new NotificationEmail("Please Activate your account", user.getEmail(), message));
}
private String generateVerificationToken(User user) {
String token = UUID.randomUUID().toString();
VerificationToken verificationToken = new VerificationToken();
verificationToken.setToken(token);
verificationToken.setUser(user);
verificationTokenRepository.save(verificationToken);
return token;
}


@Transactional(readOnly = true)
User getCurrentUser() {
org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
getContext().getAuthentication().getPrincipal();
return userRepository.findByUsername(principal.getUsername())
.orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
}

public AuthenticationResponse login(LoginRequest loginRequest) {
Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
loginRequest.getPassword()));
SecurityContextHolder.getContext().setAuthentication(authenticate);
String authenticationToken = jwtProvider.generateToken(authenticate);
return new AuthenticationResponse(authenticationToken, loginRequest.getUsername());
}

private String encodePassword(String password) {
return passwordEncoder.encode(password);
}
public void verifyAccount(String token) {
Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
verificationTokenOptional.orElseThrow(() -> new SpringRedditException("Invalid Token"));
fetchUserAndEnable(verificationTokenOptional.get());
}
@Transactional
private void fetchUserAndEnable(VerificationToken verificationToken) {
String username = verificationToken.getUser().getUsername();
User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User Not Found with id - " + username));
user.setEnabled(true);
userRepository.save(user);
}
}