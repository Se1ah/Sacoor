package com.zhyar.controller;

import com.zhyar.EmailManager;
import com.zhyar.Inventory;
import com.zhyar.view.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InventoryWindowController extends BaseController implements Initializable {
    public InventoryWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    public ObservableList<Inventory> getUsersData(){
        Connection conn = DBConnection.getConnection();
        ObservableList<Inventory> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT id, productname, quantity, " +
                    ", purchaseprice, retailprice FROM inventory;");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new Inventory(Integer.parseInt(rs.getString("id")), rs.getString("productname"),
                        Integer.parseInt(rs.getString("quantity")), rs.getDouble("purchaseprice"),
                        rs.getDouble("retailprice")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    ObservableList<Inventory> listM;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_id.setCellValueFactory(new PropertyValueFactory<Inventory,Integer>("id"));
        col_productName.setCellValueFactory(new PropertyValueFactory<Inventory,String>("productname"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<Inventory,Integer>("quantity"));
        col_category.setCellValueFactory(new PropertyValueFactory<Inventory,String>("category_id"));
        col_purchasePrice.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("purchaseprice"));
        col_retailPrice.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("retailprice"));

        listM = getUsersData();
        inventoryList.setItems(listM);
    }

    @FXML
    private TableView<Inventory> inventoryList;

    @FXML
    private TableColumn<Inventory, Integer> col_id;

    @FXML
    private TableColumn<Inventory, String> col_productName;

    @FXML
    private TableColumn<Inventory, String> col_category;

    @FXML
    private TableColumn<Inventory, Integer> col_quantity;

    @FXML
    private TableColumn<Inventory, Double> col_purchasePrice;

    @FXML
    private TableColumn<Inventory, Double> col_retailPrice;

    @FXML
    private TextField txt_id;

    @FXML
    private TextField txt_productName;

    @FXML
    private TextField txt_category;

    @FXML
    private TextField txt_quantity;

    @FXML
    private TextField txt_purchasePrice;

    @FXML
    private TextField txt_retailPrice;


    @FXML
    void buyButtonAction() {
        viewFactory.showPurchaseWindow();
    }

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int index = -1;

    @FXML
    void getSelected(MouseEvent event){
        index = inventoryList.getSelectionModel().getSelectedIndex();
        if (index <= -1){
            return;
        }
        txt_id.setText(col_id.getCellData(index).toString());
        txt_productName.setText(col_productName.getCellData(index));
        txt_category.setText(col_category.getCellData(index));
        txt_quantity.setText(col_quantity.getCellData(index).toString());
        txt_purchasePrice.setText(col_purchasePrice.getCellData(index).toString());
        txt_retailPrice.setText(col_retailPrice.getCellData(index).toString());

    }

    public void UpdateTable(){
        col_id.setCellValueFactory(new PropertyValueFactory<Inventory,Integer>("id"));
        col_productName.setCellValueFactory(new PropertyValueFactory<Inventory,String>("productname"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<Inventory,Integer>("quantity"));
        col_category.setCellValueFactory(new PropertyValueFactory<Inventory,String>("category_id"));
        col_purchasePrice.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("purchaseprice"));
        col_retailPrice.setCellValueFactory(new PropertyValueFactory<Inventory, Double>("retailprice"));

        listM = getUsersData();
        inventoryList.setItems(listM);
    }

    @FXML
    void updateButtonAction() {
        try {
            conn = DBConnection.getConnection();
            String value1 = txt_id.getText();
            String value2 = txt_productName.getText();
            Integer value3 = Integer.parseInt(txt_quantity.getText());
            String value4 = txt_category.getText();
            Double value5 = Double.parseDouble(txt_purchasePrice.getText());
            Double value6 = Double.parseDouble(txt_retailPrice.getText());

            String sql = "update inventory set id= '"+value1+"',productname= '"+value2+"',quantity= '"+
                    value3+"',category_id= (SELECT id FROM categories WHERE inventory.category_id = id),purchaseprice= '"+value5+"', retailprice= '" +
                    value6+"' where id='"+value1+"' ";
            pst = conn.prepareStatement(sql);
            pst.execute();
            UpdateTable();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
