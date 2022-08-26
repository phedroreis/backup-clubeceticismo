package backupcc.fetch;

import static backupcc.file.Util.mkDirs;
import backupcc.pages.UnexpectedHtmlFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author "Pedro Reis"
 * @since 25 de agosto de 2022
 * @version 1.0
 */
public final class FetchPages {
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     *  
     * @throws IOException
     * @throws FileNotFoundException
     * @throws UnexpectedHtmlFormatException 
     */
    public static void downloadPages() throws
        IOException, FileNotFoundException, UnexpectedHtmlFormatException {
        
        mkDirs();
        
        backupcc.pages.Main main = 
            new backupcc.pages.Main(
                backupcc.net.Util.FORUM_URL, "clubeceticismo.com.br.html"
            );
                         
        FetchSections sectionsPages = new FetchSections(main);
        sectionsPages.download();
        
        /*
        TreeSet<Section> sections = headersPages.getSections();
        
        for (Section section: sections) {
            System.out.println(section.getName());
        }*/
  
        
    }//mainPage()
    
    /*[--]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void main(String[] args) 
        throws 
            IOException,
            FileNotFoundException, 
            UnexpectedHtmlFormatException {
        
        downloadPages();
        
    }//main()
    
    
}//classe FetchPages
