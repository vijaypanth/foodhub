package com.mangal.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mangal.demo.Common.Common;
import com.mangal.demo.model.user;

public class signIn extends AppCompatActivity {
    EditText edtPhone,edtPssword;
    Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edtPhone=findViewById(R.id.EdtPhone);
        edtPssword=findViewById(R.id.EdtPasswaord);
        btnSignIn=(Button)findViewById(R.id.SignIn);


        //init firebase
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("user");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Common.isConnectedToInternet(getBaseContext())) {


                    final ProgressDialog mDialog = new ProgressDialog(signIn.this);
                    mDialog.setMessage("Please waiting.....");
                    mDialog.show();
                    table_user.addValueEventListener(new ValueEventListener()

                    {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //check if user not exixt in data base
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                mDialog.dismiss();
                                //get user information
                                user User = dataSnapshot.child(edtPhone.getText().toString()).getValue(user.class);

                                User.setPhone(edtPhone.getText().toString());//Set Phone
                                if (User.getPassword().equals(edtPssword.getText().toString())) {
                                    //Toast.makeText(signIn.this, "sign In succssfully", Toast.LENGTH_SHORT).show();
                                    Intent homeIntent = new Intent(signIn.this, home.class);
                                    Common.currentUser = User;
                                    startActivity(homeIntent);
                                    finish();

                                } else {
                                    Toast.makeText(signIn.this, "wrong password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(signIn.this, "user not exist in database", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(signIn.this, "Please check your connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }


        });
    }
}
