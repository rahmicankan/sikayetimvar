package com.example.sikayetimvar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.sikayetimvar.R;
import com.example.sikayetimvar.databinding.ActivityUploadBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {
    private FirebaseStorage firebaseStorage;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    Uri imageData;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    private ActivityUploadBinding binding;

     CheckBox checkBox;
     ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        registerLauncher();

        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();

          checkBox = findViewById(R.id.check);
          imageView = findViewById(R.id.imageView);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    imageView.setVisibility(View.VISIBLE);
                }else{

                    imageView.setVisibility(View.GONE);
                }
            }
        });
    }

    public void şikayetgonderevent(View view){

    if(imageData != null && checkBox.isChecked() == true && binding.yorumyazEditText != null ){
        UUID uuid = UUID.randomUUID();
        String imageName = "images/" + uuid + ".jpg";
    storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            StorageReference newReferance = firebaseStorage.getReference(imageName);
            newReferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String dawnloadUrl = uri.toString();

                    String sikayet = binding.yorumyazEditText.getText().toString();

                    FirebaseUser user = auth.getCurrentUser();
                    String email = user.getEmail();

                    HashMap<String, Object> postData = new HashMap<>();
                    postData.put("useremail",email);
                    postData.put("downloadurl",dawnloadUrl);
                    postData.put("sikayet",sikayet);
                    postData.put("date", FieldValue.serverTimestamp());


                    firebaseFirestore.collection("Posts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Intent intent = new Intent(UploadActivity.this, feedactivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

                }
            });
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(UploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

        }
    });
     }if(checkBox.isChecked() == false  && imageData == null && binding.yorumyazEditText != null){

            String sikayet = binding.yorumyazEditText.getText().toString();

            FirebaseUser user = auth.getCurrentUser();

            String email = user.getEmail();

            HashMap<String, Object> postData = new HashMap<>();
            postData.put("useremail",email);
            postData.put("sikayet",sikayet);
            postData.put("date", FieldValue.serverTimestamp());
            firebaseFirestore.collection("Posts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Intent intent = new Intent(UploadActivity.this,feedactivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(UploadActivity.this,"Şikayet alanı boş bırakılmaz", Toast.LENGTH_SHORT).show();
        }
    }
    public void selectImage(View view){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Galeri için izin gerekiyor",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //izin iste
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }else {
                //izin iste
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intentToGallery);

        }


    }

    private void registerLauncher(){

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if (intentFromResult !=null){
                        imageData = intentFromResult.getData();
                        binding.imageView.setImageURI(imageData);
                    }
                }
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result){
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);

                }else{
                    Toast.makeText(UploadActivity.this, "İzin gerekli!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

}

