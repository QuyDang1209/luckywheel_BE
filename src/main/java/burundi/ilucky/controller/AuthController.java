package burundi.ilucky.controller;

import burundi.ilucky.jwt.JwtTokenProvider;
import burundi.ilucky.model.CustomUserDetails;
import burundi.ilucky.model.User;
import burundi.ilucky.payload.*;
import burundi.ilucky.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;


	@PostMapping
	public ResponseEntity<?> auth(@Valid @RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		User user = customUserDetails.getUser();
		String jwt = tokenProvider.generateToken(user);
		userService.addNewSpinInNextDay(user);
		AuthResponse authResponse = new AuthResponse(jwt);

		return ResponseEntity.ok().body(authResponse);
	}
}
