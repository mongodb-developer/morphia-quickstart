package com.example.app;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import dev.morphia.annotations.Field;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexes;
import dev.morphia.annotations.Reference;

import org.bson.types.ObjectId;

import java.util.List;

/**** Define entities ****/

// Ingredient Model
@Entity("ingredients")
@Indexes(@Index(options = @IndexOptions(name = "name"), fields = @Field("name")))
class Ingredient {
    @Id
    private ObjectId id;
    private String name;
    private Boolean healthy;

    Ingredient(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setHealthy(Boolean healthy) {
        this.healthy = healthy;
    }

    public String toString() {
        return this.name;
    }
}

// Recipe Model
@Entity("recipes")
@Indexes(
    @Index(fields = @Field(value = "name"))
)
class Recipe {
    @Id
    private ObjectId id;
    private String name;
    @Reference
    private List<Ingredient> ingredients;

    Recipe() {
    }

    Recipe(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        String output = this.name;
        List<Ingredient> ingredients = this.getIngredients();
        output += " - " + ingredients.size() + " ingredients";
        for (Ingredient ingredient : ingredients) {
            output += "\n  - " + ingredient;
        }
        return output;
    }

    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    public void setIngredients( List<Ingredient> list) {
        this.ingredients = list;
    }
}