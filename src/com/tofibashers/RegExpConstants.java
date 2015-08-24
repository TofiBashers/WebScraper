package com.tofibashers;

/**
 * Created by TofixXx on 24.08.2015.
 */
public class RegExpConstants {
    public static final String URL_FORMAT_EXP = "((http://)?[a-z]+\\.[a-z0-9\\.]+\\.[a-z]{2,3})";
    public static final String COMMANDS_FORMAT_EXP = "((-v)|(-w)|(-c)|(-e))";
    public static final String SENTENCE_EXP = "(/n\\s+|\n{2,}|(?<=\\.)|(?<=!)|(?<=\\?))";

    private RegExpConstants(){}

}
