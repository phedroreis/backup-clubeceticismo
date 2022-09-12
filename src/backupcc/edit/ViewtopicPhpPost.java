package backupcc.edit;

import static backupcc.edit.EditableLink.START_REGEX;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author "Pedro Reis"
 * @since 9 de setembro de 2022
 * @version 1.0
 */
final class ViewtopicPhpPost extends EditableLink {
    
    private static final Pattern VIEWTOPIC_REGEX = 
        Pattern.compile("href=\"\\S*?(/viewtopic\\.php\\?\\S*?#p(\\d+))\"");
    
    private static final Pattern CANNONICAL_URL = 
        Pattern.compile("\\s*?<link rel=\"canonical\".+?t=(\\d+).*?\">\\s*");
    
    private static final String[] MSGS= {
        "Falha!",
        "Imposs\u00EDvel decodificar link para post\n",
        "Voc\u00EA pode tentar novamente ou pular este link\n",
        "Se pular, o link n\u00E3o ir\u00E1 funcionar na " +
        "c\u00F3pia est\u00E1tica,",
        "mas o problema ser\u00E1 provavelmente corrigido no pr\u00F3ximo" +
        " backup incremental"        
    };
    
    private static final String[] OPTIONS = {
        "Pular este link",
        "Tentar novamente"
    };
    
    private static final backupcc.tui.OptionBox FAIL_BOX = 
        new backupcc.tui.OptionBox(
            MSGS, 
            OPTIONS, 
            "pt", 
            backupcc.tui.Tui.YELLOW, 
            backupcc.tui.Tui.WHITE
        );
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    private static String getFilename(
        final String theUrl,
        final String post
    ) throws IOException {

        URL url = new URL(theUrl);
        
        try (
            BufferedReader br = 
                new BufferedReader(
                    new InputStreamReader(url.openStream()), 8192
                )
        ) {

            String line; 
     
            while ((line = br.readLine()) != null) {
                
                Matcher cannonicalMatcher = CANNONICAL_URL.matcher(line);

                if (cannonicalMatcher.find()) {
                    
                    StringBuilder sb = new StringBuilder(256);
                    
                    sb.append("/t=").append(cannonicalMatcher.group(1));
                    
                    Matcher startMatcher = START_REGEX.matcher(line);
                    
                    if (startMatcher.find()) 
                        sb.append('&').append(startMatcher.group());
                    
                    sb.append(".html#p").append(post);
                    
                    return sb.toString();
                }
            }

        }//try
        
        return null;
        
    }//getFilename()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public void map(Matcher matcher, HashMap<String, String> hashMap) {
        
        String original = matcher.group(1);
        
        String url = backupcc.net.Util.FORUM_URL + original;
        
        url = url.replace("amp;", "");
        
        String post = matcher.group(2);
        
        boolean retry = false;
        
        do {
            
            try {

                String filename = getFilename(url, post);

                hashMap.put(original, filename);

            } catch (IOException e) {
                
                backupcc.tui.Tui.println(" ");
                backupcc.tui.Tui.println(" ");
                
                backupcc.tui.Tui.printlnc(e.getMessage(), backupcc.tui.Tui.RED);
                
                backupcc.tui.Tui.println(" ");
                
                backupcc.tui.Tui.printlnc(
                    "LINK: " + original, 
                    backupcc.tui.Tui.RED
                );
                
                backupcc.tui.Tui.println(" ");

                retry = (FAIL_BOX.showBox() == 't');
            }
        }
        while (retry);        

    }//map()
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public Pattern getPattern() {
        
        return VIEWTOPIC_REGEX;
        
    }//getPattern()
    
        
}//classe ViewtopicPhpPost

