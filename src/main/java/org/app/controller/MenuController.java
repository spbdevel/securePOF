package org.app.controller;

import org.app.entity.Menu;
import org.app.entity.Restaurant;
import org.app.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RequestMapping("/rest")
@RestController()
public class MenuController extends AbstractController {

    @Autowired
    private MenuRepository menuRepository;

    @RequestMapping(value = "/menues", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Menu> todayMenus() {
        return menuRepository.findByActiveToday(true);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/menues/{restId}", method = RequestMethod.POST)
    public Menu add(@Valid @RequestBody Menu menu, @PathVariable("restId") Long rest_id) {
        Restaurant restau = restauRepository.findById(rest_id).orElseThrow(NoSuchElementException::new);;
        menu.setRestaurant(restau);
        menu.setCreated(new Date());
        return menuRepository.save(menu);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/menues/{menuId}", method = RequestMethod.PUT)
    public Menu changeStatus(@PathVariable("menuId") Long id, @RequestParam Boolean activate) {
        Menu menu = menuRepository.findById(id).orElseThrow(NoSuchElementException::new);
        menu.setActiveToday(activate);
        return menuRepository.save(menu);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/menues/{menuId}", method = RequestMethod.DELETE)
    public Boolean del(@PathVariable("menuId") Long id) {
        menuRepository.deleteById(id);
        return true;
    }

}
