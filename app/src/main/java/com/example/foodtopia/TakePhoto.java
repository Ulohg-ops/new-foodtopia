package com.example.foodtopia;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class TakePhoto extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName()+"My";

    private String mPath = "";//設置高畫質的照片位址
    private Button uploadBtn;
    private FloatingActionButton back;
    public static final int CAMERA_PERMISSION = 100;//檢測相機權限用
    public static final int REQUEST_HIGH_IMAGE = 101;//檢測相機回傳

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button btHigh = findViewById(R.id.buttonHigh);
        /**取得相機權限*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION);
        }

        /**按下拍攝按鈕*/
        btHigh.setOnClickListener(v->{
            Intent highIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //檢查是否已取得權限
            if (highIntent.resolveActivity(getPackageManager()) == null) return;
            //取得相片檔案的URI位址及設定檔案名稱
            File imageFile = getImageFile();
            if (imageFile == null) return;
            //取得相片檔案的URI位址
            Uri imageUri = FileProvider.getUriForFile(
                    this,
                    "com.example.foodtopia.CameraEx",//記得要跟AndroidManifest.xml中的authorities 一致
                    imageFile
            );
            highIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            startActivityForResult(highIntent,REQUEST_HIGH_IMAGE);//開啟相機
        });

        uploadBtn = findViewById(R.id.cameraPhotoUploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TakePhoto.this,"分析照片",Toast.LENGTH_SHORT).show();
            }
        });
        back = findViewById(R.id.camera_back_fab);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TakePhoto.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**取得相片檔案的URI位址及設定檔案名稱*/
    private File getImageFile() {
        String time = new SimpleDateFormat("yyMMdd").format(new Date());
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

    /**取得照片回傳*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**可在此檢視回傳為哪個相片，requestCode為上述自定義，resultCode為-1就是有拍照，0則是使用者沒拍照*/
        Log.d(TAG, "onActivityResult: requestCode: "+requestCode+", resultCode "+resultCode);

        /**如果是高畫質的相片回傳*/
        if (requestCode == REQUEST_HIGH_IMAGE && resultCode == -1){
            ImageView imageHigh = findViewById(R.id.imageViewHigh);
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
        }/***/

        else{
            Toast.makeText(this, "未作任何拍攝", Toast.LENGTH_SHORT).show();
        }

    }
}