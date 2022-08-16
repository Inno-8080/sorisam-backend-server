package com.sparta.sorisam.security;

import com.sparta.sorisam.Model.User;
import com.sparta.sorisam.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User findUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find " + username));

        if(findUser != null) {
            return UserDetailsImpl.builder()
                    .username(findUser.getUsername())
                    .password(findUser.getPassword())
                    .img(findUser.getImg())
                    .intro(findUser.getIntro())
                    .build();
        }
        return null;
    }
}
