 package org.app;

 import org.app.config.AppConfig;
 import org.app.config.WebSecurity;
 import org.app.entity.*;
 import org.app.repository.*;
 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.context.annotation.PropertySource;
 import org.springframework.context.annotation.PropertySources;
 import org.springframework.test.context.ContextConfiguration;
 import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 import org.springframework.test.context.web.WebAppConfiguration;
 import org.springframework.web.servlet.config.annotation.EnableWebMvc;

 import java.beans.PropertyDescriptor;
 import java.util.*;
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

     @Autowired
     private UserRepository userRepository;

     @Autowired
     private UserDataRepository userDataRepository;

     @Autowired
     private UserFieldRepository userFieldRepository;



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

         Util.copyProperties(dish, dishCleaned, fields);

         System.out.println(dishCleaned);
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

     @Test
     public void initUserFields() {
         UserField uf = new UserField();
         uf.setApiName("birthdate");
         uf.setType(UserField.TypeEnum.form_field );
         uf.setCategory(UserField.Category.contact_information);
         uf.setLayoutMetadata("{\"type\": \"date\", \"size\": 2}");

         uf = userFieldRepository.save(uf);

         User user2 = userRepository.findByAccountName("user2");
         UserData  userData = new UserData();
         userData.setCreated(new Date());
         userData.setField(uf);
         userData.setUser(user2);
         userData.setModified(new Date());
         userData.setModifiedBy(user2);
         userData.setValue("01-01-1980");
         userData = userDataRepository.save(userData);

         userData.getId();

     }

     @Test
     public void userFieldsData() {
         UserField one = userFieldRepository.findById(13L).get();
         String layoutMetadata = one.getLayoutMetadata();
         layoutMetadata.length();

         UserData byId = userDataRepository.findById(16L).get();
         User user = byId.getUser();
         user.getId();

     }


 }
