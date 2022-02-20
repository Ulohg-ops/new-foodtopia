package com.example.foodtopia.social;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodtopia.MainActivity;
import com.example.foodtopia.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    private Uri mImageUri;
    String miUrlOk = "";
    private StorageTask uploadTask;
    StorageReference storageRef;
    FirebaseAuth auth;
    StorageReference mStorageRef;
    private static final int PICK_IMAGE_REQUEST = 1;

    ImageView image_added;
    Button post;
    EditText description,foodname;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_post);

        image_added = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        back=findViewById(R.id.back);
        foodname=findViewById(R.id.foodName);
        description = findViewById(R.id.description);

        storageRef = FirebaseStorage.getInstance().getReference("posts");
        mStorageRef = FirebaseStorage.getInstance().getReference("posts");

        System.out.println(FirebaseStorage.getInstance().getReference("posts"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            uploadFile();
                uploadImage_10();
            }
        });

        image_added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage_10() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();
        System.out.println(userID);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Posting");
        pd.show();
        if (mImageUri != null) {
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            uploadTask = fileReference.putFile(mImageUri);
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
                        miUrlOk = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

                        String postid = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid", postid);
                        hashMap.put("postimage", miUrlOk);
                        hashMap.put("foodname",foodname.getText().toString());
                        hashMap.put("description", description.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());


                        reference.child(postid).setValue(hashMap);

                        pd.dismiss();

                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                        finish();

                    } else {
                        Toast.makeText(PostActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(PostActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            System.out.println(mImageUri);
            Picasso.get().load(mImageUri).into(image_added);
            System.out.println("dasdsauiu" + mImageUri);
        }
    }


//    private void uploadFile() {
//        if (mImageUri != null) {
//            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
//            mUploadTask = fileReference.putFile(mImageUri);
//            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    Toast.makeText(PostActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
//                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
//                    while (!urlTask.isSuccessful()) ;
//                    Uri downloadUrl = urlTask.getResult();
//
//                    Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
//        }
//    }
}

