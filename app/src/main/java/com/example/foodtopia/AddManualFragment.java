package com.example.foodtopia;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddManualFragment extends Fragment{
    EditText editName, editAmount, editCarbohydrate, editProtein, editFat, editSugar, editSodium, editKcal;
    Button add;
    FloatingActionButton back;
    String  choice;
    RadioGroup radioGroup;
    RadioButton breakfastBtn,lunchBtn,dinnerBtn,dessertBtn;
    Spinner spinnerAmount;

    private DatabaseReference mDatabase;
    private String key;

    Boolean spinner1First = true;

    public AddManualFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add__manual, container, false);

        editName = view.findViewById(R.id.manual_name);
        editAmount   = view.findViewById(R.id.manual_amount);
        editCarbohydrate = view.findViewById(R.id.manual_carbohydrate);
        editProtein = view.findViewById(R.id.manual_protein);
        editFat = view.findViewById(R.id.manual_fat);
        editSugar = view.findViewById(R.id.manual_sugar);
        editSodium = view.findViewById(R.id.manual_sodium);
        editKcal = view.findViewById(R.id.manual_kcal);

        add = view.findViewById(R.id.addManualBtn);
        back = view.findViewById(R.id.add_menu_back_fab);

        radioGroup = view.findViewById(R.id.manual_radioGroup);
        breakfastBtn = view.findViewById(R.id.manual_radio_breakfast);
        lunchBtn = view.findViewById(R.id.manual_radio_lunch);
        dinnerBtn = view.findViewById(R.id.manual_radio_dinner);
        dessertBtn = view.findViewById(R.id.manual_radio_dessert);

        //判斷使用者點選哪一個新增按鈕(早餐還是晚餐)，先接收資料，再設預設按鈕
        getParentFragmentManager().setFragmentResultListener("option1"
                , this, (requestKey, result) -> {
                    choice  = result.getString("option");
                    breakfastBtn.setChecked(true);
                });
        getParentFragmentManager().setFragmentResultListener("option2"
                , this, (requestKey, result) -> {
                    choice  = result.getString("option");
                    lunchBtn.setChecked(true);
                });
        getParentFragmentManager().setFragmentResultListener("option3"
                , this, (requestKey, result) -> {
                    choice  = result.getString("option");
                    dinnerBtn.setChecked(true);
                });
        getParentFragmentManager().setFragmentResultListener("option4"
                , this, (requestKey, result) -> {
                    choice  = result.getString("option");
                    dessertBtn.setChecked(true);
                });

        spinnerAmount = view.findViewById(R.id.spinnerAmount);
        //Spinner的Adapter
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(getActivity()
                ,R.array.quantifier_array,android.R.layout.simple_dropdown_item_1line);
        spinnerAmount.setAdapter(adapter1);

        //get dietID，並且獲取資料讓使用者修改
        getParentFragmentManager().setFragmentResultListener("dietID"
                , this, (requestKey, result) -> {
                    key  = result.getString("key");
                    TextView title = view.findViewById(R.id.manual_title);
                    title.append("修改");
                    editMeal();
                });

        //按下第一個Spinner，選擇份量單位
        spinnerAmount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner1First){spinner1First = false;}
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        //新增
        add.setOnClickListener(view1 -> {

            String name = editName.getText().toString().trim();
            if(TextUtils.isEmpty(name)) {
                editName.setError("請輸入食品名稱");
                return;
            }
            String amount = editAmount.getText().toString().trim();
            if(TextUtils.isEmpty(amount)) {
                editAmount.setError("請輸入份量");
                return;
            }
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

            //份量的單位
            String amountQuantifier = spinnerAmount.getSelectedItem().toString();

            //將selectedText 指定為使用者選擇的項目:早餐、午餐、晚餐
            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = radioGroup.findViewById(radioButtonID);
            String selectedText = (String) radioButton.getText();

            //設定 date, time
            Date current = new Date();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
            String date = sdfDate.format(current);
            SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
            String time = sdfTime.format(current);

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String userid_date = String.format("%s_%s", uid, date);

            //設定餐點資料
            Diet diet = new Diet(name,amount,amountQuantifier,kcal,carbohydrate,fat,selectedText,
                    protein,sodium,sugar,date,time,uid,userid_date);

            if (key != null){   //修改
                mDatabase = FirebaseDatabase.getInstance().getReference("Diets");
                mDatabase.child(key).setValue(diet)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),"修改失敗",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else {  //新增
                mDatabase = FirebaseDatabase.getInstance().getReference("Diets");
                //new diet node
                String dietId = mDatabase.push().getKey();
                mDatabase.child(dietId).setValue(diet).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(),"上傳成功",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"上傳失敗",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //返回手動頁面
        back.setOnClickListener(view12 -> {
            Fragment fragment = new AddFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();
        });

        return view;
    }

    private void editMeal() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Diets").child(key);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Diet diet = snapshot.getValue(Diet.class);
                    //填入資料
                    editName.setText(diet.getFoodname());
                    if (diet.getAmount()!=null){
                        editAmount.setText(diet.getAmount());
                    }
                    if (diet.getCarbohydrate()!=null){
                        editCarbohydrate.setText(diet.getCarbohydrate());
                    }
                    if (diet.getProtein()!=null){
                        editProtein.setText(diet.getProtein());
                    }
                    if (diet.getFat()!=null){
                        editFat.setText(diet.getFat());
                    }
                    if (diet.getSugar()!=null){
                        editSugar.setText(diet.getSugar());
                    }
                    if (diet.getSodium()!=null){
                        editSodium.setText(diet.getSodium());
                    }
                    if (diet.getCalories()!=null){
                        editKcal.setText(diet.getCalories());
                    }
                    if (diet.getMealtime()!=null){
                        String mealtime = diet.getMealtime();
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
                    }
                    if (diet.getAmountQuantifier()!=null){
                        String amountQuantifier = diet.getAmountQuantifier();
                        ArrayAdapter mAdap = (ArrayAdapter) spinnerAmount.getAdapter();
                        int spinnerPos = mAdap.getPosition(amountQuantifier);
                        spinnerAmount.setSelection(spinnerPos);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}