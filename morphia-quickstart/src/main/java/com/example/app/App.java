package com.example.app;

import com.mongodb.client.MongoClients;

import dev.morphia.Morphia;
import dev.morphia.Datastore;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.DeleteOptions;

import dev.morphia.annotations.Field;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexes;

import org.bson.types.ObjectId;

import dev.morphia.mapping.experimental.MorphiaReference;

import java.util.ArrayList;
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
    private MorphiaReference<List<Ingredient>> ingredients;

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
        return ingredients.get();
    }

    public void setIngredients( List<Ingredient> list) {
        this.ingredients = MorphiaReference.wrap(list);
    }

    public void addIngredient(Ingredient ingredient) {
        List<Ingredient> currentIngredients = this.getIngredients();
        currentIngredients.add(ingredient);
        this.ingredients = MorphiaReference.wrap(currentIngredients);
    }

    public void removeIngredient(Ingredient ingredient) {
        List<Ingredient> currentIngredients = this.getIngredients();
        currentIngredients.remove(ingredient);
        this.ingredients = MorphiaReference.wrap(currentIngredients);
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

// Main class
public class App 
{
    public static void main( String[] args )
    {
        /**** Configure database connection ****/
        // MongoDB Atlas Connection String
        String uri = "mongodb+srv://blog:blog@cluster0.2grje.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";

        // Define a datastore that will connect to the database
        final Datastore datastore = Morphia.createDatastore(
            MongoClients.create(uri), 
            "morphia_quickstart"
        );
        // Bind the entities
        datastore.getMapper().mapPackage("com.example.app");

        // Clean up the database by deleting any existing entries
        datastore.find(Ingredient.class)
            .delete(new DeleteOptions()
                .multi(true)
            );
        datastore.find(Recipe.class)
            .delete(new DeleteOptions().multi(true));

        /**** Create records (Crud) ****/
        // Create ingredients
        final Ingredient bread = new Ingredient("Bread");
        datastore.save(bread);
        final Ingredient peanutButter = new Ingredient("Peanut Butter");
        datastore.save(peanutButter);
        final Ingredient jelly = new Ingredient("Jelly");
        datastore.save(jelly);

        // Create recipe
        final Recipe pbnj = new Recipe("PB & J");
        
        // Create the references
        List<Ingredient> list=new ArrayList<Ingredient>();  
        //Adding elements in the List  
        list.add(bread);  
        list.add(peanutButter);  
        list.add(jelly);
        pbnj.setIngredients(list);
        datastore.save(pbnj);
        System.out.println(pbnj);
        for (Ingredient i : pbnj.getIngredients()) {
            System.out.println(i);
        }

        pbnj.setName("Gourmet PB & J");
        final Ingredient waffle = new Ingredient("Waffle");
        datastore.save(waffle);
        pbnj.removeIngredient(bread);
        pbnj.addIngredient(waffle);
        datastore.save(pbnj);
        System.out.println(pbnj);

        for (Ingredient i : pbnj.getIngredients()) {
            System.out.println(i);
        }
    }
}

