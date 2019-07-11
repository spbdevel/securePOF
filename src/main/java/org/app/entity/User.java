package org.app.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Persistable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user")
public class User implements Persistable {

    private Long id;
    private String accountName;
    private String firstName;
    private String familyName;
    private String description;
    private String password;
    private List<Role> roles;
    private boolean isNew = false;

    public User(){}

    public User(String accountName, String firstName, String familyName, String description, String password, List<Role> roles) {
        this.accountName = accountName;
        this.firstName = firstName;
        this.familyName = familyName;
        this.description = description;
        this.password = password;
        this.roles = roles;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "account_name", unique = true, nullable = false)
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String accountName) {
        this.firstName = accountName;
    }


    @Column(name = "family_name")
    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @JsonIgnore
    @Column(name = "user_password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @JsonIgnore
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_lnk", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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