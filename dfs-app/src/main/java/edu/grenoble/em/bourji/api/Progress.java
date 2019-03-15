package edu.grenoble.em.bourji.api;

import java.util.List;

/**
 * Created by Moe on 11/5/2017.
 */
public class Progress {

    private List<ProgressStatus> completed;

    public Progress(List<ProgressStatus> completed) {
        this.completed = completed;
    }

    public Progress() {
        // default no-arg constructor for jackson
    }

    public List<ProgressStatus> getCompleted() {
        return completed;
    }

}