package backupcc.edit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author "Pedro Reis"
 * @since 5 de setembro 2022
 * @version 1.0
 */
public final class Edit {
    
    private static final String HREF_STR = "href=\"./";
   
    private static final String FORUM_URL_STR = 
        backupcc.net.Util.FORUM_URL.replace(".", "[.]"); 
    
    private static final Pattern INDEX_REGEX =
        Pattern.compile(
            "href=\"(" +
            FORUM_URL_STR + 
            "|[.]/index\\.php).*?\""                  
        );
         
    private static final Pattern VIEWTOPIC_REGEX = 
        Pattern.compile(
            "href=\"(" +
            FORUM_URL_STR + 
            "|[.])/viewtopic\\.php\\?.*?(t=\\d+).*?\""
        );
    
    private static final Pattern VIEWFORUM_REGEX =
        Pattern.compile(
            "href=\"(" +
            FORUM_URL_STR + 
            "|[.])/viewforum\\.php\\?.*?(f=\\d+).*?\""    
        );
    
    private static final Pattern START_REGEX =
        Pattern.compile("start=\\d+");
    
    private static HashMap<String, String> hashMap;
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    private static String indexPhp(String contentFile) {
        
        hashMap = new HashMap<>();
        
        Matcher index = INDEX_REGEX.matcher(contentFile);

        while (index.find()) {

            String editedUrl = HREF_STR + backupcc.file.Util.INDEX_HTML + "\"";
            
            hashMap.put(index.group(), editedUrl);

        }//while

        Set<String> keySet = hashMap.keySet();

        for (String originalUrl : keySet) {

            String editedUrl = hashMap.get(originalUrl);

            contentFile = contentFile.replace(originalUrl, editedUrl);

        }//for originalUrl
        
        return contentFile;
        
    }//indexPhp()
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    private static String viewtopicPhp(String contentFile) {
        
        hashMap = new HashMap<>();
        
        Matcher viewtopic = VIEWTOPIC_REGEX.matcher(contentFile);

        while (viewtopic.find()) {
            
            String originalUrl = viewtopic.group();

            String editedUrl = HREF_STR + viewtopic.group(2);
            
            Matcher start = START_REGEX.matcher(originalUrl);

            if (start.find()) editedUrl += '&' + start.group();

            editedUrl += ".html\""; 

            hashMap.put(originalUrl, editedUrl);

        }//while

        Set<String> keySet = hashMap.keySet();

        for (String originalUrl : keySet) {

            String editedUrl = hashMap.get(originalUrl);

            contentFile = contentFile.replace(originalUrl, editedUrl);

        }//for oldUrl
        
        return contentFile;

    }//viewtopicPhp()
    
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    private static String viewforumPhp(String contentFile) {
        
        hashMap = new HashMap<>();
        
        Matcher viewforum = VIEWFORUM_REGEX.matcher(contentFile);

        while (viewforum.find()) {
            
            String originalUrl = viewforum.group();

            String editedUrl = HREF_STR + viewforum.group(2);
            
            Matcher start = START_REGEX.matcher(originalUrl);

            if (start.find()) editedUrl += '&' + start.group();

            editedUrl += ".html\""; 

            hashMap.put(originalUrl, editedUrl);

        }//while

        Set<String> keySet = hashMap.keySet();

        for (String originalUrl : keySet) {

            String editedUrl = hashMap.get(originalUrl);

            contentFile = contentFile.replace(originalUrl, editedUrl);

        }//for oldUrl
        
        return contentFile;

    }//viewforumPhp()
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void edit() throws IOException {
                
        File dir = new File(backupcc.file.Util.RAW_PAGES + "/teste");
        
        File[] listFiles = dir.listFiles(new backupcc.file.ForumPageFilter());
        
        for (File file: listFiles) {
             
            backupcc.tui.Tui.println("Editando " + file.getName());
            
            String contentFile = backupcc.file.Util.readTextFile(file);
            
            contentFile = indexPhp(contentFile);
            
            contentFile = viewtopicPhp(contentFile);
            
            contentFile = viewforumPhp(contentFile);
              
            File editedFile = 
                new File(backupcc.file.Util.FORUM_HOME + '/' + file.getName());
            
            backupcc.file.Util.writeTextFile(editedFile, contentFile);
            
        }//for file
        
    }//edit()
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void main(String[] args) throws IOException {
        
        edit();
        
    }
    
}//classe Edit
