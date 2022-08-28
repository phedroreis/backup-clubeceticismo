package backupcc.incremental;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author "Pedro Reis"
 * @since 27 de agosto de 2022
 * @version 1.0
 */
public final class Incremental {
    
    private static final String INCREMENTAL_DATA_DIR = 
        backupcc.file.Util.FORUM_HOME + "/incremental";
    
    private static final String LIST_OF_LAST_POSTNUMBERS_PER_TOPIC =
        INCREMENTAL_DATA_DIR + "/last-posts.dat";
    
    private static String previousLastPostsList;
    
    private static StringBuilder updatedLastPostsList;
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void initialize() throws IOException {
        
        String tryGetPreviousLastPostsList = null;
        
        try {
            
            tryGetPreviousLastPostsList = backupcc.file.Util.readTextFile(
                LIST_OF_LAST_POSTNUMBERS_PER_TOPIC
            );
            
        }        
        catch (NoSuchFileException | FileNotFoundException e) {
            
            backupcc.file.Util.mkDirs(INCREMENTAL_DATA_DIR);
                    
        }
        
        previousLastPostsList = tryGetPreviousLastPostsList;
                
        updatedLastPostsList = new StringBuilder(16384);
        
    }//initialize()
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static int lastPostOnPreviousBackup(final int topicId) {
        
        if (previousLastPostsList == null) return 0;//Nenhum backup ainda
        
        Pattern find = Pattern.compile("<" + topicId + " (\\d+)>");
        
        Matcher matcher = find.matcher(previousLastPostsList);
        
        if (matcher.find()) return Integer.valueOf(matcher.group(1));
        
        return 0;//Topico ainda inexistente quando do ultimo backup
    }
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void updateLastPostNumber(
        final int topicId, 
        final int postNumber
    ) {
        
        updatedLastPostsList.append('<').append(topicId).append(' ').
            append(postNumber).append('>');
    }
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void saveUpdatedLastPostsList() throws IOException {
        
        backupcc.file.Util.writeTextFile(
            LIST_OF_LAST_POSTNUMBERS_PER_TOPIC, 
            updatedLastPostsList.toString()
        );
        
    }  
    
    
}//classe Incremental
