package com.example.qr_go;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Random;

/**
 * Class for generating unique usernames
 */
public class UsernameGenerator {
    // TODO add more adjectives and nouns
    // Possible adjectives and nouns which are used for random username generation
    private static String[] adjectives = {"Funky", "Stinky", "Sluggish", "Adorable", ""};
    private static String[] nouns = {"Worm", "Koala", "Scooter", "Skateboard", "Teacher", "Sailor",
                                    "Pirate", "Photographer", ""};
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

        // Generate a new username while the username isn't valid
        if (!isValidUsername(username)) {
            username = generateUsername();
        }
        return username;
    }

    /**
     * Tests if a generated username is valid/unique
     * @param username The username to test
     * @return true if the username is valid, false otherwise
     */
    public static Boolean isValidUsername(String username) {
        // TODO test with Firestore database
        // check database to see if username already exists
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection("Users");
        final Boolean[] valid = new Boolean[1];

        colRef.whereEqualTo("username", username).limit(1).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {
                        if (documentSnapshots.isEmpty()) {
                            valid[0] = true;
                        } else {
                            valid[0] = false;
                        }
                        return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("FAILURE", "Error accessing data");
                    }
                });

        return valid[0];
    }
}
