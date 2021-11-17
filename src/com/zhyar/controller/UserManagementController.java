package com.zhyar.controller;

import com.zhyar.EmailManager;
import com.zhyar.Users;
import com.zhyar.view.Role;
import com.zhyar.view.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UserManagementController extends BaseController implements Initializable {

    @FXML
    private TableView<Users> userList;

    @FXML
    private TableColumn<Users, Integer> col_id;

    @FXML
    private TableColumn<Users, String> col_username;

    @FXML
    private TableColumn<Users, String> col_password;

    @FXML
    private TableColumn<Users, String> col_email;

    @FXML
    private TableColumn<Users, Role> col_type;

    @FXML
    private TextField txt_id;

    @FXML
    private TextField txt_username;

    @FXML
    private TextField txt_password;

    @FXML
    private TextField txt_email;

    @FXML
    private ChoiceBox<Role> rolePicker;

    @FXML
    //Fills in all values of ENUM Role into choice box
    void getRoleClicked(MouseEvent event) {
        rolePicker.getItems().setAll(Role.values());
    }

    public UserManagementController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    void addButtonAction() {
        viewFactory.showCreateAccountWindow();
    }

    @FXML
    void deleteButtonAction() {
            conn = DBConnection.getConnection();
            String sql = "DELETE FROM users WHERE id = ?::integer";
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
    void editButtonAction() {
        try {
            conn = DBConnection.getConnection();
            String value1 = txt_id.getText();
            String value2 = txt_username.getText();
            String value3 = txt_password.getText();
            String value4 = txt_email.getText();
            Role value5 = rolePicker.getValue();
            String sql = "update users set id= '"+value1+"',username= '"+value2+"',password= '"+
                    value3+"',email= '"+value4+"',role= '"+value5+"' where id='"+value1+"' ";
            pst = conn.prepareStatement(sql);
            pst.execute();
            UpdateTable();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int index = -1;

    @FXML
    void getSelected(MouseEvent event){
        index = userList.getSelectionModel().getSelectedIndex();
        if (index <= -1){
            return;
        }
        txt_id.setText(col_id.getCellData(index).toString());
        txt_username.setText(col_username.getCellData(index).toString());
        txt_password.setText(col_password.getCellData(index).toString());
        txt_email.setText(col_email.getCellData(index).toString());
        rolePicker.setValue(col_type.getCellData(index));

    }


    public void UpdateTable(){
        col_id.setCellValueFactory(new PropertyValueFactory<Users,Integer>("id"));
        col_username.setCellValueFactory(new PropertyValueFactory<Users,String>("username"));
        col_password.setCellValueFactory(new PropertyValueFactory<Users,String>("password"));
        col_email.setCellValueFactory(new PropertyValueFactory<Users,String>("email"));
        col_type.setCellValueFactory(new PropertyValueFactory<Users,Role>("role"));

        listM = getUsersData();
        userList.setItems(listM);
    }


    public ObservableList<Users> getUsersData(){
        Connection conn = DBConnection.getConnection();
        ObservableList<Users> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new Users(Integer.parseInt(rs.getString("id")), rs.getString("username"),
                        rs.getString("password"), rs.getString("email"), Role.valueOf(rs.getString("role"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    ObservableList<Users> listM;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<Users, Integer>("id"));
        col_username.setCellValueFactory(new PropertyValueFactory<Users, String>("username"));
        col_password.setCellValueFactory(new PropertyValueFactory<Users, String>("password"));
        col_email.setCellValueFactory(new PropertyValueFactory<Users, String>("email"));
        col_type.setCellValueFactory(new PropertyValueFactory<Users, Role>("role"));

        listM = getUsersData();
        userList.setItems(listM);
    }

}
