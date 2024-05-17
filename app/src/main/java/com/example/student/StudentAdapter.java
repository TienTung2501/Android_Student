package com.example.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {
    public StudentAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public StudentAdapter(@NonNull Context context, int resource, @NonNull List<Student> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v= convertView;
        if(v==null){
            LayoutInflater vi=LayoutInflater.from(getContext());
            v=vi.inflate(R.layout.student_item,null);// lấy từ layout ta thiết kế

        }
        Student sv=getItem(position);
        if(sv!= null){

            TextView sbd= v.findViewById(R.id.SBDTxt);
            TextView hoten=v.findViewById(R.id.HoTenTxt);
            TextView tongdiem=v.findViewById(R.id.TongDiemTxt);

            sbd.setText(String.valueOf(sv.getSoBaoDanh()));
            hoten.setText(String.valueOf(sv.getHoTen()));
            tongdiem.setText(String.valueOf(sv.tinhTongDiem()));
        }
        return v;
    }
}
