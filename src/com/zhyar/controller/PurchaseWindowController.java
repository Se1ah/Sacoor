package com.zhyar.controller;

import com.zhyar.*;
import com.zhyar.view.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.sql.*;
import java.util.*;

public class PurchaseWindowController extends BaseController implements Initializable {

    public PurchaseWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }
    private Integer product_id = 0;
    private Integer supplier_id = 0;
    private String productSelected = null;

    @FXML
    private TableView<PurchaseList> purchaseListTable;

    @FXML
    private TableColumn<PurchaseList, Integer> col_product_id;

    @FXML
    private TableColumn<PurchaseList, String> col_productname;

    @FXML
    private TableColumn<PurchaseList, Integer> col_quantity;

    @FXML
    private TableColumn<PurchaseList, Double> col_price;

    @FXML
    private TableColumn<PurchaseList, Double> col_total;

    @FXML
    private TextField txt_orderNumber;

    @FXML
    private ComboBox<KeyValuePair> supplierPicker;

    @FXML
    private ComboBox<KeyValuePair> productPicker;


    @FXML
    private Label product_label;

    @FXML
    private TextField txt_quantity;

    @FXML
    private TextField txt_price;

    @FXML
    void addProductButtonAction() {
        viewFactory.showProductWindow();
    }

    @FXML
    void addSupplierButtonAction() {

    }

    @FXML
    void addToListButtonAction() {
        Integer quantity = Integer.parseInt(txt_quantity.getText());
        Double price = Double.parseDouble(txt_price.getText());
        Double total = price * quantity;



        purchaseListTable.getItems().add(new PurchaseList(product_id, productSelected, "Some",quantity, price, total));

        //clears element's content when pressing button add to the list
        productPicker.getSelectionModel().clearSelection();
        //supplierPicker.getSelectionModel().clearSelection();
        txt_quantity.clear();
        txt_price.clear();
        System.out.println(UserSession.getUserName());
        System.out.println("Current user Role is "+UserSession.getPrivileges());
    }

    @FXML
    void deleteButtonAction() {
        purchaseListTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<PurchaseList> selectedRows = purchaseListTable.getSelectionModel().getSelectedItems();
        ArrayList<PurchaseList> rows = new ArrayList<>(selectedRows);
        rows.forEach(row -> purchaseListTable.getItems().remove(row));
    }

    @FXML
    void cancelButtonAction() {

    }

    @FXML
    void saveButtonAction() {
        Integer purchase_no = Integer.parseInt(txt_orderNumber.getText());
        int purchaseID = 0;
        try {
            Connection con1 = DBConnection.getConnection();
            PreparedStatement ps = con1.prepareStatement("INSERT INTO purchase(purchase_no, created_by, supplier_id) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, purchase_no);
            //We must work around saving user ID instead of username in the below line
            ps.setInt(2, UserSession.getUserID());
            ps.setInt(3, supplier_id);
            ps.execute();

            //get the id of inserted row
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next())
                purchaseID = rs.getInt(1);
        } catch (SQLException throwables) {
            System.out.println(throwables);
        }

        try {
            List<Purchase> list = new ArrayList<>();

            Connection con2 = DBConnection.getConnection();
            con2.setAutoCommit(false);
            PreparedStatement pst = con2.prepareStatement("INSERT INTO purchase_row(purchase_id, product_id, quantity, unit_price) VALUES(?, ?, ?, ?");
            for(Iterator<Purchase> iterator = list.iterator(); iterator.hasNext();) {
                Purchase purchase = (Purchase) iterator.next();

                pst.addBatch();
            }

        }
        catch (Exception throwable) {

        }


        int finalPurchaseID = purchaseID;
        purchaseListTable.getItems().stream().forEach((o) -> {
            Integer id = col_product_id.getCellData(o);
            Integer quantity = col_quantity.getCellData(o);
            Double price = col_price.getCellData(o);
            //Double total = col_total.getCellData(o);
            try {
                Connection con2 = DBConnection.getConnection();
                con2.setAutoCommit(false);
                PreparedStatement pst = con2.prepareStatement("INSERT INTO purchase_row(purchase_id, product_id, quantity, unit_price) VALUES(?, ?, ?, ?)");
                pst.setInt(1, finalPurchaseID);
                pst.setInt(2, id);
                pst.setInt(3, quantity);
                pst.setDouble(4, price);
                pst.addBatch();

                int[] updateCounts = pst.executeBatch();

                con2.commit();
                con2.setAutoCommit(true);
                try{
                    Connection con3 = DBConnection.getConnection();
                    PreparedStatement pstmnt = con3.prepareStatement("UPDATE products SET quantity = (SELECT quantity FROM products WHERE id = "+id+")+ "+quantity+" WHERE id = "+id+"");
                    pstmnt.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            catch (Exception throwable) {
                System.out.println(throwable);
            }
        });
    }

        @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //initialize products

        try {
            Connection con = DBConnection.getConnection();
            assert con != null;
            ResultSet rs = con.createStatement().executeQuery("SELECT id, name FROM products");
            ObservableList<KeyValuePair> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new KeyValuePair(rs.getString("name") ,rs.getInt("id")));

                productPicker.valueProperty().addListener((obs, oldval, newval) -> {
                    if (newval != null) {
                        product_id = newval.getValue();
                        productSelected = newval.getKey();
                    }
                });

            }
            productPicker.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //initialize supplier
        try {
            Connection con = DBConnection.getConnection();
            assert con != null;
            ResultSet rs = con.createStatement().executeQuery("SELECT id, supplier FROM suppliers");
            ObservableList<KeyValuePair> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new KeyValuePair(rs.getString("supplier") ,rs.getInt("id")));

                supplierPicker.valueProperty().addListener((obs, oldval, newval) -> {
                    if (newval != null) {
                        supplier_id = newval.getValue();
                    }
                });

            }
            supplierPicker.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

/*
new PropertyValueFactory gets the model attributes not the column of tableview. If you do so you will
get this error: main.carstore.model.Cars java.lang.IllegalStateException: Cannot read from unreadable property txt_productname
*/
        col_product_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_productname.setCellValueFactory(new PropertyValueFactory<>("product"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
}


}
