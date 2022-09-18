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
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    private static String getFilenameOnServer(final String url, final int post)
        throws IOException {
        
        try (
            BufferedReader br = 
                new BufferedReader(
                    new InputStreamReader(new URL(url).openStream()), 8192
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
                    
                    String filename = sb.toString();
                    
                    boolean putOk = 
                        backupcc.incremental.Incremental.putFilenameOnMap(
                            filename
                        );
                    
                    assert(putOk) : "This filename was already on map";
                                    
                    backupcc.incremental.Incremental.putFilenameOnList(
                        filename
                    );
                                       
                    return filename;
                    
                }//if
                
            }//while line

        }//try
        
        return null;   
        
    }//getFilenameOnServer()
       
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    private static String getFilename(final String url, final int post) 
        throws IOException {
        
        String filename = 
            backupcc.incremental.Incremental.getFilenameOnMap(post);
        
        if (filename != null) return filename;
        
        return getFilenameOnServer(url, post);    
        
    }//getFilename()
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public void map(Matcher matcher, HashMap<String, String> hashMap) {
        
        String original = matcher.group(1);
        
        String url = backupcc.net.Util.FORUM_URL + original;
        
        url = url.replace("amp;", "");
        
        int post = Integer.valueOf(matcher.group(2));
        
        boolean retry;
        
        do {
            
            retry = false;
            
            try {

                String filename = getFilename(url, post);
                
                assert(filename != null) : "Can't find the file of this post";

                hashMap.put(original, filename);

            } catch (IOException e) {
                                
                String[] msgs = {
                    e.getMessage(),
                    "LINK: " + original,
                    "Erro ao decodificar link para post\n",
                    "Voc\u00EA pode tentar novamente ou pular este link\n",
                    "Se pular, o link n\u00E3o ir\u00E1 funcionar na " +
                    "c\u00F3pia est\u00E1tica,",
                    "mas o problema ser\u00E1 provavelmente corrigido no" +
                    " pr\u00F3ximo backup incremental"        
                };                
                
                retry = backupcc.tui.OptionBox.retryBox(msgs);
            }
            
        }while (retry);        

    }//map()
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    @Override
    public Pattern getPattern() {
        
        return VIEWTOPIC_REGEX;
        
    }//getPattern()    
        
}//classe ViewtopicPhpPost

