package com.example.foodtopia;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class Add_Manual extends Fragment {
    EditText name,amount,carbohydrate,protein,fat,sugar,sodium,kcal;
    Button add;
    FloatingActionButton back;
    String  choice;
    RadioGroup radioGroup;
    RadioButton breakfastBtn,lunchBtn,dinnerBtn,dessertBtn;

    public Add_Manual() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add__manual, container, false);

        name = view.findViewById(R.id.manual_name);
        amount = view.findViewById(R.id.manual_amount);
        carbohydrate = view.findViewById(R.id.manual_carbohydrate);
        protein = view.findViewById(R.id.manual_protein);
        fat = view.findViewById(R.id.manual_fat);
        sugar = view.findViewById(R.id.manual_sugar);
        sodium = view.findViewById(R.id.manual_sodium);
        kcal = view.findViewById(R.id.manual_kcal);

        add = view.findViewById(R.id.addManualBtn);
        back = view.findViewById(R.id.add_menu_back_fab);

        radioGroup = view.findViewById(R.id.manual_radioGroup);
        breakfastBtn = view.findViewById(R.id.manual_radio_breakfast);
        lunchBtn = view.findViewById(R.id.manual_radio_lunch);
        dinnerBtn = view.findViewById(R.id.manual_radio_dinner);
        dessertBtn = view.findViewById(R.id.manual_radio_dessert);

//        radioGroup.clearCheck();

        //判斷使用者點選哪一個新增按鈕(早餐還是晚餐)，先接收資料，再設預設按鈕
        getParentFragmentManager().setFragmentResultListener("option1"
                , this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        choice  = result.getString("option");
                        breakfastBtn.setChecked(true);
                    }
                });
        getParentFragmentManager().setFragmentResultListener("option2"
                , this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        choice  = result.getString("option");
                        lunchBtn.setChecked(true);
                    }
                });
        getParentFragmentManager().setFragmentResultListener("option3"
                , this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        choice  = result.getString("option");
                        dinnerBtn.setChecked(true);
                    }
                });
        getParentFragmentManager().setFragmentResultListener("option4"
                , this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        choice  = result.getString("option");
                        dessertBtn.setChecked(true);
                    }
                });

        //新增
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"新增",Toast.LENGTH_SHORT).show();
            }
        });
        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Add();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment)
                        .commit();
            }
        });

        return view;
    }
}