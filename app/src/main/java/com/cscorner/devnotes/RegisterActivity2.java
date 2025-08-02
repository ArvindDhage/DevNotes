package com.cscorner.devnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity2 extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText mob;
    private EditText pass;
    private MaterialButton btn;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;
    private TextView loginnow;

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            // User is already logged in, go to home screen
            startActivity(new Intent(RegisterActivity2.this, HomeActivity2.class));
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register2);

        mAuth = FirebaseAuth.getInstance();


        databaseRef = FirebaseDatabase.getInstance().getReference();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mob = findViewById(R.id.phone);
        pass = findViewById(R.id.pass);
        btn = findViewById(R.id.register);
        progressbar=findViewById(R.id.progressbar);
        loginnow=findViewById(R.id.loginnow);

        loginnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class );
                startActivity(intent);
                finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                String userName = name.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String userMob = mob.getText().toString().trim();
                String userPass = pass.getText().toString().trim();

                if (userName.isEmpty() || userEmail.isEmpty() || userMob.isEmpty() || userPass.isEmpty()) {
                    Toast.makeText(RegisterActivity2.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return; // अगर कोई फील्ड खाली है तो आगे न बढ़ें
                }
                mAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(task -> {
                    progressbar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {

                        //if Auth Success, Push other details to Real time Database

                        HashMap<String, Object> userMap = new HashMap<String, Object>(); // 'm' की जगह 'userMap' बेहतर नाम है
                        userMap.put("name", userName);
                        userMap.put("email", userEmail);
                        userMap.put("mob", userMob);
                        userMap.put("pass", userPass);

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String uid = firebaseUser != null ? firebaseUser.getUid() : databaseRef.push().getKey();

                        // यहाँ बदलाव है: .push() का उपयोग करें और listeners जोड़ें
                        databaseRef.child("users").push().setValue(userMap)
                                .addOnSuccessListener(aVoid -> {
                                    // डेटा सफलतापूर्वक भेजा गया!
                                    Toast.makeText(RegisterActivity2.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                    Log.d("RegisterActivity2", "User data pushed successfully!");
                                    // Optionally clear fields
                                    name.setText("");
                                    email.setText("");
                                    mob.setText("");
                                    pass.setText("");
                                })
                                .addOnFailureListener(e -> {
                                    // डेटा भेजने में त्रुटि हुई!
                                    Toast.makeText(RegisterActivity2.this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    Log.e("RegisterActivity2", "Error pushing user data", e);
                                });
                    } else {
                        Toast.makeText(RegisterActivity2.this, "Auth Failed" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
    }
}










