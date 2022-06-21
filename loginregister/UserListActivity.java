package com.example.loginregister;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private  UsersAdapter usersAdapter;
    private List<Users> usersList;
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this,AdminControlPanel.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        recyclerView = findViewById(R.id.userrecyclerlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersList = new ArrayList<>();
        LoadALLUsers();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void LoadALLUsers() {
        JsonArrayRequest request = new JsonArrayRequest(Urls.SHOW_All_USER_DATA_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                for (int i=0;i<array.length();i++){
                    try {
                        JSONObject object = array.getJSONObject(i);
                        String username = object.getString("username").trim();
                        String email = object.getString("email").trim();
                        String fullname = object.getString("fullname").trim();
                        String id = object.getString("id").trim();
                        String userlevel = object.getString("userlevel").trim();

                        Users users = new Users();
                        users.setName(username);
                        users.setEmail(email);
                        users.setFullname(fullname);
                        users.setUserid(id);
                        users.setUserlevel(userlevel);
                        usersList.add(users);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                usersAdapter = new UsersAdapter(UserListActivity.this,usersList);
                recyclerView.setAdapter(usersAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserListActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(UserListActivity.this);
        requestQueue.add(request);
    }
}