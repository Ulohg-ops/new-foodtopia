package com.example.foodtopia;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodtopia.ml.Model;
import com.example.foodtopia.ml.Model2;
import com.example.foodtopia.ml.Model3;
import com.example.foodtopia.ml.Model4;
import com.example.foodtopia.ml.Model5;
import com.example.foodtopia.ml.Model6;
import com.example.foodtopia.ml.Model7;
import com.example.foodtopia.ml.Model8;
import com.example.foodtopia.ml.Model9;
import com.google.android.gms.common.util.ArrayUtils;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class MainActivity2 extends AppCompatActivity {

    TextView result;
    ImageView imageView;
    Button picture, predict1, predict2, predict3, folder;
    int imageSize = 224;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
        folder = findViewById(R.id.buttonFolder);
        predict1 = findViewById(R.id.predict1);
        predict2 = findViewById(R.id.predict2);
        predict3 = findViewById(R.id.predict3);

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

        folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });
    }



    public void classifyImage(Bitmap image) {
        try {

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

            //todo change the model
            Model model = Model.newInstance(getApplicationContext());
            Model2 model2 = Model2.newInstance(getApplicationContext());
            Model3 model3 = Model3.newInstance(getApplicationContext());
            Model4 model4 = Model4.newInstance(getApplicationContext());
            Model5 model5 = Model5.newInstance(getApplicationContext());
            Model6 model6 = Model6.newInstance(getApplicationContext());
            Model7 model7 = Model7.newInstance(getApplicationContext());
            Model8 model8 = Model8.newInstance(getApplicationContext());
            Model9 model9 = Model9.newInstance(getApplicationContext());

            // Runs model inference and gets result.
            //todo create variable
            Model.Outputs outputs = model.process(inputFeature0);
            Model2.Outputs outputs2 = model2.process(inputFeature0);
            Model3.Outputs outputs3 = model3.process(inputFeature0);
            Model4.Outputs outputs4 = model4.process(inputFeature0);
            Model5.Outputs outputs5 = model5.process(inputFeature0);
            Model6.Outputs outputs6 = model6.process(inputFeature0);
            Model7.Outputs outputs7 = model7.process(inputFeature0);
            Model8.Outputs outputs8 = model8.process(inputFeature0);
            Model9.Outputs outputs9 = model9.process(inputFeature0);

            TensorBuffer outputFeature1 = outputs.getOutputFeature0AsTensorBuffer();
            TensorBuffer outputFeature2 = outputs2.getOutputFeature0AsTensorBuffer();
            TensorBuffer outputFeature3 = outputs3.getOutputFeature0AsTensorBuffer();
            TensorBuffer outputFeature4 = outputs4.getOutputFeature0AsTensorBuffer();
            TensorBuffer outputFeature5 = outputs5.getOutputFeature0AsTensorBuffer();
            TensorBuffer outputFeature6 = outputs6.getOutputFeature0AsTensorBuffer();
            TensorBuffer outputFeature7 = outputs7.getOutputFeature0AsTensorBuffer();
            TensorBuffer outputFeature8 = outputs8.getOutputFeature0AsTensorBuffer();
            TensorBuffer outputFeature9 = outputs9.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature1.getFloatArray();
            float[] confidences2 = outputFeature2.getFloatArray();
            float[] confidences3 = outputFeature3.getFloatArray();
            float[] confidences4 = outputFeature4.getFloatArray();
            float[] confidences5 = outputFeature5.getFloatArray();
            float[] confidences6 = outputFeature6.getFloatArray();
            float[] confidences7 = outputFeature7.getFloatArray();
            float[] confidences8 = outputFeature8.getFloatArray();
            float[] confidences9 = outputFeature9.getFloatArray();

//            int maxPos = 0;
//            float maxConfidence = 0;
//            int arrayFlag = 1;
//            for (int i=0; i< confidences.length; i++) {
//                if (confidences[i] > maxConfidence) {
//                    maxConfidence = confidences[i];
//                    maxPos = i;
//                }
//            }
//            for (int i=0; i<confidences2.length; i++) {
//                if (confidences2[i] > maxConfidence) {
//                    maxConfidence = confidences2[i];
//                    maxPos = i;
//                    arrayFlag = 2;
//                }
//            }
//            for (int i=0; i<confidences3.length; i++) {
//                if (confidences2[i] > maxConfidence) {
//                    maxConfidence = confidences2[i];
//                    maxPos = i;
//                    arrayFlag = 3;
//                }
//            }
//            for (int i=0; i<confidences4.length; i++) {
//                if (confidences2[i] > maxConfidence) {
//                    maxConfidence = confidences2[i];
//                    maxPos = i;
//                    arrayFlag = 3;
//                }
//            }

            String[] classes = {"apple_pie", "baby_back_ribs", "beignets", "bibimbap", "bread_pudding", "breakfast_burrito", "bruschetta", "caesar_salad", "cheesecake", "chicken_curry"};
            String[] classes2 = {"chicken_quesadilla", "chicken_wings", "chocolate_cake", "chocolate_mousse", "churros", "clam_chowder", "club_sandwich", "crab_cakes", "creme_brulee", "croque_madame"};
            String[] classes3 = {"cup_cakes", "deviled_egg", "donuts", "dumplings", "edamame", "eggs_benedict", "escargots", "filet_mignon", "fish_and_chips", "foie_gras"};
            String[] classes4 = {"french_fries", "french_onion_soup", "french_toast", "fried_calamari", "fried_rice", "frozen_yogurt", "garlic_bread", "greek_salad", "grilled_cheese_sandwich", "grilled_salmon"};


            TreeMap<Float, String> confidenceMap = new TreeMap<>();
            for(int i = 0; i < classes.length; i++){
                confidenceMap.put(confidences[i] * 100,classes[i]);
            }
            for(int i = 0; i < classes2.length; i++){
                confidenceMap.put(confidences2[i] * 100,classes2[i]);
            }
            for(int i = 0; i < classes3.length; i++){
                confidenceMap.put(confidences3[i] * 100,classes3[i]);
            }
            for(int i = 0; i < classes4.length; i++){
                confidenceMap.put(confidences4[i] * 100,classes4[i]);
            }
            Log.d("tag", confidenceMap.toString());
//            for(int i = 0; i < classes5.length; i++){
//                confidenceMap.put(confidences5[i] * 100,classes5[i]);
//            }
//            for(int i = 0; i < classes6.length; i++){
//                confidenceMap.put(confidences6[i] * 100,classes6[i]);
//            }
//            for(int i = 0; i < classes7.length; i++){
//                confidenceMap.put(confidences7[i] * 100,classes7[i]);
//            }
//            for(int i = 0; i < classes8.length; i++){
//                confidenceMap.put(confidences8[i] * 100,classes8[i]);
//            }
//            for(int i = 0; i < classes9.length; i++){
//                confidenceMap.put(confidences9[i] * 100,classes9[i]);
//            }

            //confidence
            List<Float> keyList = new ArrayList<>(confidenceMap.keySet());
            //label classes
            List<String> valueList = new ArrayList<>(confidenceMap.values());

            predict1.setText(1+". "+valueList.get(valueList.size()-1)+
                    ", Confidence: "+String.format("%.1f%%",keyList.get(keyList.size()-1)));
            predict2.setText(2+". "+valueList.get(valueList.size()-2)+
                    ", Confidence: "+String.format("%.1f%%",keyList.get(keyList.size()-2)));
            predict3.setText(3+". "+valueList.get(valueList.size()-3)+
                    ", Confidence: "+String.format("%.1f%%",keyList.get(keyList.size()-3)));

//            //todo change to switch
//            switch (arrayFlag) {
//                case 1:
//                    result.setText(classes[maxPos]);
//                    break;
//                case 2:
//                    result.setText(classes2[maxPos]);
//                    break;
//                case 3:
//                    result.setText(classes3[maxPos]);
//            }


            // Releases model resources if no longer used.
            model.close();
            model2.close();

        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            Bitmap image = (Bitmap) data.getExtras().get("data");
//            int dimension = Math.min(image.getWidth(), image.getHeight());
//            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
//            imageView.setImageBitmap(image);
//
//            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
//            classifyImage(image);
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageView.setImageURI(selectedImage);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                bitmap = Bitmap.createScaledBitmap(bitmap,imageSize,imageSize,false);
                classifyImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}