package backupcc.fetch;

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
        
        backupcc.pages.Main main = 
            new backupcc.pages.Main(
                backupcc.net.Util.FORUM_URL, 
                backupcc.file.Util.INDEX_HTML
            );
                         
        FetchTopics topicsPages = new FetchTopics(main);
        
        topicsPages.download();
               
    }//downloadPages()
    
   /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/ 
    protected static void specialPrintln(
        final String s1, final String s2, final String s3, final int color
    ) {
        backupcc.tui.Tui.printc(s1, color);
        backupcc.tui.Tui.setDecoration(
            backupcc.tui.Tui.BOLD + backupcc.tui.Tui.UNDERLINE
        );
        backupcc.tui.Tui.print(s2);
        backupcc.tui.Tui.resetColorsAndDecorations();
        backupcc.tui.Tui.printlnc(s3, color);
    }//specialPrintln()
        
}//classe FetchPages
