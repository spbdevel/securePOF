package org.app.repository.loader;

import org.app.entity.*;
import org.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private AllowedFieldsRepository allowedFieldsRepository;

    BiConsumer<User, Role> biConsumer = (x, y) -> {
        x.getRoles().add(y);
        userRepository.save(x);
    };

/*

    @Override
    public void run(String... strings) throws Exception {
        User user2 = userRepository.findByAccountName("user2");
        Role role_user = roleRepository.findByName("ROLE_USER");
        biConsumer.accept(user2, role_user);
    }
*/

    @Override
    public void run(String... strings) throws Exception {
        User frodo = userRepository.findByAccountName("frodo").
                orElseThrow(() -> new IllegalArgumentException("username not found"));
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

        Role editorRole = new Role();
        editorRole .setName("ROLE_EDITOR");
        editorRole = roleRepository.save(editorRole );


        biConsumer.accept(admin, admRole);
        biConsumer.accept(user1, userRole);
        biConsumer.accept(user2, userRole);
        biConsumer.accept(user2, editorRole);

        initData();
    }


    public void initData() {
        String restName = "restaurant 1";
        String dishName = "dish1";
        String menuName = "menu 1";

        //create restaurant
        Restaurant rest = restaurantRepository.findByName(restName);
        if(rest != null) {
            return;
        }
        rest = new Restaurant();
        rest.setName(restName);
        rest.setDescription("desc restaurant 1");
        restaurantRepository.save(rest);
        rest = restaurantRepository.findByName(restName);

        //create dish
        Dish dish = new Dish();
        dish.setName(dishName);
        dish.setDescription("desc dish 1");
        dish.setRestaurant(rest);
        dish.setPrice(100.0);
        dishRepository.save(dish);

        //create menu
        Menu menu = new Menu();
        menu.setName(menuName);
        menu.setActiveToday(true);
        menu.setDescription("description 1");
        menu.setRestaurant(restaurantRepository.findByName(restName));
        menuRepository.save(menu);

        AllowedFields afs = new AllowedFields();
        afs.setFieldList("id, restaurant, name, description, price");
        afs.setObjectName("dish");
        afs.setRole(roleRepository.findByName("ROLE_ADMIN"));
        AllowedFields save = allowedFieldsRepository.save(afs);


        afs = new AllowedFields();
        afs.setFieldList("name, description");
        afs.setObjectName("dish");
        afs.setRole(roleRepository.findByName("ROLE_USER"));
        save = allowedFieldsRepository.save(afs);
    }

}