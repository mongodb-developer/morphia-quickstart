package com.mongodb.morphia;

import com.mongodb.client.MongoClients;
import com.mongodb.morphia.entities.Ingredient;
import com.mongodb.morphia.entities.Recipe;
import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.Morphia;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Sort;

import java.util.ArrayList;
import java.util.List;

import static dev.morphia.query.experimental.filters.Filters.eq;

public class App {

    private final static String DATABASE = "morphia_quickstart";

    public static void main(String[] args) {
        String uri = System.getenv("MONGODB_URI");

        // Define a datastore that will connect to the database
        final Datastore datastore = Morphia.createDatastore(MongoClients.create(uri), DATABASE);
        // Configure the data store
        datastore.getMapper().mapPackage("com.mongodb.morphia.entities");
        datastore.ensureIndexes();

        // Clean up the database by deleting any existing entries
        datastore.find(Ingredient.class).delete(new DeleteOptions().multi(true));
        datastore.find(Recipe.class).delete(new DeleteOptions().multi(true));

        // Create ingredients
        Ingredient bread = new Ingredient("Bread");
        Ingredient peanutButter = new Ingredient("Peanut Butter");
        Ingredient jelly = new Ingredient("Jelly");
        Ingredient flour = new Ingredient("Flour");
        datastore.save(bread);
        datastore.save(peanutButter);
        datastore.save(jelly);
        datastore.save(flour);

        // Query and sort the ingredients collection
        List<Ingredient> ingredientList = datastore.find(Ingredient.class)
                                                   .iterator(new FindOptions().sort(Sort.descending("name")))
                                                   .toList();
        System.out.println("List of ingredients in our MongoDB Collection:");
        ingredientList.forEach(System.out::println);
        System.out.println("");

        // Update an ingredient to change its name
        System.out.println("Updating the Flour to Whole Weat Flour so it's healthy.");
        flour.setName("Whole Wheat Flour");
        flour.setHealthy(true);
        datastore.save(flour);

        // List healthy ingredients
        List<Ingredient> healthyIngredients = datastore.find(Ingredient.class).filter(eq("healthy", true)).iterator().toList();
        System.out.println("Healthy ingredients in the collection:");
        healthyIngredients.forEach(System.out::println);
        System.out.println("");

        // Create recipe
        System.out.println("Create and save a new Recipe.");
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(bread);
        ingredients.add(peanutButter);
        ingredients.add(jelly);
        Recipe pbnj = new Recipe("PB & J", ingredients);
        datastore.save(pbnj);

        // Fetching the references automatically fetches the associated documents
        System.out.println("Retrieving the Recipe document from the database using the reference.");
        System.out.println(pbnj);
        System.out.println("");

        // Updating the references
        System.out.println("Updating the recipe to make it gourmet.");
        pbnj.setName("Gourmet PB & J");
        Ingredient waffle = new Ingredient("Waffle");
        datastore.save(waffle);
        List<Ingredient> pbnjIngredients = pbnj.getIngredients();
        pbnjIngredients.remove(bread);
        pbnjIngredients.add(waffle);
        datastore.save(pbnj);
        System.out.println(pbnj);
    }
}

