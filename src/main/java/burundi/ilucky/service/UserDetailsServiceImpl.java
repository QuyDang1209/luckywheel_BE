package burundi.ilucky.service;

import java.util.Arrays;
import java.util.List;

import burundi.ilucky.model.CustomUserDetails;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import burundi.ilucky.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        burundi.ilucky.model.User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
//        return new User(user.getUsername(), user.getPassword(), List.of(grantedAuthority));
        return new CustomUserDetails(user);
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        burundi.ilucky.model.User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        UserDetails userDetails = new User(user.getUsername(), user.getPassword(), Arrays.asList(grantedAuthority));
//        return userDetails;
        return new CustomUserDetails(user);
    }

}
