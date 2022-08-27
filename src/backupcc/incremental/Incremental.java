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
    
    private static final String INC_DIR = 
        backupcc.file.Util.FORUM_HOME + "/incremental";
    
    private static final String LAST_POSTS_FILE = "last-posts.dat";
    
    private static final String LAST_POSTS_PATHNAME =
        INC_DIR + '/' + LAST_POSTS_FILE;
    
    private static String lastBackupLastPosts;
    
    private static StringBuilder thisBackupLastPosts;
    
    public static void initialize() throws IOException {
        
        String testFile = null;
        
        try {
            
            testFile = backupcc.file.Util.readTextFile(LAST_POSTS_PATHNAME);
            
        }
        
        catch (NoSuchFileException | FileNotFoundException e) {
            
            backupcc.file.Util.mkDirs(INC_DIR);
                    
        }
        
        lastBackupLastPosts = testFile;
                
        thisBackupLastPosts = new StringBuilder(16384);
        
    }//initialize()
    
    public static int retrieveLastPostNumber(final int topicId) {
        
        if (lastBackupLastPosts == null) return 0;//Nenhum backup ainda
        
        Pattern find = Pattern.compile("<" + topicId + " (\\d+)>");
        
        Matcher matcher = find.matcher(lastBackupLastPosts);
        
        if (matcher.find()) return Integer.valueOf(matcher.group(1));
        
        return 0;//Topico inexistente quando do ultimo backup
    }
    
    public static void updateLastPostNumber(final int topicId, final int postNumber) {
        
        thisBackupLastPosts.append('<').append(topicId).append(' ').
            append(postNumber).append('>');
    }
    
    public static void saveLastPostsFile() throws IOException {
        
        backupcc.file.Util.writeTextFile(
            LAST_POSTS_PATHNAME, 
            thisBackupLastPosts.toString()
        );
        
    }  
    
    
}//classe Incremental
