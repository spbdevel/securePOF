 package org.app;

 import org.app.config.AppConfig;
 import org.app.config.WebSecurity;
 import org.app.entity.AllowedFields;
 import org.app.entity.Dish;
 import org.app.entity.Role;
 import org.app.repository.AllowedFieldsRepository;
 import org.app.repository.DishRepository;
 import org.app.repository.RoleRepository;
 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.springframework.beans.BeanUtils;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.context.annotation.PropertySource;
 import org.springframework.context.annotation.PropertySources;
 import org.springframework.test.context.ContextConfiguration;
 import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 import org.springframework.test.context.web.WebAppConfiguration;
 import org.springframework.web.servlet.config.annotation.EnableWebMvc;

 import java.beans.PropertyDescriptor;
 import java.util.Arrays;
 import java.util.Collection;
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
 public class NewTests {
     private Logger logger = Logger.getLogger(NewTests.class.getName());


     @Autowired
     private RoleRepository roleRepository;

     @Autowired
     private AllowedFieldsRepository allowedFieldsRepository;

     @Autowired
     private DishRepository dishRepository;



     @Test
     public void checkGetFields() {
         Role one = roleRepository.findByName("ROLE_ADMIN");
         AllowedFields field = allowedFieldsRepository.findByRoleAndObjectName(one, "dish");

         String[] split = field.getFieldList().split(", ");
         List<String> fields = Arrays.asList(split);
         fields = fields.subList(1,3);
         Dish dish = dishRepository.findAll().get(0);
         Dish dishCleaned = new Dish();
         //BeanUtils.copyProperties(dish, dishCleaned, split);
         //BeanUtils.copyProperties(dish, dishCleaned, split);

         copyProperties2(dish, dishCleaned, fields);

         System.out.println(dishCleaned);
     }

     public static void copyProperties2(Object src, Object trg, Collection<String> props) {
         String[] excludedProperties =
                 Arrays.stream(BeanUtils.getPropertyDescriptors(src.getClass()))
                         .map(PropertyDescriptor::getName)
                         .filter(name -> !props.contains(name))
                         .toArray(String[]::new);

         BeanUtils.copyProperties(src, trg, excludedProperties);
     }

     @Test
     public void initFields() {
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
