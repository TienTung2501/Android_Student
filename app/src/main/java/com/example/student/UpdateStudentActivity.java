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

public class UpdateStudentActivity extends AppCompatActivity {

    private Student object;
    TextInputEditText sbdTxt,hotenTxt,diemToan,diemLy,diemHoa;
    Button btnUpdate,btnBack;
    private static final int REQUEST_UPDATE_CONTACT = 1;
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
        setContentView(R.layout.activity_update_student);
        getIntentExtra();
        initComponent();
        eventListener();
    }

    private void eventListener() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _sbd=sbdTxt.getText().toString();
                String _hoten= hotenTxt.getText().toString();
                double _toan= Double.parseDouble(diemToan.getText().toString());
                double _ly= Double.parseDouble(diemLy.getText().toString());
                double _hoa= Double.parseDouble(diemHoa.getText().toString());
                mydb.Update(_sbd,_hoten,_toan,_ly,_hoa);
                Toast.makeText(UpdateStudentActivity.this,"Update Suceess", Toast.LENGTH_SHORT).show();
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
        btnUpdate=findViewById(R.id.UpdateBtn);
        btnBack=findViewById(R.id.BackBtn);
        sbdTxt=findViewById(R.id.SBDTxt);
        hotenTxt=findViewById(R.id.HoTenTxt);
        diemToan=findViewById(R.id.DiemToanTxt);
        diemLy=findViewById(R.id.DiemLyTxt);
        diemHoa=findViewById(R.id.DiemHoaTxt);
        sbdTxt.setText(object.getSoBaoDanh());
        hotenTxt.setText(object.getHoTen());
        diemToan.setText(String.valueOf(object.getDiemToan()));
        diemLy.setText(String.valueOf(object.getDiemLy()));
        diemHoa.setText(String.valueOf(object.getDiemHoa()));
    }

    private void getIntentExtra() {
        Intent intent = getIntent();// cần khai báo biến object ở trên
        object=(Student) intent.getSerializableExtra("object");
    }
}