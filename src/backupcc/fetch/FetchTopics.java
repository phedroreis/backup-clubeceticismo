package backupcc.fetch;

import backupcc.incremental.Incremental;
import static backupcc.net.Util.downloadUrl2Pathname;
import backupcc.pages.Topic;
import backupcc.pages.UnexpectedHtmlFormatException;
import backupcc.tui.ProgressBar;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeSet;

/**
 * Classe encarregada de baixar as paginas de topico.
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
    
    private final int color;
    
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
        color = backupcc.tui.Tui.GREEN;
        
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
        
        backupcc.tui.Tui.println(" ");
        backupcc.tui.Tui.printlnc(
            "Obtendo p\u00E1ginas de t\u00F3picos ...", 
            color
        );
        
        int total = topics.size();
         
        ProgressBar pBar = 
            new ProgressBar(total, backupcc.tui.ProgressBar.LENGTH, color);
        
        int countTopics = 0;
        
        if (!backupcc.incremental.Incremental.isIncremental()) 
            pBar.show();        
        else
            backupcc.tui.Tui.println(" ");
         
        for (Topic topic: topics) {
            
            countTopics++;
            
            int lastPostNumberOnPreviousBackup = 
                Incremental.lastPostOnPreviousBackup(topic.getId());
            
            if (topic.getNumberOfPosts() > lastPostNumberOnPreviousBackup) {
            
                int firstPageToDownload = Topic.getPageNumberOfThisPost(
                    lastPostNumberOnPreviousBackup + 1
                );
                
                int lastPageToDownload = topic.getNumberOfPages();

                for (
                    int i = firstPageToDownload - 1; i < lastPageToDownload; i++
                ) {
                    
                    if (backupcc.incremental.Incremental.isIncremental()) {
                                            
                        downloadUrl2Pathname(
                            topic.getAbsoluteURL(i), 
                            backupcc.file.Util.RAW_PAGES + '/' + 
                            topic.getFilename(i),
                            topic.getName() + " [" + (i+1) +"]",
                            color,
                            countTopics * 100 / total
                        );
                    }
                    else {
                    
                        downloadUrl2Pathname(
                            topic.getAbsoluteURL(i), 
                            backupcc.file.Util.RAW_PAGES + '/' + 
                            topic.getFilename(i)
                        );
                        
                    }//if-else
                    
                }//for i
            
            }//if
            
            pBar.update(countTopics);
            
        }//for topic
        
    }//download()


}//classe FetchTopics

