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

 import java.util.Calendar;
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
     private MenuRepository menuRepository;

     @Autowired
     private UserRepository userRepository;

     @Autowired
     private VoteRepository voteRepository;


     private final String restName = "restaurant 1";
     private final String dishName = "dish1";
     private final String menuName = "menu 1";
     private final String adminName = "admin";
     private Calendar instance = Calendar.getInstance();

     @Before
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

         //create menu
         Menu menu = new Menu();
         menu.setName(menuName);
         menu.setActiveToday(true);
         menu.setDescription("description 1");
         menu.setRestaurant(restaurantRepository.findByName(restName));
         menuRepository.save(menu);

     }


     @Test
     public void checkMenuFinds() {
         //find by restaurant
         Restaurant rest = restaurantRepository.findByName(restName);
         List<Menu> menus = menuRepository.findByRestaurant(rest);
         Assert.assertNotNull(menus);
         logger.info("menus size: " + menus.size());

         //find existing
         Menu menu = menuRepository.findByName(menuName);
         Assert.assertNotNull(menu);
         //find not existing
         menu = menuRepository.findByName("no_name");
         Assert.assertNull(menu);

         List<Menu> list = menuRepository.findByActiveToday(true);
         Assert.assertNotNull(list);
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
     public void checkVote() {
         User user = userRepository.findByAccountName(adminName).
                 orElseThrow(() -> new IllegalArgumentException("username not found"));;
         Vote vote = voteRepository.findByUserAndModified(user, instance.getTime());
         Assert.assertNotNull(vote);

         List<Vote> votes = voteRepository.findByUser(user);
         Assert.assertNotNull(votes);
         logger.info("dishes size: " + votes.size());

         vote = voteRepository.findByUserTodayBeforeTime(user, instance.getTime());
         Assert.assertNotNull(vote);
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
