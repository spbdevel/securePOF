package org.app.controller;

import org.app.entity.Dish;
import org.app.entity.Restaurant;
import org.app.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

@RequestMapping("/rest")
@RestController
public class DishController  extends  AbstractController {

    private Logger logger = Logger.getLogger(DishController.class.getName());

    @Autowired
    private DishRepository dishRepository;

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping(value = "/dishes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Dish> dishes(@AuthenticationPrincipal UserDetails user) {
        return dishRepository.findAll();
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
