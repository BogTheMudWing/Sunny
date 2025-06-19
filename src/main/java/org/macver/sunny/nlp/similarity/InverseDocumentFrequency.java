package org.macver.sunny.nlp.similarity;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Package private class that implements Inverse Document Frequency (IDF).
 */
final class InverseDocumentFrequency {
    private final Map<String, Double> weights = new HashMap<>();
    private final List<List<String>> documents = new ArrayList<>();

    public void addDocument(List<String> document) {
        documents.add(document);
    }

    public void calculateWeights() {
        for(List<String> document : documents) {
            List<String> uniqueTerms = document.stream().distinct().toList(); // Remove duplicates
            for(String term : uniqueTerms) {
                double weight = weights.getOrDefault(term, 0.0);
                weight++;
                weights.put(term, weight);
            }
        }

        weights.replaceAll((k, v) -> Math.log10(v / documents.size()));
    }

    public double getWeight(String key) {
        return weights.getOrDefault(key, 0.0);
    }

    @NotNull
    public List<String> getAllTerms() {
        List<String> significantTerms = new ArrayList<>();

        for(Map.Entry<String, Double> weight : weights.entrySet()) {
            if(weight.getValue() != 0.0) {
                significantTerms.add(weight.getKey());
            }
        }

        Collections.sort(significantTerms);

        return significantTerms;
    }
}