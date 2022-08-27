package backupcc.fetch;

import static backupcc.net.Util.downloadUrl2Pathname;
import backupcc.pages.Topic;
import backupcc.pages.UnexpectedHtmlFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeSet;

/**
 * Classe encarregada de localizar (nas paginas dos headers) o endereco das 
 * paginas das secoes e disponibilizar estes enderecos atraves de um objeto 
 * TreeSet.
 *
 * @author "Pedro Reis"
 * @since 25 de agosto de 2022
 * @version 1.0
 */
public final class FetchTopics {
    /*
    Objeto com os dados da pagina principal do forum.
    */  
    private final backupcc.pages.Main main;
    
    private TreeSet<Topic> topics;
        
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor da classe.
     * 
     * @param main Objeto que deve conter os dados da pagina principal do forum.
     */
    public FetchTopics(final backupcc.pages.Main main) {
        
        this.main = main;
        
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
    public void download() throws
        FileNotFoundException, IOException, UnexpectedHtmlFormatException {
        
        FetchSections sectionsPages = new FetchSections(main);
        
        topics = sectionsPages.getTopics();
         
        for (Topic topic: topics)
            
            for (int i = 0; i < topic.getNumberOfPages(); i++) {
         
                downloadUrl2Pathname(
                    topic.getAbsoluteURL(i), 
                    backupcc.file.Util.RAW_PAGES + '/' + topic.getFilename(i)
                );
            }//for i
        
    }//download()


}//classe FetchTopics

