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
public final class IndexPhp extends EditableLink {
    
    private static final Pattern INDEX_REGEX =
    Pattern.compile(
        "href=\"(" +
        FORUM_URL_STR + 
        "|[.]/index\\.php.*?)\""                  
    );
    
    private static final String INDEX_LINK = 
        "href=\"./" + backupcc.file.Util.INDEX_HTML + "\"";
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public void map(Matcher matcher, HashMap<String, String> hashMap) {
                        
        hashMap.put(matcher.group(), INDEX_LINK);

    }//map()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public Pattern getPattern() {
        
        return INDEX_REGEX;
        
    }//getPattern()
    
      
}//classe IndexPhp