package com.baith;

import com.baith.ui.ExamConsole;
import com.baith.ui.ExamMenuBar;
import com.baith.ui.GridBuilder;
import com.baith.ui.QuestionsTable;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * 主界面启动类
 */
public class Main extends Application {

    private static final int COLUMN = 5;    //习题列数
    private static final int ROW = 10;  //习题行数
    private static final double HBOX_WIDTH = 150.0, HBOX_HEIGHT = 50.0; //习题表格中的长宽
    private static final double CONSOLE_HEIGHT = 50.0;  //控制面板高度
    private static final double GRID_SPACING = 5.0; //表格中的HBox中元素之间的空隙宽度
    private static final int SCENE_WIDTH = (int) (HBOX_WIDTH * COLUMN + ROW * GRID_SPACING);
    private static final int SCENE_HEIGHT = (int) (HBOX_HEIGHT * ROW + CONSOLE_HEIGHT );
//    private static final double BAR_HEIGHT = 20.0;

    private BorderPane mainPane = new BorderPane(); //主面板

    private QuestionsTable questionsTable;  //习题表

    @Override
    public void init() throws Exception {
        //构造问题体表
        HBox[][] qstHBoxes = new HBox[ROW][COLUMN];
        GridPane gridPane = new GridBuilder()
                .buildVHGap(5, 10)
                .buildGridLinesVisible(false)
                .full(qstHBoxes, HBox.class)
                .build();
        questionsTable = new QuestionsTable()
                .setHBoxesSpacing(GRID_SPACING)
                .setHBoxesSize(HBOX_WIDTH, HBOX_HEIGHT)
                .construct(gridPane, qstHBoxes, new Label[ROW][COLUMN], new TextField[ROW][COLUMN]);
        mainPane.setCenter(gridPane);

        //创建控制菜单
        ExamMenuBar menuBar = new ExamMenuBar(questionsTable);
        mainPane.setTop(menuBar
                .buildMenu(0, "分析")
                .buildMenuItem(0, 0, "饼状图")
                .buildMenuItem(0,1, "柱状图")
                .buildMenu(1, "错题")
                .buildMenuItem(1, 0, "错题本")
                .build());

        //构建控制台
        mainPane.setBottom(new ExamConsole(questionsTable)
                .buildButton(0, "加法题")
                .buildButton(1, "减法题")
                .buildButton(2, "加/减法题")
                .buildButton(3, "刷新")
                .buildButton(4, "提交")
                .build());
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("100以内加减算式习题");
        Scene scene = new Scene(mainPane, SCENE_WIDTH + questionsTable.getGridPane().getHgap() * COLUMN,
                SCENE_HEIGHT + questionsTable.getGridPane().getVgap() * ROW + CONSOLE_HEIGHT);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
