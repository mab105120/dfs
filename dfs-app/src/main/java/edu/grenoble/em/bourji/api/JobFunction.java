package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 9/5/2017.
 */
public enum JobFunction {

    STUDENT_LEARNING("Student Learning", "SL"),
    INSTRUCTIONAL_PRACTICE("Instructional Practice", "IP"),
    PROFESSIONALISM("Professionalism", "PF");

    private String name;
    private String code;

    JobFunction(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
