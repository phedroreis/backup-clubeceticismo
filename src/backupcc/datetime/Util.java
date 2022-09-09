package backupcc.datetime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Metodos utilitarios para data e hora.
 * 
 * @author "Pedro Reis"
 * @since 1 de setembro de 2022
 * @version 1.0
 */
public class Util {
    
    private final static Pattern DATE_TIME = 
        Pattern.compile(
            "(\\d{2})-(\\d{2})-(\\d{4})\\((\\d{2}h\\d{2}m\\d{2}s)"
        );
    
    private final static String[] MESES = {
        
        "janeiro", "fevereiro", "mar\u00E7o", "abril", "maio", "junho",
        "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"
    };
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna a data e hora atuais formatada como dd-MM-yyyy(HH'h'mm'm'ss's')
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
    
    public static String dateTime(final String datetime) {
        
        Matcher m = DATE_TIME.matcher(datetime);
                     
        if (m.find()) {
            
            int mth = Integer.valueOf(m.group(2)) - 1;
         
            return 
                m.group(1) + " de " + MESES[mth] + " de " + m.group(3) + 
                " \u00E0s " + m.group(4);
            
        }
        
        return null;
        
    }//dateTime()
            
}//classe Util