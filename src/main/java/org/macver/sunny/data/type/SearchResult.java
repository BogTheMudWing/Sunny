package org.macver.sunny.data.type;

import org.jetbrains.annotations.Nullable;

public class SearchResult {

    @Nullable
    public final Answer answer;
    public final double confidence;

    public SearchResult(@Nullable Answer answer, double confidence) {
        this.answer = answer;
        this.confidence = confidence;
    }

    public boolean isNone() {
        return answer == null;
    }
}
