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
public abstract class EditableLink {
    
    protected static final String FORUM_URL_STR = 
        backupcc.net.Util.FORUM_URL.replace(".", "[.]"); 
        
    protected static final Pattern START_REGEX =
        Pattern.compile("start=\\d+");
    
    private static HashMap<String, String> hashMap;
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public abstract void map(
        final Matcher matcher, 
        final HashMap<String, String> hashMap
    ); 
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public abstract Pattern getPattern();
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static String editFile(
        String contentFile,
        final EditableLink link
    ) {
        
        hashMap = new HashMap<>();
        
        Matcher matcher = link.getPattern().matcher(contentFile);
        
        while (matcher.find()) {
            
            link.map(matcher, hashMap);
            
        }//while
        
        Set<String> keySet = hashMap.keySet();

        for (String originalUrl : keySet) {

            String editedUrl = hashMap.get(originalUrl);

            contentFile = contentFile.replace(originalUrl, editedUrl);

        }//for originalUrl
        
        return contentFile;
        
    }//editFile()    
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void editFiles() throws IOException {
                
        File dir = new File(backupcc.file.Util.RAW_PAGES);
        
        File[] listFiles = dir.listFiles(new backupcc.file.ForumPageFilter());
        
        for (File file: listFiles) {
             
            backupcc.tui.Tui.println("Editando " + file.getName());
            
            String contentFile = backupcc.file.Util.readTextFile(file);
            
            contentFile = editFile(contentFile, new Root());
            
            contentFile = editFile(contentFile, new IndexPhp());
            
            contentFile = editFile(contentFile, new ViewtopicPhp());
            
            contentFile = editFile(contentFile, new ViewforumPhp());
            
            contentFile = editFile(contentFile, new AnyPhp());
              
            File editedFile = 
                new File(backupcc.file.Util.FORUM_HOME + '/' + file.getName());
            
            backupcc.file.Util.writeTextFile(editedFile, contentFile);
            
        }//for file
        
    }//editFiles()
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void main(String[] args) throws IOException {
        
        editFiles();
        
    }
    
}//classe EditableLink
