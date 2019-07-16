package org.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "field_permission")
public class FieldPermission {

    public enum ACCESS_TYPE {NONE, VIEW, CREATE, EDIT}

    private Long id;

    private Role role;

    private UserField field;

    private ACCESS_TYPE accessType = ACCESS_TYPE.NONE;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "role_id", columnDefinition = "bigint not null")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_field_id", columnDefinition = "bigint not null")
    public UserField getField() {
        return field;
    }

    public void setField(UserField field) {
        this.field = field;
    }

    @Column(name = "access_type", nullable = false)
    public ACCESS_TYPE getAccessType() {
        return accessType;
    }

    public void setAccessType(ACCESS_TYPE accessType) {
        this.accessType = accessType;
    }
}