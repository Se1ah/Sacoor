package com.zhyar.controller;

import com.zhyar.EmailManager;
import com.zhyar.view.ViewFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MainWindowController extends BaseController{
    public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }
    @FXML
    private TreeView<?> emailsTreeView;

    @FXML
    private TableView<?> emailsTableView;

    @FXML
    private WebView emailsWebView;

    @FXML
    void addAccountAction(){
        //viewFactory.showCreateAccountWindow();
        viewFactory.showUserManagementWindow();
    }

    @FXML
    void optionsAction(){
        viewFactory.showOptionsWindow();
    }

    @FXML
    void closeAction(){
        Stage stage = (Stage) emailsWebView.getScene().getWindow();
        viewFactory.closeStage(stage);
    }

    @FXML
    void categoryAction() {
        viewFactory.showCategoryWindow();
    }

    @FXML
    void inventoryManagementAction() {

        viewFactory.showInventoryWindow();
    }

    @FXML
    void purchaseButtonAction() {
        viewFactory.showPurchaseWindow();
    }

    @FXML
    void salesButtonAction() {
        viewFactory.showSalesWindow();
    }

    @FXML
    void userMngmntButtonAction() {
        viewFactory.showUserManagementWindow();
    }
}
