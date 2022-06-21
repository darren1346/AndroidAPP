package com.example.loginregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminControlPanel extends AppCompatActivity {
    Button buttonchecknode,buttoncheckuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control_panel);
        buttonchecknode = findViewById(R.id.checknode);
        buttoncheckuser = findViewById(R.id.checkuser);
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