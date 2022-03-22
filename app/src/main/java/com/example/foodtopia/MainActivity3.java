package com.example.foodtopia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodtopia.add.Upload;
import com.example.foodtopia.databinding.ActivityAddUploadBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;

public class MainActivity3 extends AppCompatActivity {
    private static final String TAG ="ccc" ;
    private ObjectDetector detector = null;
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
        initializeDetector();

        imageView = findViewById(R.id.uploadImageView);

        Intent intent=getIntent();
        mealtime = intent.getStringExtra("choice");

        binding.chooseBtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK);
            intent1.setType("image/*");
            startActivityForResult(intent1,SELECT_PHOTO);
        });

        binding.PhotoUploadBtn.setOnClickListener(view -> uploadImage());

        binding.addUploadBackFab.setOnClickListener(view -> {
            Intent intent12 = new Intent(MainActivity3.this, MainActivity.class);
            startActivity(intent12);
        });
    }

    private void uploadImage() {
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap,imageSize,imageSize,false);
//        binding.uploadImageView.setImageBitmap(imageBitmap);
        classifyImage(imageBitmap);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void classifyImage(Bitmap imageBitmap) {
        initializeDetector();
        runObjectDetection(imageBitmap);
    }
    private void initializeDetector() {
        ObjectDetector.ObjectDetectorOptions options = ObjectDetector.ObjectDetectorOptions.builder()
                .setMaxResults(5)
                .build();
        try {
            detector = ObjectDetector.createFromFileAndOptions(
                    this,
                    "salad.tflite",
                    options
            );
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }


    private void runObjectDetection(Bitmap bitmap) {
        //Create TFLite's TensorImage object
        TensorImage image = TensorImage.fromBitmap(bitmap);

        // Feed given image to the detector
        List<Detection> results = detector.detect(image);

        if (!results.isEmpty()) {
            //results list contain list of detected objects
            Log.e(TAG,"Objects = " + results);
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