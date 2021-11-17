package com.zhyar.controller;

import com.zhyar.EmailManager;
import com.zhyar.UserSession;
import com.zhyar.Users;
import com.zhyar.view.Role;
import com.zhyar.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class LoginWindowController extends BaseController implements Initializable {
    public Users user;

    public LoginWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @FXML
    private Button loginButton;

    @FXML
    private TextField emailAddressField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    void loginButtonAction() {
        userLogger();
    }

    @FXML
    void onEnter(KeyEvent event) {
        userLogger();
    }

    public void setErrorLabel(Label errorLabel) {
        this.errorLabel = errorLabel;
    }

    private void userLogger(){
        String username = emailAddressField.getText();
        String password = passwordField.getText();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            Connection con = DBConnection.getConnection();
            preparedStatement = con.prepareStatement("SELECT id, password, role FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Invalid username or password!");
            }
            else
                while(resultSet.next()){
                    Integer retrievedID = resultSet.getInt("id");
                    String retrivedPassword = resultSet.getString("password");
                    String retrivedRole = resultSet.getString("role");
                    if(retrivedPassword.equals(password)){
                        UserSession.getInstace(retrievedID,username, retrivedRole);
                        viewFactory.showMainWindow();
                        Stage stage = (Stage) errorLabel.getScene().getWindow();
                        viewFactory.closeStage(stage);
                    }

                }
                con.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailAddressField.requestFocus();
    }
}
