package org.app.controller;

import org.app.Util;
import org.app.entity.*;
import org.app.repository.AllowedFieldsRepository;
import org.app.repository.DishRepository;
import org.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestMapping("/rest")
@RestController
public class DishController  extends  AbstractController {

    private Logger logger = Logger.getLogger(DishController.class.getName());

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AllowedFieldsRepository allowedFieldsRepository;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = "/dishes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dish> dishes(@AuthenticationPrincipal UserDetails user) {
        List<Dish> all = dishRepository.findAll();

        User curUser = userRepository.findByAccountName(user.getUsername());
        List<Role> roles = curUser.getRoles();
        List<Dish> res = all.stream().map(d -> {
            Dish dish = new Dish();
            if (roles.isEmpty()) {
                return dish;
            }
            AllowedFields field = allowedFieldsRepository.findByRoleAndObjectName(roles.get(0), "dish");

            if (field == null)
                return d;

            String[] split = field.getFieldList().split(", ");

            List<String> fields = Arrays.asList(split);

            Util.copyProperties(d, dish, fields);
            return dish;

        }).collect(Collectors.toList());

        return res;
    }

    private boolean isAdmin(@AuthenticationPrincipal UserDetails user) {
        return user.getAuthorities().stream().map(a -> (SimpleGrantedAuthority)a).anyMatch(a -> a.getAuthority().matches("ROLE_ADMIN"));
    }

    private List<String>  userAuthorities(@AuthenticationPrincipal UserDetails user) {
        return user.getAuthorities().stream().map(a -> (SimpleGrantedAuthority) a).map(a -> a.getAuthority()).collect(Collectors.toList());
    }


    @RequestMapping(value = "/dishes/{restId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dish> dishesByRestaurant(@PathVariable("restId")Long id) {
        Restaurant restau = restauRepository.findById(id).orElseThrow(NoSuchElementException::new);
        logger.info("restau: " + restau.getName());
        return dishRepository.findByRestaurant(restau);
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = "/dishes", method = RequestMethod.POST)
    public Dish add(@Valid @RequestBody Dish dish) {
        Dish saved = dishRepository.save(dish);
        return saved;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/dishes/{restId}", method = RequestMethod.POST)
    public Dish add(@Valid @RequestBody Dish dish, @PathVariable("restId") Long rest_id) {
        Restaurant restau = restauRepository.findById(rest_id).orElseThrow(NoSuchElementException::new);
        dish.setRestaurant(restau);
        Dish saved = dishRepository.save(dish);
        return saved;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/dishes/{dishId}", method = RequestMethod.DELETE)
    public Boolean del(@PathVariable("dishId") Long id) {
        dishRepository.deleteById(id);
        return true;
    }


}
