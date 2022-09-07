package backupcc.datetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Metodos utilitarios para data e hora.
 * 
 * @author "Pedro Reis"
 * @since 1 de setembro de 2022
 * @version 1.0
 */
public class Util {
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna a data e hora atuais formatada como dd-MM-yyyy(HH:mm:ss)
     * 
     * @return Data e hora formatadas
     */
    public static String now() {
          LocalDateTime localDateTime = LocalDateTime.now();
  
        DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern("dd-MM-yyyy(HH.mm;ss,)");
        
        return 
            localDateTime.format(formatter).replace('.','h').replace(';','m').
                replace(',','s');

    }//now()
        
}//classe Util
