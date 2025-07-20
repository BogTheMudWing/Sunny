package org.macver.sunny.data.type;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SearchResult {

    @Nullable
    public final Map<Double, Answer> answers;

    public SearchResult(@Nullable Map<Double, Answer> confidenceAnswerMap) {
        answers = confidenceAnswerMap;
    }
}
