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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NodeListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private nodeAdapter nodeAdapter;
    private List<node> nodeList;
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
        setContentView(R.layout.activity_node_list);
        recyclerView = findViewById(R.id.recyclerlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nodeList = new ArrayList<>();
        LoadAllnode();
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void LoadAllnode() {
        JsonArrayRequest request = new JsonArrayRequest(Urls.SHOW_ALL_NODE_DATA_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray array) {
                for(int i= 0 ;i< array.length();i++){
                    try {
                        JSONObject object = array.getJSONObject(i);
                        String nodename = object.getString("nodename").trim();
                        String nodeid = object.getString("nodeid").trim();
                        String inputx = object.getString("inputx").trim();
                        String inputy = object.getString("inputy").trim();

                        node node = new node();
                        node.setNodename(nodename);
                        node.setNodeid(nodeid);
                        node.setInputx(inputx);
                        node.setInputy(inputy);
                        nodeList.add(node);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                nodeAdapter = new nodeAdapter(NodeListActivity.this,nodeList);
                recyclerView.setAdapter(nodeAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NodeListActivity.this, error.toString() , Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(NodeListActivity.this);
        requestQueue.add(request);
    }
}