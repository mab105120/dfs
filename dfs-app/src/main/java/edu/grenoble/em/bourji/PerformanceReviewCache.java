package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.api.TeacherDossier;
import edu.grenoble.em.bourji.api.TeacherDossiers;
import edu.grenoble.em.bourji.db.dao.PerformanceReviewDAO;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moe on 9/5/2017.
 */
public class PerformanceReviewCache {

    private final PerformanceReviewDAO dao;
    private Map<String, TeacherDossier> teacherDossiers = new HashMap<>();

    public PerformanceReviewCache(PerformanceReviewDAO dao) {
        this.dao = dao;
    }

    @UnitOfWork
    void instantiateCache() {
        this.teacherDossiers = dao.getTeacherDossiers();
    }

    public TeacherDossiers getTeacherDossiers(String evaluationCode, String mode) {
        if(teacherDossiers.isEmpty()) throw new RuntimeException("Please instantiate the cache first!");
        Pair<String, String> teachers = ExperimentMode.getEvaluationCodes(mode).get(evaluationCode);
        if (teachers == null) throw new NullPointerException("Invalid evaluation code: " + evaluationCode);
        return new TeacherDossiers(
                teacherDossiers.get(teachers.getLeft()),
                teacherDossiers.get(teachers.getRight()) // this would be null in case of absolute evaluation
        );
    }

    public static boolean isValid(String evaluationCode, String mode) {
        return ExperimentMode.getEvaluationCodes(mode).get(evaluationCode.toLowerCase()) != null;
    }
}