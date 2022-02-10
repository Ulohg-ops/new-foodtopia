package com.example.foodtopia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.foodtopia.Adpater.RestaurantFoodCardRecycleAdapter;
import com.example.foodtopia.classes.restaurantProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Reaturant_add_Activity extends AppCompatActivity {
    TextView name;
    String store_name;
    ArrayList<restaurantProduct> memberList = new ArrayList<>();
    RecyclerView recyclerView;
    RestaurantFoodCardRecycleAdapter adapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
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
        readCsv();
//        memberList.add(new restaurantProductGet( "beef", "1250"));
//        memberList.add(new restaurantProductGet( "beef", "150"));
//        for(restaurantProductGet name:memberList) {
//            System.out.println(name);
//        }

//        Map<String, Object> city = new HashMap<>();
//        city.put("name", "  金海派富貴起司雞");
//        city.put("calories", "309");
//
//        db.collection("kfc_food").document()
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
    }


    private void getDatav2() {
        System.out.println(store_name.toLowerCase(Locale.ROOT)+"_food");
        db.collection(store_name.toLowerCase(Locale.ROOT)+"_food")
              .addSnapshotListener(new EventListener<QuerySnapshot>() {
                  @Override
                  public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null){
                        Log.e("Firestore error",error.getMessage());
                        return;
                    }
                    for(DocumentChange dc:value.getDocumentChanges()){
                        if(dc.getType()==DocumentChange.Type.ADDED){
                            memberList.add(dc.getDocument().toObject(restaurantProduct.class));
                        }
                    }
                    adapter.notifyDataSetChanged();
                  }
              });
    }

    public void readCsv(){
        String file = "C:\\Users\\gggg5\\AndroidStudioProjects\\new-foodtopia\\app\\src\\main\\assets\\subway.csv";
        ArrayList<Map<String,String>> arrayList=new ArrayList<>();
        BufferedReader reader = null;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(file));
//            System.out.println(reader.readLine());
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                Map<String,String> map=new HashMap<>();
                map.put("calories",row[0]);
                map.put("food",row[1]);
                arrayList.add(map);
//                for (String index : row) {
//                    System.out.printf("%-10s", index);
//                }
                System.out.println();
            }
//            for(Map map : arrayList){
//            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {

            }
        }
    }


    public void getDatav1() {
        docRef = db.collection("mcdonald's").document("mcdonald's_breakfast");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                List<String> list = new ArrayList<>();
                Map<String, Object> map = task.getResult().getData();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    memberList.add(new restaurantProduct(entry.getKey().toString(), entry.getValue().toString()));
                    System.out.println(entry.getKey().toString() + " " + entry.getValue().toString());

                    Map<String, Object> city = new HashMap<>();
                    city.put("name", entry.getKey().toString());
                    city.put("calories",entry.getValue().toString());

                    db.collection("mcdonalds_food").document()
                            .set(city)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
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