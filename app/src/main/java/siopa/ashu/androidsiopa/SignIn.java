package siopa.ashu.androidsiopa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;
import siopa.ashu.androidsiopa.Common.Common;
import siopa.ashu.androidsiopa.Model.User;

public class SignIn extends AppCompatActivity {
    EditText edtPhone, edtPassword;
    Button btnSignIn;
    com.rey.material.widget.CheckBox ckbRemember;
    TextView txtForgotPwd;

    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        ckbRemember = (com.rey.material.widget.CheckBox) findViewById(R.id.ckbRemember);
        txtForgotPwd = (TextView)findViewById(R.id.txtForgotPwd);

        //Init Paper
        Paper.init(this);

        //Init Firebase

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        txtForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPwdDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {

                    //save user and password
                    if (ckbRemember.isChecked())
                    {
                        Paper.book().write(Common.USER_KEY,edtPhone.getText().toString());
                        Paper.book().write(Common.PWD_KEY,edtPassword.getText().toString());
                    }

                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Please wait...");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {


                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Check if user doesn't exist in database
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                //Get User Information
                                mDialog.dismiss();
                                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                user.setPhone(edtPhone.getText().toString());  //set phone
                                if (user.getPassword().equals(edtPassword.getText().toString())) {
                                    {
                                        Intent homeIntent = new Intent(SignIn.this, Home.class);
                                        Common.CurrentUser = user;
                                        startActivity(homeIntent);
                                        finish();

                                        table_user.removeEventListener(this);
                                    }

                                } else {
                                    Toast.makeText(SignIn.this, "Wrong Password !!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "User does not exist in database", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(SignIn.this,"Please check your connection !!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

        });



    }

    private void showForgotPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        builder.setMessage("Enter your secure code");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgot_view = inflater.inflate(R.layout.forgot_password_layout,null);

        builder.setView(forgot_view);
        builder.setIcon(R.drawable.ic_security_black_24dp);

        final MaterialEditText edtPhone = (MaterialEditText)forgot_view.findViewById(R.id.edtPhone);
        final MaterialEditText edtSecureCode = (MaterialEditText)forgot_view.findViewById(R.id.edtSecureCode);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //check if user available
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.child(edtPhone.getText().toString())
                                .getValue(User.class);

                        if (user.getSecureCode().equals(edtSecureCode.getText().toString()))
                            Toast.makeText(SignIn.this, "Your Password : "+user.getPassword(), Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(SignIn.this, "Wrong Secure Code !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

            }
        });

        builder.show();
    }
}
