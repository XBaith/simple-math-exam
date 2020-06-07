package com.baith.ui;

import com.baith.cell.WrongQst;
import com.baith.util.SQLClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 菜单栏
 */
public class ExamMenuBar implements Builder<MenuBar> {

    private MenuBar menuBar = new MenuBar();
    private QuestionsTable table;

    public ExamMenuBar(QuestionsTable table) {
        this.table = table;
        menuBar.setStyle("-fx-font-size: 16");
    }

    public ExamMenuBar buildMenu(int index, String text){
        Menu menu = new Menu();
        menu.setText(text);
        menuBar.getMenus().add(index, menu);
        return this;
    }

    public ExamMenuBar buildMenuItem(int menuIndex, int itemIndex, String text) {
        MenuItem item = new MenuItem(text);
        menuBar.getMenus().get(menuIndex)
                .getItems().add(itemIndex, item);
        return this;
    }

    public Menu getMenu(String menu) {
        Menu res = null;
        for(Menu m : menuBar.getMenus()){
            if(m.getText().equals(menu))
                return m;
        }
        return res;
    }

    public MenuItem getMenuItem(String menu, String item) {
        MenuItem res = null;
        for(MenuItem menuItem : getMenu(menu).getItems()){
            if(menuItem.getText().equals(item))
                return menuItem;
        }
        return res;
    }

    private void buildPie() {
        MenuItem item = getMenuItem("分析", "饼状图");
        item.setOnAction(actionEvent -> {
            Map<String, Integer> res = SQLClient.getAnalysis();
            int right = res.get("right");
            int wrong = res.get("wrong");
            int space = res.get("space");
            int total = right + wrong + space;
            if(table == null || space == total ) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("请先答题");
                System.err.println("请先答题");
                alert.show();
            }
            // 创建新的stage
            Stage pieStage = new Stage();
            Scene pieScene = new Scene(new Group(), 500, 500);
            pieStage.setScene(pieScene);

            double rightCap = new BigDecimal(right * 1.0 / total * 1.0)
                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
            double wrongCap = new BigDecimal(wrong * 1.0 / total * 1.0)
                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
            double noCap = new BigDecimal(space * 1.0 / total * 1.0)
                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("正确率", rightCap * 100),
                            new PieChart.Data("错误率", wrongCap * 100),
                            new PieChart.Data("空题率", noCap * 100)
                    );
            final PieChart chart = new PieChart(pieChartData);
            chart.setTitle("答题分析：答题数 " + total);
            final Label caption = new Label("");
            caption.setTextFill(Color.DARKORANGE);
            caption.setStyle("-fx-font: 30 arial;");

            for (PieChart.Data data : chart.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                        new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent e) {
                                caption.setTranslateX(e.getSceneX());
                                caption.setTranslateY(e.getSceneY());
                                caption.setText(data.getPieValue() + "%");
                            }
                        }
                );
            }
            ((Group) pieScene.getRoot()).getChildren().addAll(chart, caption);
            pieStage.show();
        });
    }

    private void buildXY() {

    }

    private void buildWrongList() {
        MenuItem item = getMenuItem("错题", "错题本");
        item.setOnAction(actionEvent -> {
            // 创建新的stage
            Stage wrongListStage = new Stage();
            Scene wrongListScene = new Scene(new Group(), 300, 300);
            wrongListStage.setTitle("错题本");

            TableView table = new TableView();
            table.setMaxWidth(300);table.setMaxHeight(300);
            table.setStyle("-fx-font-size: 20");
            TableColumn qstCol = new TableColumn("问题");
            TableColumn ansCol = new TableColumn("答案");
            qstCol.setMinWidth(150);
            ansCol.setMinWidth(150);
            table.getColumns().addAll(qstCol, ansCol);

            Map<String, String> qsts = SQLClient.getWrongList();
            List<WrongQst> qstList = new ArrayList<>();
            for(Map.Entry<String, String> entry : qsts.entrySet())
                qstList.add(new WrongQst(entry.getKey(), entry.getValue()));

            qstCol.setCellValueFactory(new PropertyValueFactory<>("question"));
            ansCol.setCellValueFactory(new PropertyValueFactory<>("answer"));

            ObservableList<WrongQst> data = FXCollections.observableArrayList(
                    FXCollections.observableArrayList(qstList)
            );
            table.setItems(data);

            ((Group) wrongListScene.getRoot()).getChildren().addAll(table);
            wrongListStage.setScene(wrongListScene);
            wrongListStage.show();
        });


    }

    @Override
    public MenuBar build() {
        buildPie();
        buildXY();
        buildWrongList();
        return menuBar;
    }

}
