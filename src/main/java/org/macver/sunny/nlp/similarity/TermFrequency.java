package org.macver.sunny.nlp.similarity;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Package private class that implements Term Frequency (TF).
 */
final class TermFrequency {
    private final Map<String, Double> frequencies = new HashMap<>();
    private double numberOfTerms;

    public void addTerms(@NotNull List<String> terms) {
        for(String term : terms) {
            double frequency = frequencies.getOrDefault(term, 0.0);
            frequency++;
            frequencies.put(term, frequency);
        }

        numberOfTerms = 0.0;
        for(double frequency : frequencies.values()) {
            numberOfTerms += frequency;
        }
    }

    public double getFrequency(String term) {
        double frequency = frequencies.getOrDefault(term, 0.0);
        if(frequency != 0.0) {
            return frequencies.get(term) / numberOfTerms;
        } else {
            return 0.0;
        }
    }
}