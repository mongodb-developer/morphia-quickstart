package com.example.app;

import com.mongodb.client.MongoClients;

import dev.morphia.Morphia;
import dev.morphia.query.internal.MorphiaCursor;
import com.mongodb.client.result.UpdateResult;
import dev.morphia.Datastore;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Reference;
import dev.morphia.DeleteOptions;
import dev.morphia.UpdateOptions;
import dev.morphia.query.Query;
import org.bson.types.ObjectId;

import static dev.morphia.query.experimental.filters.Filters.lte;
import static dev.morphia.query.experimental.filters.Filters.eq;
import static dev.morphia.query.experimental.updates.UpdateOperators.mul;

/**** Define entities ****/
// Books Model
@Entity("books")
class Book {
    @Id
    private ObjectId id;
    private String title;
    private Author author;
    private Double price;
    private Boolean onSale;

    Book() {
    }

    Book(String title, Author author, Double price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public String toString() {
        return this.title + " by " + this.author.toString() + " -- " + this.price + "$";
    }

    public void setOnSale() {
        this.price = this.price * 0.5;
        this.onSale = true;
    }
}

// Authors Model
@Entity("authors")
class Author {
    @Id
    private ObjectId id;
    private String firstName;
    private String lastName;

    Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String toString() {
        return this.firstName + " " + this.lastName;
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
        // Bind the Book and Author entities
        datastore.getMapper().mapPackage("com.example.app");

        // Clean up the database by deleting any existing entries
        datastore.find(Book.class)
            .delete(new DeleteOptions()
                .multi(true)
            );

        /**** Create records (Crud) ****/
        // Create authors
        final Author carroll = new Author("Lewis", "Carroll");
        datastore.save(carroll);
        final Author tolkien = new Author("John Ronald Reuel", "Tolkien");
        datastore.save(tolkien);
        final Author kafka = new Author("Franz", "Kafka");
        datastore.save(kafka);

        // Create books
        final Book alice = new Book("Alice's Adventures In Wonderland", carroll, 20.0);
        datastore.save(alice);
        final Book hobbit = new Book("The Hobbit", tolkien, 8.0);
        datastore.save(hobbit);
        final Book lotr = new Book("Lord of the Ring", tolkien, 30.0);
        datastore.save(lotr);
        final Book metamorphosis = new Book("The Metamorphosis", kafka, 10.0);
        datastore.save(metamorphosis);

        /**** Read from the database (cRud) ****/
        // Count the number of books in the collection
        Query<Book> query = datastore.find(Book.class);
        final long booksCount = query.count();
        System.out.println("Found " + booksCount + " books");

        // Find all books by Tolkien
        final Query<Book> tolkienBooks = datastore.find(Book.class)
            .filter("author", tolkien);
        for (Book book : tolkienBooks) {
            System.out.println(book.toString());
        }

        /**** Update records (crUd) ****/
        // Set the Tolkien books on sale
        for (Book book : tolkienBooks) {
            book.setOnSale();
            datastore.save(book);
        }

        for (Book book : tolkienBooks) {
            System.out.println(book.toString());
        }

        /**** Delete records (cruD) ****/
        datastore.find(Book.class)
            .filter(eq("onSale", true))
            .delete(new DeleteOptions()
                .multi(true)
            );
    }
}

