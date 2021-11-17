package com.zhyar.controller;

import com.zhyar.Categories;
import com.zhyar.EmailManager;
import com.zhyar.view.Role;
import com.zhyar.view.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CategoryWindowController extends BaseController implements Initializable {

    @FXML
    private TableView<Categories> catList;

    @FXML
    private TableColumn<Categories, Integer> col_id;

    @FXML
    private TableColumn<Categories, String> col_category;

    @FXML
    private TextField txt_id;

    @FXML
    private TextField txt_cat;

    public CategoryWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }
    int index = -1;
    @FXML
    void getSelected(MouseEvent event){
        index = catList.getSelectionModel().getSelectedIndex();
        if (index <= -1){
            return;
        }
        txt_id.setText(col_id.getCellData(index).toString());
        txt_cat.setText(col_category.getCellData(index).toString());
    }

    @FXML
    void addButtonAction() {
        try{
            String category = txt_cat.getText();
            conn = DBConnection.getConnection();
            pst = conn.prepareStatement("SELECT category FROM categories WHERE category = ?");
            pst.setString(1, txt_cat.toString());
            rs = pst.executeQuery();

            if(rs.isBeforeFirst()) {
                System.out.println("category empty");
            }
            else{
                String sql = "INSERT INTO categories(category) VALUES(?)";
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, category);
                ps.execute();
                UpdateTable();
            }
        }catch (SQLException throwables){
            System.out.println(throwables);
        }
    }

    @FXML
    void deleteButtonAction() {
        conn = DBConnection.getConnection();
        String sql = "DELETE FROM categories WHERE id = ?::integer";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txt_id.getText());
            pst.execute();
            UpdateTable();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    void updateButtonAction() {
        try {
            conn = DBConnection.getConnection();
            String value1 = txt_id.getText();
            String value2 = txt_cat.getText();
            String sql = "update categories set category= '"+value2+"' where id='"+value1+"'";
            pst = conn.prepareStatement(sql);
            pst.execute();
            UpdateTable();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<Categories, Integer>("id"));
        col_category.setCellValueFactory(new PropertyValueFactory<Categories, String>("category"));

        listM = getUsersData();
        catList.setItems(listM);
    }

    public void UpdateTable(){
        col_id.setCellValueFactory(new PropertyValueFactory<Categories,Integer>("id"));
        col_category.setCellValueFactory(new PropertyValueFactory<Categories,String>("category"));

        listM = getUsersData();
        catList.setItems(listM);
    }

    public ObservableList<Categories> getUsersData(){
        Connection conn = DBConnection.getConnection();
        ObservableList<Categories> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM categories");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new Categories(Integer.parseInt(rs.getString("id")),rs.getString("category")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    ObservableList<Categories> listM;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
}
