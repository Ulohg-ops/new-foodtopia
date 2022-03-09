package com.example.foodtopia.pytorch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.foodtopia.R;
import com.example.foodtopia.pytorch.Classifier;
import com.example.foodtopia.pytorch.Utils;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity2 extends AppCompatActivity {

    int cameraRequestCode = 001;
    int PICK_IMAGE = 1;
    Classifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        classifier = new Classifier(Utils.assetFilePath(this,"mobilenet-v2.pt"));

        Button capture = findViewById(R.id.capture2);

        capture.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
//                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent,cameraRequestCode);

            }


        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==RESULT_OK &&requestCode==1){
            try {
                InputStream inputStream= getContentResolver().openInputStream(data.getData());
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                String pred = classifier.predict(bitmap);
                System.out.println(pred);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            super.onActivityResult(requestCode, resultCode, data);

        }





////
//        if(requestCode == cameraRequestCode && resultCode == RESULT_OK){
//
//            Intent resultView = new Intent(this,Result.class);
//
//            resultView.putExtra("imagedata",data.getExtras());
//
//            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
//
//            String pred = classifier.predict(imageBitmap);
//            resultView.putExtra("pred",pred);
//
//            startActivity(resultView);
//
//        }
//            resultView.putExtra("pred",pred);
//
//                if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
//            Intent resultView = new Intent(this,Result.class);
//            resultView.putExtra("imagedata",data.getExtras());
//            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
//
//            String pred = classifier.predict(imageBitmap);
//            resultView.putExtra("pred",pred);
//            System.out.println(pred);
//            startActivity(resultView);

//        }

    }

}