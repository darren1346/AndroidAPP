package com.example.loginregister;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextInputEditText textInputEditTextUsername,textInputEditTextPassword;
    Button buttonLogin;
    TextView textViewSignUp,showForgotPasswordDialog;
    ProgressBar progressBar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progress);
        showForgotPasswordDialog = findViewById(R.id.btnForgotPassword);
        showForgotPasswordDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserForgotPasswordWithEmail();
            }
        });
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
                finish();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  username ,password  ;
                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());

                if( !username.equals("") && !password.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;
                            PutData putData = new PutData("http://192.168.1.103/LoginRegister/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Admin Login Success")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),AdminControlPanel.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if (result.equals("User Login Success")){
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),UserControlPanel.class);
                                        startActivity(intent);
                                        finish();
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

    }

    private void UserForgotPasswordWithEmail() {
        View forgot_password_layout = LayoutInflater.from(this).inflate(R.layout.forgot_password,null);
        EditText forgot_email = forgot_password_layout.findViewById(R.id.forgot_email);
        Button btnForgotPass = forgot_password_layout.findViewById(R.id.forgot_password_button);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        builder.setView(forgot_password_layout);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                final String email = forgot_email.getText().toString().trim();
                if(email.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(Login.this,"Enter a email",Toast.LENGTH_SHORT).show();
                }else
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.FORGOT_PASSWORD_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String mail = object.getString("mail");
                                if(mail.equals("send")){
                                    progressDialog.dismiss();
                                    dialog.dismiss();
                                    Toast.makeText(Login.this,"Email are successfully send",Toast.LENGTH_SHORT).show();
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(Login.this,response,Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            dialog.dismiss();
                            Toast.makeText(Login.this,error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError{
                            HashMap<String,String> forgotparam = new HashMap<>();
                            forgotparam.put("email",email);
                            return forgotparam;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                    requestQueue.add(stringRequest);
                }

            }
        });
    }
}