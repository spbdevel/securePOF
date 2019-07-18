 package org.app;

 import org.app.config.AppConfig;
 import org.app.config.WebSecurity;
 import org.app.entity.*;
 import org.app.repository.*;
 import org.junit.Assert;
 import org.junit.Before;
 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.context.annotation.PropertySource;
 import org.springframework.context.annotation.PropertySources;
 import org.springframework.security.crypto.password.PasswordEncoder;
 import org.springframework.test.context.ContextConfiguration;
 import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 import org.springframework.test.context.web.WebAppConfiguration;
 import org.springframework.web.servlet.config.annotation.EnableWebMvc;

 import java.util.Arrays;
 import java.util.Calendar;
 import java.util.HashSet;
 import java.util.List;
 import java.util.logging.Logger;

 @RunWith(SpringJUnit4ClassRunner.class)
 @ContextConfiguration(classes = {
         WebSecurity.class,
         AppConfig.class
 })
 @PropertySources({
         @PropertySource("classpath:application.properties")
 })
 @EnableWebMvc
 @WebAppConfiguration
 public class DataTests {
     private Logger logger = Logger.getLogger(DataTests.class.getName());

     @Autowired
     private PasswordEncoder passwordEncoder;

     @Autowired
     private DishRepository dishRepository;

     @Autowired
     private RestaurantRepository restaurantRepository;


     @Autowired
     private UserRepository userRepository;


     @Autowired
     private PrivilegeRepository privilegeRepository;


     private final String restName = "restaurant 1";
     private final String dishName = "dish1";
     private final String menuName = "menu 1";
     private final String adminName = "admin";
     private Calendar instance = Calendar.getInstance();

     //@Before
     public void initData() {
         instance.set(Calendar.SECOND, 0);
         instance.set(Calendar.MILLISECOND, 0);
         instance.set(Calendar.MINUTE, 0);
         instance.set(Calendar.HOUR_OF_DAY, 0);

         //create restaurant
         Restaurant rest = restaurantRepository.findByName(restName);
         if(rest != null) {
             logger.info("data already initialized");
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


     }


     @Test
     public void createDish() {
         //find by restaurant
         Restaurant rest = restaurantRepository.findByName(restName);

         Dish dish = new Dish();
         dish.setName("new dish");
         dish.setDescription("desc asdf 2");
         dish.setRestaurant(rest);
         dish.setPrice(110.0);
         Dish save = dishRepository.save(dish);
         save.getId();
     }

     @Test
     public void getDish() {
         Dish byId = dishRepository.findById(41L).get();
         System.out.println(byId.getDescription());
     }


     @Test
     public void initPriviliges() {
         Privilege privilege1 = new Privilege();
         privilege1.setName("FOO_READ_PRIVILEGE");
         privilegeRepository.save(privilege1);

         Privilege privilege2 = new Privilege();
         privilege2.setName("FOO_WRITE_PRIVILEGE");
         privilegeRepository.save(privilege2);

         privilege1 = privilegeRepository.findByName("FOO_READ_PRIVILEGE").get();
         privilege2 = privilegeRepository.findByName("FOO_WRITE_PRIVILEGE").get();

         User user1 = userRepository.findByAccountName("user1").
                 orElseThrow(() -> new IllegalArgumentException("username not found"));
         User user2 = userRepository.findByAccountName("user2").
                 orElseThrow(() -> new IllegalArgumentException("username not found"));

         user2.setPrivileges(new HashSet<>(Arrays.asList(privilege1, privilege2)));
         user2 = userRepository.save(user2);
         user1.setPrivileges(new HashSet<>(Arrays.asList(privilege1)));
         user1 = userRepository.save(user1);
         user1.getRoles();
     }




     @Test
     public void checkDishFinds() {
         //find by restaurant
         Restaurant rest = restaurantRepository.findByName(restName);
         List<Dish> dishes = dishRepository.findByRestaurant(rest);
         Assert.assertNotNull(dishes);
         logger.info("dishes size: " + dishes.size());

         //find existing
         Dish dish = dishRepository.findByName(dishName);
         Assert.assertNotNull(dish);
         //find not existing
         dish = dishRepository.findByName("no_name");
         Assert.assertNull(dish);
     }



     @Test
     public void testEncode() {
         final String rawPassword = "12345";
         String encoded = passwordEncoder.encode(rawPassword);
         boolean matches = passwordEncoder.matches(rawPassword, encoded);
         Assert.assertTrue(matches);
         logger.info("matches " + matches);
     }

 }
