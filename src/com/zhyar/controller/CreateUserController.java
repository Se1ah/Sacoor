package com.zhyar.controller;

import com.zhyar.EmailManager;
import com.zhyar.view.ColorTheme;
import com.zhyar.view.Role;
import com.zhyar.view.ViewFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CreateUserController extends BaseController implements Initializable {
    public CreateUserController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpRolePicker();
    }
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private ChoiceBox<Role> rolePicker;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label message;

    @FXML
    void cancelButton() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        viewFactory.closeStage(stage);
    }

    @FXML
    void submitButton() {
        createUser();
    }

    private void setUpRolePicker() {
        rolePicker.setItems(FXCollections.observableArrayList(Role.values()));
        rolePicker.setValue(viewFactory.getRole());
    }

    private void createUser(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        long id = 0;
        try{
            Connection con = DBConnection.getConnection();
            preparedStatement = con.prepareStatement("SELECT password FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.isBeforeFirst())
                message.setText("Username already exist");
            else{
                String sql = "INSERT INTO users(id, username, password, email, role) VALUES((SELECT MAX(id)+1 FROM users),?,?,?,CAST(? AS Role))";
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, email);
                ps.setString(4, rolePicker.getValue().toString());

                int affectedRows = ps.executeUpdate();
                // check the affected rows
                if (affectedRows > 0) {
                    // get the ID back
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getLong(1);
                            if(id > 0) {
                                message.setTextFill(Color.GREEN);
                                message.setText("User created successfully");
                            }
                            else {
                                message.setTextFill(Color.RED);
                                message.setText("User creation failed!");
                            }
                        }
                    } catch (SQLException ex) {
                        message.setText("username already exists");
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}


