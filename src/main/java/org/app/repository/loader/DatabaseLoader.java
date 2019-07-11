package org.app.repository.loader;

import org.app.entity.Role;
import org.app.entity.User;
import org.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiConsumer;

@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public void run(String... strings) throws Exception {
        User frodo = userRepository.findByAccountName("frodo");
        Optional<User> opt = Optional.ofNullable(frodo);
        if(opt.isPresent())
            return;
        userRepository.save(new User("frodo", "Frodo", "Baggins", "", passwordEncoder.encode("12345"), null));
        User admin = userRepository.save(new User("admin", "Admin", "Zloy", "", passwordEncoder.encode("12345"), null));
        User user1 = userRepository.save(new User("user1", "Vasia", "Petrov", "", passwordEncoder.encode("12345"), null));
        User user2 = userRepository.save(new User("user2", "Kostia", "Ivanov", "", passwordEncoder.encode("12345"), null));

        Role admRole = new Role();
        admRole.setName("ROLE_ADMIN");
        admRole = roleRepository.save(admRole);

        //create regular users
        Role userRole = new Role();
        userRole.setName("ROLE_USER");
        userRole = roleRepository.save(userRole);

        BiConsumer<User, Role> biConsumer = (x, y) -> {
            x.setRoles(Arrays.asList(y));
            userRepository.save(x);
        };

        biConsumer.accept(admin, admRole);
        biConsumer.accept(user1, userRole);
        biConsumer.accept(user2, userRole);
    }
}