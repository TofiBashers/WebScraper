package com.tofibashers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static List parseUrl(String url)
    {
        List<String> urls = new ArrayList<String>();
        if (checkURL(url))
        {
            urls.add(url);
        }
        else
        {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(url));
                while(bufferedReader.ready()){
                    String u = bufferedReader.readLine();
                    if(checkURL(u))
                    {
                        urls.add(u);
                    }
                    else{
                        System.out.println("Некорректный формат URL");
                        System.exit(0);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Ошибка: файл с URL не найден");
                e.printStackTrace();
                System.exit(0);
            } catch (IOException e){
                System.out.println("Ошибка чтения из файла");
                e.printStackTrace();
                System.exit(0);
            }
        }
        return urls;
    }

    static boolean checkURL(String url){
        Pattern pattern = Pattern.compile("((http://)?[a-z]+\\.[a-z0-9\\.]+\\.[a-z]{2,3})");
        Matcher matcher = pattern.matcher(url);
        if(matcher.matches()){
            return true;
        }
        return false;
    }

    static boolean checkCommand(String command){
        Pattern pattern = Pattern.compile("((-v)|(-w)|(-c)|(-e))");
        Matcher matcher = pattern.matcher(command);
        if(matcher.matches()){
            return true;
        }
        return false;
    }


    public static void main(String[] args){
        String[] words = args[1].split(",");
        String[] commands = new String[args.length - 2];
        for(int i=2; i < args.length; i++){

            if(checkCommand(args[i])){
                commands[i-2] = args[i];
            }
            else{
                System.out.println("Ошибка: несуществующая команда " + args[i]);
                System.exit(0);
            }
        }

        List<String> urls = parseUrl(args[0]);
        for(String url : urls){
            String doc = HTMLtoDocConverter.getDocByUrl(url);
            DocAnalyser docAnalyser = new DocAnalyser(doc, words);
            docAnalyser.analyseDocument(commands);

            System.out.println("URL: " + url);
            for(String command : commands)
            {
                if(command.equals("-v"))
                {
                    System.out.println(String.format("Временные затраты на анализ: %d мс", docAnalyser.getAnalyseTime()));
                }
                else if(command.equals("-w"))
                {
                    int[] wordsNums = docAnalyser.getWordsNums();
                    for(int i = 0; i < words.length; i++)
                    {
                        System.out.println(String.format("\"%s\" : встречается %d раз", words[i], wordsNums[i]));
                    }
                }
                else if(command.equals("-e"))
                {
                    System.out.println("Предложения, в которых использовались заданные слова:");
                    List<String> sentences = docAnalyser.geteSents();
                    for(String sentence : sentences){
                        sentence = sentence.replace("\n", "");
                        System.out.println(String.format("\"%s\"", sentence));
                    }
                    System.out.println();
                }
                else{
                    System.out.println(String.format("Общее количество символов: %d", docAnalyser.getSymbolsNum()));
                }
            }
            System.out.println();
        }


	// write your code here
    }
}
