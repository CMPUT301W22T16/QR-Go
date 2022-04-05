package com.example.qr_go.utils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

/**
 * Class for generating usernames
 */
public class UsernameGenerator {
    // TODO add more adjectives and nouns
    // Possible adjectives and nouns which are used for random username generation
    private static String[] adjectives = {"Funky", "Stinky", "Sluggish", "Adorable", "Fuzzy",
                                            "Jolly", "Bold", "Gentle", "Calm", "Timid", "Hasty",
                                            "Bashful", "Quirky", "Sassy", "Naughty", "Brave",
                                            "Adamant", "Hardy", "Relaxed", "Naive", "Serious", "Docile", "Modest"};
    private static String[] nouns = {"Worm", "Koala", "Scooter", "Skateboard", "Teacher", "Coder",
                                    "Pirate", "Photographer", "Kid", "Engineer", "Collector", "Camper", "Maniac" };
    // Number of numbers to append to the end of a username to further make it unique. Can be made
    // larger for more uniqueness
    private static int numNumbers = 4;

    /**
     *  Generates a random username using given adjectives, nouns, and number of numbers
     * @return generated username
     */
    public static String generateUsername() {
        Random rand = new Random();
        int lenAdjectives = adjectives.length;
        int lenNouns = nouns.length;

        // Generate a username
        String username = adjectives[rand.nextInt(lenAdjectives)] + nouns[rand.nextInt(lenNouns)];
        for (int i = 0; i < numNumbers; i++) {
            Integer toAdd = rand.nextInt(10);
            username += toAdd.toString();
        }

        return username;
    }
}
