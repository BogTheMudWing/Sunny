package org.macver.sunny;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.Channel;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.jetbrains.annotations.NotNull;
import org.macver.sunny.data.IndexManager;
import org.macver.sunny.data.type.Answer;
import org.macver.sunny.data.type.Index;
import org.macver.sunny.data.type.SearchResult;

import java.io.IOException;
import java.util.*;

public class Searcher {

    @NotNull
    public SearchResult findRelevantDocument(Guild guild, Channel channel, String question) throws IOException {
        CosineSimilarity cosine = new CosineSimilarity();

        List<Index> indexes = new IndexManager().getIndexes(guild);

        List<Answer> answers = new ArrayList<>();

        for (Index index : indexes) {
            if (index.channels.contains(channel.getIdLong())) {
                answers.addAll(index.answers);
            }
        }

        if (answers.isEmpty()) return new SearchResult(null, 0);

        Optional<AbstractMap.SimpleEntry<Answer, Double>> max = answers.stream()
                .map(article -> new AbstractMap.SimpleEntry<>(article,
                        cosine.cosineSimilarity(toVector(question), toVector(Arrays.toString(article.query.toArray())))))
                .filter(entry -> entry.getValue() != null) // Remove null answers
                .filter(entry -> entry.getKey().minimumConfidence > entry.getValue()) // Remove answers if minimumConfidence is less than confidence
                .max(Comparator.comparingDouble(Map.Entry::getValue));

        return max.map(answerDoubleSimpleEntry -> new SearchResult(answerDoubleSimpleEntry.getKey(), answerDoubleSimpleEntry.getValue())).orElseGet(() -> new SearchResult(null, 0));

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

        for (String token : tokens) {
            if (token.isBlank()) continue;
            vector.put(token, vector.getOrDefault(token, 0) + 1);
        }
        return vector;
    }

}
