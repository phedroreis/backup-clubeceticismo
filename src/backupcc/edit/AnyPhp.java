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
public final class AnyPhp extends EditableLink {
    
    private static final Pattern PHP = 
        Pattern.compile("href=\".*?(\"|\\.php.*?\")");

    @Override
    public void map(Matcher matcher, HashMap<String, String> hashMap) {
        
        if (matcher.group(1).equals("\"")) return;
        
        hashMap.put(matcher.group(), "href=\"warning.html\"");
    
    }

    @Override
    public Pattern getPattern() {
        
        return PHP;
    
    }
    
    
    
}//classe AnyPhp
