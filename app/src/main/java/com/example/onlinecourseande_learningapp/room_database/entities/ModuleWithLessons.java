package com.example.onlinecourseande_learningapp.room_database.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ModuleWithLessons {
    @Embedded
    private Module module;

    @Relation(parentColumn = "module_id", entityColumn = "module_id")
    private List<Lesson> lessons;

    public ModuleWithLessons(Module module, List<Lesson> lessons) {
        this.module = module;
        this.lessons = lessons;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Module getModule() {
        return module;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }
}