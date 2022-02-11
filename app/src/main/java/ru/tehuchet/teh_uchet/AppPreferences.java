package ru.tehuchet.teh_uchet;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import ru.tehuchet.teh_uchet.data.User;

public class AppPreferences {
    private static final String PREFERENCES_NAME = "preferences";
    private static final String PREFERENCE_USERS = "users";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    DatabaseReference database = null; // lateinit
    Context ctx;

    private static AppPreferences instance = null;

    private AppPreferences() {
    }

    private AppPreferences(Context context) {
        init(context);
    }

    private void init(Context context) {
//        preferences = context.getSharedPreferences(PREFERENCES_NAME, 0);
//        editor = preferences.edit();
        ctx = context;
        database = FirebaseDatabase.getInstance().getReference();
    }

//    public static AppPreferences getInstance(Context context) {
//        if (instance == null) {
//            instance = new AppPreferences(context);
//        }
//
//        return instance;
//    }

//    public AppPreferences deleteUser(User user) {
//
//    }

    public AppPreferences putUsers(List<User> users) {
        return this;
    }

    public AppPreferences addUser(User user) {
        database = FirebaseDatabase.getInstance().getReference();
        String newUserId = "user" + user.getId();
        DatabaseReference childRef = database.child("users").child("user1");

        childRef.setValue("NewValue");
//        childRef.setValue(user).addOnFailureListener(e -> {
//            Log.d("AppPreferences", e.getMessage());
//        }).addOnSuccessListener(unused -> {
//            Log.d("AppPreferences", "Success");
//        }).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                Log.d("AppPreferences", "Compeleted");
//            }
//        });

//        String usersJson = new Gson().toJson(users.toArray());
//        editor.putString(PREFERENCE_USERS, usersJson);
//        editor.apply();
        return this;
    }

    public List<User> getUsers() {
//        String usersJson = preferences.getString(PREFERENCE_USERS, "[]");
//        return Arrays.asList(new Gson().fromJson(usersJson, User[].class));
        return new ArrayList<>();
    }

    public boolean hasUsers() {
        return true;
//        return preferences.getString(PREFERENCE_USERS, null) != null;
    }
}
