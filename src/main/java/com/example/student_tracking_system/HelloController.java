package com.example.student_tracking_system;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.sql.*;

public class HelloController implements Initializable {

    @FXML
    private ComboBox<String> cmbBolum;

    @FXML
    private ComboBox<String> cmbSinifi;

    @FXML
    private DatePicker dpKayitTarihi;

    @FXML
    private Spinner<Integer> spnOgrenciNo;

    @FXML
    private TableColumn<Ogrenci, String> stnAdSoyad;

    @FXML
    private TableColumn<Ogrenci, String> stnBolum;

    @FXML
    private TableColumn<Ogrenci, LocalDate> stnKayitTarihi;

    @FXML
    private TableColumn<Ogrenci, Integer> stnOgrenciNO;

    @FXML
    private TableColumn<Ogrenci, String> stnSinif;

    @FXML
    private TableView<Ogrenci> tblOgrenciler;

    @FXML
    private TextField txtAdSoyad;

    private ObservableList<Ogrenci>Ogrenciler=FXCollections.observableArrayList();

    private Connection baglanti;

    private PreparedStatement sorgu;

    private  ResultSet rs=null;

    private Alert hatamesaji=new Alert(Alert.AlertType.ERROR);


    @FXML
    void ogrenciKaydet(ActionEvent event) {

        int ogrenciNo= spnOgrenciNo.getValue();

        for (int i = 0; i < Ogrenciler.size(); i++) {

            if (ogrenciNo==Ogrenciler.get(i).getOgrenciNo()){
                hatamesaji.setTitle("Hata");
                hatamesaji.setHeaderText("Ogrenci Numarası Zaten Kayıtlı...");
                hatamesaji.show();
                return;
            }
        }
        String adsoyad=txtAdSoyad.getText();
        String bolumu=cmbBolum.getValue();
        LocalDate kayitTarihi=dpKayitTarihi.getValue();
        String sinifi=cmbSinifi.getValue();

        Ogrenci ogrenci=new Ogrenci(adsoyad,bolumu,sinifi,ogrenciNo,kayitTarihi);
        Ogrenciler.add(ogrenci);

        try {
            sorgu=baglanti.prepareStatement("insert into Tablo (ogrenciNo,advesoyad,bolumu,kayitTarihi,sinifi) values(?,?,?,?,?)");
            sorgu.setInt(1,ogrenciNo);
            sorgu.setString(2,adsoyad);
            sorgu.setString(3,bolumu);
            sorgu.setDate(4, Date.valueOf(kayitTarihi));
            sorgu.setString(5,sinifi);
            sorgu.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void ogrenciGuncelle(ActionEvent event){

        int index=tblOgrenciler.getSelectionModel().getSelectedIndex();

        if (index!=-1){
            int ogrno=spnOgrenciNo.getValue();
            for (int i = 0; i <Ogrenciler.size(); i++) {
                if (ogrno==Ogrenciler.get(i).getOgrenciNo() && ogrno!=Ogrenciler.get(index).getOgrenciNo()){
                    hatamesaji.setTitle("Hata");
                    hatamesaji.setHeaderText("Bu Öğrenci Numarası İle Kayıtlı Öğrenci Mevcut...");
                    hatamesaji.show();
                    return;
                }
            }

            String advesoyad=txtAdSoyad.getText();
            String bolumu=cmbBolum.getValue();
            LocalDate kayitTarihi=dpKayitTarihi.getValue();
            String sinifi=cmbSinifi.getValue();

            Ogrenci ogrenci=new Ogrenci(advesoyad,bolumu,sinifi,ogrno,kayitTarihi);
            Ogrenciler.set(index,ogrenci);


            try {
                sorgu=baglanti.prepareStatement("update Tablo set ogrenciNo=?, advesoyad=?, bolumu=?, kayitTarihi=?, sinifi=? where ogrenciNo="+"'"+ogrno+"'");
                sorgu.setInt(1,ogrno);
                sorgu.setString(2,advesoyad);
                sorgu.setString(3,bolumu);
                sorgu.setDate(4, Date.valueOf(kayitTarihi));
                sorgu.setString(5,sinifi);
                sorgu.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            hatamesaji.setTitle("Hata");
            hatamesaji.setHeaderText("Güncellenecek öğrenci kaydı yok veya seçilmedi... ");
            hatamesaji.show();
        }

    }

    @FXML
    void ogrenciSil(ActionEvent event){

        int index=tblOgrenciler.getSelectionModel().getSelectedIndex();

        if (index>=0){

            try {
                sorgu=baglanti.prepareStatement("delete from Tablo where ogrenciNo="+"'"+Ogrenciler.get(index).getOgrenciNo()+"'" );
                Ogrenciler.remove(index);
                sorgu.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            baglanti=DriverManager.getConnection("jdbc:sqlserver://ONUR\\MSSQLSERVER;database=OgrenciBilgileri;integratedSecurity=true");
            System.out.println(baglanti.isValid(0));

        } catch (Exception e) {
            e.printStackTrace();
        }

        spnOgrenciNo.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,1000,1,1));
        cmbBolum.setItems(FXCollections.observableArrayList("Bilgisayar Programcılığı","Elektrik Programı","Elektronik Programı","Makine Programı","Mekatronik Programı"));
        cmbSinifi.setItems(FXCollections.observableArrayList("Hazırlık","1","2","3","4","Uzatma Durumu"));




        stnOgrenciNO.setCellValueFactory(new PropertyValueFactory<>("ogrenciNo"));
        stnAdSoyad.setCellValueFactory(new PropertyValueFactory<>("advesoyad"));
        stnBolum.setCellValueFactory(new PropertyValueFactory<>("bolumu"));
        stnKayitTarihi.setCellValueFactory(new PropertyValueFactory<>("kayitTarihi"));
        stnSinif.setCellValueFactory(new PropertyValueFactory<>("sinifi"));

        tblOgrenciler.setItems(Ogrenciler);

        tblOgrenciler.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                int index=t1.intValue();
                if (index!=-1){

                    spnOgrenciNo.getValueFactory().setValue(Ogrenciler.get(index).getOgrenciNo());
                    txtAdSoyad.setText(Ogrenciler.get(index).getAdvesoyad());
                    cmbBolum.setValue(Ogrenciler.get(index).getBolumu());
                    dpKayitTarihi.setValue(Ogrenciler.get(index).getKayitTarihi());
                    cmbBolum.setValue(Ogrenciler.get(index).getBolumu());

                }else{

                }
            }
        });



        try {
            sorgu=baglanti.prepareStatement("Select * from Tablo");
            rs=sorgu.executeQuery();

            Integer ogrenciNo;
            String advesoyad,bolumu,sinifi;
            LocalDate kayitTarihi;

            while (rs.next()){
                ogrenciNo=rs.getInt(1);
                advesoyad=rs.getString(2);
                bolumu=rs.getString(3);


               kayitTarihi=rs.getDate(4).toLocalDate();


                sinifi=rs.getString(5);

                Ogrenci ogrenci=new Ogrenci(advesoyad,bolumu,sinifi,ogrenciNo,kayitTarihi);
                Ogrenciler.add(ogrenci);
            }





        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
