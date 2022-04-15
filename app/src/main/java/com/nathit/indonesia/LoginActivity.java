package com.nathit.indonesia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText Email, Password;
    FirebaseAuth firebaseAuth;
    String uid,etName;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);

        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);

        Button LoginBtn = (Button) findViewById(R.id.btn_login);
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String etEmail = Email.getText().toString().trim();
                String etPassword = Password.getText().toString().trim();
                String checkPassword = "^(?=\\S+$).{6,20}$";
                if (!Patterns.EMAIL_ADDRESS.matcher(etEmail).matches()) {
                    Email.setError("โปรดป้อนที่อยู่อีเมลของคุณให้ครบถ้วน");
                    Email.requestFocus();
                } else if (TextUtils.isEmpty(etPassword)) {
                    Password.setError("กรุณาใส่รหัสผ่านของคุณ");
                    Password.requestFocus();
                } else if (!etPassword.matches(checkPassword)) {
                    Password.setError("กรุณาป้อนรหัสผ่านตั้งแต่ 6 ตัวอักษรขึ้นไป");
                    Password.requestFocus();
                } else {
                    dialog.setTitle("รอสักครู่...");
                    dialog.show();
                    loginUser(etEmail, etPassword);
                }
            }
        });
    }

    private void loginUser(String etEmail, String etPassword) {
        firebaseAuth.signInWithEmailAndPassword(etEmail, etPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                String etEmail = user.getEmail();
                                uid = user.getUid();

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("uid", uid);
                                hashMap.put("email", etEmail);
                                hashMap.put("name", etName);
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(uid).setValue(hashMap);
                            }
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            dialog.dismiss();
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Email.setError("ชื่อผู้ใช้ไม่มีอยู่ในระบบ กรุณาลงทะเบียนใหม่อีกครั้ง");
                                Email.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Email.setError("ข้อมูลบางอย่างไม่ถูกต้อง โปรดตรวจสอบและลองอีกครั้ง");
                                Email.requestFocus();
                            } catch (Exception e) {
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    }
                });
    }

    int backPressed = 0;
    @Override
    public void onBackPressed() {
        backPressed++;
        if (backPressed == 1) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        super.onBackPressed();
    }
}