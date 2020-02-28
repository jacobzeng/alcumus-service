package org.fangzz.alcumus.alcumusservice.dto;

public class ExerciseAnswerResponse {
    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    private boolean right = false;
}
