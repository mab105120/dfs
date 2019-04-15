package edu.grenoble.em.bourji;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moe on 2/13/2018.
 */
public enum ExperimentMode {

    DFS(new HashMap<String, Pair<String, String>>() {{
        put("p1", new ImmutablePair<>("A", null));
        put("p2", new ImmutablePair<>("B", null));
        put("p3", new ImmutablePair<>("D", null));
        put("p4", new ImmutablePair<>("F", null));
        put("1", new ImmutablePair<>("C", null));
        put("2", new ImmutablePair<>("E", null));
    }}),
    NFS(new HashMap<String, Pair<String, String>>() {{
        put("p1", new ImmutablePair<>("A", null));
        put("p2", new ImmutablePair<>("B", null));
        put("p3", new ImmutablePair<>("D", null));
        put("p4", new ImmutablePair<>("F", null));
        put("1", new ImmutablePair<>("C", null));
        put("2", new ImmutablePair<>("E", null));
    }}),
    IFS(new HashMap<String, Pair<String, String>>() {{
        put("p1", new ImmutablePair<>("A", null));
        put("p2", new ImmutablePair<>("B", null));
        put("p3", new ImmutablePair<>("D", null));
        put("p4", new ImmutablePair<>("F", null));
        put("1", new ImmutablePair<>("C", null));
        put("2", new ImmutablePair<>("E", null));
    }});

    private Map<String, Pair<String, String>> evaluationCodes;

    ExperimentMode(Map<String, Pair<String, String>> codes) {
        evaluationCodes = codes;
    }

    Map<String, Pair<String, String>> getEvaluationCodes() {
        return this.evaluationCodes;
    }

    public static Map<String, Pair<String, String>> getEvaluationCodes(String mode) {
        for(ExperimentMode m: ExperimentMode.values())
            if(m.name().equalsIgnoreCase(mode))
                return m.getEvaluationCodes();
        throw new IllegalArgumentException("Unknown experiment mode: " + mode);
    }
}