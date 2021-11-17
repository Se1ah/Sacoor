package com.zhyar.view;

import com.zhyar.EmailManager;
import com.zhyar.Products;
import com.zhyar.controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ViewFactory {
    private EmailManager emailManager;

    //View options handling
    private ColorTheme colorTheme = ColorTheme.DEFAULT;
    private FontSize fontSize = FontSize.MEDIUM;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    private Role role = Role.Employee;
    private ArrayList<Stage> activeStages;

    public ColorTheme getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }

    public ViewFactory(EmailManager emailManager) {

        this.emailManager = emailManager;
        activeStages = new ArrayList<Stage>();
    }

    public void showLoginWindow(){
        BaseController controller = new LoginWindowController(emailManager, this , "LoginWindow.fxml");
        initializeStage(controller);
    }

    public void showMainWindow(){
        BaseController controller = new MainWindowController(emailManager, this , "MainWindow.fxml");
        initializeStage(controller);
    }

    public void showOptionsWindow(){
        BaseController controller = new OptionsWindowController(emailManager, this , "OptionsWindow.fxml");
        initializeStage(controller);
    }

    public void showCreateAccountWindow(){
        BaseController controller = new CreateUserController(emailManager, this , "CreateUserWindow.fxml");
        initializeStage(controller);
    }
    public void showUserManagementWindow(){
        BaseController controller = new UserManagementController(emailManager, this , "UserManagement.fxml");
        initializeStage(controller);
    }

    public void showCategoryWindow(){
        BaseController controller = new CategoryWindowController(emailManager, this , "CategoryWindow.fxml");
        initializeStage(controller);
    }

    public void showInventoryWindow(){
        BaseController controller = new InventoryWindowController(emailManager, this, "InventoryWindow.fxml");
        initializeStage(controller);
    }

    public void showPurchaseWindow(){
        BaseController controller = new PurchaseWindowController(emailManager, this, "PurchaseWindow.fxml");
        initializeStage(controller);
    }

    public void showProductWindow(){
        BaseController controller = new ProductWindowController(emailManager, this, "ProductWindow.fxml");
        initializeStage(controller);
    }

    public void showSalesWindow(){
        BaseController controller = new SalesWindowController(emailManager, this, "SalesWindow.fxml");
        initializeStage(controller);
    }

    private void initializeStage(BaseController baseController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(baseController.getFxmlName()));
        fxmlLoader.setController(baseController);

        Parent parent;

        try {
            parent = fxmlLoader.load();
        } catch (IOException e){
            e.printStackTrace();
            return;
        }
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        activeStages.add(stage);
    }

    public void closeStage(Stage stageToClose){

        stageToClose.close();
        activeStages.remove(stageToClose);
    }

    public void updateStyles() {
        for(Stage stage: activeStages){
            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource(ColorTheme.getCssPath(colorTheme)).toExternalForm());
            scene.getStylesheets().add(getClass().getResource(FontSize.getCssPath(fontSize)).toExternalForm());
        }
    }
}
