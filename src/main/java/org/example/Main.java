package org.example;
import java.util.*;
import static spark.Spark.*;
import com.google.gson.Gson;
public class Main {

    public static void main(String[] args) {

        // Enable CORS
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
        });

        post("/merge", (req, res) -> {
            String requestBody = req.body();
            Gson gson = new Gson();
            String[] texts = gson.fromJson(requestBody, String[].class);

            String mergedText = mergeTexts(texts);
            System.out.println("Merged text: " + mergedText);
            return mergedText;
        });
    }

    public static String mergeTexts(String[] texts) {
        StringBuilder mergedText = new StringBuilder();
        String lastMatchedWord = "";
        for (String text : texts) {
            String[] words = text.split(" ");
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                if (mergedText.toString().contains(word.substring(0, Math.min(word.length(), 3)))) {
                    int startIndex = mergedText.toString().lastIndexOf(word.substring(0, Math.min(word.length(), 3)));
                    mergedText = new StringBuilder(mergedText.substring(0, startIndex));
                }
                if (lastMatchedWord.length() >= 3 && word.startsWith(lastMatchedWord.substring(0, 3))) {
                    mergedText.append(word.substring(0)).append(" ");
                } else {
                    mergedText.append(word).append(" ");
                }
                lastMatchedWord = word;
            }
        }
        return mergedText.toString().trim();
    }




}
