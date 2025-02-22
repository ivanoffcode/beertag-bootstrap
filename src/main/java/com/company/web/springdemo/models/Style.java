package com.company.web.springdemo.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "styles")
public class Style {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "style_id")
    private int id;

    @Column(name = "name")
    private String name;

    public Style() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Style style = (Style) o;
        return id == style.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
