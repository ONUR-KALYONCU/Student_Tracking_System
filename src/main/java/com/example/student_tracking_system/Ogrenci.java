package com.example.student_tracking_system;

import java.time.LocalDate;

public class Ogrenci {
    private String advesoyad,bolumu,sinifi;
    private Integer ogrenciNo;
    private LocalDate kayitTarihi;

    public Ogrenci(String advesoyad, String bolumu, String sinifi, Integer ogrenciNo, LocalDate kayitTarihi) {
        super();
        this.advesoyad = advesoyad;
        this.bolumu = bolumu;
        this.sinifi = sinifi;
        this.ogrenciNo = ogrenciNo;
        this.kayitTarihi = kayitTarihi;
    }

    public String getAdvesoyad() {
        return advesoyad;
    }

    public void setAdvesoyad(String advesoyad) {
        this.advesoyad = advesoyad;
    }

    public String getBolumu() {
        return bolumu;
    }

    public void setBolumu(String bolumu) {
        this.bolumu = bolumu;
    }

    public String getSinifi() {
        return sinifi;
    }

    public void setSinifi(String sinifi) {
        this.sinifi = sinifi;
    }

    public Integer getOgrenciNo() {
        return ogrenciNo;
    }

    public void setOgrenciNo(Integer ogrenciNo) {
        this.ogrenciNo = ogrenciNo;
    }

    public LocalDate getKayitTarihi() {
        return kayitTarihi;
    }

    public void setKayitTarihi(LocalDate kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }
}
