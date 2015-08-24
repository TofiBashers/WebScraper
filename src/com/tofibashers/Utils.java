package com.tofibashers;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.tofibashers.RegExpConstants.URL_FORMAT_EXP;
import static com.tofibashers.RegExpConstants.COMMANDS_FORMAT_EXP;

/**
 * Created by TofixXx on 15.09.2014.
 */
public class Utils {

    private static final Logger log = Logger.getLogger(Utils.class.getName());

    /** Reads text of web page, given by url
     * @return text in one string
     * */
    public static String getDocTextByUrl(String url){

        EditorKit editorKit = new HTMLEditorKit();
        Document document = editorKit.createDefaultDocument();
        document.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        Reader reader = getReader(url);
        try{
            editorKit.read(reader, document, 0);
            return document.getText(0, document.getLength());
        }catch(IOException e){
            log.log(Level.SEVERE, "IOException", e);

        }catch(BadLocationException e){
            log.log(Level.SEVERE, "Error with text creating", e);
        }
        return null;

    }

    /** Gets reader for connection with uri. If an error with trying connect occurs, exits from program*/
    private static Reader getReader(String uri)
    {
        try{
            URLConnection conn = new URL(uri).openConnection();
            return new InputStreamReader(conn.getInputStream());
        }catch(IOException e){
            log.log(Level.SEVERE, "Error with tying connect to URL: " + uri, e);
            System.exit(0);
        }
        return null;
    }

    public static boolean isURL(String url){
        return url.matches(URL_FORMAT_EXP);
    }

    public static boolean validateCommands(String[] commands) {
        for(String command : commands){
            if(!command.matches(COMMANDS_FORMAT_EXP)){
                return false;
            }
        }
        return true;
    }

    /**
     * @param path path to file with one, or more lines with url
     *  */
    public static List<String> getUrlsFromFile(String path){
        List<String> urls = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))){
            while(bufferedReader.ready()){
                String u = bufferedReader.readLine();
                if(isURL(u))
                {
                    urls.add(u);
                }
                else{
                    log.severe("Incorrect URL format");
                    System.exit(0);
                }
            }
        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE, "File with URL not found", e);
            System.exit(0);
        } catch (IOException e){
            log.log(Level.SEVERE, "File reading error", e);
            System.exit(0);
        }
        return urls;
    }
}
