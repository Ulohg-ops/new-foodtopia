package com.example.foodtopia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foodtopia.ml.Food101ModelUnquant;
import com.example.foodtopia.ml.Model;
import com.google.android.gms.common.util.ArrayUtils;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity2 extends AppCompatActivity {

    TextView result, confidence;
    ImageView imageView;
    Button picture;
    int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 1);
                    } else {
                        //Request camera permission if we don't have it.
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                    }
                }
            }
        });
    }

    public void classifyImage(Bitmap image) {
        try {
            Model model = Model.newInstance(getApplicationContext());
            Food101ModelUnquant model1 = Food101ModelUnquant.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for (int i=0; i<imageSize; i++) {
                for (int j=0; j<imageSize; j++) {
                    int val = intValues[pixel++];  // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            Food101ModelUnquant.Outputs outputs1 = model1.process(inputFeature0);

            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            TensorBuffer outputFeature1 = outputs1.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            float[] confidences2 = outputFeature1.getFloatArray();
//            float[] combine = ArrayUtils.concat(confidences, confidences2);
            int maxPos = 0;
            float maxConfidence = 0;
            int arrayFlag = 0;
            for (int i=0; i< confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            for (int i=0; i<confidences2.length; i++) {
                if (confidences2[i] > maxConfidence) {
                    maxConfidence = confidences2[i];
                    maxPos = i;
                    arrayFlag = 1;
                    Log.d("Tag", "hihi");
                }
            }
//            String[][] classes = {{"caesar salad", "cheese cake"}, {"apple_pie", "baby_back_ribs", "baklava", "beef_carpaccio", "beef_tartare",
//                    "beet_salad", "beignets", "bibimbap"}};
//            String[] classes = {"caesar salad", "cheese cake"};

            String[] classes = {"caesar salad", "cheese cake"};
            String[] classes2 = {"apple_pie", "baby_back_ribs", "baklava", "beef_carpaccio", "beef_tartare",
                    "beet_salad", "beignets", "bibimbap"};

            if (arrayFlag == 0) {
                result.setText(classes[maxPos]);
            }else {
                result.setText(classes2[maxPos]);
            }

//            result.setText(classes[arrayFlag][maxPos]);

            String s ="";
            //todo
            for (int i=0; i<classes.length; i++) {
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }
            for (int i=0; i<classes2.length; i++) {
                s += String.format("%s: %.1f%%\n", classes2[i], confidences2[i] * 100);
            }

            confidence.setText(s);

            // Releases model resources if no longer used.
            model.close();
            model1.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}