package com.mycompany.morphia.entities;

import dev.morphia.annotations.*;
import org.bson.types.ObjectId;

@Entity("ingredients")
@Indexes(@Index(options = @IndexOptions(name = "name"), fields = @Field("name")))
public class Ingredient {
    @Id
    private ObjectId id;
    private String name;
    private Boolean healthy;

    // Constructors
    public Ingredient() { }
    public Ingredient(String name) {
        this.name = name;
        this.healthy = false;
    }

    // Getters and Setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Boolean getHealthy() { return healthy; }
    public void setHealthy(Boolean healthy) { this.healthy = healthy; }

    // toString for output
    @Override
    public String toString() {
        return " - " + name + " -> healthy: " + healthy;
    }
}