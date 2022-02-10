package com.example.foodtopia;

import static androidx.media.MediaBrowserServiceCompat.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * 參考網址 :
 * https://medium.com/nerd-for-tech/how-to-add-extended-floating-action-button-in-android-android-studio-java-481cc9b3cdcb
 * https://medium.com/@waynechen323/android-%E5%9F%BA%E7%A4%8E%E7%9A%84-fragment-%E4%BD%BF%E7%94%A8%E6%96%B9%E5%BC%8F-730858c12a43
 */

public class Add extends Fragment {

    private View root;
    //餐點文字和熱量
    private TextView breakfastKcalText,breakfastText,lunchKcalText,lunchText;
    private TextView dinnerKcalText,dinnerText,dessertKcalText,dessertText;

    //Floating action button 按鈕
    FloatingActionButton mAddManualFab, mAddCameraFab, mAddUploadFab;
    ExtendedFloatingActionButton mAddFab;
    TextView addManualActionText, addCameraActionText, addUploadActionText;

    // to check whether sub FABs are visible or not
    Boolean isAllFabsVisible;


    public Add() {
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
        breakfastText = root.findViewById(R.id.breakfastText);
        lunchText = root.findViewById(R.id.lunchText);
        dinnerText = root.findViewById(R.id.dinnerText);
        dessertText = root.findViewById(R.id.dessertText);

        breakfastKcalText = root.findViewById(R.id.breakfastKcalText);
        lunchKcalText = root.findViewById(R.id.lunchKcalText);
        dinnerKcalText = root.findViewById(R.id.dinnerKcalText);
        dessertKcalText = root.findViewById(R.id.dessertKcalText);

        mAddFab = root.findViewById(R.id.add_fab);
        mAddUploadFab = root.findViewById(R.id.upload_fab);
        mAddCameraFab = root.findViewById(R.id.camera_fab);
        mAddManualFab = root.findViewById(R.id.manual_fab);

        addManualActionText = root.findViewById(R.id.add_manual_action_text);
        addCameraActionText = root.findViewById(R.id.add_camera_action_text);
        addUploadActionText = root.findViewById(R.id.add_upload_action_text);

        // Now set all the FABs and all the action name
        // texts as GONE
        mAddManualFab.setVisibility(View.GONE);
        mAddCameraFab.setVisibility(View.GONE);
        mAddUploadFab.setVisibility(View.GONE);
        addManualActionText.setVisibility(View.GONE);
        addCameraActionText.setVisibility(View.GONE);
        addUploadActionText.setVisibility(View.GONE);

        // make the boolean variable as false, as all the
        // action name texts and all the sub FABs are
        // invisible
        isAllFabsVisible = false;

        // Set the Extended floating action button to
        // shrinked state initially
        mAddFab.shrink();

        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAllFabsVisible){
                    mAddManualFab.show();
                    mAddCameraFab.show();
                    mAddUploadFab.show();
                    addManualActionText.setVisibility(View.VISIBLE);
                    addCameraActionText.setVisibility(View.VISIBLE);
                    addUploadActionText.setVisibility(View.VISIBLE);
                    mAddFab.extend();
                    isAllFabsVisible = true;

                }
                else {
                    mAddManualFab.hide();
                    mAddCameraFab.hide();
                    mAddUploadFab.hide();
                    addManualActionText.setVisibility(View.GONE);
                    addCameraActionText.setVisibility(View.GONE);
                    addUploadActionText.setVisibility(View.GONE);
                    mAddFab.shrink();
                    isAllFabsVisible = false;
                }
            }
        });
        mAddManualFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Manual",Toast.LENGTH_SHORT).show();
            }
        });
        mAddCameraFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Camera",Toast.LENGTH_SHORT).show();
            }
        });
        mAddUploadFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Upload",Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }
}