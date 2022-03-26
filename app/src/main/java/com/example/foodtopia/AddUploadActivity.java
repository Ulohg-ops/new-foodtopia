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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodtopia.add.Upload;
import com.example.foodtopia.databinding.ActivityAddUploadBinding;
import com.example.foodtopia.ml.ModelAll;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;

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
    private String prediction;

    private Bitmap imageBitmap;
    private final int imageSize=224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageView = findViewById(R.id.uploadImageView);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mealtime = bundle.getString("mealtime");
        }

        binding.chooseBtn.setOnClickListener(view -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK);
            intent1.setType("image/*");
            startActivityForResult(intent1,SELECT_PHOTO);
        });

        binding.PhotoUploadBtn.setOnClickListener(view -> uploadImage());

        binding.addUploadBackFab.setOnClickListener(view -> {
            Intent intent2 = new Intent(AddUploadActivity.this, MainActivity.class);
            startActivity(intent2);
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


        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            return fileReference.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                imgURL = downloadUri.toString();

                String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                mDatabase = FirebaseDatabase.getInstance().getReference("uploads");
                //new node
                String uploadID = mDatabase.push().getKey();
                Upload photo = new Upload(uid, date, mealtime, imgURL);

                assert uploadID != null;
                mDatabase.child(uploadID).setValue(photo);

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

            } else {
                Toast.makeText(AddUploadActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(AddUploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

        imageBitmap = Bitmap.createScaledBitmap(imageBitmap,imageSize,imageSize,false);
//        binding.uploadImageView.setImageBitmap(imageBitmap);
        classifyImage(imageBitmap);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void classifyImage(Bitmap imageBitmap) {
        try {
            //TODO Change Model
            ModelAll model = ModelAll.newInstance(getApplicationContext());

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
            ModelAll.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();

            // TODO Adjust label
            //label
            String[] classes = {"apple pie", "baby back ribs", "beignets", "bibimbap", "bread pudding",
                    "breakfast burrito", "bruschetta", "caesar salad", "cheesecake", "chicken curry",
                    "chicken quesadilla", "chicken wings", "chocolate cake", "chocolate mousse",
                    "churros", "clam chowder", "club sandwich", "crab cakes", "creme brulee",
                    "croque madame", "cup cakes", "deviled egg", "donuts", "dumplings", "edamame",
                    "eggs benedict", "escargots", "filet mignon", "fish and chips", "foie gras",
                    "french fries", "french onion soup", "french toast", "fried calamari", "fried rice",
                    "frozen yogurt", "garlic bread", "greek salad", "grilled cheese sandwich",
                    "grilled salmon", "guacamole","gyoza", "hamburger", "hot and sour soup","hot dog",
                    "hummus","ice cream","lasagna","lobster bisque","lobster roll sandwich",
                    "macaroni and cheese","macarons", "miso soup","mussels","nachos","omelette",
                    "onion rings","oysters","pad thai","paella","pancakes","peking duck", "pho","pizza",
                    "pork chop","poutine","prime rib","pulled pork sandwich","ramen","ravioli",
                    "red velvet cake","risotto", "samosa","sashimi","scallops","seaweed salad",
                    "spaghetti bolognese","spaghetti carbonara","spring rolls","steak","strawberry shortcake",
                    "sushi","tacos","takoyaki","tiramisu","waffles"};

            TextView result = findViewById(R.id.textView_upload_result);
            result.setText("預估結果:");

            TreeMap<Float, String> confidenceMap = new TreeMap<>();
            for(int i = 0; i < classes.length; i++){
                confidenceMap.put(confidences[i] * 100,classes[i]);
            }
            //confidence
            List<Float> keyList = new ArrayList<>(confidenceMap.keySet());
            //label classes
            List<String> valueList = new ArrayList<>(confidenceMap.values());

            Button predict1 = findViewById(R.id.btn_upload_predict1);
            Button predict2 = findViewById(R.id.btn_upload_predict2);
            Button predict3 = findViewById(R.id.btn_upload_predict3);
            predict1.setText(1+". "+valueList.get(valueList.size()-1)+
                    ", Confidence: "+String.format("%.1f%%",keyList.get(keyList.size()-1)));
            predict2.setText(2+". "+valueList.get(valueList.size()-2)+
                    ", Confidence: "+String.format("%.1f%%",keyList.get(keyList.size()-2)));
            predict3.setText(3+". "+valueList.get(valueList.size()-3)+
                    ", Confidence: "+String.format("%.1f%%",keyList.get(keyList.size()-3)));

            Intent intentPrediction = new Intent(AddUploadActivity.this, AnalysisResultsActivity.class);
            intentPrediction.removeExtra("prediction");
            intentPrediction.putExtra("mealtime",mealtime);

            predict1.setOnClickListener(view -> {
                prediction = valueList.get(valueList.size()-1);
                intentPrediction.putExtra("prediction",prediction);
                startActivity(intentPrediction);
            });
            predict2.setOnClickListener(view -> {
                prediction = valueList.get(valueList.size()-2);
                intentPrediction.putExtra("prediction",prediction);
                startActivity(intentPrediction);
            });
            predict3.setOnClickListener(view -> {
                prediction = valueList.get(valueList.size()-3);
                intentPrediction.putExtra("prediction",prediction);
                startActivity(intentPrediction);
            });


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            e.printStackTrace();
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