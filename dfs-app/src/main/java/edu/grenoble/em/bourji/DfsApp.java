package edu.grenoble.em.bourji;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.grenoble.em.bourji.db.dao.*;
import edu.grenoble.em.bourji.db.pojo.*;
import edu.grenoble.em.bourji.resource.*;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import java.util.HashMap;

/**
 * Created by Moe on 8/16/2017.
 */
public class DfsApp extends Application<DfsConfig> {

    protected final HibernateBundle<DfsConfig> hibernate = new HibernateBundle<DfsConfig>(
            PerformanceReview.class, UserDemographic.class, UserExperience.class,
            UserConfidence.class, RelativeEvaluation.class, Status.class, Activity.class, Invite.class, GroupAttentionCheck.class,
            EvaluationActivity.class, AbsoluteEvaluation.class, ParticipantProfile.class, ExpertEvaluation.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(DfsConfig configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(String[] args) throws Exception {
        new DfsApp().run(args);
    }

    @Override
    public void run(DfsConfig config, Environment environment) throws Exception {

        FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/api/*");
        HashMap<String, String> params = new HashMap<>();
        params.put(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        params.put(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        params.put(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        cors.setInitParameters(params);

        environment.getObjectMapper().registerModule(new JavaTimeModule());
        environment.getObjectMapper().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        PerformanceReviewDAO dao = new PerformanceReviewDAO(hibernate.getSessionFactory());
        PerformanceReviewCache performanceReviewCache = new UnitOfWorkAwareProxyFactory(hibernate).create(PerformanceReviewCache.class, PerformanceReviewDAO.class, dao);

        performanceReviewCache.instantiateCache();

        QuestionnaireDAO questionnaireDAO = new QuestionnaireDAO()
                .withUserDemographicDao(new UserDemographicDAO(hibernate.getSessionFactory()))
                .withUserExperienceDao(new UserExperienceDAO(hibernate.getSessionFactory()))
                .withUserConfidenceDao(new UserConfidenceDAO(hibernate.getSessionFactory()));

        environment.jersey().register(new AuthFilter());
        StatusDAO statusDAO = new StatusDAO(hibernate.getSessionFactory());
        InviteDAO inviteDAO = new InviteDAO(hibernate.getSessionFactory());
        ActivityDAO activityDAO = new ActivityDAO(hibernate.getSessionFactory());
        AppraisalConfidenceDAO confidenceDAO = new AppraisalConfidenceDAO(hibernate.getSessionFactory());
        EvaluationActivityDAO evaluationActivityDAO = new EvaluationActivityDAO(hibernate.getSessionFactory());
        AbsoluteEvaluationDao absEvalDao = new AbsoluteEvaluationDao(hibernate.getSessionFactory());
        ExpertEvaluationDAO expertEvaluationDAO = new ExpertEvaluationDAO(hibernate.getSessionFactory());
        ParticipantProfileDAO participantProfileDAO = new ParticipantProfileDAO(hibernate.getSessionFactory());
        GroupAttentionCheckDAO groupAttentionCheckDAO = new GroupAttentionCheckDAO(hibernate.getSessionFactory());
        Emails emails = new Emails();
        // register resources
        environment.jersey().register(new PerformanceReviewResource(performanceReviewCache));
        environment.jersey().register(new QuestionnaireResource(questionnaireDAO, statusDAO));
        environment.jersey().register(new StatusResource(statusDAO, inviteDAO));
        environment.jersey().register(new ActivityResource(activityDAO));
        environment.jersey().register(new CommunicationResource(config.getEmailConfiguration().getUsername(),
                config.getEmailConfiguration().getPassword(), emails, inviteDAO, config.getInviteConfig()));
        environment.jersey().register(new ValidationResource(confidenceDAO, questionnaireDAO));
        environment.jersey().register(new AbsoluteEvaluationResource(absEvalDao, evaluationActivityDAO, statusDAO, inviteDAO));
        environment.jersey().register(new ParticipantProfileResource(participantProfileDAO));
        environment.jersey().register(new ExpertEvaluationResource(expertEvaluationDAO));
        environment.jersey().register(new GroupAttentionCheckResource(groupAttentionCheckDAO, statusDAO, participantProfileDAO, inviteDAO));
        environment.jersey().register(new MTurkResource(config.getAwsConfig(), inviteDAO, participantProfileDAO, config.getInviteConfig().getTimelag()));
    }

    @Override
    public void initialize(Bootstrap<DfsConfig> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
        bootstrap.addBundle(hibernate);
    }
}