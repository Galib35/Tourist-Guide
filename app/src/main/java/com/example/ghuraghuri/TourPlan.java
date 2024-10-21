package com.example.ghuraghuri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TourPlan extends AppCompatActivity {

    EditText where,todo;
    Button save,another;

    String place_id;
    int stepCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_plan);

        place_id=getIntent().getStringExtra("plc_id");

        where=findViewById(R.id.where_edt);
        todo=findViewById(R.id.todo_edt);

        save=findViewById(R.id.plan_save_btn);
        another=findViewById(R.id.plan_another_btn);

        where.addTextChangedListener(watcher);
        todo.addTextChangedListener(watcher);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveplan();
            }
        });

        another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                where.getText().clear();
                todo.getText().clear();
                another.setEnabled(false);
            }
        });


    }

    TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String whr,td;
            whr=where.getText().toString().trim();
            td=todo.getText().toString().trim();

            save.setEnabled(!whr.isEmpty() && !td.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    void saveplan()
    {
        String whr,td;
        whr=where.getText().toString().trim();
        td=todo.getText().toString().trim();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("Places").child(place_id).child("Tour Plan");
        stepCount++;
        String step="Step-"+String.valueOf(stepCount);

        ref.child(step).child("place").setValue(whr);
        ref.child(step).child("todo").setValue(td);

        Toast.makeText(this, "Information Saved", Toast.LENGTH_SHORT).show();
        save.setEnabled(false);
        another.setEnabled(true);
    }
}