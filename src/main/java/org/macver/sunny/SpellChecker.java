package org.macver.sunny;

import org.jetbrains.annotations.NotNull;
import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;
import org.languagetool.rules.spelling.SpellingCheckRule;

import java.io.IOException;
import java.util.List;

public class SpellChecker {

    public String correctSpelling(String input, List<String> noCorrectionPhrases) throws IOException {
        JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode("en-US"));
        for (Rule rule : langTool.getAllActiveRules()) {
            if (rule instanceof SpellingCheckRule) {
                ((SpellingCheckRule)rule).acceptPhrases(noCorrectionPhrases);
            }
        }

        List<RuleMatch> matches = langTool.check(input);
        StringBuilder corrected = getCorrected(input, matches);

        return corrected.toString();
    }

    @NotNull
    private static StringBuilder getCorrected(String input, @NotNull List<RuleMatch> matches) {
        StringBuilder corrected = new StringBuilder(input);
        int offset = 0;

        for (RuleMatch match : matches) {
            List<String> suggestions = match.getSuggestedReplacements();
            if (!suggestions.isEmpty()) {
                String bestReplacement = suggestions.getFirst();

                int start = match.getFromPos() + offset;
                int end = match.getToPos() + offset;

                corrected.replace(start, end, bestReplacement);

                // Update offset in case the replacement length differs from the original
                offset += bestReplacement.length() - (end - start);
            }
        }
        return corrected;
    }
}
