package org.macver.sunny;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import org.apache.commons.collections4.map.SingletonMap;
import org.jetbrains.annotations.NotNull;
import org.macver.sunny.data.IndexManager;
import org.macver.sunny.data.type.Answer;
import org.macver.sunny.data.type.Index;
import org.macver.sunny.data.type.SearchResult;
import org.macver.sunny.nlp.similarity.TextSimilarity;

import java.io.IOException;
import java.util.*;

public class Searcher {

    @NotNull
    public SearchResult findRelevantDocument(Guild guild, Channel channel, String question) throws IOException {

        List<Index> indexes = new IndexManager().getIndexes(guild);

        List<Answer> answers = new ArrayList<>();

        // Add answers of all indexes which have this channel linked
        for (Index index : indexes) {
            if (index.channels.contains(channel.getIdLong())) {
                answers.addAll(index.answers);
            }
        }

        // If no answers, return empty result
        if (answers.isEmpty()) return new SearchResult(new HashMap<>());

        // If exact match, return that
        String cleanQuestion = question.toLowerCase().replaceAll("[^a-z0-9 ]", "");
        for (Answer answer : answers) {
            List<String> cleanedQueries = new ArrayList<>();
            for (String s : answer.query) {
                cleanedQueries.add(s.toLowerCase().replaceAll("[^a-z0-9 ]", ""));
            }
            if (cleanedQueries.contains(cleanQuestion)) return new SearchResult(new SingletonMap<>(1.0, answer));
        }

        TextSimilarity ts = new TextSimilarity();

        ts.addDocument("query", question.toLowerCase().replaceAll("[^a-z0-9 ]", ""));

        for (int i = 0; i < answers.size(); i++) {
            ts.addDocument(String.valueOf(i), Arrays.toString(answers.get(i).query.toArray()).toLowerCase().replaceAll("[^a-z0-9 ]", ""));
        }
        ts.calculate();

        Map<Double, String> result = ts.getSimilarDocuments("query");

        Map<Double, Answer> answerMap = new HashMap<>();

        for (Double confidence : result.keySet()) {
            for (int i = 0; i < answers.size(); i++) {
                answerMap.put(confidence, answers.get(Integer.parseInt(result.get(confidence))));
            }
        }

        return new SearchResult(answerMap);

//        // Cosine similarity
//        Optional<AbstractMap.SimpleEntry<Answer, Double>> max = answers.stream()
//                .map(article -> new AbstractMap.SimpleEntry<>(article,
//                        cosine.cosineSimilarity(toVector(question), toVector(Arrays.toString(article.query.toArray())))))
//                .filter(entry -> entry.getValue() != null) // Remove null answers
//                .filter(entry -> entry.getKey().minimumConfidence > entry.getValue()) // Remove answers if minimumConfidence is less than confidence
//                .max(Comparator.comparingDouble(Map.Entry::getValue));
//
//        return max.map(answerDoubleSimpleEntry -> new SearchResult(answerDoubleSimpleEntry.getKey(), answerDoubleSimpleEntry.getValue())).orElseGet(() -> new SearchResult(null, 0));

    }

//    public Optional<Answer> findRelevantDocument(Guild guild, Channel channel, String question, double threshold) throws IOException {
//        CosineSimilarity cosine = new CosineSimilarity();
//
//        List<Index> indexes = new IndexManager().getIndexes(guild);
//
//        List<Answer> answers = new ArrayList<>();
//
//        for (Index index : indexes) {
//            if (index.channels.contains(channel.getIdLong())) {
//                answers.addAll(index.answers);
//            }
//        }
//
//        if (answers.isEmpty()) return Optional.empty();
//
//        return answers.stream()
//                .map(article -> new AbstractMap.SimpleEntry<>(article,
//                        cosine.cosineSimilarity(toVector(question), toVector(article.query))))
//                .filter(entry -> entry.getValue() != null && entry.getValue() > threshold)
//                .max(Comparator.comparingDouble(Map.Entry::getValue))
//                .map(Map.Entry::getKey);
//    }

    @NotNull
    public Map<CharSequence, Integer> toVector(@NotNull String text) {
        Map<CharSequence, Integer> vector = new HashMap<>();
        String[] tokens = text.toLowerCase().replaceAll("[^a-z0-9 ]", "").split("\\s+");

        Sunny.logger.info("Converted {} to vector {}", text, Arrays.toString(tokens));

        for (String token : tokens) {
            if (token.isBlank()) continue;
            vector.put(token, vector.getOrDefault(token, 0) + 1);
        }
        return vector;
    }

}
