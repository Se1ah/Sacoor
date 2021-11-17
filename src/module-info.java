module Sacoor{

    requires javafx.controls;
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;
    requires jasperreports;
    requires org.controlsfx.controls;

    opens com.zhyar.controller;
    opens com.zhyar.view;
    opens com.zhyar;

    exports com.zhyar;
}