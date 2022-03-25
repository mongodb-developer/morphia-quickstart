package com.example.app;

import dev.morphia.annotations.*;
import org.bson.types.ObjectId;

import java.util.List;

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