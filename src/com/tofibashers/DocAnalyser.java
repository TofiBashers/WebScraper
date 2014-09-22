package com.tofibashers;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TofixXx on 16.09.2014.
 */
public class DocAnalyser {
    private String[] words;
    private Pattern[] wordPatterns;
    private Pattern disjunctionWords;
    private String doc;

    private List<String> eSents;
    private int symbolsNum;
    private int[] wordsNums;
    private long analyseTime;

    public List<String> geteSents() {
        return eSents;
    }

    public int getSymbolsNum() {
        return symbolsNum;
    }

    public int[] getWordsNums() {
        return wordsNums;
    }

    public long getAnalyseTime() {
        return analyseTime;
    }


    public DocAnalyser(String doc, String[] words)
    {
        this.words = words;
        this.doc = doc;
        eSents = new LinkedList();
        wordsNums = new int[words.length];
        wordPatterns = new Pattern[words.length];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(".*(");
        for(int i = 0; i < words.length - 1; i++){
            stringBuilder.append(words[i] + "|");
            wordPatterns[i] = Pattern.compile(words[i]);
        }
        stringBuilder.append(words[words.length - 1] + ").*");
        String wordsString = stringBuilder.toString();
        String wordsRegEx = String.format("\\b%s\\b", wordsString);
        disjunctionWords = Pattern.compile(wordsRegEx);
    }
    private DocAnalyser(){};

    public void analyseDocument(String[] comands)
    {
        long begintime = System.currentTimeMillis();
        for(String comand : comands)
        {
            if(comand.equals("-e"))
            {
                if(eSents.size() == 0)
                {
                    String[] sentences = getSentences(doc);
                    for(String sentence : sentences)
                    {
                        if(includedWords(sentence))
                        {
                            eSents.add(sentence);
                        }
                    }
                }
            }
            else if(comand.equals("-w"))
            {
                for(int i = 0; i < words.length; i++)
                {
                    int count = 0;
                    Matcher matcher = wordPatterns[i].matcher(doc);
                    while(matcher.find()){
                        count ++;
                    }
                    wordsNums[i] = count;
                }
            }
            else if(comand.equals("-c"))
            {
                String[] symSequences = doc.split("(\\s+)|(/n)");
                for(String seq : symSequences)
                {
                    symbolsNum += seq.length();
                }
            }
        }
        analyseTime = System.currentTimeMillis() - begintime;
    }
    private String[] getSentences(String doc)
    {
        return doc.split("(/n\\s+|\n{2,}|(?<=\\.)|(?<=!)|(?<=\\?))");
    }

    private boolean includedWords(String sentence){
        Matcher m = disjunctionWords.matcher(sentence);
        if(m.find()){
            return true;
        }
        return false;
    }

}
