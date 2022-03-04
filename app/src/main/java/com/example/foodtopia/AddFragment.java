package com.example.foodtopia;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodtopia.add.MealAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * 參考網址 :
 * https://medium.com/nerd-for-tech/how-to-add-extended-floating-action-button-in-android-android-studio-java-481cc9b3cdcb
 * https://medium.com/@waynechen323/android-%E5%9F%BA%E7%A4%8E%E7%9A%84-fragment-%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F-730858c12a43
 */

public class AddFragment extends Fragment  {

    //Date 按鈕
    private Button dateBtn;
    //choice 餐點選擇
    private String choice;
    //餐點熱量
    private TextView breakfastKcalText,lunchKcalText,dinnerKcalText,dessertKcalText;

    private AlertDialog dialog;
    private String chosenDate;

    //當天餐點
    private ArrayList<HashMap<String,String>> arrayList_Breakfast;
    private ArrayList<HashMap<String,String>> arrayList_Lunch;
    private ArrayList<HashMap<String,String>> arrayList_Dinner;
    private ArrayList<HashMap<String,String>> arrayList_Dessert;

    //RecyclerView
    RecyclerView recyclerView_breakfast;
    RecyclerView recyclerView_lunch;
    RecyclerView recyclerView_dinner;
    RecyclerView recyclerView_dessert;


    public AddFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add, container, false);

        //各個種類下方的新增按鈕
        Button addBreakfastBtn = root.findViewById(R.id.addBreakfastBtn);
        Button addLunchBtn = root.findViewById(R.id.addLunchBtn);
        Button addDinnerBtn = root.findViewById(R.id.addDinnerBtn);
        Button addDessertBtn = root.findViewById(R.id.addDessertBtn);

        //各個項目的熱量
        breakfastKcalText = root.findViewById(R.id.breakfastKcalText);
        lunchKcalText = root.findViewById(R.id.lunchKcalText);
        dinnerKcalText = root.findViewById(R.id.dinnerKcalText);
        dessertKcalText = root.findViewById(R.id.dessertKcalText);

        //Date Picker
        dateBtn = root.findViewById(R.id.dateBtn);
        DatePickerDialog.OnDateSetListener datePicker;
        Calendar calendar = Calendar.getInstance();
        //顯示選擇日期
        String Format = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.TAIWAN);
        dateBtn.setText(sdf.format(calendar.getTime()));
        //查詢日期
        String DatabaseFormat = "yyyyMMdd";
        SimpleDateFormat dbf = new SimpleDateFormat(DatabaseFormat, Locale.TAIWAN);
        chosenDate = dbf.format(calendar.getTime());
        //設定查詢日期的recyclerView
        setCurrentDateMeals();
        //設定日期
        datePicker = (datePicker1, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            dateBtn.setText(sdf.format(calendar.getTime()));
            String DatabaseFormat1 = "yyyyMMdd";
            SimpleDateFormat dbf1 = new SimpleDateFormat(DatabaseFormat1, Locale.TAIWAN);
            chosenDate = dbf1.format(calendar.getTime());
            setCurrentDateMeals();
        };
        dateBtn.setOnClickListener(view -> new DatePickerDialog(getActivity(),R.style.DatePickerTheme,datePicker,calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show());

        //早餐的 recyclerview
        recyclerView_breakfast = root.findViewById(R.id.recyclerView_breakfast);
        recyclerView_breakfast.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView_breakfast.setLayoutManager(new LinearLayoutManager(getActivity()));

        //午餐的 recyclerview
        recyclerView_lunch = root.findViewById(R.id.recyclerView_lunch);
        recyclerView_lunch.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView_lunch.setLayoutManager(new LinearLayoutManager(getActivity()));

        //晚餐的 recyclerview
        recyclerView_dinner = root.findViewById(R.id.recyclerView_dinner);
        recyclerView_dinner.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView_dinner.setLayoutManager(new LinearLayoutManager(getActivity()));

        //點心的 recyclerview
        recyclerView_dessert = root.findViewById(R.id.recyclerView_dessert);
        recyclerView_dessert.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView_dessert.setLayoutManager(new LinearLayoutManager(getActivity()));


        //新增餐點
        addBreakfastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle option = new Bundle();
                choice = "早餐";
                option.putString("option","1");
                getParentFragmentManager().setFragmentResult("option1",option);
                createPopUpDialog();
            }
        });
        addLunchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle option = new Bundle();
                choice = "午餐";
                option.putString("option","2");
                getParentFragmentManager().setFragmentResult("option2",option);
                createPopUpDialog();
            }
        });
        addDinnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle option = new Bundle();
                choice = "晚餐";
                option.putString("option","3");
                getParentFragmentManager().setFragmentResult("option3",option);
                createPopUpDialog();
            }
        });
        addDessertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle option = new Bundle();
                choice = "點心";
                option.putString("option","4");
                getParentFragmentManager().setFragmentResult("option4",option);
                createPopUpDialog();
            }
        });

        return root;
    }

    private void setCurrentDateMeals() {
        //資料庫
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Diets");
        String uid = fAuth.getCurrentUser().getUid();
        String uid_date = String.format("%s_%s",uid,chosenDate);

        //查詢使用者新增的餐點中符合userid_date的項目
        Query uid_dateQuery = mDatabase.orderByChild("userid_date").equalTo(uid_date);
        uid_dateQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList_Breakfast = new ArrayList<>();
                arrayList_Lunch = new ArrayList<>();
                arrayList_Dinner = new ArrayList<>();
                arrayList_Dessert = new ArrayList<>();
                float breakfastKcal =0;
                float lunchKcal =0;
                float dinnerKcal =0;
                float dessertKcal =0;

                for (DataSnapshot record : snapshot.getChildren()){
                    //將每一筆dietID、foodname、quantifier存入Hashmap中，並計算卡路里
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("Id",record.getKey());
                    hashMap.put("foodname",record.child("foodname").getValue(String.class));
                    String quantifier = String.format("%s %s",record.child("amount").getValue(String.class),
                            record.child("amountQuantifier").getValue(String.class));
                    hashMap.put("quantifier",quantifier);

                    switch (record.child("mealtime").getValue(String.class)){
                        case "早餐":
                            breakfastKcal += Float.valueOf(record.child("calories").getValue(String.class));
                            arrayList_Breakfast.add(hashMap);
                            break;
                        case "午餐":
                            lunchKcal += Float.valueOf(record.child("calories").getValue(String.class));
                            arrayList_Lunch.add(hashMap);
                            break;
                        case "晚餐":
                            dinnerKcal += Float.valueOf(record.child("calories").getValue(String.class));
                            arrayList_Dinner.add(hashMap);
                            break;
                        case "點心":
                            dessertKcal += Float.valueOf(record.child("calories").getValue(String.class));
                            arrayList_Dessert.add(hashMap);
                            break;
                    }
                }
                //顯示早餐熱量並設定早餐的Adapter
                breakfastKcalText.setText(breakfastKcal+" kcal");
                MealAdapter adapter1 = new MealAdapter(arrayList_Breakfast);
                recyclerView_breakfast.setAdapter(adapter1);

                //午餐
                lunchKcalText.setText(lunchKcal+" kcal");
                MealAdapter adapter2 = new MealAdapter(arrayList_Lunch);
                recyclerView_lunch.setAdapter(adapter2);

                //晚餐
                dinnerKcalText.setText(dinnerKcal+" kcal");
                MealAdapter adapter3 = new MealAdapter(arrayList_Dinner);
                recyclerView_dinner.setAdapter(adapter3);

                //點心
                dessertKcalText.setText(dessertKcal+" kcal");
                MealAdapter adapter4 = new MealAdapter(arrayList_Dessert);
                recyclerView_dessert.setAdapter(adapter4);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //產生彈出視窗
    public void createPopUpDialog(){
        //彈出視窗
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View popUpView = getLayoutInflater().inflate(R.layout.dialog_add_menu,null);

        dialogBuilder.setView(popUpView);
        dialog = dialogBuilder.create();
        //設定背景為透明
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();


        //按下手動輸入
        CardView manualBtn = popUpView.findViewById(R.id.manualCard);
        manualBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddManualFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, fragment)
                        .commit();
                dialog.dismiss();
            }
        });

        //按下拍攝照片
        CardView cameraBtn = popUpView.findViewById(R.id.cameraCard);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTakePhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("choice",choice);
                intent.putExtras(bundle);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        //按下選擇照片
        CardView uploadBtn = popUpView.findViewById(R.id.uploadCard);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddUploadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("choice",choice);
                intent.putExtras(bundle);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        //按下返回按鈕
        FloatingActionButton back = popUpView.findViewById(R.id.add_menu_back_fab);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}