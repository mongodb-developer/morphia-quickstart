package com.example.app;

import com.mongodb.client.MongoClients;

import dev.morphia.Morphia;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;

import dev.morphia.query.Query;
import dev.morphia.query.FindOptions;
import dev.morphia.query.MorphiaCursor;
import dev.morphia.query.Sort;

import static dev.morphia.query.experimental.filters.Filters.eq;

import java.util.ArrayList;
import java.util.List;

// Main class
public class App 
{
    public static void main( String[] args )
    {
        // MongoDB Atlas Connection String
        String uri = System.getenv("MONGODB_URI");
        System.out.println("Using " + uri);
        String database = "morphia_quickstart";

        // Define a datastore that will connect to the database
        final Datastore datastore = Morphia.createDatastore(
            MongoClients.create(uri), 
            database
        );
        // Configure the data store
        datastore.getMapper().mapPackage("com.example.app");
        datastore.ensureIndexes();

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
        final Ingredient flour = new Ingredient("Flour");
        datastore.save(flour);

        // Query and count the ingredients collection
        MorphiaCursor<Ingredient> ingredientsQuery = datastore.find(Ingredient.class)
            .iterator(new FindOptions().sort(Sort.descending("name")));

        while (ingredientsQuery.hasNext()) {
            Ingredient i = ingredientsQuery.next();
            System.out.println(i);
        }
        ingredientsQuery.close();

        // Query<Ingredient> ingredientsQuery = datastore.find(Ingredient.class);
                
        // System.out.println("There are " + ingredientsQuery.count() + " ingredients in the collection");
        // for (Ingredient ingredient : ingredientsQuery) {
        //     System.out.println(ingredient);
        // }

        // Update an ingredient to change its name
        flour.setName("Whole Wheat Flour");
        flour.setHealthy(true);
        datastore.save(flour);

        // List healthy ingredients
        Query<Ingredient> healthyIngredientsQuery = datastore.find(Ingredient.class)
            .filter(eq("healthy", true));
        System.out.println("Healthy ingredients in the collection");
        for (Ingredient ingredient : healthyIngredientsQuery) {
            System.out.println(ingredient);
        }

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

        // Updating the references
        pbnj.setName("Gourmet PB & J");
        final Ingredient waffle = new Ingredient("Waffle");
        datastore.save(waffle);
        pbnj.getIngredients().remove(bread);
        pbnj.getIngredients().add(waffle);
        datastore.save(pbnj);
        System.out.println(pbnj);
    }
}

