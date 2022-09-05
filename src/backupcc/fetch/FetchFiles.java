package backupcc.fetch;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author "Pedro Reis"
 * @since 3 de setembro 2022
 * @version 1.0
 */
public final class FetchFiles {
    
    private static final int COLOR = backupcc.tui.Tui.CYAN;
    
    private static final Pattern HTML_SEARCH = 
        Pattern.compile("(href|src|data-src-hd)=\"(.+?)\"");
    
    private static final Pattern CSS_SEARCH =
        Pattern.compile("url\\(['\"](.+?)['\"]");
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    private static void searchInFile(
        final File file,
        final Pattern pattern,
        final int group,
        final String serverPath
    ) throws IOException {
        
        String forumUrlPrefix = backupcc.net.Util.FORUM_URL + '/'; 

        String contentFile = backupcc.file.Util.readTextFile(file);

        Matcher matcher = pattern.matcher(contentFile);

        while (matcher.find()) {

            String url =  matcher.group(group);

            if (
                (url.matches(".+?\\.php.*")) &&
                (!url.matches(".+?file\\.php[/?]avatar=.*"))
            ) 
                continue;

            if (url.startsWith(".") || url.startsWith(forumUrlPrefix)) {
                
                backupcc.net.URL urlInfo =
                    new backupcc.net.URL(serverPath + url);
                
                String localPathname = urlInfo.getLocalPathname();
                String localPath = urlInfo.getLocalPath();
                String localFilename = urlInfo.getLocalFilename(); 
                
                File f = new File(localPathname);

                if (!f.exists()) {

                    backupcc.file.Util.mkDirs(localPath);

                    backupcc.net.Util.downloadUrl2Pathname(
                        urlInfo.getAbsoluteUrl(), 
                        localPathname, 
                        localFilename, 
                        COLOR
                    );

                    if (localFilename.endsWith(".css")) {
                        
                        searchInFile(
                            new File(localPathname),
                            CSS_SEARCH,
                            1,
                            urlInfo.getServerPath() + '/'
                        );
                        
                    }
                       
                    
                }//if

            }//if

        }//while

    }//searchInFile()
       
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/    
    public static void fetchFiles() throws IOException {
        
        File dir = new File(backupcc.file.Util.RAW_PAGES);
        
        File[] listFiles = dir.listFiles(new ForumPageFilter());
        
        backupcc.tui.Tui.println(" ");
        backupcc.tui.Tui.printlnc("Obtendo os arquivos do servidor ...", COLOR);
        backupcc.tui.Tui.println(" ");
        
        for (File file: listFiles) {
            
            searchInFile(file, HTML_SEARCH, 2, ""); 
            
        }//for file
        
    }//fetch()
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void main(String[] args) throws IOException {

        fetchFiles();
    }
    
}//classe FetchFiles
