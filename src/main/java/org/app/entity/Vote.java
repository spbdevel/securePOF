package org.app.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "vote")
public class Vote implements Persistable {

    private Long id;
    private Restaurant restaurant;
    private User user;
    private Date modified;
    private boolean isNew = false;

    @JsonIgnore
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", columnDefinition = "bigint not null")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "restaurant_id", columnDefinition = "bigint not null")
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @JsonIgnore
    //@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm a z")
    @Column(nullable = false)
    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Transient
    public Long getRestaurantId() {
        return restaurant.getId();
    }

    @Transient
    public String getRestaurantName() {
        return restaurant.getName();
    }

    @Transient
    public Long getUserId() {
        return user.getId();
    }

    @Transient
    public String getUserName() {
        return user.getAccountName();
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