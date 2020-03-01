package org.fangzz.alcumus.alcumusservice.dto;

public class ExerciseAnswerResponse {
    private boolean right = false;
    private String answer;
    private String answerDesc;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerDesc() {
        return answerDesc;
    }

    public void setAnswerDesc(String answerDesc) {
        this.answerDesc = answerDesc;
    }

    public boolean isSubmitAble() {
        return submitAble;
    }

    public void setSubmitAble(boolean submitAble) {
        this.submitAble = submitAble;
    }

    private boolean submitAble = true;

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }
}
