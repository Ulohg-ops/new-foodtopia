package com.example.foodtopia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodtopia.add.Upload;
import com.example.foodtopia.databinding.ActivityAddUploadBinding;
import com.example.foodtopia.ml.Food101ModelUnquant;
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

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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

    private Bitmap imageBitmap;
    private final int imageSize=224;

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
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.TAIWAN);
        String time = timeFormat.format(now);

        storageRef = FirebaseStorage.getInstance().getReference("meals");
        final StorageReference fileReference = storageRef.child(time
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

        imageBitmap = Bitmap.createScaledBitmap(imageBitmap,imageSize,imageSize,false);
//        binding.uploadImageView.setImageBitmap(imageBitmap);
        classifyImage(imageBitmap);
    }

    private void classifyImage(Bitmap imageBitmap) {
        try {
            //TODO Change Model
            Food101ModelUnquant model = Food101ModelUnquant.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int [] intValues = new int[imageSize * imageSize];
            imageBitmap.getPixels(intValues, 0, imageBitmap.getWidth(), 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Food101ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            TextView result = findViewById(R.id.textView_upload_result);
            // TODO Adjust label
            //label
            String[] classes = {"apple_pie", "baby_back_ribs", "baklava", "beef_carpaccio", "beef_tartare",
                    "beet_salad", "beignets","bibimbap"};
            result.setText(classes[maxPos]);

            String s = "";
            for(int i = 0; i < classes.length; i++){
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }
            result.append("\n"+s);


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {

        }
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
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}