package com.baith.ui;

import com.baith.generator.Generator;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;

/**
 * 控制台，负责选择、刷新、提交题目等功能
 */
public class ExamConsole implements Builder<ButtonBar> {

    private ButtonBar buttonBar = new ButtonBar();
    private QuestionsTable table;

    public ExamConsole(QuestionsTable table) {
        this.table = table;
    }

    public ExamConsole buildButton(String text) {
        buttonBar.getButtons().add(new Button(text));
        return this;
    }

    public ExamConsole buildButton(int index, String text) {
        Button btn = new Button(text);
        buttonBar.getButtons().add(index, btn);
        if("加法题".equals(text)) {
            btn.getStyleClass().add("add_button");
            btn.setOnAction(actionEvent -> {
                table.generate(Generator.Type.ADD);
                buttonBar.getButtons().get(4).setDisable(false);    //刷新显示提交按钮
            });
        }else if("减法题".equals(text)) {
            btn.getStyleClass().add("sub_button");
            btn.setOnAction(actionEvent -> {
                table.generate(Generator.Type.SUB);
                buttonBar.getButtons().get(4).setDisable(false);
            });
        }else if("加/减法题".equals(text)){
            btn.getStyleClass().add("mix_button");
            btn.setOnAction(actionEvent -> {
                table.generate(Generator.Type.MIX);
                buttonBar.getButtons().get(4).setDisable(false);
            });
        }else if ("刷新".equals(text)){
            btn.getStyleClass().add("flush_button");
            btn.setOnAction(actionEvent -> {
                table.generate();
                buttonBar.getButtons().get(4).setDisable(false);
            });
        }else if("提交".equals(text)) {
            btn.getStyleClass().add("commit_button");
            btn.setOnAction(actionEvent -> {
                table.checkAnswer();
                btn.setDisable(true);   //提交后不能再次提交
            });
        }
        return this;
    }

    public ExamConsole buildButtonMinWidth(double width) {
        buttonBar.setButtonMinWidth(width);
        return this;
    }

    @Override
    public ButtonBar build() {
        return buttonBar;
    }
}
