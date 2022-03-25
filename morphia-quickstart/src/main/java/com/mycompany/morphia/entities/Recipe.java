package com.mycompany.morphia.entities;

import dev.morphia.annotations.*;
import org.bson.types.ObjectId;

import java.util.List;

@Entity("recipes")
@Indexes(@Index(fields = @Field(value = "name")))
public class Recipe {
    @Id
    private ObjectId id;
    private String name;
    @Reference
    private List<Ingredient> ingredients;

    // Constructors
    public Recipe() { }
    public Recipe(String name, List<Ingredient> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
    }

    // Getters and Setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Ingredient> getIngredients() { return this.ingredients; }
    public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; } 
    
    // toString for output
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder(this.name);
        List<Ingredient> ingredients = this.getIngredients();
        output.append(" - ")
              .append(ingredients.size())
              .append(" ingredients with MongoDB _id: ")
              .append(this.id);
        for (Ingredient ingredient : ingredients) {
            output.append("\n").append(ingredient);
        }
        return output.toString();
    }
}