package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class nodemanage extends AppCompatActivity {
    TextInputEditText textInputEditTextname,textInputEditTextinputx,textInputEditTextinputy;
    Button buttonaddnode;
    Button buttonchecknode,buttoncheckuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nodemanage);
        textInputEditTextname = findViewById(R.id.nodename);
        textInputEditTextinputx = findViewById(R.id.inputx);
        textInputEditTextinputy = findViewById(R.id.inputy);
        buttonaddnode = findViewById(R.id.addnode);
        buttonchecknode = findViewById(R.id.checknode);
        buttoncheckuser = findViewById(R.id.checkuser);
        buttonaddnode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nodename ,inputx ,inputy ;
                nodename = String.valueOf(textInputEditTextname.getText());
                inputx = String.valueOf(textInputEditTextinputx.getText());
                inputy = String.valueOf(textInputEditTextinputy.getText());
                if(!nodename.equals("")&& !inputx.equals("") && !inputy.equals("") ) {
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[3];
                            field[0] = "nodename";
                            field[1] = "inputx";
                            field[2] = "inputy";
                            String[] data = new String[3];
                            data[0] = nodename;
                            data[1] = inputx;
                            data[2] = inputy;
                            PutData putData = new PutData("http://192.168.1.102/LoginRegister/addnode.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Add Success")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),NodeListActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(),"All fields required",Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonchecknode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),NodeListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        buttoncheckuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}