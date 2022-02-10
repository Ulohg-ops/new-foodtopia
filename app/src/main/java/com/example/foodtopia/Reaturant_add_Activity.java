package com.example.foodtopia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reaturant_add_Activity extends AppCompatActivity {
    TextView name;
    String store_name;
    ArrayList<restaurantProductGet> memberList = new ArrayList<>();
    RecyclerView recyclerView;
    RestaurantFoodCardRecycleAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef ;
    private static final String TAG = "MyActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaturant_add);
        name = findViewById(R.id.txtStoreName);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        store_name = getIntent().getStringExtra("name");
        name.setText(store_name);
//        getDatav1();
            getDatav2();
        adapter = new RestaurantFoodCardRecycleAdapter(Reaturant_add_Activity.this, memberList);
        recyclerView.setAdapter(adapter);
//        memberList.add(new restaurantProductGet( "beef", "1250"));
//        memberList.add(new restaurantProductGet( "beef", "150"));
//        for(restaurantProductGet name:memberList) {
//            System.out.println(name);
//        }
    }

    private void getDatav2() {
      db.collection("mcdonald's_breakfast")
              .addSnapshotListener(new EventListener<QuerySnapshot>() {
                  @Override
                  public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        Log.e("Firestore error",error.getMessage());
                        return;
                    }
                    for(DocumentChange dc:value.getDocumentChanges()){
                        if(dc.getType()==DocumentChange.Type.ADDED){
                            memberList.add(dc.getDocument().toObject(restaurantProductGet.class));
                            System.out.println(dc.getDocument().toObject(restaurantProductGet.class));

                        }
                    }
                    adapter.notifyDataSetChanged();
                  }
              });
    }


    public void getDatav1() {
        docRef= db.collection("mcdonald's").document("mcdonald's_breakfast");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<String> list = new ArrayList<>();
                Map<String, Object> map = task.getResult().getData();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    memberList.add(new restaurantProductGet(entry.getKey().toString(), entry.getValue().toString()));
                    System.out.println(entry.getKey().toString() + " " + entry.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

        });

    }
}

//
//        Map<String, Object> city = new HashMap<>();
//        city.put("name", "  經典美式咖啡");
//        city.put("calories", "14");
//
//        db.collection("mcdonald's_beverage").document()
//                .set(city)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                    }
//                });