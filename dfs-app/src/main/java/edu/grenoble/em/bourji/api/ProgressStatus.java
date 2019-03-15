package edu.grenoble.em.bourji.api;

/**
 * Created by Moe on 11/5/2017.
 */
public enum ProgressStatus {

    NOT_STARTED(0),
    QUEST_DEMO(1),
    QUEST_EXP(2),
    QUEST_CON(3),
    EVALUATION_P1(4),
    EVALUATION_P2(5),
    EVALUATION_P3(6),
    EVALUATION_P4(7),
    EVALUATION_1(8),
    EVALUATION_2(9),
    EVALUATION_3(10),
    EVALUATION_4(11),
    EVALUATION_5(12),
    EVALUATION_6(13),
    EVALUATION_7(14),
    EVALUATION_8(15),
    EVALUATION_9(16),
    EVALUATION_10(17),
    EVALUATION_11(18),
    EVALUATION_12(19),
    EVALUATION_13(20),
    EVALUATION_14(21),
    EVALUATION_15(22),
    COMPLETE(23);

    private int priority;

    ProgressStatus(int priority) {
        this.priority = priority;
    }

    public static ProgressStatus getNextStatus(ProgressStatus current) {
        return ProgressStatus.values()[current.priority + 1];
    }

    public int getPriority() {
        return priority;
    }
}