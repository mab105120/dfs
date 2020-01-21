package edu.grenoble.em.bourji.db.dao;

/**
 * Created by Moe on 9/9/2017.
 */
public class QuestionnaireDAO {

    private UserDemographicDAO userDemographicDAO;
    private UserExperienceDAO userExperienceDAO;
    private UserRecipOrientationDAO userRecipOrientationDAO;

    public QuestionnaireDAO withUserDemographicDao(UserDemographicDAO dao) {
        this.userDemographicDAO = dao;
        return this;
    }

    public QuestionnaireDAO withUserExperienceDao(UserExperienceDAO dao) {
        this.userExperienceDAO = dao;
        return this;
    }

    public QuestionnaireDAO withUserRecipOrientationDao(UserRecipOrientationDAO dao) {
        this.userRecipOrientationDAO = dao;
        return this;
    }

    public UserDemographicDAO getUserDemographicDAO() {
        return userDemographicDAO;
    }

    public UserExperienceDAO getUserExperienceDAO() {
        return userExperienceDAO;
    }

    public UserRecipOrientationDAO getUserRecipOrientationDAO() {
        return userRecipOrientationDAO;
    }
}
