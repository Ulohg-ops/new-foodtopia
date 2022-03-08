package com.example.foodtopia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodtopia.getUserInfo.GetUserInfoActivity1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText mName, mEmail, mPassword, mRePassword;
    Button mRegisterBtn;
    TextView mToLogin;
    FirebaseAuth fAuth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mRePassword = findViewById(R.id.rePassword);
        mRegisterBtn =  findViewById(R.id.registerBotton);
        mToLogin = findViewById(R.id.toLogin);

        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String rePassword = mRePassword.getText().toString().trim();
                String name=mName.getText().toString();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("請輸入信箱");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("請輸入密碼");
                    return;
                }

                if(TextUtils.isEmpty(rePassword)){
                    mRePassword.setError("請重複輸入密碼");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("請輸入至少六碼");
                    return;
                }

                if(!password.equals(rePassword)){
                    mRePassword.setError("密碼不一致");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = fAuth.getCurrentUser();
                            String userID = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", userID);
                            map.put("username", name.toLowerCase());
                            map.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/instagramtest-fcbef.appspot.com/o/placeholder.png?alt=media&token=b09b809d-a5f8-499b-9563-5252262e9a49");
                            map.put("bio", "");

                            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, GetUserInfoActivity1.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "錯誤:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        mToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
//                Intent intent = new Intent();
//                intent.setClass(Login.this, Register.class);
//                startActivity(intent);
            }
        });
    }
}



//orginal rigister
//package com.example.foodtopia;
//
//        import androidx.annotation.NonNull;
//        import androidx.appcompat.app.AppCompatActivity;
//
//        import android.content.Intent;
//        import android.os.Bundle;
//        import android.text.TextUtils;
//        import android.view.View;
//        import android.widget.Button;
//        import android.widget.EditText;
//        import android.widget.TextView;
//        import android.widget.Toast;
//
//        import com.google.android.gms.tasks.OnCompleteListener;
//        import com.google.android.gms.tasks.Task;
//        import com.google.firebase.auth.AuthResult;
//        import com.google.firebase.auth.FirebaseAuth;
//
//public class Register extends AppCompatActivity {
//    EditText mName, mEmail, mPassword, mRePassword;
//    Button mRegisterBtn;
//    TextView mToLogin;
//    FirebaseAuth fAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        mName = findViewById(R.id.fullName);
//        mEmail = findViewById(R.id.email);
//        mPassword = findViewById(R.id.password);
//        mRePassword = findViewById(R.id.rePassword);
//        mRegisterBtn =  findViewById(R.id.registerBotton);
//        mToLogin = findViewById(R.id.toLogin);
//
//        fAuth = FirebaseAuth.getInstance();
//
//        if(fAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }
//
//        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = mEmail.getText().toString().trim();
//                String password = mPassword.getText().toString().trim();
//                String rePassword = mRePassword.getText().toString().trim();
//
//                if(TextUtils.isEmpty(email)){
//                    mEmail.setError("請輸入信箱");
//                    return;
//                }
//
//                if(TextUtils.isEmpty(password)){
//                    mPassword.setError("請輸入密碼");
//                    return;
//                }
//
//                if(TextUtils.isEmpty(rePassword)){
//                    mRePassword.setError("請重複輸入密碼");
//                    return;
//                }
//
//                if(password.length() < 6){
//                    mPassword.setError("請輸入至少六碼");
//                    return;
//                }
//
//                if(!password.equals(rePassword)){
//                    mRePassword.setError("密碼不一致");
//                    return;
//                }
//
//                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(Register.this, "註冊成功", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                        }else{
//                            Toast.makeText(Register.this, "錯誤:" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//            }
//        });
//
//        mToLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), Login.class));
//                finish();
////                Intent intent = new Intent();
////                intent.setClass(Login.this, Register.class);
////                startActivity(intent);
//            }
//        });
//    }
//}
