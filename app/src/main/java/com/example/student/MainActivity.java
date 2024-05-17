package com.example.student;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;


import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_POST_NOTIFICATIONS = 100;
    private static final int PERMISSION_REQUEST_CODE = 999;
    private static final int REQUEST_UPDATE_CONTACT = 1;
    private static final int REQUEST_ADD_CONTACT = 2;
    public int position;
    public int valueSelected=3;
    public String idStudent;
    private Sqlite myDb;
    private ListView listViewStudent;
    private ArrayList<Student> students;
    private ArrayAdapter<Student> adapter;
    ImageView addBtn;
    TextInputEditText searchTxt;
    String titleMenu;
    NetworkReceiver networkReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
        registerForContextMenu(listViewStudent);
        getPermission();
        eventListener();
    }
    @Override
    protected void onStart() {
        super.onStart();
        myDb.openDb();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        BatteryReceiver batteryReceiver = new BatteryReceiver();
        registerReceiver(batteryReceiver, filter);
//        filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        NetworkReceiver networkReceiver = new NetworkReceiver();
//        registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        myDb.closeDb();
   //     unregisterReceiver(networkReceiver);
    }

    private void eventListener() {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,String.valueOf(students.size()),Toast.LENGTH_LONG).show();
            }
        });
        listViewStudent.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
                idStudent=students.get(i).getSoBaoDanh();
                position=i;
                titleMenu=students.get(i).getHoTen();
                return false;
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AddStudentActivity.class);
                startActivityForResult(intent, REQUEST_ADD_CONTACT);
            }
        });
    }
    private void getPermission() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_CONTACTS},999);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_POST_NOTIFICATIONS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_total_score_asc) {
            valueSelected = 1;
            displayData();
            return true;
        } else if (id == R.id.action_sort_total_score_desc) {
            valueSelected = 2;
            displayData();
            return true;
        } else if (id == R.id.action_sort_student_id_asc) {
            valueSelected = 3;
            displayData();
            return true;
        } else if (id == R.id.action_sort_student_id_desc) {
            valueSelected = 4;
            displayData();
            return true;
        } else if (id == R.id.action_sort_average_score_asc) {
            valueSelected = 5;
            displayData();
            return true;
        } else if (id == R.id.action_sort_average_score_desc) {
            valueSelected = 6;
            displayData();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
        MenuItem editItem = menu.findItem(R.id.mnEdit);
        MenuItem deleteItem = menu.findItem(R.id.mnDelete);
        editItem.setTitle("Edit " + titleMenu);
        deleteItem.setTitle("Delete " + titleMenu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() ==  R.id.mnEdit) {
            Intent intent=new Intent(MainActivity.this, UpdateStudentActivity.class);
            intent.putExtra("object", (Serializable) students.get(position));
            //startActivity(intent);
            startActivityForResult(intent, REQUEST_UPDATE_CONTACT);
            return true;
        }
        else if (item.getItemId() == R.id.mnDelete){
            showDeleteConfirmationDialog();
            return true;
        }
        else if(item.getItemId() == R.id.mnSearchPhone){
            String studentName = students.get(position).getHoTen(); // Lấy tên của thí sinh từ danh sách
            searchPhoneNumber(studentName); // Gọi phương thức tìm kiếm số điện thoại
            return true;
        }
        else {
            return super.onContextItemSelected(item);
        }
    }

    private void searchPhoneNumber(String studentName) {
        // Khởi tạo một URI cho truy vấn danh bạ
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        // Danh sách các cột cần truy vấn từ danh bạ
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER // Lấy số điện thoại
        };

        // Điều kiện truy vấn: tìm kiếm các contact có tên giống với tên của thí sinh
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?";
        String[] selectionArgs = {studentName};

        // Sắp xếp theo thứ tự tên
        String sortOrder = null; // Không cần sắp xếp

        // Thực hiện truy vấn sử dụng ContentResolver
        Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        // Kiểm tra kết quả truy vấn
        if (cursor != null && cursor.moveToFirst()) {
            // Lấy số điện thoại từ kết quả truy vấn
            @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            // Hiển thị số điện thoại trong một Toast hoặc một cách khác tùy thuộc vào nhu cầu của bạn
            Toast.makeText(this, "Số điện thoại của " + studentName + " là: " + phoneNumber, Toast.LENGTH_LONG).show();

            // Đóng cursor sau khi sử dụng
            cursor.close();
        } else {
            // Không tìm thấy số điện thoại cho thí sinh
            Toast.makeText(this, "Không tìm thấy số điện thoại cho " + studentName, Toast.LENGTH_LONG).show();
        }
    }

    private void initComponent() {
        myDb=new Sqlite(this);
        students=new ArrayList<>();
        listViewStudent=findViewById(R.id.ListViewStudent);
        addBtn=findViewById(R.id.addBtn);
        searchTxt=findViewById(R.id.SearchTxt);

        displayData();

    }
    private void sortContactsByName() {
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return s1.getHoTen().compareToIgnoreCase(s2.getHoTen());
            }
        });
    }
    private void displayData(){
        students.clear();
        fetchData();
        switch (valueSelected){
            case 1:
                sortStudentsByTotalScoreAsc();
                break;
            case 2:
                sortStudentsByTotalScoreDesc();
                break;
            case 3:
                sortStudentsByStudentIdAsc();
                break;
            case 4:
                sortStudentsByStudentIdDesc();
                break;
            case 5:
                sortStudentsByAverageScoreAsc();
                break;
            case 6:
                sortStudentsByAverageScoreDesc();
                break;
        }
        adapter=new StudentAdapter(MainActivity.this, R.layout.student_item, students);
        listViewStudent.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // Cập nhật dữ liệu trong Adapter
    }
    private  void fetchData(){
        Cursor cursor= myDb.DisplayAll();
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            String sbd=cursor.getString(cursor.getColumnIndexOrThrow(Sqlite.getSBD()));
            String hoten=cursor.getString(cursor.getColumnIndexOrThrow(Sqlite.getHoTen()));
            double toan=cursor.getDouble(cursor.getColumnIndexOrThrow(Sqlite.getDiemToan()));
            double ly=cursor.getDouble(cursor.getColumnIndexOrThrow(Sqlite.getDiemLy()));
            double hoa=cursor.getDouble(cursor.getColumnIndexOrThrow(Sqlite.getDiemHoa()));
            Student c=new Student(sbd,hoten,toan,ly,hoa);
            students.add(c);
        }

    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(students.get(position).getHoTen()+" Are you want to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Xử lý khi người dùng đồng ý xóa
                        deleteContact(idStudent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    private void deleteContact(String sbd) {
        myDb.Delete(sbd);
        for (int i=0;i<students.size();i++){
            if(students.get(i).getSoBaoDanh()==sbd){
                students.remove(i);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPDATE_CONTACT && resultCode == RESULT_OK) {
            // Nếu dữ liệu đã được cập nhật trong InformationDetailActivity, làm mới danh sách và hiển thị lại
            displayData();
        }
        if(requestCode == REQUEST_ADD_CONTACT && resultCode == RESULT_OK){
            // If a new contact is added successfully, refresh the contact list
            displayData();
        }
    }
    private void sortStudentsByTotalScoreAsc() {
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Double.compare(s1.tinhTongDiem(), s2.tinhTongDiem());
            }
        });

    }
    private void sortStudentsByTotalScoreDesc() {
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Double.compare(s2.tinhTongDiem(), s1.tinhTongDiem());
            }
        });

    }
    private void sortStudentsByStudentIdAsc() {
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return s1.getSoBaoDanh().compareTo(s2.getSoBaoDanh());
            }
        });
    }
    private void sortStudentsByStudentIdDesc() {
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return s2.getSoBaoDanh().compareTo(s1.getSoBaoDanh());
            }
        });

    }
    private void sortStudentsByAverageScoreAsc() {
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Double.compare(s1.tinhDiemTrungBinh(), s2.tinhDiemTrungBinh());
            }
        });

    }
    private void sortStudentsByAverageScoreDesc() {
        Collections.sort(students, new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                return Double.compare(s2.tinhDiemTrungBinh(), s1.tinhDiemTrungBinh());
            }
        });

    }


}