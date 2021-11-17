package com.zhyar.controller;

import com.zhyar.*;
import com.zhyar.view.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class SalesWindowController extends BaseController implements Initializable {
    public SalesWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
        super(emailManager, viewFactory, fxmlName);
    }

    private Integer product_id = 0;
    private String productSelected = null;

    @FXML
    private Label label;

    @FXML
    private TextField productName;

    @FXML
    private TextField textFieldBarcode;

    @FXML
    private TableView<SalesList> tableViewItem;

    @FXML
    private TableColumn<SalesList, Integer> col_product_id;

    @FXML
    private TableColumn<SalesList, String> col_product;

    @FXML
    private TableColumn<SalesList, Integer> col_quantity;

    @FXML
    private TableColumn<SalesList, Double> col_price;

    @FXML
    private TextField textFieldQty;

    @FXML
    private TextField textFieldPrice;

    @FXML
    private TextField textFieldAmount;

    @FXML
    private ComboBox comboBoxCurrency;

    @FXML
    private TextField textFieldTotalQuantity;

    @FXML
    private TextField textFieldTotalAmount;

    @FXML
    private Button buttonSave;

    @FXML
    private TextField textFieldTotalOther;

    @FXML
    private TextField textFieldTotalPayableAmount;

    @FXML
    private TextField textFieldTotalPaidAmount;

    @FXML
    private TextField textFieldTotalDueAmount;

    @FXML
    private DatePicker date;

    @FXML
    void handleBarcode() throws SQLException {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM products WHERE barcode = ?");
        ps.setString(1, textFieldBarcode.getText());
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            int productId;
            String item = rs.getString(2);
            Double price = rs.getDouble(6);
            productName.setText(item);
            textFieldPrice.setText(String.valueOf(price));
        }
    }

    private int selectedTableViewRow = 0;
    private Integer itemId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

//        try {
//            Connection con = DBConnection.getConnection();
//            assert con != null;
//            ResultSet rs = con.createStatement().executeQuery("SELECT id, name FROM products");
//            ObservableList<KeyValuePair> data = FXCollections.observableArrayList();
//            while (rs.next()) {
//                data.add(new KeyValuePair(rs.getString("name") ,rs.getInt("id")));
//
//                productName.valueProperty().addListener((obs, oldval, newval) -> {
//                    if (newval != null) {
//                        product_id = newval.getValue();
//                        productSelected = newval.getKey();
//                    }
//                });
//
//            }
//            productName.setItems(data);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        col_product_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_product.setCellValueFactory(new PropertyValueFactory<>("product"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        comboBoxCurrency.getItems().setAll("USD", "IQD");
        comboBoxCurrency.getSelectionModel().select("USD");

        date.setValue(LocalDate.now());
    }

    public void calculatePrice() {
        if (!textFieldPrice.getText().isEmpty() && !textFieldQty.getText().isEmpty()) {
            Double qty = Double.parseDouble(textFieldQty.getText());
            Double amt = Double.parseDouble(textFieldPrice.getText());
            Double tot = qty * amt;
            tot = (Double) (Math.round(tot * 100) / 100.0);
            textFieldAmount.setText(String.valueOf(tot));
        }
    }

    public void addItemInTableView() {
        Integer quantity = Integer.parseInt(textFieldQty.getText());
        Double price = Double.parseDouble(textFieldPrice.getText());
        Double total = price * quantity;



        tableViewItem.getItems().add(new SalesList(product_id, productSelected, quantity, price));

        //clears element's content when pressing button add to the list
        productName.clear();
        //supplierPicker.getSelectionModel().clearSelection();
        textFieldQty.clear();
        textFieldPrice.clear();
        this.clearHeaderForm();
        this.calculateTotalAmount();
        textFieldBarcode.requestFocus();
    }

    public void clearHeaderForm() {
        productName.clear();
        textFieldQty.clear();
        textFieldPrice.clear();
        textFieldAmount.clear();
        this.calculateTotalAmount();
        tableViewItem.scrollTo(tableViewItem.getItems().size());
        this.selectedTableViewRow = 0;
        itemId = 0;
    }

    @FXML
    private void calculateDueAmount() {
        Double paidAmount = (Double) 0.0;
        Double payableAmount = Double.parseDouble(textFieldTotalPayableAmount.getText());
        if (!textFieldTotalPaidAmount.getText().isEmpty()) {
            paidAmount = Double.parseDouble(textFieldTotalPaidAmount.getText());
        }

        textFieldTotalDueAmount.setText(Double.toString(payableAmount - paidAmount));
    }

    @FXML
    private void calculateTotalAmount() {
        Double amount = 0.0;
        Integer quantity = 0;
        Double other = 0.0;
        amount = tableViewItem.getItems().stream().map((item) -> item.getPrice()*item.getQuantity()).reduce(amount, (accumulator, _item) -> accumulator + _item);

        quantity = tableViewItem.getItems().stream().map((item) -> item.getQuantity()).reduce(quantity, (accumulator, _item) -> accumulator + _item);

        try {
            other = Double.parseDouble(textFieldTotalOther.getText());
        } catch (Exception e) {

        }
        textFieldTotalPayableAmount.setText(Double.toString(amount + other));
        textFieldTotalOther.setText(Double.toString(other));
        textFieldTotalQuantity.setText(Integer.toString(quantity));
        textFieldTotalAmount.setText(Double.toString(amount));
        calculateDueAmount();
    }

    public void deleteTableViewRow() {
        int selectedRowNum = tableViewItem.getSelectionModel().getSelectedIndex();
        if (selectedRowNum >= 0) {
            tableViewItem.getItems().remove(selectedRowNum);
        }
        this.clearHeaderForm();
    }

    @FXML
    private void save() {
        try {
            Connection con = DBConnection.getConnection();
            assert con != null;
            //Statement stmt = con.createStatement();
            String order_no= "A321";
            System.out.println(comboBoxCurrency.getValue());
            PreparedStatement ps = con.prepareStatement("INSERT INTO sales (order_no, total_quantity, total_amount, other_amount, total_payable, total_paid_amount, total_due_amount, currency, created_by) VALUES(?,?,?,?,?,?,?,CAST(? AS currency),?)");



            //int rs = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,order_no);
            ps.setInt(2, Integer.parseInt(textFieldTotalQuantity.getText()));
            ps.setDouble(3,Double.parseDouble(textFieldTotalAmount.getText()));
            ps.setDouble(4, Double.parseDouble(textFieldTotalOther.getText()));
            ps.setDouble(5, Double.parseDouble(textFieldTotalPayableAmount.getText()));
            ps.setDouble(6, Double.parseDouble(textFieldTotalPaidAmount.getText()));
            ps.setDouble(7, Double.parseDouble(textFieldTotalDueAmount.getText()));
            ps.setString(8, (String)comboBoxCurrency.getValue());
            ps.setInt(9, UserSession.getUserID());
            ResultSet rs = ps.executeQuery();
            int posSequence = 0;
            ResultSet index = ps.getGeneratedKeys();
            if (index.next()) {
                posSequence = index.getInt(1);
            }

            String posDetailsQuery = "insert into sale_details (sales_id, product_id, quantity, price) ";
            int count = 0;
            for (SalesList item : tableViewItem.getItems()) {
                posDetailsQuery += "VALUES("+ posSequence + "','"  + item.getId() + "','" + item.getProduct() + "','" + item.getQuantity() + "','" + item.getPrice() + ")";
                if (count != (tableViewItem.getItems().size() - 1)) {
                    posDetailsQuery += "union all ";
                }
                count++;
            }
            ResultSet record = ps.executeQuery(posDetailsQuery);

            Window owner = buttonSave.getScene().getWindow();

            AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Information",
                    "A record has been saved successfully.");
            printInvoice();
            clearFooterForm();
            //textFieldItem.requestFocus();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
//        int sales_number = 0;
//        try {
//            Connection con = DBConnection.getConnection();
//            assert con != null;
//            PreparedStatement ps = con.prepareStatement("INSERT INTO sales(created_by, total, paid) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
//            ps.setInt(1, UserSession.getUserID());
//            Double tempQty = Double.parseDouble("quantity");
//            Double tempPrice = Double.parseDouble("21.1");
//            ps.setDouble(2, tempPrice*tempQty);
//            ps.setDouble(3,234.5);
//            ps.execute();
//
//            ResultSet rs = ps.getGeneratedKeys();
//            if (rs.next()) {
//                sales_number = rs.getInt(1);
//            }
//        }catch (SQLException throwables) {
//            System.out.println(throwables);
//        }
//
//        int deduction = 0;
//        int finalSales_number = sales_number;
//        tableViewItem.getItems().stream().forEach((o) -> {
//            Integer product_id = col_product_id.getCellData(o);
//            Integer quantity = col_quantity.getCellData(o);
//            Double price = col_price.getCellData(o);
//            //Double total = col_total.getCellData(o);
//            try {
//                Connection con2 = DBConnection.getConnection();
//                con2.setAutoCommit(false);
//                PreparedStatement pst = con2.prepareStatement("INSERT INTO sale_details(sales_id, product_id, quantity, price) VALUES(?, ?, ?, ?)");
//                pst.setInt(1, finalSales_number);
//                pst.setInt(2, product_id);
//                pst.setInt(3, quantity);
//                pst.setDouble(4, price);
//                pst.addBatch();
//
//                int[] updateCounts = pst.executeBatch();
//
//                con2.commit();
//                con2.setAutoCommit(true);
//                try{
//                    Connection con3 = DBConnection.getConnection();
//                    PreparedStatement pstmnt = con3.prepareStatement("UPDATE products SET quantity = (SELECT quantity FROM products WHERE id = "+product_id+")- "+quantity+" WHERE id = "+product_id+"");
//                    pstmnt.executeUpdate();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                }
//                Window owner = buttonSave.getScene().getWindow();
//
//                AlertHelper.showAlert(Alert.AlertType.INFORMATION, owner, "Information",
//                        "A record has been saved successfully.");
//                printInvoice();
//                clearFooterForm();
//            }catch (Exception throwable) {
//                    System.out.println(throwable);
//                }
//            });
    }

    @FXML
    private void clearWholeForm() {
        clearHeaderForm();
        clearFooterForm();
    }

    private void clearFooterForm() {
        tableViewItem.getItems().clear();
        textFieldTotalAmount.clear();
        textFieldTotalQuantity.clear();
        textFieldTotalAmount.clear();
        textFieldTotalDueAmount.clear();
        textFieldTotalOther.clear();
        textFieldTotalPaidAmount.clear();
        textFieldTotalPayableAmount.clear();
    }

    public void printInvoice() {
        String sourceFile = "/Users/user1/Documents/Sacoor/src/com/zhyar/Invoice.jrxml";
        JasperDesign jasperdesign = null;
        try {
            jasperdesign = JRXmlLoader.load(sourceFile);
            JasperReport jr = JasperCompileManager.compileReport(jasperdesign);
            System.out.println("Inside printInvoice");
            HashMap<String, Object> para = new HashMap<>();
            para.put("invoiceNo", "SHOP01/000001");
            para.put("currency", comboBoxCurrency.getValue());
            para.put("totalQuantity", textFieldTotalQuantity.getText());
            para.put("totalAmount", textFieldTotalAmount.getText());
            para.put("otherAmount", textFieldTotalOther.getText());
            para.put("payableAmount", textFieldTotalPayableAmount.getText());
            para.put("paidAmount", textFieldTotalPaidAmount.getText());
            para.put("dueAmount", textFieldTotalDueAmount.getText());
            para.put("point1", "Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
            para.put("point2", "If you have any questions concerning this invoice, use the following contact information:");
            para.put("point3", "+964 7507039933, sales@sacoor.com");

            ArrayList<SalesList> plist = new ArrayList<>();

            for (SalesList item : tableViewItem.getItems()) {
                plist.add(new SalesList(item.getId(), item.getProduct(), item.getQuantity(),item.getPrice()));
            }

            JRBeanCollectionDataSource jcs = new JRBeanCollectionDataSource(plist);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, jcs);
            JasperViewer.viewReport(jp, false);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}