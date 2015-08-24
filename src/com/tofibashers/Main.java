package com.tofibashers;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    /** makes all print operations, printed url of page, and all analysis info, depends on the command line parameters
     * @param url valid url adress of paage
     * @param commands valid commands
     * @param words non-empty array with words
     * @param textAnalyser TextAnalyser after complete the analysis
     * */
    private static void printAnalysisResults(String url, String[] commands, String[] words, TextAnalyser textAnalyser){
        System.out.println("URL: " + url);
        for(String command : commands)
        {
            switch(command){
                case "-v":
                    System.out.println(String.format("Time costs for analysis: %d мс", textAnalyser.getAnalysisTime()));
                    break;
                case "-w":
                    int[] wordsNums = textAnalyser.getWordsOccurrenceNum();
                    for(int i = 0; i < words.length; i++)
                    {
                        System.out.println(String.format("\"%s\" : found %d times", words[i], wordsNums[i]));
                    }
                    break;
                case "-e":
                    System.out.println("Sentences, where used the words:");
                    List<String> sentences = textAnalyser.getSentencesWithWords();
                    for(String sentence : sentences){
                        sentence = sentence.replace("\n", "");
                        System.out.println(String.format("\"%s\"", sentence));
                    }
                    System.out.println();
                    break;
                case "-c":
                    System.out.println(String.format("Total characters number: %d", textAnalyser.getSymbolsNum()));
            }
        }
    }


    public static void main(String[] args){

        if(args.length < 3){
            log.severe("Too low arguments");
            return;
        }

        List<String> urls;
        if(Utils.isURL(args[0])){
            urls = Arrays.asList(args[0]);
        }
        else{
            urls = Utils.getUrlsFromFile(args[0]);
        }

        String[] words = args[1].split(",");
        if((args.length - 2) > 4){
            log.severe("Incorrect number of commands");
            return;
        }

        String[] commands = new String[args.length - 2];
        System.arraycopy(args, 2, commands, 0, commands.length);

        if(!Utils.validateCommands(commands)){
            log.severe("Incorrect commands format");
            return;
        }

        for(String url : urls){
            String doc = Utils.getDocTextByUrl(url);
            TextAnalyser textAnalyser = new TextAnalyser(doc, words);
            textAnalyser.analyseTextByCommands(commands);
            printAnalysisResults(url, commands, words, textAnalyser);
        }

    }
}
