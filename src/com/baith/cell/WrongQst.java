package com.baith.cell;

import javafx.beans.property.SimpleStringProperty;

/**
 * 错题表格的填充数据
 */
public class WrongQst {
    private final SimpleStringProperty question;
    private final SimpleStringProperty answer;

    public WrongQst(String question, String answer) {
        this.question = new SimpleStringProperty(question);
        this.answer = new SimpleStringProperty(answer.equals(String.valueOf(Integer.MIN_VALUE)) ? "" : answer);
    }

    public String getQuestion() {
        return question.get();
    }

    public SimpleStringProperty questionProperty() {
        return question;
    }

    public void setQuestion(String question) {
        this.question.set(question);
    }

    public String getAnswer() {
        return answer.get();
    }

    public SimpleStringProperty answerProperty() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer.set(answer);
    }
}
