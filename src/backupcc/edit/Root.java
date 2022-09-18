package backupcc.edit;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Quando o nome de domínio em um link apontar para o domínio do fórum, este 
 * nome de domínio será substituído por ./ na cópia estática.
 * 
 * @author "Pedro Reis"
 * @since 1.0 (6 de setembro de 2022)
 * @version 1.0
 */
final class Root extends EditableLink {
    /**
     * Regex para localizar o domínio do fórum em links.
     */
    private final static Pattern ROOT_PATTERN = 
        Pattern.compile("href=\"" + FORUM_URL_REGEX_STYLE + "/\\w");
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Mapeia o domínio do fórum para ./
     * 
     * @param matcher Logo após find() ter retornado true.
     * 
     * @param hashMap Mapeia link original com string de substituição.
     */
    @Override
    public void map(Matcher matcher, HashMap<String, String> hashMap) {
        
        hashMap.put(backupcc.net.Util.FORUM_URL + '/', "./");
        
    }//map()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * A regex que localiza domínios do fórum em links.
     * 
     * @return Regex para localizar domínio do fórum em links.
     */
    @Override
    public Pattern getPattern() {
        
        return ROOT_PATTERN;
       
    }//getPattern()   
        
}//classe Root
