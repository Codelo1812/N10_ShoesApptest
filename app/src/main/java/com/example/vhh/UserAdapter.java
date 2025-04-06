package com.example.vhh;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> lstUser;
    private Context context;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public UserAdapter(List<User> lstUser,Context context){
        this.lstUser = lstUser;
        this.context=context;
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder,int position){
        User user = lstUser.get(position);
        holder.text2.setText("Email: " + user.getEmail());
        holder.text1.setText(user.getUserName());
        holder.text3.setText("Role: " + user.getRole());

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context).setTitle("Xoa nguoi dung")
                    .setMessage("Ban co chac chan muon xoa nguoi dung nay?")
                    .setPositiveButton("Xoa",(dialog,which)->{
                        getUserUidFromFirestore(user.getEmail(),uid -> {
                            if(uid !=null){
                                db.collection("users").document(uid).
                                        delete().
                                        addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context,"Da xoa nguoi dung!",Toast.LENGTH_SHORT).show();
                                        lstUser.remove(position);
                                        mAuth.getCurrentUser().delete();
                                        notifyDataSetChanged();;
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(context,"Loi khi xoa!",Toast.LENGTH_SHORT).show();

                                });
                            }
                        });
                    })
                    .setNegativeButton("Huy",null).show();
        });

        holder.btnEdit.setOnClickListener(v -> showEditDialog(user));
    }
    private void showEditDialog(User user){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sua thong tin nguoi dung");

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_user,null);
        EditText editEmail = view.findViewById(R.id.editUserEmail);
        EditText editRole = view.findViewById(R.id.editUserRole);
        EditText editName = view.findViewById(R.id.editUserName);

        editEmail.setText(user.getEmail());
        editRole.setText(user.getRole());
        editName.setText(user.getUserName());

        builder.setView(view);

        builder.setPositiveButton("Luu",(dialog,which) ->{
            String newEmail = editEmail.getText().toString();
            String newRole = editRole.getText().toString();
            String newName = editName.getText().toString();

            getUserUidFromFirestore(user.getEmail(),uid -> {
                if(uid != null){
                    db.collection("users").document(uid).update("email",newEmail,"role",newRole,"username",newName)
                            .addOnSuccessListener(aVoid ->{
                                Toast.makeText(context,"Da cap nhat!",Toast.LENGTH_SHORT).show();
                                user.setEmail(newEmail);
                                user.setRole(newRole);
                                user.setUserName(newName);
                                notifyDataSetChanged();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(context,"Loi khi cap nhat!",Toast.LENGTH_SHORT).show()
                            );
                }else {
                    Toast.makeText(context,"Khong tim thay UID nguoi dung!",Toast.LENGTH_SHORT).show();
                }
            });
        });

        builder.setNegativeButton("Huy",null);
        builder.show();
    }
    public int getItemCount(){
        return lstUser.size();
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder{
        TextView text1,text2,text3;
        Button btnEdit,btnDelete;

        public UserViewHolder(View itemview){
            super(itemview);
            text1 = itemview.findViewById(R.id.text1);
            text2 = itemview.findViewById(R.id.text2);
            text3 = itemview.findViewById(R.id.text3);
            btnEdit = itemview.findViewById(R.id.btnEdit);
            btnDelete = itemview.findViewById(R.id.btnDelete);
        }
    }
    private void getUserUidFromFirestore(String email,OnUidFetchedListener listener){
        db.collection("users").whereEqualTo("email",email).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(!queryDocumentSnapshots.isEmpty()){
                String uid = queryDocumentSnapshots.getDocuments().get(0).getId();
                listener.onUidFetched(uid);
            }
            else {
                listener.onUidFetched(null);
            }
        }).addOnFailureListener(e -> listener.onUidFetched(null));
    }
    interface OnUidFetchedListener{
        void onUidFetched(String uid);
    }
}
