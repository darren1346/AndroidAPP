package com.example.loginregister;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersHolder>{
    Context context;
    List<Users> usersList;

    public UsersAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View userLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list,parent,false);
        return new UsersHolder(userLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull  UsersHolder holder, int position) {
        Users users = usersList.get(position);
        holder.Name.setText(users.getName());
        holder.Email.setText(users.getEmail());
        holder.Fullname.setText(users.getFullname());
        holder.Userid.setText(users.getUserid());
        holder.Userlevel.setText(users.getUserlevel());
        holder.Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View editLayout = LayoutInflater.from(context).inflate(R.layout.editlevel,null);
                EditText Userlevel = editLayout.findViewById(R.id.edt_userlevel);
                //Userlevel.setText(users.getUserlevel());
                final String username = users.getName();
                String oldlevel = users.getUserlevel();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                if(oldlevel.equals("0")==true) {
                    builder.setTitle("Change user level to manager");
                }else {
                    builder.setTitle("Change manager level to user");
                }
                builder.setView(editLayout);
                //final String userlevel = Userlevel.getText().toString();
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //if(userlevel.isEmpty()){
                         //  Toast.makeText(context,"Field can not be empty",Toast.LENGTH_SHORT).show();
                        //}else{
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.EDIT_USER_URL, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    HashMap<String,String> params = new HashMap<>();
                                    if(oldlevel.equals("1")) {
                                        params.put("userlevel", "0");
                                    }else{
                                        params.put("userlevel", "1");
                                    }
                                    params.put("username",username);
                                    return params;
                                }
                            };
                            RequestQueue queue = Volley.newRequestQueue(context);
                            queue.add(stringRequest);
                        //}
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.deleteuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete User");
                builder.setMessage("Confirm to delete "+users.getName());
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.DELET_USER_URL,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject object = new JSONObject(response);
                                            String check = object.getString("state");
                                            if(check.equals("delete")){
                                                deleteuser(position);
                                                Toast.makeText(context,"Delete Success",Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context,error.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                HashMap<String,String> deleteParams = new HashMap<>();
                                deleteParams.put("username",users.getName());
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
        return usersList.size();
    }


    public class UsersHolder extends RecyclerView.ViewHolder {
        TextView Name,Email,Fullname,Userid,Userlevel;
        ImageButton deleteuser;
        Button Edit;
        public UsersHolder(@NonNull  View itemView) {
            super(itemView);
            Name  = itemView.findViewById(R.id.rcy_username);
            Email = itemView.findViewById(R.id.rcy_email);
            Fullname = itemView.findViewById(R.id.rcy_fullname);
            Userid = itemView.findViewById(R.id.rcy_userid);
            Userlevel = itemView.findViewById(R.id.rcy_userlevel);
            deleteuser = itemView.findViewById(R.id.deleteuser);
            Edit = itemView.findViewById(R.id.rcy_edit);
        }
    }
    public void deleteuser(int item){
        usersList.remove(item);
        notifyItemRemoved(item);
    }
}
