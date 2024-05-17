package com.example.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class AddStudentActivity extends AppCompatActivity {

    TextInputEditText sbdTxt,hoTenTxt,diemToanTxt,diemLyTxt,diemHoaTxt;
    Button btnAdd,btnBack;
    private static final int REQUEST_ADD_CONTACT = 2;
    Sqlite mydb;
    @Override
    protected void onStart() {
        super.onStart();
        mydb.openDb();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mydb.closeDb();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_student);
        getIntentExtra();
        initComponent();
        eventListener();
    }

    private void eventListener() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _sbd=sbdTxt.getText().toString();
                String _hoten= hoTenTxt.getText().toString();
                double _toan= Double.parseDouble(diemToanTxt.getText().toString());
                double _ly= Double.parseDouble(diemLyTxt.getText().toString());
                double _hoa= Double.parseDouble(diemHoaTxt.getText().toString());
                mydb.Insert(_sbd,_hoten,_toan,_ly,_hoa);
                Toast.makeText(AddStudentActivity.this,"Update Suceess", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initComponent() {
        mydb=new Sqlite(this);
        btnAdd=findViewById(R.id.AddBtn);
        btnBack=findViewById(R.id.BackBtn);
        sbdTxt=findViewById(R.id.SBDTxt);
        hoTenTxt=findViewById(R.id.HoTenTxt);
        diemToanTxt=findViewById(R.id.DiemToanTxt);
        diemLyTxt=findViewById(R.id.DiemLyTxt);
        diemHoaTxt=findViewById(R.id.DiemHoaTxt);
    }

    private void getIntentExtra() {
        Intent intent = getIntent();// cần khai báo biến object ở trên
        //object=(Contact) intent.getSerializableExtra("object");
    }
}