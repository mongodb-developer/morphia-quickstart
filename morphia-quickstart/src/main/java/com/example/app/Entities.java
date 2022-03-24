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
        return this.name + " - " + this.getIngredients().size() + " ingredients";
    }

    public List<Ingredient> getIngredients() {
        // return ingredients.get();
        return this.ingredients;
    }

    public void setIngredients( List<Ingredient> list) {
        // this.ingredients = MorphiaReference.wrap(list);
        this.ingredients = list;
    }

    public void addIngredient(Ingredient ingredient) {
        List<Ingredient> currentIngredients = this.getIngredients();
        currentIngredients.add(ingredient);
        this.setIngredients(currentIngredients);
    }

    public void removeIngredient(Ingredient ingredient) {
        List<Ingredient> currentIngredients = this.getIngredients();
        currentIngredients.remove(ingredient);
        this.setIngredients(currentIngredients);
    }
}

// Ingredient Model
@Entity("ingredients")
@Indexes(@Index(options = @IndexOptions(name = "name"), fields = @Field("name")))
class Ingredient {
    @Id
    private ObjectId id;
    private String name;

    Ingredient(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}