package com.example.foodtopia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodtopia.databinding.ActivityAddUploadBinding;
import com.example.foodtopia.databinding.ActivityMain2Binding;
import com.example.foodtopia.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Add_Upload extends AppCompatActivity {

    ActivityAddUploadBinding binding;

    int SELECT_PHOTO = 1;
    Uri uri ;
    ImageView imageView;

    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageView = findViewById(R.id.uploadImageView);

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
//                Toast.makeText(Add_Upload.this,"分析照片",Toast.LENGTH_SHORT).show();
                uploadImage();
            }
        });

        binding.addUploadBackFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Upload.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("上傳中...");
        progressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.TAIWAN);
        Date now = new Date();
        String fileName = formatter.format(now);


        storageReference = FirebaseStorage.getInstance().getReference("meals/"+fileName);

        storageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        binding.uploadImageView.setImageURI(null);
                        Toast.makeText(Add_Upload.this,"上傳成功",Toast.LENGTH_SHORT).show();
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Toast.makeText(Add_Upload.this,"上傳失敗",Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData()!= null){
            uri = data.getData();
            binding.uploadImageView.setImageURI(uri);

//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
//                imageView.setImageBitmap(bitmap);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }
}