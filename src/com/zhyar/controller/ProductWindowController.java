package com.zhyar.controller;

import com.zhyar.AlertHelper;
import com.zhyar.EmailManager;
import com.zhyar.KeyValuePair;
import com.zhyar.Products;
import com.zhyar.view.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

import java.net.URL;
import java.security.Key;
import java.sql.*;
import java.util.ResourceBundle;

public class ProductWindowController extends BaseController implements Initializable {

    @FXML
    private TableView<Products> productList;

    @FXML
    private TableColumn<Products, Integer> col_id;

    @FXML
    private TableColumn<Products, String> col_product;

    @FXML
    private TableColumn<Products, String> col_barcode;

    @FXML
    private TableColumn<Products, Integer> col_stock;

    @FXML
    private TableColumn<Products, Double> col_priceIn;

    @FXML
    private TableColumn<Products, Double> col_priceOut;

    @FXML
    private TableColumn<Products, Integer> col_color_id;

    @FXML
    private TableColumn<Products, String> col_color;

    @FXML
    private TableColumn<Products, Integer> col_size_id;

    @FXML
    private TableColumn<Products, String> col_size;

    @FXML
    private TextField txt_id;

    @FXML
    private TextField txt_product;

    @FXML
    private TextField txt_barcode;

    @FXML
    private TextField txt_stock;

    @FXML
    private TextField txt_priceIn;

    @FXML
    private TextField txt_priceOut;

    @FXML
    private ComboBox<KeyValuePair> colorPicker;

    @FXML
    private ComboBox<KeyValuePair> sizePicker;



    public ProductWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }
    private Integer size_id = 0;
    private Integer color_id = 0;
    private String colorSelected = null;
    private String sizeSelected = null;
    int index = -1;
    @FXML
    void getSelected(MouseEvent event){
        index = productList.getSelectionModel().getSelectedIndex();
        if (index <= -1){
            return;
        }
        txt_id.setText(col_id.getCellData(index).toString());
        txt_product.setText(col_product.getCellData(index));
        txt_barcode.setText(col_barcode.getCellData(index));
        txt_stock.setText(col_stock.getCellData(index).toString());
        txt_priceIn.setText(col_priceIn.getCellData(index).toString());
        txt_priceOut.setText(col_priceOut.getCellData(index).toString());
        colorPicker.getSelectionModel().select(Integer.parseInt(col_color.getCellData(index)));
        sizePicker.getSelectionModel().select(Integer.parseInt(col_size.getCellData(index)));

    }

    @FXML
    void addButtonAction() {
        int duplicatenameID = 0;
        try{
            String productName = txt_product.getText();
            String barcode = txt_barcode.getText();
            int quantity = Integer.parseInt(txt_stock.getText());
            Double priceIn = Double.parseDouble(txt_priceIn.getText());
            Double priceOut = Double.parseDouble(txt_priceOut.getText());
            Integer color = color_id;
            Integer size = size_id;

            conn = DBConnection.getConnection();
            pst = conn.prepareStatement("SELECT name FROM products WHERE LOWER(name) = LOWER(?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, productName);
            rs = pst.executeQuery();

            ResultSet rs = pst.getGeneratedKeys();
            if(rs.isBeforeFirst()) {

                duplicatenameID = rs.getInt(1);
                System.out.println("duplicate id is "+duplicatenameID);

                String sql = "UPDATE products SET quantity = "+quantity+"WHERE id ="+duplicatenameID;
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.execute();
            }
            else {
                String sql = "INSERT INTO products(name, barcode, quantity, price_in, price_out, color_id, size_id) VALUES(?,?,?,?,?,?,?)";
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, productName);
                ps.setString(2, barcode);
                ps.setInt(3, quantity);
                ps.setDouble(4, priceIn);
                ps.setDouble(5, priceOut);
                ps.setInt(6, color);
                ps.setInt(7, size);
                ps.execute();
            }
            UpdateTable();
            clearFields();
        }catch (SQLException throwables){
            Window owner = txt_product.getScene().getWindow();

            AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Error",
                    "Duplicate name.");
        }
    }

    @FXML
    void deleteButtonAction() {
        conn = DBConnection.getConnection();
        String sql = "DELETE FROM products WHERE id = ?::integer";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txt_id.getText());
            pst.execute();
            UpdateTable();
            clearFields();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    void updateButtonAction() {
        try {
            conn = DBConnection.getConnection();
            String value1 = txt_id.getText();
            String value2 = txt_product.getText();
            String value3 = txt_barcode.getText();
            int value4 = Integer.parseInt(txt_stock.getText());
            Double value5 = Double.parseDouble(txt_priceIn.getText());
            Double value6 = Double.parseDouble(txt_priceOut.getText());
            int value7 = Integer.parseInt(colorSelected);
            int value8 = Integer.parseInt(sizeSelected);

            String sql = "UPDATE products SET name= '"+value2+"', barcode= '"+value3+"', quantity = '"+value4+"' , price_in= '"+value5+"', price_out= '"+value6+"' ,color_id = '"+value7+"', size_id= '"+value8+"' where id='"+value1+"'";
            pst = conn.prepareStatement(sql);
            pst.execute();
            UpdateTable();
            clearFields();
        } catch (Exception e) {
            System.out.println("Update button action");
            System.out.println(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Connection con = DBConnection.getConnection();
            assert con != null;
            ResultSet rs = con.createStatement().executeQuery("SELECT id, color FROM colors");
            ObservableList<KeyValuePair> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new KeyValuePair(rs.getString("color") ,rs.getInt("id")));

                colorPicker.valueProperty().addListener((obs, oldval, newval) -> {
                    if (newval != null) {
                        color_id = newval.getValue();
                        colorSelected = newval.getKey();
                    }
                });
            }
            colorPicker.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Connection con = DBConnection.getConnection();
            assert con != null;
            ResultSet rs = con.createStatement().executeQuery("SELECT id, size FROM sizes");
            ObservableList<KeyValuePair> data = FXCollections.observableArrayList();
            while (rs.next()) {
                data.add(new KeyValuePair(rs.getString("size") ,rs.getInt("id")));

                colorPicker.valueProperty().addListener((obs, oldval, newval) -> {
                    if (newval != null) {
                        size_id = newval.getValue();
                        sizeSelected = newval.getKey();
                    }
                });
            }
            sizePicker.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        col_id.setCellValueFactory(new PropertyValueFactory<Products, Integer>("id"));
        col_product.setCellValueFactory(new PropertyValueFactory<Products, String>("name"));
        col_barcode.setCellValueFactory(new PropertyValueFactory<Products, String>("barcode"));
        col_stock.setCellValueFactory(new PropertyValueFactory<Products, Integer>("quantity"));
        col_priceIn.setCellValueFactory(new PropertyValueFactory<Products, Double>("price_in"));
        col_priceOut.setCellValueFactory(new PropertyValueFactory<Products, Double>("price_out"));
        col_color_id.setCellValueFactory(new PropertyValueFactory<Products, Integer>("color_id"));
        col_color.setCellValueFactory(new PropertyValueFactory<Products, String>("colorSelected"));
        col_size_id.setCellValueFactory(new PropertyValueFactory<Products, Integer>("size_id"));
        col_size.setCellValueFactory(new PropertyValueFactory<Products, String>("sizeSelected"));

        listM = getUsersData();
        productList.setItems(listM);
    }

    public void UpdateTable(){
        col_id.setCellValueFactory(new PropertyValueFactory<Products,Integer>("id"));
        col_product.setCellValueFactory(new PropertyValueFactory<Products,String>("name"));
        col_barcode.setCellValueFactory(new PropertyValueFactory<Products,String>("barcode"));
        col_stock.setCellValueFactory(new PropertyValueFactory<Products,Integer>("quantity"));
        col_priceIn.setCellValueFactory(new PropertyValueFactory<Products, Double>("price_in"));
        col_priceOut.setCellValueFactory(new PropertyValueFactory<Products, Double>("price_out"));
        col_color.setCellValueFactory(new PropertyValueFactory<Products, String>("colorSelected"));
        col_color_id.setCellValueFactory(new PropertyValueFactory<Products,Integer>("color_id"));
        col_size_id.setCellValueFactory(new PropertyValueFactory<Products,Integer>("size_id"));
        col_size.setCellValueFactory(new PropertyValueFactory<Products, String>("sizeSelected"));

        listM = getUsersData();
        productList.setItems(listM);
    }

    public ObservableList<Products> getUsersData(){
        Connection conn = DBConnection.getConnection();
        ObservableList<Products> list = FXCollections.observableArrayList();
        try {
            assert conn != null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM products");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new Products(rs.getInt("id"),rs.getString("name"), rs.getString("barcode"),
                        rs.getInt("quantity"), rs.getDouble("price_in"), rs.getDouble("price_out"),
                        Integer.parseInt(rs.getString("color_id")), Integer.parseInt(rs.getString("size_id"))));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }

    public void clearFields(){
        txt_id.clear();
        txt_barcode.clear();
        txt_product.clear();
        txt_stock.clear();
        txt_priceIn.clear();
        txt_priceOut.clear();
        colorPicker.getSelectionModel().clearSelection();
        sizePicker.getSelectionModel().clearSelection();
    }

    ObservableList<Products> listM;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
}
