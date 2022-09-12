package backupcc.edit;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author "Pedro Reis"
 * @since 6 de setembro de 2022
 * @version 1,0
 */
final class ViewtopicPhp extends EditableLink {
    
    private static final Pattern VIEWTOPIC_REGEX = 
        Pattern.compile("href=\"\\S*?(/viewtopic\\.php\\?.*?(t=\\d+).*?\")");

    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public void map(Matcher matcher, HashMap<String, String> hashMap) {
        
        String original = matcher.group(1);
        
        String edited = '/' + matcher.group(2);
            
        Matcher start = START_REGEX.matcher(original);

        if (start.find()) edited += '&' + start.group();

        edited += ".html\""; 
        
        hashMap.put(original, edited);

    }//map()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public Pattern getPattern() {
        
        return VIEWTOPIC_REGEX;
        
    }//getPattern()
        
}//classe ViewtopicPhp
