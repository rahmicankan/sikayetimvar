package com.example.sikayetimvar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.sikayetimvar.databinding.ActivityKayitactivityBinding;
import com.example.sikayetimvar.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class kayitactivity extends AppCompatActivity {

    private ActivityKayitactivityBinding binding;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKayitactivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

    }
    public void kayitolClick(View view){

        String email =binding.emailtext.getText().toString();
        String sifre =binding.sifretext.getText().toString();

        if(email.equals("") || sifre.equals("")){
            Toast.makeText(this, "Formda boş alan bırakılmaz !", Toast.LENGTH_LONG).show();
        }else{
            auth.createUserWithEmailAndPassword(email,sifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(kayitactivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(kayitactivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void girisyapClick(View view){
        Intent intent = new Intent(kayitactivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}