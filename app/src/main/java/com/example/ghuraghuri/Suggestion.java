package com.example.ghuraghuri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Suggestion extends AppCompatActivity {
    EditText suggestion;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        suggestion=findViewById(R.id.sugg_txt);
        submit=findViewById(R.id.submit_fb_btn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=suggestion.getText().toString().trim();

                if(text.isEmpty())
                {
                    Toast.makeText(Suggestion.this, "Please write something", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Feedback");
                    FirebaseAuth auth=FirebaseAuth.getInstance();

                    String uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();

                    ref.child(uid).setValue(text).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                suggestion.setText(null);
                                Toast.makeText(Suggestion.this, "Thanks for your opinion", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Suggestion.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}