package hcmute.kltn.backend.service.service_implementation;

import com.darkprograms.speech.translator.GoogleTranslate;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import hcmute.kltn.backend.nlp.Pipeline;
import hcmute.kltn.backend.service.NlpService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class NlpServiceImpl implements NlpService {
    @Override
    public String nerKeyword(String text) {
        String result = "";
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreLabel> coreLabelList = coreDocument.tokens();
        for (CoreLabel coreLabel : coreLabelList) {
            String ner = coreLabel.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            if (ner.equals("DATE") || ner.equals("ORGANIZATION") || ner.equals("LOCATION")
                    || ner.equals("PERSON")) {
                if (!result.contains(coreLabel.originalText())) {
                    result = result + " " + coreLabel.originalText();
                }
            }
        }
        return result;
    }

    @Override
    public String separateSentenceAndTranslate(String text) {
        String result = "";
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreSentence> sentences = coreDocument.sentences();
        for (CoreSentence sentence : sentences) {
            String sentenceEnglish = translateViToEn(sentence.toString());
            result = result + " " + sentenceEnglish;
        }
        return result;
    }

    @Override
    public Float calculateSimilarity(String str1, String str2) {
        int commonChars1 = 0;
        int commonChars2 = 0;
        for (char c1 : str1.toCharArray()) {
            if (str2.indexOf(c1) != -1) {
                commonChars1++;
            }
        }
        for (char c2 : str2.toCharArray()) {
            if (str1.indexOf(c2) != -1) {
                commonChars2++;
            }
        }
        int totalChars = str1.length() + str2.length();
        return (float) (commonChars1 + commonChars2) / totalChars;
    }

    @Override
    public String translateViToEn(String text) {
        if (text.length() < 5000) {
            String result = text;
            try {
                String language = GoogleTranslate.detectLanguage(text);
                if (Objects.equals(language, "vi")) {
                    result = GoogleTranslate.translate("en", text);
                }
                return result;
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            return separateSentenceAndTranslate(text);
        }

    }


}
