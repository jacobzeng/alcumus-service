package org.fangzz.alcumus.alcumusservice.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_exercises")
public class Exercise extends DeletedAbleEntity {
    @ManyToOne
    private ExerciseCategory category;
    private String name;

    @Column(name = "exercise_desc", length = 4096)
    private String desc;

    private float difficulty = 0; //0到1之间

    private String answer; //答案

    @Column(length = 4096)
    private String answerDesc; //答案解析

    @Column(name = "exercise_from")
    private String from; //练习题摘录自哪里
    @Column(name = "exercise_online")
    private boolean online = false;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<ExerciseTag> tags = new HashSet<ExerciseTag>();
    private String tagNames;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public ExerciseCategory getCategory() {
        return category;
    }

    public void setCategory(ExerciseCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Set<ExerciseTag> getTags() {
        return tags;
    }

    public void setTags(Set<ExerciseTag> tags) {
        this.tags = tags;
    }

    public String getTagNames() {
        return tagNames;
    }

    public void setTagNames(String tagNames) {
        this.tagNames = tagNames;
    }
}
