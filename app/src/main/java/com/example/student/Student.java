package com.example.student;

import java.io.Serializable;

public class Student implements Serializable {
    private String soBaoDanh;
    private String hoTen;
    private double diemToan;
    private double diemLy;
    private double diemHoa;
    public Student(String soBaoDanh, String hoTen, double diemToan, double diemLy, double diemHoa) {
        this.soBaoDanh = soBaoDanh;
        this.hoTen = hoTen;
        this.diemToan = diemToan;
        this.diemLy = diemLy;
        this.diemHoa = diemHoa;
    }

    public String getSoBaoDanh() {
        return soBaoDanh;
    }

    public String getHoTen() {
        return hoTen;
    }

    public double getDiemToan() {
        return diemToan;
    }

    public double getDiemLy() {
        return diemLy;
    }

    public double getDiemHoa() {
        return diemHoa;
    }

    public void setSoBaoDanh(String soBaoDanh) {
        this.soBaoDanh = soBaoDanh;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setDiemToan(double diemToan) {
        this.diemToan = diemToan;
    }

    public void setDiemLy(double diemLy) {
        this.diemLy = diemLy;
    }

    public void setDiemHoa(double diemHoa) {
        this.diemHoa = diemHoa;
    }
    // Phương thức tính tổng điểm
    public double tinhTongDiem() {
        return diemToan + diemLy + diemHoa;
    }

    // Phương thức tính điểm trung bình
    public double tinhDiemTrungBinh() {
        return tinhTongDiem() / 3;
    }
}
