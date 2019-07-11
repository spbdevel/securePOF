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
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByAccountName(username);
        Optional<User> opt = Optional.ofNullable(user);
        if (!opt.isPresent()) {
            throw new UsernameNotFoundException("username not found");
        }
        Collection<Role> roles = Collections.synchronizedList(user.getRoles());
        StringBuffer sb = new StringBuffer();
        roles.stream().forEach(e -> {
            if(sb.length() !=0) sb.append(", ");
            sb.append(e.getName());
        });
        List<GrantedAuthority> auth = AuthorityUtils.createAuthorityList(sb.toString());
        String password = user.getPassword();
        return new org.springframework.security.core.userdetails.User(username, password, auth);
    }

}