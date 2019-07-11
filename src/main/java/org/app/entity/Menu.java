package org.app.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints={@UniqueConstraint(columnNames={"menu_name", "restaurant_id"})})
public class Menu implements Persistable {

    private Long id;
    private String name;
    private Restaurant restaurant;
    private List<Dish> dishes;
    private String description;
    private boolean activeToday = false;
    private Date created = new Date();
    private boolean isNew = false;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "menu_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", columnDefinition = "bigint not null")
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Transient
    public String getRestaurantName() {
        return restaurant.getName();
    }

    @Transient
    public Long getRestaurantId() {
        return restaurant.getId();
    }

   public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "dish_menu_lnk", joinColumns = { @JoinColumn(name = "menu_id") }, inverseJoinColumns = { @JoinColumn(name = "dish_id") })
    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Column(name="active_today")
    public boolean isActiveToday() {
        return activeToday;
    }

    public void setActiveToday(boolean activeToday) {
        this.activeToday = activeToday;
    }

    @JsonIgnore
    //@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm a z")
    @Column(nullable = false)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}