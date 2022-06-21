package com.example.loginregister;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class nodeAdapter extends RecyclerView.Adapter<nodeAdapter.NodeHolder>{
    Context context;
    List<node> nodeList;

    public nodeAdapter(Context context, List<node> nodeList) {
        this.context = context;
        this.nodeList = nodeList;
    }

    @NonNull
    @Override
    public NodeHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View nodelayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.node_list,parent,false);
        return new NodeHolder(nodelayout);
    }

    @Override
    public void onBindViewHolder(@NonNull  nodeAdapter.NodeHolder holder, int position) {
        node node = nodeList.get(position);
        holder.nodename.setText(node.getNodename());
        holder.nodeid.setText(node.getNodeid());
        holder.inputx.setText(node.getInputx());
        holder.inputy.setText(node.getInputy());
        holder.Deletenode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("DELETE NODE");
                builder.setMessage("Confirm to delete "+node.getNodename());
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DELETE_NODE_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                       try{
                                           JSONObject object = new JSONObject(response);
                                           String check = object.getString("state");
                                           if(check.equals("delete")){
                                               Delete(position);
                                               Toast.makeText(context,"Delete Successful",Toast.LENGTH_SHORT).show();
                                           }else{
                                               Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                                           }
                                       }catch (JSONException e){
                                           e.printStackTrace();
                                       }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context,error.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> deleteParams = new HashMap<>();
                                deleteParams.put("nodename",node.getNodename());
                                return deleteParams;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return nodeList.size();
    }

    public class NodeHolder extends RecyclerView.ViewHolder{
        TextView nodename,nodeid,inputx,inputy;
        ImageButton Deletenode ;
        public NodeHolder(@NonNull View itemView){
            super(itemView);
            nodename = itemView.findViewById(R.id.rcy_nodename);
            nodeid = itemView.findViewById(R.id.rcy_nodeid);
            inputx = itemView.findViewById(R.id.rcy_inputx);
            inputy = itemView.findViewById(R.id.rcy_inputy);
            Deletenode = itemView.findViewById(R.id.deletenode);
        }

    }
    public void Delete(int item ){
        nodeList.remove(item);
        notifyItemRemoved(item);
    }
}
