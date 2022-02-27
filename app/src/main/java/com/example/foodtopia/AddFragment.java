package com.example.foodtopia;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * 參考網址 :
 * https://medium.com/nerd-for-tech/how-to-add-extended-floating-action-button-in-android-android-studio-java-481cc9b3cdcb
 * https://medium.com/@waynechen323/android-%E5%9F%BA%E7%A4%8E%E7%9A%84-fragment-%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F-730858c12a43
 */

public class AddFragment extends Fragment  {

    private View root;
    //Date 按鈕
    private Button dateBtn;
    //choice
    private String choice;
    //餐點熱量
    private TextView breakfastKcalText,lunchKcalText,dinnerKcalText,dessertKcalText;

    //餐點
    private TextView breakfastText, breakfastQuantifier;
    private TextView lunchText, lunchQuantifier;
    private TextView dinnerText, dinnerQuantifier;
    private TextView dessertText, dessertQuantifier;
    // breakfast list
    private ArrayList<String> breakfastList;
    private ArrayList<String> breakfastQuantifierList;
    // lunch list
    private ArrayList<String> lunchList;
    private ArrayList<String> lunchQuantifierList;
    // dinner list
    private ArrayList<String> dinnerList;
    private ArrayList<String> dinnerQuantifierList;
    // dessert list
    private ArrayList<String> dessertList;
    private ArrayList<String> dessertQuantifierList;

    //各個種類下方的新增按鈕
    private Button addBreakfastBtn, addLunchBtn, addDinnerBtn, addDessertBtn;

    //彈出視窗
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    //彈出視窗的新增按鈕
    private CardView cameraBtn,manualBtn,uploadBtn;
    //返回按鈕
    private FloatingActionButton back;

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
        root = inflater.inflate(R.layout.fragment_add, container, false);

        //新增餐點按鈕
        addBreakfastBtn = root.findViewById(R.id.addBreakfastBtn);
        addLunchBtn = root.findViewById(R.id.addLunchBtn);
        addDinnerBtn = root.findViewById(R.id.addDinnerBtn);
        addDessertBtn = root.findViewById(R.id.addDessertBtn);

        //各個項目的熱量
        breakfastKcalText = root.findViewById(R.id.breakfastKcalText);
        lunchKcalText = root.findViewById(R.id.lunchKcalText);
        dinnerKcalText = root.findViewById(R.id.dinnerKcalText);
        dessertKcalText = root.findViewById(R.id.dessertKcalText);

        //顯示各項目的文字
        breakfastText = root.findViewById(R.id.breakfastText);
        breakfastQuantifier = root.findViewById(R.id.breakfastQuantifier);
        lunchText = root.findViewById(R.id.lunchText);
        lunchQuantifier = root.findViewById(R.id.lunchQuantifier);
        dinnerText = root.findViewById(R.id.dinnerText);
        dinnerQuantifier = root.findViewById(R.id.dinnerQuantifier);
        dessertText = root.findViewById(R.id.dessertText);
        dessertQuantifier = root.findViewById(R.id.dessertQuantifier);

        //餐點和單位的ArrayList
        breakfastList = new ArrayList<>();
        breakfastQuantifierList = new ArrayList<>();
        lunchList = new ArrayList<>();
        lunchQuantifierList = new ArrayList<>();
        dinnerList = new ArrayList<>();
        dinnerQuantifierList = new ArrayList<>();
        dessertList = new ArrayList<>();
        dessertQuantifierList = new ArrayList<>();

        //Date Picker
        dateBtn = root.findViewById(R.id.dateBtn);
        DatePickerDialog.OnDateSetListener datePicker;
        Calendar calendar = Calendar.getInstance();
        String Format = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.TAIWAN);
        dateBtn.setText(sdf.format(calendar.getTime()));
        datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                dateBtn.setText(sdf.format(calendar.getTime()));
            }
        };
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(),R.style.DatePickerTheme,datePicker,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        breakfastList.add("豆漿");
        breakfastQuantifierList.add("一杯(小)");
        lunchList.add("吐司");
        lunchQuantifierList.add("一片");
        dinnerList.add("豆漿");
        dinnerQuantifierList.add("一杯(小)");
        dessertList.add("吐司");
        dessertQuantifierList.add("一片");

        //依照各項目的紀錄顯示餐點在畫面上
        for (int i=0;i<breakfastList.size();i++){
            breakfastText.append(breakfastList.get(i)+"\n");
            breakfastQuantifier.append(breakfastQuantifierList.get(i)+"\n");
        }
        for (int i=0;i<lunchList.size();i++){
            lunchText.append(lunchList.get(i)+"\n");
            lunchQuantifier.append(lunchQuantifierList.get(i)+"\n");
        }
        for (int i=0;i<dinnerList.size();i++){
            dinnerText.append(dinnerList.get(i)+"\n");
            dinnerQuantifier.append(dinnerQuantifierList.get(i)+"\n");
        }
        for (int i=0;i<dessertList.size();i++){
            dessertText.append(dessertList.get(i)+"\n");
            dessertQuantifier.append(dessertQuantifierList.get(i)+"\n");
        }

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
    //產生彈出視窗
    public void createPopUpDialog(){
        dialogBuilder = new AlertDialog.Builder(getActivity());
        View popUpView = getLayoutInflater().inflate(R.layout.dialog_add_menu,null);

        dialogBuilder.setView(popUpView);
        dialog = dialogBuilder.create();
        //設定背景為透明
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();


        //按下手動輸入
        manualBtn = popUpView.findViewById(R.id.manualCard);
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
        cameraBtn = popUpView.findViewById(R.id.cameraCard);
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
        uploadBtn = popUpView.findViewById(R.id.uploadCard);
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
        back = popUpView.findViewById(R.id.add_menu_back_fab);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}