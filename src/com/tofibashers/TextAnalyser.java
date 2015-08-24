package com.tofibashers;

/**
 * Created by TofixXx on 16.09.2014.
 */

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tofibashers.RegExpConstants.SENTENCE_EXP;

/** Class for analysis text by added commands*/
public class TextAnalyser {

    private Pattern[] wordPatterns;
    private Pattern disjunctionOfWords;
    private String doc;

    private List<String> sentencesWithWords;
    private int symbolsNum;
    private int[] wordsOccurrenceNum;
    private long analysisTime;

    public List<String> getSentencesWithWords() {
        return sentencesWithWords;
    }

    public int getSymbolsNum() {
        return symbolsNum;
    }

    public int[] getWordsOccurrenceNum() {
        return wordsOccurrenceNum;
    }

    public long getAnalysisTime() {
        return analysisTime;
    }

    public TextAnalyser(String doc, String[] words)
    {
        this.doc = doc;
        sentencesWithWords = new LinkedList();
        wordsOccurrenceNum = new int[words.length];
        wordPatterns = new Pattern[words.length];
        disjunctionOfWords = Pattern.compile(convertArrayToRegExp(words));
    }

    /**
     * @param commands array of non-repeated commands for analysis
     *  */
    public void analyseTextByCommands(String[] commands)
    {
        //TODO: split this method to multiple methods for each command
        long analysisBeginTime = System.currentTimeMillis();
        for(String command : commands)
        {
            switch (command){
                case "-e":
                    if(sentencesWithWords.size() == 0)
                    {
                        String[] sentences = getSentences(doc);
                        for(String sentence : sentences)
                        {
                            if(isIncludeWords(sentence))
                            {
                                sentencesWithWords.add(sentence);
                            }
                        }
                    }
                    break;
                case "-w":
                    for(int i = 0; i < wordPatterns.length; i++)
                    {
                        int count = 0;
                        Matcher matcher = wordPatterns[i].matcher(doc);
                        while(matcher.find()){
                            count ++;
                        }
                        wordsOccurrenceNum[i] = count;
                    }
                    break;
                case "-c":
                    String[] symSequences = doc.split("(\\s+)|(/n)");
                    for(String seq : symSequences)
                    {
                        symbolsNum += seq.length();
                    }
                    break;
            }
        }
        analysisTime = System.currentTimeMillis() - analysisBeginTime;
        //TODO: create single class for analyser tool. Add to this class total analysis info
    }

    /** found sentences, that separated by ".", "!", "?" or "\n" symbols
     * @return array on sentences
     * */
    private String[] getSentences(String doc)
    {
        return doc.split("(/n\\s+|\n{2,}|(?<=\\.)|(?<=!)|(?<=\\?))");
    }

    private String convertArrayToRegExp(String[] words){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(".*(");
        for(int i = 0; i < words.length - 1; i++){
            stringBuilder.append(words[i] + "|");
            wordPatterns[i] = Pattern.compile(words[i]);
        }
        stringBuilder.append(words[words.length - 1] + ").*");
        return String.format("\\b%s\\b", stringBuilder.toString());
    }

    private boolean isIncludeWords(String sentence){
        Matcher m = disjunctionOfWords.matcher(SENTENCE_EXP);
        return m.find();
    }

}
