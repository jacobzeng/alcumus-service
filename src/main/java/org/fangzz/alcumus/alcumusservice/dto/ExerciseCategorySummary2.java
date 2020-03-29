package org.fangzz.alcumus.alcumusservice.dto;

import org.fangzz.alcumus.alcumusservice.model.ExerciseCategory;

/**
 * 包含parent category的摘要信息
 */
public class ExerciseCategorySummary2 extends ExerciseCategorySummary {
    private ExerciseCategorySummary2 parent;

    public static ExerciseCategorySummary2 from(ExerciseCategory model) {
        if (null == model) {
            return null;
        }
        ExerciseCategorySummary2 dto = new ExerciseCategorySummary2();
        BaseDto.convert(model, dto);
        if (null != model.getParent()) {
            dto.setParent(ExerciseCategorySummary2.from(model.getParent()));
        }
        return dto;
    }


    public ExerciseCategorySummary2 getParent() {
        return parent;
    }

    public void setParent(ExerciseCategorySummary2 parent) {
        this.parent = parent;
    }
}
