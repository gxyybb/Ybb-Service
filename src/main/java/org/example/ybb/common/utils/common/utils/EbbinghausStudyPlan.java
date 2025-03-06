package org.example.ybb.common.utils.common.utils;

import java.util.ArrayList;
import java.util.List;

public class EbbinghausStudyPlan {

    public static <T> List<List<T>> generateStudyPlan(List<T> studyItems) {
        int[] reviewIntervals = {0,1, 2, 4, 7, 15};

        List<List<T>> studyPlan = new ArrayList<>(studyItems.size() + 15);

        for (int i = 0; i < studyItems.size() + 15; i++) {
            studyPlan.add(new ArrayList<>());
        }

        for (int i = 0; i < studyItems.size(); i++) {

            for (int interval : reviewIntervals) {
                int reviewDay = i + interval;
                if (reviewDay < studyPlan.size()) {
                    studyPlan.get(reviewDay).add(studyItems.get(i));
                }
            }
        }

        return studyPlan;
    }
}
