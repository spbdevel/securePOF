package org.app.entity;

import javax.persistence.*;

@Entity
@Table(name = "allowed_fields")
public class AllowedFields {

    private Long id;

    private Role role;

    private String objectName;

    private String fieldList;

    private boolean isNew = false;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "role_id", columnDefinition = "bigint not null", unique = true)
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Column(name = "object_name")
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    @Column(name = "field_name")
    public String getFieldList() {
        return fieldList;
    }

    public void setFieldList(String fieldList) {
        this.fieldList = fieldList;
    }

}