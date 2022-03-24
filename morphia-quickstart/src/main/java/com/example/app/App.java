package com.example.app;

import com.mongodb.client.MongoClients;

import dev.morphia.Morphia;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;

import java.util.ArrayList;
import java.util.List;

// Main class
public class App 
{
    public static void main( String[] args )
    {
        // MongoDB Atlas Connection String
        String uri = "mongodb+srv://blog:blog@cluster0.2grje.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
        String database = "morphia_quickstart";

        // Define a datastore that will connect to the database
        final Datastore datastore = Morphia.createDatastore(
            MongoClients.create(uri), 
            database
        );
        // Bind the entities
        datastore.getMapper().mapPackage("com.example.app");

        // Clean up the database by deleting any existing entries
        datastore.find(Ingredient.class)
            .delete(new DeleteOptions().multi(true));
        datastore.find(Recipe.class)
            .delete(new DeleteOptions().multi(true));

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
        List<Ingredient> ingredients = new ArrayList<Ingredient>();  
        //Adding elements in the List  
        ingredients.add(bread);  
        ingredients.add(peanutButter);  
        ingredients.add(jelly);
        pbnj.setIngredients(ingredients);
        datastore.save(pbnj);

        // Fetching the references automatically fetches the associated documents
        System.out.println(pbnj);
        for (Ingredient i : pbnj.getIngredients()) {
            System.out.println(i);
        }

        // Updating the references
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

