package edu.grenoble.em.bourji.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Moe on 8/29/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherDossiers {

    @JsonProperty("T1")
    private TeacherDossier teacher1;
    @JsonProperty("T2")
    private TeacherDossier teacher2;

    public TeacherDossiers() {
        // default no-arg constructor for jackson
    }

    public TeacherDossiers(TeacherDossier teacher1, TeacherDossier teacher2) {
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
    }

    public TeacherDossier getTeacher1() {
        return teacher1;
    }

    public TeacherDossier getTeacher2() {
        return teacher2;
    }

    public void setTeacher1(TeacherDossier teacher1) {
        this.teacher1 = teacher1;
    }

    public void setTeacher2(TeacherDossier teacher2) {
        this.teacher2 = teacher2;
    }
}