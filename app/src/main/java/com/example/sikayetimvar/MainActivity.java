package com.example.sikayetimvar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sikayetimvar.databinding.ActivityMainBinding;
import com.example.sikayetimvar.kayitactivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();

        if (user != null){
            Intent intent = new Intent(MainActivity.this, feedactivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void girisyapClick(View view){
        String email =binding.emailtext.getText().toString();
        String password =binding.passwordtext.getText().toString();
        if(email.equals("") || password.equals("")){
            Toast.makeText(this, "E-posta veye Şifre boş bırakılamaz !", Toast.LENGTH_LONG).show();
        }else {
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(MainActivity.this,feedactivity.class);
                startActivity(intent);
                finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "e-posta veya şifre hatalı", Toast.LENGTH_LONG).show();

                }
            });
        }
    }
    public void kayitolClick(View view){
        Intent intent = new Intent(MainActivity.this, kayitactivity.class);
        startActivity(intent);
        finish();
    }
}//asd
