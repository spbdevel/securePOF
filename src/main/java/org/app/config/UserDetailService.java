package org.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.app.entity.Role;
import org.app.entity.User;
import org.app.repository.UserRepository;
import java.util.Collections;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByAccountName(username).
                orElseThrow(() -> new IllegalArgumentException("username not found"));
        Optional<User> opt = Optional.ofNullable(user);
        if (!opt.isPresent()) {
            throw new UsernameNotFoundException(user.getAccountName());
        }

        List<String> auths = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        String[] roles = new String[auths.size()];
        auths.toArray(roles);

        List<GrantedAuthority> auth = auths.size() > 0 ?
                AuthorityUtils.createAuthorityList(roles) : Collections.EMPTY_LIST ;
        String password = user.getPassword();
        return new org.springframework.security.core.userdetails.User(username, password, auth);
    }

}