package org.app.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dish", uniqueConstraints={@UniqueConstraint(columnNames={"dish_name", "restaurant_id"})})
public class Dish implements Persistable {

    public static final String MYSECRET = "mysecret";
    public static final String AES_DESCRIPTION_DECRYPT = "cast(aes_decrypt(description, '" + MYSECRET +"') as char(255))";
    public static final String AES_ENCRYPT = "aes_encrypt(?, '"+ MYSECRET +"')";
    private Long id;
    private Restaurant restaurant;
    private String name;
    private String description;
    private Double price;
    private boolean isNew = false;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Length(max = 50)
    @Column(name = "dish_name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", columnDefinition = "bigint not null", nullable = false)
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @ColumnTransformer(
            read = AES_DESCRIPTION_DECRYPT,
            write = AES_ENCRYPT
    )    @Length(max = 50)
   public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
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