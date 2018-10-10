package com.ankur.a3_2016225.model;

public class Question {

    private int qId;
    private String questionBody;
    private boolean answer;
    private int selectedAnswer;

    public Question() {
        selectedAnswer = -1;
    }

    public int getqId() {
        return qId;
    }

    public void setqId(int qId) {
        this.qId = qId;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }

    public boolean getAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public int getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(int selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "qId=" + qId +
                ", questionBody='" + questionBody + '\'' +
                ", answer=" + answer +
                ", selectedAnswer=" + selectedAnswer +
                '}';
    }
}
