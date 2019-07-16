package org.app.entity;


import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
@Table(name = "user_field")
public class UserField {

    public enum  TypeEnum {
        form_field
    }

    public enum  Category {
        contact_information
    }

    private Long id;
    private String apiName;
    private String layoutMetadata;
    private TypeEnum type;
    private Category category;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Length(max = 50)
    @Column(name = "api_name", nullable = false)
    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    @Column(name = "field_type", nullable = false)
    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    @Column(name = "layout_metadata", nullable = false, columnDefinition = "json")
    public String getLayoutMetadata() {
        return layoutMetadata;
    }

    public void setLayoutMetadata(String layoutMetadata) {
        this.layoutMetadata = layoutMetadata;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}