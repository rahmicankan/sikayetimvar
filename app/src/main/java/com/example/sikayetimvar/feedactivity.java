package com.example.sikayetimvar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.sikayetimvar.adapter.PostAdapter;
import com.example.sikayetimvar.databinding.ActivityFeedactivityBinding;
import com.example.sikayetimvar.model.Post;
import com.example.sikayetimvar.MainActivity;
import com.example.sikayetimvar.UploadActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class feedactivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<Post> postArrayList;
    private ActivityFeedactivityBinding binding;
    PostAdapter postAdapter;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeedactivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        postArrayList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getData();

        binding.RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postArrayList);
        binding.RecyclerView.setAdapter(postAdapter);
    }
    private void getData(){
        firebaseFirestore.collection("Posts").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    Toast.makeText(feedactivity.this,error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
                if(value != null){
                 for(DocumentSnapshot snapshot: value.getDocuments()) {
                     Map<String, Object> data = snapshot.getData();
                     //Casting işlemi
                     String userEmail = (String) data.get("useremail");
                     String sikayet = (String) data.get("sikayet");
                     String downloadUrl = (String) data.get("downloadurl");

                     Post post = new Post(userEmail,sikayet,downloadUrl);
                     postArrayList.add(post);
                 }
                 postAdapter.notifyDataSetChanged();
                }
            }
        });

    }
    public void homeButtonclicked (View view){
        Intent intentToFeed = new Intent(feedactivity.this,feedactivity.class);

    }
    public void uploadbuttonclicked(View view){
        Intent intentToUpload = new Intent(feedactivity.this, UploadActivity.class);
        startActivity(intentToUpload);
    }
    public void exitbuttonclicked(View view){
        auth.signOut();

        Intent intentToMain= new Intent(feedactivity.this, MainActivity.class);
        startActivity(intentToMain);
        finish();
    }
}