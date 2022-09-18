package backupcc.fetch;

import java.io.IOException;

/**
 * Métodos estáticos utilitários para as classes do pacote.
 * 
 * @author "Pedro Reis"
 * @since 1.0 (18 de setembro de 2022)
 * @version 1.0
 */
final class Util {
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public static void readTextFileExceptionHandler(final IOException e) {
        
        String[] msgs = {
            e.getMessage() + '\n'
        };
        
        backupcc.tui.OptionBox.abortBox(msgs);
        
    }//readTextFileExceptionHandler()
    
    
}//classe Util
