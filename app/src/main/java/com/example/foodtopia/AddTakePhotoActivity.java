package com.example.foodtopia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodtopia.Adpater.AnalysisResultRecycleAdapter;
import com.example.foodtopia.add.Upload;
import com.example.foodtopia.databinding.ActivityAddTakePhotoBinding;
import com.example.foodtopia.ml.ModelUnquant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
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
import java.util.concurrent.atomic.AtomicReference;

public class AddTakePhotoActivity extends AppCompatActivity {

    ActivityAddTakePhotoBinding binding;

    public static final String TAG = MainActivity.class.getSimpleName()+"My";

    Uri uri ;
    String imgURL;
    String mealtime;
    ImageView imageView;
    ProgressDialog progressDialog;
    StorageReference storageRef;
    private DatabaseReference mDatabase;

    private List<Float> keyList;
    private List<String> valueList;
    private RecyclerView resultRecyclerView;

    private String mPath = "";//設置高畫質的照片位址
    public static final int CAMERA_PERMISSION = 100;//檢測相機權限用
    public static final int REQUEST_HIGH_IMAGE = 101;//檢測相機回傳

    private final int imageSize = 224;

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTakePhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        resultRecyclerView = findViewById(R.id.recyclerview_taking_result);
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(AddTakePhotoActivity.this));
        resultRecyclerView.addItemDecoration(new DividerItemDecoration(AddTakePhotoActivity.this, DividerItemDecoration.VERTICAL));


        imageView = findViewById(R.id.cameraImageView);

        Intent intent=getIntent();
        mealtime = intent.getStringExtra("mealtime");

        /*取得相機權限*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION);
        }

        /*按下拍攝按鈕*/
        binding.buttonHigh.setOnClickListener(v->{
            Intent highIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //檢查是否已取得權限
            if (highIntent.resolveActivity(getPackageManager()) == null) return;
            //取得相片檔案的URI位址及設定檔案名稱
            File imageFile = getImageFile();
            if (imageFile == null) return;
            //取得相片檔案的URI位址
            uri = FileProvider.getUriForFile(
                    this,
                    "com.example.foodtopia.CameraEx",//記得要跟AndroidManifest.xml中的authorities 一致
                    imageFile
            );
            binding.cameraImageView.setImageURI(uri);
            highIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
            startActivityForResult(highIntent,REQUEST_HIGH_IMAGE);//開啟相機
        });

        /*按下分析按鈕*/
        binding.cameraPhotoUploadBtn.setOnClickListener(view -> {
            uploadImage();
//                Toast.makeText(TakePhoto.this,"分析照片",Toast.LENGTH_SHORT).show();
        });
        /*按下返回按鈕*/
        binding.cameraBackFab.setOnClickListener(view -> {
            Intent intent1 = new Intent(AddTakePhotoActivity.this, MainActivity.class);
            startActivity(intent1);
        });
    }

    private void uploadImage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("上傳中...");
        progressDialog.show();

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.TAIWAN);
        String date = formatter.format(now);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.TAIWAN);
        String time = timeFormatter.format(now);

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
                Toast.makeText(AddTakePhotoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(AddTakePhotoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        Bitmap imageBitmap = BitmapFactory.decodeFile(mPath);
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap,imageSize,imageSize,false);
        classifyImage(imageBitmap);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void classifyImage(Bitmap image){
        try {
            //TODO Change Model
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

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
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();

            TextView result = findViewById(R.id.textView_taking_result);
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
//
            result.setText("預估結果 :");

            TreeMap<Float, String> confidenceMap = new TreeMap<>();
            for(int i = 0; i < classes.length; i++){
                if (confidences[i]*100 > 0.5){
                    confidenceMap.put(confidences[i] * 100,classes[i]);
                }
            }
            //confidence
            keyList = new ArrayList<>(confidenceMap.keySet());
            //label classes
            valueList = new ArrayList<>(confidenceMap.values());

            launch_countdown();

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //顯示預測結果
    public void launch_countdown(){
        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                AnalysisResultRecycleAdapter resultAdapter = new AnalysisResultRecycleAdapter(mealtime,keyList,valueList,AddTakePhotoActivity.this);
                resultRecyclerView.setAdapter(resultAdapter);
            }
        }.start();
    }
    //取得副檔名
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    /**取得相片檔案的URI位址及設定檔案名稱*/
    private File getImageFile() {
        String time = new SimpleDateFormat("yyMMdd",Locale.TAIWAN).format(new Date());
        String fileName = time+"_";
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            //給予檔案命名及檔案格式
            File imageFile = File.createTempFile(fileName,".jpg",dir);
            //給予全域變數中的照片檔案位置，方便後面取得
            mPath = imageFile.getAbsolutePath();
            return imageFile;
        } catch (IOException e) {
            return null;
        }
    }

    /*取得照片回傳*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*可在此檢視回傳為哪個相片，requestCode為上述自定義，resultCode為-1就是有拍照，0則是使用者沒拍照*/
        Log.d(TAG, "onActivityResult: requestCode: "+requestCode+", resultCode "+resultCode);

        /*如果是高畫質的相片回傳*/
        if (requestCode == REQUEST_HIGH_IMAGE && resultCode == -1){
            ImageView imageHigh = binding.cameraImageView;
            new Thread(()->{
                //在BitmapFactory中以檔案URI路徑取得相片檔案，並處理為AtomicReference<Bitmap>，方便後續旋轉圖片
                AtomicReference<Bitmap> getHighImage = new AtomicReference<>(BitmapFactory.decodeFile(mPath));
                Matrix matrix = new Matrix();
//                matrix.setRotate(90f);//轉90度
                getHighImage.set(Bitmap.createBitmap(getHighImage.get()
                        ,0,0
                        ,getHighImage.get().getWidth()
                        ,getHighImage.get().getHeight()
                        ,matrix,true));
                runOnUiThread(()->{
                    //以Glide設置圖片(因為旋轉圖片屬於耗時處理，故會LAG一下，且必須使用Thread執行緒)
                    Glide.with(this)
                            .load(getHighImage.get())
                            .centerCrop()
                            .into(imageHigh);
                });
            }).start();
        }
        else{
            Toast.makeText(this, "未作任何拍攝", Toast.LENGTH_SHORT).show();
        }

    }
}