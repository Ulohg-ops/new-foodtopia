package com.example.foodtopia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AddManualFragment extends Fragment {
    EditText editName, editAmount, editCarbohydrate, editProtein, editFat, editSugar, editSodium, editKcal;
    Button add;
    FloatingActionButton back;
    String  choice;
    RadioGroup radioGroup;
    RadioButton breakfastBtn,lunchBtn,dinnerBtn,dessertBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        Spinner spinnerAmount = view.findViewById(R.id.spinnerAmount);
        //Spinner的Adapter
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(getActivity()
                ,R.array.quantifier_array,android.R.layout.simple_dropdown_item_1line);
        spinnerAmount.setAdapter(adapter1);

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
            String amount = editAmount.getText().toString().trim();
            String carbohydrate = editCarbohydrate.getText().toString().trim();
            String protein = editProtein.getText().toString().trim();
            String fat = editFat.getText().toString().trim();
            String sugar = editSugar.getText().toString().trim();
            String sodium = editSodium.getText().toString().trim();
            String kcal = editKcal.getText().toString().trim();

            //將份量的數字和單位合併為amount
            String amountQuantifier = spinnerAmount.getSelectedItem().toString();
            amount = amount+amountQuantifier;

            Map<String,Object> meal = new HashMap<>();
            meal.put("name",name);
            meal.put("amount",amount);
            meal.put("carbohydrate",carbohydrate);
            meal.put("protein",protein);
            meal.put("fat",fat);
            meal.put("sugar",sugar);
            meal.put("sodium",sodium);
            meal.put("kcal",kcal);

            //將selectedText 指定為使用者選擇的項目:早餐、午餐、晚餐
            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
            String selectedText = (String) radioButton.getText();

            //設定document name eg:20220215_022555_早餐
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date current = new Date();
            String documentName = sdf.format(current)+"_"+selectedText;

            //得到使用者的信箱
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email="test_users";
            if (user != null) {
                email = user.getEmail();
            }

            //新增document
            db.collection(email).document(documentName)
                    .set(meal)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(),"成功新增",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(getActivity(),"錯誤",Toast.LENGTH_SHORT).show());
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
}