module com.example.student_tracking_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.student_tracking_system to javafx.fxml;
    exports com.example.student_tracking_system;
}