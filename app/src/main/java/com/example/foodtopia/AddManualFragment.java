package com.example.foodtopia;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class AddManualFragment extends Fragment {
    EditText name,amount,carbohydrate,protein,fat,sugar,sodium,kcal;
    Button add;
    FloatingActionButton back;
    String  choice;
    RadioGroup radioGroup;
    RadioButton breakfastBtn,lunchBtn,dinnerBtn,dessertBtn;

    Boolean spinner1First = true;
    Boolean spinner2First = true;
    Boolean spinner3First = true;
    Boolean spinner4First = true;
    Boolean spinner5First = true;
    Boolean spinner6First = true;
    Boolean spinner7First = true;

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

        Spinner spinner1 = view.findViewById(R.id.spinner1);
        Spinner spinner2 = view.findViewById(R.id.spinner2);
        Spinner spinner3 = view.findViewById(R.id.spinner3);
        Spinner spinner4 = view.findViewById(R.id.spinner4);
        Spinner spinner5 = view.findViewById(R.id.spinner5);
        Spinner spinner6 = view.findViewById(R.id.spinner6);
        Spinner spinner7 = view.findViewById(R.id.spinner7);


        //Spinner的Adapter
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(getActivity()
                ,R.array.quantifier_array,android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter1);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(getActivity()
                ,R.array.quantifier_array,android.R.layout.simple_dropdown_item_1line);
        spinner2.setAdapter(adapter2);
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(getActivity()
                ,R.array.quantifier_array,android.R.layout.simple_dropdown_item_1line);
        spinner3.setAdapter(adapter3);
        ArrayAdapter adapter4 = ArrayAdapter.createFromResource(getActivity()
                ,R.array.quantifier_array,android.R.layout.simple_dropdown_item_1line);
        spinner4.setAdapter(adapter4);
        ArrayAdapter adapter5 = ArrayAdapter.createFromResource(getActivity()
                ,R.array.quantifier_array,android.R.layout.simple_dropdown_item_1line);
        spinner5.setAdapter(adapter5);
        ArrayAdapter adapter6 = ArrayAdapter.createFromResource(getActivity()
                ,R.array.quantifier_array,android.R.layout.simple_dropdown_item_1line);
        spinner6.setAdapter(adapter6);
        ArrayAdapter adapter7 = ArrayAdapter.createFromResource(getActivity()
                ,R.array.quantifier_array,android.R.layout.simple_dropdown_item_1line);
        spinner7.setAdapter(adapter7);

        //按下第一個Spinner
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner1First){spinner1First = false;}
                else{
//                    Toast.makeText(view.getContext(),parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //按下第2個Spinner
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner2First){spinner2First = false;}
                else{
//                    Toast.makeText(view.getContext(),parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //按下第3個Spinner
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner3First){spinner3First = false;}
                else{
//                    Toast.makeText(view.getContext(),parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //按下第4個Spinner
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner4First){spinner4First = false;}
                else{
//                    Toast.makeText(view.getContext(),parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //按下第5個Spinner
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner5First){spinner5First = false;}
                else{
//                    Toast.makeText(view.getContext(),parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //按下第6個Spinner
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner6First){spinner6First = false;}
                else{
//                    Toast.makeText(view.getContext(),parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //按下第7個Spinner
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spinner7First){spinner7First = false;}
                else{
//                    Toast.makeText(view.getContext(),parent.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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
                Fragment fragment = new AddFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment)
                        .commit();
            }
        });

        return view;
    }
}