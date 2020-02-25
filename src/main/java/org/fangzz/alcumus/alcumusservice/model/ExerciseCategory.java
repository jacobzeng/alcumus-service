package org.fangzz.alcumus.alcumusservice.model;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_exercise_categories")
public class ExerciseCategory extends DeletedAbleEntity {
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private ExerciseCategory parent;

    /**
     * 编码,规则就是父类code+id,如果没有父类,则等于id
     */
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExerciseCategory getParent() {
        return parent;
    }

    public void setParent(ExerciseCategory parent) {
        this.parent = parent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
