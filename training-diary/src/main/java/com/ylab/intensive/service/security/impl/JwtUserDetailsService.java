package com.ylab.intensive.service.security.impl;

import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.repository.UserDao;
import com.ylab.intensive.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    /**
     * This DAO is responsible for data access operations related to users.
     */
    private final UserDao userRepository;

    /**
     * Loads user details by username.
     *
     * @param username The username for which to load details.
     * @return UserDetails containing user details.
     * @throws NotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username){
        return userRepository.findByEmail(username)
                .map(JwtUserDetails::new)
                .orElseThrow(()-> new NotFoundException("User %s not found".formatted(username)));
    }

    /**
     * UserDetails implementation for JwtUserDetailsService.
     */
    private record JwtUserDetails(User user) implements UserDetails {
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString()));
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
