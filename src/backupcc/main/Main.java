package backupcc.main;

import static backupcc.fetch.FetchPages.downloadPages;
import backupcc.pages.UnexpectedHtmlFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Ponto de entrada para execucao da aplicacao.
 * 
 * @since 14 de agosto de 2022
 * @author "Pedro Reis"
 * @version 1.0
 */
public class Main {
    
    /*[--]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void main(String[] args) 
        throws 
            IOException,
            FileNotFoundException, 
            UnexpectedHtmlFormatException {
        
        downloadPages();
        
    }//main()
    
}//classe Main
