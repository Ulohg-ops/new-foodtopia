package com.example.foodtopia;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodtopia.add.Diet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AnalyzeResultActivity extends AppCompatActivity {

    private TextView title;
    private EditText manual_name;
    private EditText editAmount, editCarbohydrate, editProtein, editFat, editSugar, editSodium, editKcal;
    private String prediction;
    private String mealtime;
    private String label;
    private float calories;
    private float carbohydrate;
    private float protein;
    private float fat;
    private float sugar;
    private float sodium;
    private RadioButton breakfastBtn,lunchBtn,dinnerBtn,dessertBtn;
    private Boolean spinner1First = true;
    private Spinner spinnerAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add__manual);

        manual_name = findViewById(R.id.manual_name);   //food name
        title = findViewById(R.id.manual_title);
        title.setTextSize(20);
        title.setText("檢查資料");
        launch_countdown();
        Bundle b = getIntent().getExtras();
        mealtime = b.getString("mealtime");
        prediction = b.getString("prediction");
        fetchData(prediction);  //get data from api

        manual_name = findViewById(R.id.manual_name);
        editAmount   = findViewById(R.id.manual_amount);
        editCarbohydrate = findViewById(R.id.manual_carbohydrate);
        editProtein = findViewById(R.id.manual_protein);
        editFat = findViewById(R.id.manual_fat);
        editSugar = findViewById(R.id.manual_sugar);
        editSodium = findViewById(R.id.manual_sodium);
        editKcal = findViewById(R.id.manual_kcal);

        breakfastBtn = findViewById(R.id.manual_radio_breakfast);
        lunchBtn = findViewById(R.id.manual_radio_lunch);
        dinnerBtn = findViewById(R.id.manual_radio_dinner);
        dessertBtn = findViewById(R.id.manual_radio_dessert);

        //份量的單位
        spinnerAmount = findViewById(R.id.spinnerAmount);
        //Spinner的Adapter
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(AnalyzeResultActivity.this
                ,R.array.quantifier_array,android.R.layout.simple_dropdown_item_1line);
        spinnerAmount.setAdapter(adapter1);
        spinnerAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner1First){spinner1First = false;}
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        FloatingActionButton back = findViewById(R.id.add_menu_back_fab);
        back.setOnClickListener(view -> {
            b.remove("mealtime");
            b.remove("prediction");
            Intent intent = new Intent(AnalyzeResultActivity.this, AddUploadActivity.class);
            startActivity(intent);

        });

        Button buttonUpload = findViewById(R.id.addManualBtn);
        buttonUpload.setOnClickListener(view -> upload());
    }
    //等待四秒鐘，避免還沒取得營養素資料就填入資料
    public void launch_countdown(){
        new CountDownTimer(4000, 2500) {

            public void onTick(long millisUntilFinished) {
                Toast.makeText(AnalyzeResultActivity.this,"稍待片刻",Toast.LENGTH_SHORT).show();
            }
            public void onFinish() {
                editMeal(mealtime);     //填入營養素資料

            }
        }.start();
    }

    //透過API得到營養素資料
    private void fetchData(String ingr) {

        String url = "https://edamam-food-and-grocery-database.p.rapidapi.com/parser?ingr="+ingr;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "edamam-food-and-grocery-database.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "6420319cffmsh0c13660db8a5b72p1c81d8jsn0df67cc171c3")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Looper.prepare();
                Toast.makeText(AnalyzeResultActivity.this, "Error"+e,
                        Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String resp = Objects.requireNonNull(response.body()).string();
                    AnalyzeResultActivity.this.runOnUiThread(() -> {
                        try {
                            JSONObject jsonObject = new JSONObject(resp);
                            //先從parsed的部分找符合預測結果的營養素資料
                            JSONArray parsedArray = jsonObject.getJSONArray("parsed");
                            if (parsedArray.length()>0){
                                JSONObject objectFood = parsedArray.getJSONObject(0).getJSONObject("food");
                                label = objectFood.optString("label");
                                if (label.equalsIgnoreCase(prediction)){
                                    JSONObject objectNutrients = objectFood.getJSONObject("nutrients");
                                    fillNutrientsValue(objectNutrients);
                                }
                                else {
                                    JSONArray hintsArray = jsonObject.getJSONArray("hints");
                                    for (int i=0;i<hintsArray.length();++i){
                                        objectFood = hintsArray.getJSONObject(i).getJSONObject("food");
                                        label = objectFood.optString("label");
                                        if (label.equalsIgnoreCase(prediction)){
                                            JSONObject objectNutrients = objectFood.getJSONObject("nutrients");
                                            fillNutrientsValue(objectNutrients);
                                            break;
                                        }
                                    }
                                }
                            }
                            else {
                                JSONArray hintsArray = jsonObject.getJSONArray("hints");
                                for (int i=0;i<hintsArray.length();++i){
                                    JSONObject objectFood = hintsArray.getJSONObject(i).getJSONObject("food");
                                    label = objectFood.optString("label");
                                    if (label.equalsIgnoreCase(prediction)){
                                        JSONObject objectNutrients = objectFood.getJSONObject("nutrients");
                                        fillNutrientsValue(objectNutrients);
                                        break;
                                    }
                                }
                            }
                            if (!label.equalsIgnoreCase(prediction)){
                                title.setText("沒有相關資料\n請手動輸入");
                            }
                            else {
                                manual_name.setText(label);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void fillNutrientsValue(JSONObject objectNutrients){
        calories = (float) objectNutrients.optDouble("ENERC_KCAL");
        carbohydrate = (float) objectNutrients.optDouble("CHOCDF");
        protein = (float) objectNutrients.optDouble("PROCNT");
        fat = (float) objectNutrients.optDouble("FAT");
        sugar = (float) objectNutrients.optDouble("SUGAR");
        sodium = (float) objectNutrients.optDouble("NA");
    }

    private void editMeal(String mealtime){
        //設定Mealtime

        switch (mealtime){
            case "早餐":
                breakfastBtn.setChecked(true);
                break;
            case "午餐":
                lunchBtn.setChecked(true);
                break;
            case "晚餐":
                dinnerBtn.setChecked(true);
                break;
            case "點心":
                dessertBtn.setChecked(true);
                break;
        }

        EditText amount = findViewById(R.id.manual_amount);
        amount.setText("1");

        //填入營養素
        EditText et_carbohydrate = findViewById(R.id.manual_carbohydrate);
        if (!Float.isNaN(carbohydrate)){
            et_carbohydrate.setText(String.format(Locale.TAIWAN,"%.2f", carbohydrate));
        }
        EditText et_protein = findViewById(R.id.manual_protein);
        if (!Float.isNaN(protein)){
            et_protein.setText(String.format(Locale.TAIWAN,"%.2f", protein));
        }
        EditText et_fat = findViewById(R.id.manual_fat);
        if (!Float.isNaN(fat)){
            et_fat.setText(String.format(Locale.TAIWAN,"%.2f", fat));
        }
        EditText et_sugar = findViewById(R.id.manual_sugar);
        if (!Float.isNaN(sugar)){
            et_sugar.setText(String.format(Locale.TAIWAN,"%.2f", sugar));
        }
        EditText et_sodium = findViewById(R.id.manual_sodium);
        if (!Float.isNaN(sodium)){
            et_sodium.setText(String.format(Locale.TAIWAN,"%.2f", sodium));
        }
        EditText et_calories = findViewById(R.id.manual_kcal);
        if (!Float.isNaN(calories)){
            et_calories.setText(String.format(Locale.TAIWAN,"%.2f", calories));
        }
    }
    private void upload(){

        String name = manual_name.getText().toString().trim();
        if(TextUtils.isEmpty(name)) {
            manual_name.setError("請輸入食品名稱");
            return;
        }
        String amount = editAmount.getText().toString().trim();
        String carbohydrate = editCarbohydrate.getText().toString().trim();
        String protein = editProtein.getText().toString().trim();
        String fat = editFat.getText().toString().trim();
        String sugar = editSugar.getText().toString().trim();
        String sodium = editSodium.getText().toString().trim();
        String kcal = editKcal.getText().toString().trim();
        if(TextUtils.isEmpty(kcal)) {
            editKcal.setError("請輸入熱量");
            return;
        }

        String amountQuantifier = spinnerAmount.getSelectedItem().toString();

        //將selectedText 指定為使用者選擇的項目:早餐、午餐、晚餐
        RadioGroup radioGroup = findViewById(R.id.manual_radioGroup);
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = radioGroup.findViewById(radioButtonID);
        String selectedText = (String) radioButton.getText();

        //設定 date, time
        Date current = new Date();
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd",Locale.TAIWAN);
        String date = sdfDate.format(current);
        SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss",Locale.TAIWAN);
        String time = sdfTime.format(current);

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String userid_date = String.format("%s_%s", uid, date);

        //設定餐點資料
        Diet diet = new Diet(name,amount,amountQuantifier,kcal,carbohydrate,fat,selectedText,
                protein,sodium,sugar,date,time,uid,userid_date);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Diets");
        //new diet node
        String dietId = mDatabase.push().getKey();
        assert dietId != null;
        mDatabase.child(dietId).setValue(diet).addOnSuccessListener(unused ->
                Toast.makeText(AnalyzeResultActivity.this,"上傳成功",Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(AnalyzeResultActivity.this,"上傳失敗",Toast.LENGTH_SHORT).show());
    }
}