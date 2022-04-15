package com.nathit.indonesia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nathit.indonesia.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password, confirmPassword;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String uid;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regster);

        firebaseAuth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        TextView nextToLogin = (TextView) findViewById(R.id.tv_nextToLogin);
        nextToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button RegisterBtn = (Button) findViewById(R.id.btn_register);
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String etEmail = email.getText().toString().trim();
                String etName = name.getText().toString().trim();
                String etPassword = password.getText().toString().trim();
                String etConfirmPassword = confirmPassword.getText().toString().trim();
                String checkPassword = "^(?=\\S+$).{6,20}$";

                    if (!Patterns.EMAIL_ADDRESS.matcher(etEmail).matches()) {
                        email.setError("กรุณากรอกอีเมลให้ถูกต้อง");
                        email.requestFocus();
                    } else if (TextUtils.isEmpty(etName)) {
                        name.setError("กรุณากรอกชื่อของท่าน");
                        name.requestFocus();
                    } else if (TextUtils.isEmpty(etPassword)) {
                        password.setError("กรุณากรอกรหัสผ่าน");
                        password.requestFocus();
                    } else if (!etPassword.matches(checkPassword)) {
                        password.setError("กรุณากรอกรหัสผ่านอย่างน้อย 6 ตัวขึ้นไป");
                        password.requestFocus();
                    } else if (TextUtils.isEmpty(etConfirmPassword)) {
                        confirmPassword.setError("กรุณายืนยันรหัสผ่าน");
                        confirmPassword.requestFocus();
                    } else if (!etConfirmPassword.matches(checkPassword)) {
                        confirmPassword.setError("รหัสผ่านไม่ตรงกัน!");
                        confirmPassword.requestFocus();
                    } else if (!etPassword.matches(etConfirmPassword)) {
                        confirmPassword.setError("รหัสผ่านไม่ตรงกัน!");
                        confirmPassword.requestFocus();
                    } else {
                        registerCreateUser(etEmail, etName, etPassword);
                    }
            }
        });
    }

    private void registerCreateUser(String etEmail, String etName, String etPassword) {
        dialog.setTitle("รอสักครู่...");
        dialog.show();
        firebaseAuth.createUserWithEmailAndPassword(etEmail, etPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            uid = user.getUid();
                            String etEmail = user.getEmail();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("uid", uid);
                            hashMap.put("email", etEmail);
                            hashMap.put("name", etName);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            dialog.dismiss();
                            reference.child(uid).setValue(hashMap);
                            Toast.makeText(RegisterActivity.this, "สมัครสมาชิกเรียบร้อย", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                password.setError("รหัสผ่านของคุณอ่อนแอเกินไป");
                                password.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                email.setError("อีเมลของคุณไม่ถูกต้องหรือมีการใช้งานอยู่แล้ว");
                                email.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                email.setError("ผู้ใช้ได้ลงทะเบียนกับอีเมลนี้แล้ว กรุณาลงทะเบียนด้วยที่อยู่อีเมลอื่น");
                                email.requestFocus();
                            } catch (Exception e) {
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    }
                });
    }

    int backPressed = 0;
    @Override
    public void onBackPressed () {
        backPressed++;
        if (backPressed == 1) {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        super.onBackPressed();
    }
}