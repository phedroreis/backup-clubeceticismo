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
public final class Root extends EditableLink {
    
    private final static String ROOT_STR = "href=\"" + FORUM_URL_STR + "/";
    
    private final static Pattern ROOT_PATTERN = Pattern.compile(ROOT_STR);
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public void map(Matcher matcher, HashMap<String, String> hashMap) {
        
        hashMap.put(ROOT_STR, "./");
        
    }//map()
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public Pattern getPattern() {
        
        return ROOT_PATTERN;
       
    }//getPattern()   
    
}//classe Root
