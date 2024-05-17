package com.example.student;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Sqlite extends SQLiteOpenHelper {
    private static final String DBName="NguyenTienTung_Sqlite.db";
    private static final int  VERSION=1;
    private static final String TABLENAME="Student";
    private  static  String SBD="_sbd";
    private static String HoTen="hoten";
    private static  String DiemToan="diemtoan";
    private static  String DiemLy="diemly";
    private static  String DiemHoa="diemhoa";

    private SQLiteDatabase myDB;

    public Sqlite(@Nullable Context context) {
        super(context, DBName,null, VERSION);
    }

    public static String getSBD() {
        return SBD;
    }

    public static String getHoTen() {
        return HoTen;
    }

    public static String getDiemToan() {
        return DiemToan;
    }

    public static String getDiemLy() {
        return DiemLy;
    }

    public static String getDiemHoa() {
        return DiemHoa;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryTable = "CREATE TABLE " + TABLENAME +
                "( " + SBD + " TEXT PRIMARY KEY," +
                HoTen + " TEXT NOT NULL, " +
                DiemToan + " REAL NOT NULL, " +
                DiemLy + " REAL NOT NULL, " +
                DiemHoa + " REAL NOT NULL " + ")";
        db.execSQL(queryTable);
        ArrayList<Student> students=new ArrayList<>();
        ArrayList<Student> list = new ArrayList<>();
        list.add(new Student("GHA01839", "Nguyen Thanh Lam", 9.5,9.5,9.5));
        list.add(new Student("GHA02939", "Nguyen Thanh Dat", 10,9,9.5));
        list.add(new Student("GHA03839", "Nguyen Van Thi", 9.5,9,8.75));
        list.add(new Student("GHA05839", "Nguyen Tien Tung", 9.5,9.5,9));
        list.add(new Student("GHA04839", "Nguyen Tien Tu", 10,10,9.5));
        list.add(new Student("GHA05820", "Nguyen Tra My", 9.75,9.5,9.8));

        for (Student student : list) {
            ContentValues values = new ContentValues();
            values.clear();
            values.put(SBD, student.getSoBaoDanh());
            values.put(HoTen,student.getHoTen());
            values.put(DiemToan, student.getDiemToan());
            values.put(DiemLy, student.getDiemLy());
            values.put(DiemHoa, student.getDiemHoa());
            db.insert(TABLENAME, null, values);
        }



    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void openDb(){
        myDB= getWritableDatabase();
    }
    public void closeDb(){
        if(myDB !=null && myDB.isOpen()){
            myDB.close();
        }
    }
    public long Insert(String sbd,String name, double toan,double ly,double hoa){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(SBD, sbd);
        values.put(HoTen,name);
        values.put(DiemToan,toan);
        values.put(DiemLy, ly);
        values.put(DiemHoa,hoa);
        return db.insert(TABLENAME,null,values);
    }
    public Cursor DisplayAll(){
        SQLiteDatabase db = getReadableDatabase(); // Use getReadableDatabase() instead of myDB
        String query = "SELECT * FROM " + TABLENAME;
        return db.rawQuery(query, null);
    }
    public long Update(String sbd, String name, double toan, double ly, double hoa) {
        ContentValues values = new ContentValues();
        values.put(SBD, sbd);
        values.put(HoTen, name);
        values.put(DiemToan, toan);
        values.put(DiemLy, ly);
        values.put(DiemHoa, hoa);

        String where = SBD + " = ?";
        String[] whereArgs = {sbd};

        return myDB.update(TABLENAME, values, where, whereArgs);
    }

    public long Delete(String sbd) {
        String where = SBD + " = ?";
        String[] whereArgs = {sbd};

        return myDB.delete(TABLENAME, where, whereArgs);
    }

}
