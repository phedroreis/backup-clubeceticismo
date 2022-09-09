package backupcc.edit;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author "Pedro Reis"
 * @since 6 de setembro de 2022
 * @version 1.0
 */
final class Root extends EditableLink {
    
    private final static Pattern ROOT_PATTERN = 
        Pattern.compile("href=\"" + FORUM_URL_STR + "/\\w");
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public void map(Matcher matcher, HashMap<String, String> hashMap) {
        
        hashMap.put(backupcc.net.Util.FORUM_URL + '/', "./");
        
    }//map()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public Pattern getPattern() {
        
        return ROOT_PATTERN;
       
    }//getPattern()   
        
}//classe Root
