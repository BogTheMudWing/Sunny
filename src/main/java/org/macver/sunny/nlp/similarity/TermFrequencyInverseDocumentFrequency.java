package org.macver.sunny.nlp.similarity;

import java.util.*;

public class TermFrequencyInverseDocumentFrequency {
    private final Map<String, List<String>> documents = new HashMap<>();
    private final Map<String, double[]> vectors = new HashMap<>();

    public void addDocument(String documentId, List<String> terms) {
        documents.put(documentId, terms);
    }

    public void calculate() {
        InverseDocumentFrequency inverseDocumentFrequency = new InverseDocumentFrequency();
        for(List<String> terms : documents.values()) {
            inverseDocumentFrequency.addDocument(terms);
        }
        inverseDocumentFrequency.calculateWeights();

        List<String> defaultDimensions = inverseDocumentFrequency.getAllTerms();
        for(Map.Entry<String, List<String>> document : documents.entrySet()) {
            TermFrequency termFrequency = new TermFrequency();
            termFrequency.addTerms(document.getValue());

            double[] vector = new double[defaultDimensions.size()];

            for(int i = 0; i < defaultDimensions.size(); i++) {
                String term = defaultDimensions.get(i);
                vector[i] = termFrequency.getFrequency(term) * inverseDocumentFrequency.getWeight(term);
            }
            vectors.put(document.getKey(), vector);
        }
    }

    public int size() {
        return documents.size();
    }

    public double[] getVectorByDocumentId(String documentId) {
        return vectors.get(documentId);
    }

    public Map<String, double[]> getVectors() {
        return vectors;
    }
}
