package com.example.foodtopia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodtopia.add.Upload;
import com.example.foodtopia.databinding.ActivityAddUploadBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddUploadActivity extends AppCompatActivity {

    ActivityAddUploadBinding binding;

    int SELECT_PHOTO = 1;
    Uri uri ;
    ImageView imageView;

    StorageReference storageRef;
    ProgressDialog progressDialog;
    String mealtime;
    String imgURL;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageView = findViewById(R.id.uploadImageView);

        Intent intent=getIntent();
        mealtime = intent.getStringExtra("choice");

        binding.chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });

        binding.PhotoUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        binding.addUploadBackFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddUploadActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("上傳中...");
        progressDialog.show();
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.TAIWAN);
        String date = dateFormat.format(now);

        storageRef = FirebaseStorage.getInstance().getReference("meals");
        final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                + "." + getFileExtension(uri));
        UploadTask uploadTask = fileReference.putFile(uri);


        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imgURL = downloadUri.toString();

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference("uploads");
                    //new node
                    String uploadID = mDatabase.push().getKey();
                    Upload photo = new Upload(uid, date, mealtime, imgURL);

                    mDatabase.child(uploadID).setValue(photo);

                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                } else {
                    Toast.makeText(AddUploadActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddUploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        /*
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    binding.uploadImageView.setImageURI(null);
                    Toast.makeText(AddUploadActivity.this,"上傳成功",Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            // Continue with the task to get the download URL
                            return storageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                imgURL = downloadUri.toString();
                            } else {
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Toast.makeText(AddUploadActivity.this,"上傳失敗",Toast.LENGTH_SHORT).show();

            }
        });
         */
    }

    //取得副檔名
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData()!= null){
            uri = data.getData();
            binding.uploadImageView.setImageURI(uri);
        }
    }
}