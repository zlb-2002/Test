package com.example.chatui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.List;

public class Account extends AppCompatActivity {

    private EditText account_edit;
    private EditText password_edit;
    private Button login_button;
    private Button create_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        account_edit = (EditText) findViewById(R.id.Account_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);
        login_button = (Button) findViewById(R.id.login_button);
        create_button = (Button) findViewById(R.id.create_button);


        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = account_edit.getText().toString();
                String password = password_edit.getText().toString();
                if (("".equals(account) || account == null) && ("".equals(password) || password == null)) {
                    Toast.makeText(Account.this, "账号密码都不能为空", Toast.LENGTH_SHORT).show();
                } else if ("".equals(password) || password == null) {
                    Toast.makeText(Account.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if ("".equals(account) || account == null) {
                    Toast.makeText(Account.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    LitePal.getDatabase();
                    Login login = new Login();
                    login.setAccount(account);
                    login.setPassword(password);
                    login.save();
                    account_edit.setText("");
                    password_edit.setText("");
                    Toast.makeText(Account.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                }
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = account_edit.getText().toString();
                String password = password_edit.getText().toString();
                List<Login> list = LitePal.findAll(Login.class);


                if (("".equals(account) || account == null) && ("".equals(password) || password == null)) {
                    Toast.makeText(Account.this, "账号密码都不能为空", Toast.LENGTH_SHORT).show();
                } else if ("".equals(password) || password == null) {
                    Toast.makeText(Account.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if ("".equals(account) || account == null) {
                    Toast.makeText(Account.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                }  else {

                    for (Login login : list) {
                        String a = login.getAccount();
                        String p = login.getPassword();

                        if (a.equals(account) && p.equals(password)) {
                            Intent intent = new Intent(Account.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return;

                        }
                    }
                    Toast.makeText(Account.this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
                    account_edit.setText("");
                    password_edit.setText("");
                }

            }
        });






    }
}