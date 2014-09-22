package com.tofibashers;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by TofixXx on 15.09.2014.
 */
public class HTMLtoDocConverter {
    public static String getDocByUrl(String url){

        EditorKit editorKit = new HTMLEditorKit();
        Document document = editorKit.createDefaultDocument();
        document.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        Reader reader = getReader(url);
        try{
            editorKit.read(reader, document, 0);
            return document.getText(0, document.getLength());
        }catch(IOException e){
            System.out.println("Ошибка при записи текста из страницы по заданному адресу");

        }catch(BadLocationException e){
            System.out.println("Ошибка при построении документа");
        }
        return null;

    }

    private static Reader getReader(String uri)
    {
        try{
            URLConnection conn = new URL(uri).openConnection();
            return new InputStreamReader(conn.getInputStream());
        }catch(IOException e){
            System.out.println("Ошибка при открытии соединения");
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }
}
