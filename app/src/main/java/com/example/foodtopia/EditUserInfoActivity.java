package com.example.foodtopia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodtopia.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditUserInfoActivity extends AppCompatActivity {
    ProgressBar progress_circular;
    private Uri mImageUri;
    private StorageTask uploadTask;
    FirebaseAuth auth;
    StorageReference mStorageRef;
    DatabaseReference reference;
    private static final int PICK_IMAGE_REQUEST = 1;
    Button btn_edit;
    ImageButton back;
    ImageView image_profile;
    TextInputLayout username, weight, height, calories_perday;
    Spinner workload, stress, target;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        username = findViewById(R.id.username);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        calories_perday = findViewById(R.id.calories_perday);

        workload = findViewById(R.id.spinner_workload);
        stress = findViewById(R.id.spinner_stress);
        target = findViewById(R.id.spinner_target);

        back = findViewById(R.id.btn_back);
        image_profile = findViewById(R.id.image_profile);
        btn_edit = findViewById(R.id.btn_edit);
        progress_circular=findViewById(R.id.progress_circular);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        getWorkLoadSelection();
        getStrssSelection();
        getTargetSelection();
        getUser();
        getImage();

        progress_circular.setVisibility(View.GONE);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(EditUserInfoActivity.this, R.style.dialog)
                        .setTitle("警告")
                        .setMessage("確定要修改嗎")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progress_circular.setVisibility(View.VISIBLE);
                                uploadFile();
                                progress_circular.setVisibility(View.GONE);
                                Toast.makeText(EditUserInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();

            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            uploadTask = fileReference.putFile(mImageUri);
            System.out.println(mImageUri);
            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    Log.d("cc", "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
                    fileUri = downloadUrl.toString();
                    System.out.println("cccd" + fileUri);
                    String username_edit = username.getEditText().getText().toString().trim();
                    String weight_edit = weight.getEditText().getText().toString().trim();
                    String height_edit = height.getEditText().getText().toString().trim();
                    String calories_perday_edit = calories_perday.getEditText().getText().toString().trim();
                    String workload_edit = workload.getSelectedItem().toString();
                    String stress_edit = stress.getSelectedItem().toString();
                    String target_edit = target.getSelectedItem().toString();
                    auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userID = firebaseUser.getUid();
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("username", username_edit);
                    map.put("weight", weight_edit);
                    map.put("height", height_edit);
                    map.put("calories_per_day", calories_perday_edit);
                    map.put("workload", workload_edit);
                    map.put("stress", stress_edit);
                    map.put("target", target_edit);
                    map.put("imageurl", fileUri);
                    reference.updateChildren(map);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } else {
            String username_edit = username.getEditText().getText().toString().trim();
            String weight_edit = weight.getEditText().getText().toString().trim();
            String height_edit = height.getEditText().getText().toString().trim();
            String calories_perday_edit = calories_perday.getEditText().getText().toString().trim();
            String workload_edit = workload.getSelectedItem().toString();
            String stress_edit = stress.getSelectedItem().toString();
            String target_edit = target.getSelectedItem().toString();
            auth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = auth.getCurrentUser();
            String userID = firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
            HashMap<String, Object> map = new HashMap<>();
            map.put("username", username_edit);
            map.put("weight", weight_edit);
            map.put("height", height_edit);
            map.put("calories_per_day", calories_perday_edit);
            map.put("workload", workload_edit);
            map.put("stress", stress_edit);
            map.put("target", target_edit);
            reference.updateChildren(map);
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
            Picasso.get().load(mImageUri).into(image_profile);
//            System.out.println("dasdsauiu" + mImageUri);
        }
    }

    private void getImage() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(image_profile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getTargetSelection() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(user.getUid()).child("target").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.target, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    target.setAdapter(adapter);
                    int a = 0;
                    switch (String.valueOf(task.getResult().getValue())) {
                        case "減重":
                            a = 0;
                            break;
                        case "維持目前體重":
                            a = 1;
                            break;
                        case "增重":
                            a = 2;
                            break;

                    }
                    target.setSelection(a);
                }
            }
        });
    }

    public void getWorkLoadSelection() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(user.getUid()).child("workload").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.work_load, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    workload.setAdapter(adapter);
                    int a = 0;
                    switch (String.valueOf(task.getResult().getValue())) {
                        case "臥床躺著不動":
                            a = 0;
                            break;
                        case "幾乎很少或坐著不動":
                            a = 1;
                            break;
                        case "每周1-2次":
                            a = 2;
                            break;
                        case "每周3-5次":
                            a = 3;
                            break;
                        case "每周6-7次":
                            a = 4;
                            break;
                        case "每天重度運動":
                            a = 5;
                            break;
                        case "重勞力工作者":
                            a = 6;
                            break;
                    }
                    workload.setSelection(a);
                }
            }
        });
    }

    public void getStrssSelection() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(user.getUid()).child("stress").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                            R.array.stress, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    stress.setAdapter(adapter);
                    int a = 0;

                    switch (String.valueOf(task.getResult().getValue())) {
                        case "正常無疾病":
                            a = 0;
                            break;
                        case "發燒":
                            a = 1;
                            break;
                        case "住院患者":
                            a = 2;
                            break;
                        case "懷孕":
                            a = 3;
                            break;
                        case "生長期":
                            a = 4;
                            break;
                        case "哺乳":
                            a = 5;
                            break;
                    }
                    stress.setSelection(a);
                }
            }
        });
    }

    public void getUser() {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Users").child(user.getUid()).child("username").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    username.getEditText().setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
        mDatabase.child("Users").child(user.getUid()).child("weight").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    weight.getEditText().setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
        mDatabase.child("Users").child(user.getUid()).child("height").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    height.getEditText().setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
        mDatabase.child("Users").child(user.getUid()).child("calories_per_day").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    calories_perday.getEditText().setText(String.valueOf(task.getResult().getValue()));
                }
            }
        });
        mDatabase.child("Users").child(user.getUid()).child("wordload").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                }
            }
        });


    }

}