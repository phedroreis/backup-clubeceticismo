package backupcc.fetch;

import static backupcc.fetch.FetchPages.specialPrintln;
import static backupcc.file.Util.readTextFile;
import backupcc.incremental.Incremental;
import static backupcc.net.Util.downloadUrl2Pathname;
import backupcc.pages.Section;
import backupcc.pages.Topic;
import backupcc.pages.UnexpectedHtmlFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe encarregada de localizar (nas paginas dos headers) o endereco das 
 * paginas das secoes e disponibilizar estes enderecos atraves de um objeto 
 * TreeSet.
 *
 * @author "Pedro Reis"
 * @since 25 de agosto de 2022
 * @version 1.0
 */
public final class FetchSections {
    /*
    Objeto com os dados da pagina principal do forum.
    */  
    private final backupcc.pages.Main main;
    
    private final int color;
    
    private static final Pattern TOPIC_FINDER = 
        backupcc.pages.Topic.getFinder();
      
    private TreeSet<Section> sections;
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor da classe.
     * 
     * @param main Objeto que deve conter os dados da pagina principal do forum.
     */
    public FetchSections(final backupcc.pages.Main main) {
        
        this.main = main;
        color = backupcc.tui.Tui.BLUE;
        
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /*
     * Baixa as paginas de secao.
     * 
     * @throws FileNotFoundException Se o caminho para o arquivo nao existir.
     * 
     * @throws IOException Se ocorrer algum erro de IO ao baixar ou na gravacao
     * do arquivo no disco local.
     * 
     * @throws backupcc.pages.UnexpectedHtmlFormatException
     */
    private void download() throws
        FileNotFoundException, IOException, UnexpectedHtmlFormatException {
        
        FetchHeaders headersPages = new FetchHeaders(main);
        
        sections = headersPages.getSections();
        
        int total = sections.size();
        
        int count = 0;
         
        for (Section section: sections) {
            
            count++;
            
            for (int i = 0; i < section.getNumberOfPages(); i++) {
         
                downloadUrl2Pathname(
                    section.getAbsoluteURL(i), 
                    backupcc.file.Util.RAW_PAGES + '/' + section.getFilename(i),
                    section.getName() + " [" + (i + 1) + "]",
                    color,
                    count * 100 / total
                );
            }//for i
            
        }//for section
        
    }//download()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * 
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws UnexpectedHtmlFormatException 
     */
    public TreeSet<Topic> getTopics()
        throws 
            FileNotFoundException,
            IOException, 
            UnexpectedHtmlFormatException {
        
        download();
        
        TreeSet<Topic> topics = new TreeSet<>();
                  
        for (Section section: sections) {
    
            for (int i = 0; i < section.getNumberOfPages(); i++) {

                String sectionPage = readTextFile(
                    backupcc.file.Util.RAW_PAGES + '/' + section.getFilename(i)
                );

                Matcher matcher = TOPIC_FINDER.matcher(sectionPage);
                
                specialPrintln(
                    "Coletando dados de ", 
                    "t\u00F3picos",
                    " em " + section.getName() + "  [" + (i+1) + "] ...", 
                    color
                );                
                
                while (matcher.find()) {
   
                    Topic topic = new Topic(matcher.group());
                    
                    Incremental.updateLastPostNumberList(
                        topic.getId(), 
                        topic.getNumberOfPosts()
                    );
                    
                    topics.add(topic);

                }//while
    
            }//for i
        
        }//for section
               
        return topics;
        
    }//getTopics()

}//classe FetchSections
