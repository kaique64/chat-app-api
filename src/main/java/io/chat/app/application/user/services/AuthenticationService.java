package io.chat.app.application.user.services;

import io.chat.app.application.exceptions.AppException;
import io.chat.app.application.user.dtos.SignInUserDTO;
import io.chat.app.application.user.dtos.SignInUserResponseDTO;
import io.chat.app.application.user.services.interfaces.IAuthenticationService;
import io.chat.app.infra.database.entity.User;
import io.chat.app.infra.database.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService, IAuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ModelMapper modelMapper;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(@Lazy AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);

        if (user.isPresent()) return modelMapper.map(user, UserDetails.class);

        return null;
    }

    public SignInUserResponseDTO signIn(SignInUserDTO userDTO) {
        UserDetails userDetails = this.loadUserByUsername(userDTO.getEmail());

        if (userDetails == null) throw new AppException("User not found", HttpStatus.NOT_FOUND);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        User user = (User) authenticate.getPrincipal();

        String token = tokenService.generateToken(user);

        SignInUserResponseDTO signInUserResponseDTO = modelMapper.map(user, SignInUserResponseDTO.class);
        signInUserResponseDTO.setToken(token);

        return signInUserResponseDTO;
    }

}
