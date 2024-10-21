package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Account extends AppCompatActivity {

    EditText name,email,currPass,newPass,confPass;
    Button saveInfoBtn,savePassBtn;

    TextInputLayout nameText,currPassText,newPassText,confPassText;

    FirebaseAuth auth;

    boolean download=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        name=findViewById(R.id.name_acc);
        email=findViewById(R.id.email_acc);
        currPass=findViewById(R.id.curr_pass_acc);
        newPass=findViewById(R.id.new_pass_acc);
        confPass=findViewById(R.id.conf_pass_acc);

        saveInfoBtn=findViewById(R.id.save_info_btn);
        savePassBtn=findViewById(R.id.save_pass_btn);

        nameText=findViewById(R.id.name_input);
        currPassText=findViewById(R.id.cur_pass_input);
        newPassText=findViewById(R.id.new_pass_input);
        confPassText=findViewById(R.id.conf_pass_input);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String nm=name.getText().toString().trim();

                if(nm.isEmpty())
                {
                    nameText.setError("Name must be provide");
                }
                else
                {
                    nameText.setError(null);
                }

                if (download)
                {
                    saveInfoBtn.setEnabled(!nm.isEmpty());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        currPass.addTextChangedListener(watcher);
        newPass.addTextChangedListener(watcher);
        confPass.addTextChangedListener(watcher);

        auth=FirebaseAuth.getInstance();

        readInfo();
        saveInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo();
            }
        });

        savePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePass();
            }
        });


    }
    TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String cpass,nPass,conPass;

            cpass=currPass.getText().toString().trim();
            nPass=newPass.getText().toString().trim();
            conPass=confPass.getText().toString().trim();

            savePassBtn.setEnabled(!cpass.isEmpty() && !nPass.isEmpty() && !conPass.isEmpty());

            switch (Constant.error)
            {
                case 'l':
                case 's':
                    newPassText.setError(null);
                    break;
                case 'c':
                    confPassText.setError(null);
                default:
                    Constant.error='n';
                    break;


            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    void readInfo()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("User");

        String uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        String em=auth.getCurrentUser().getEmail();
        email.setText(em);

        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uName= Objects.requireNonNull(snapshot.child("Name").getValue()).toString();
                name.setText(uName);
                download=true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    void saveInfo()
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("User");

        String uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        ref.child(uid).child("Name").setValue(name.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Account.this, "Information Updated", Toast.LENGTH_SHORT).show();
                    recreate();
                }
            }
        });
    }

    void changePass()
    {
        String cur_pass,n_pass,conf_pass;
        cur_pass=currPass.getText().toString().trim();
        n_pass=newPass.getText().toString().trim();
        conf_pass=confPass.getText().toString().trim();

        if (n_pass.length()<6)
        {
            newPassText.setError("Password must have at least 6 characters!");
            Constant.error='l';
        }
        else if(n_pass.contains(" "))
        {
            newPassText.setError("No space is allowed!");
            Constant.error='s';
        }
        else if(!n_pass.equals(conf_pass))
        {
            confPassText.setError("Password does't match!");
            Constant.error='c';
        }
        else
        {

            String uEmail= Objects.requireNonNull(auth.getCurrentUser()).getEmail();

            assert uEmail != null;
            AuthCredential credential= EmailAuthProvider.getCredential(uEmail,cur_pass);

            FirebaseUser user=auth.getCurrentUser();

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        user.updatePassword(n_pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor=preferences.edit();
                                    editor.putBoolean("rem",false);
                                    editor.apply();
                                    Toast.makeText(Account.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(Account.this,SignIn.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(Account.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(Account.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
}