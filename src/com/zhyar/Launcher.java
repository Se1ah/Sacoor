package com.zhyar;

import com.zhyar.view.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ViewFactory viewFactory = new ViewFactory(new EmailManager());
        viewFactory.showLoginWindow();
        //viewFactory.showPurchaseWindow();
        //viewFactory.showProductWindow();
        //viewFactory.showInventoryWindow();
        //viewFactory.showSalesWindow();
    }
}
