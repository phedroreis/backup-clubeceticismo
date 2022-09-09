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
final class FetchTopics {
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
         
        int total = topics.size(); 
        
        backupcc.tui.Tui.println(" ");   
        backupcc.tui.Tui.printlnc(
            total + " t\u00F3picos p\u00FAblicos encontrados.", color);
        backupcc.tui.Tui.println(" "); 
        backupcc.tui.Tui.printlnc(
            "Obtendo p\u00E1ginas de t\u00F3picos ...", color
        );
         
        ProgressBar pBar = 
            new ProgressBar(total, backupcc.tui.ProgressBar.LENGTH, color);
        
        int countTopics = 0;
        
        int updatedTopics = 0;
        
        int totalOfNewPosts = 0;
        
        if (!backupcc.incremental.Incremental.isIncremental()) 
            pBar.show();        
        else
            backupcc.tui.Tui.println(" ");
         
        for (Topic topic: topics) {
            
            countTopics++;
            
            int lastPostNumberOnPreviousBackup = 
                Incremental.lastPostOnPreviousBackup(topic.getId());
            
            int newPostsOnThisTopic = 
                topic.getNumberOfPosts() - lastPostNumberOnPreviousBackup;
            
            if (newPostsOnThisTopic > 0) {
                
                updatedTopics++;
                
                totalOfNewPosts += newPostsOnThisTopic;
            
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
        
        if (backupcc.incremental.Incremental.isIncremental()) {
            
            if (updatedTopics > 0) backupcc.tui.Tui.println(" ");
            
            String topico = " t\u00F3pico" + ((updatedTopics == 1) ? "" : "s");
            
            backupcc.tui.Tui.printlnc(
                updatedTopics + 
                topico + " com novas postagens desde o \u00FAltimo backup.",
                color
            );
            
            String post = " post" + ((totalOfNewPosts == 1) ? "" : "s");
            
            backupcc.tui.Tui.printlnc(
                totalOfNewPosts + post + " desde o \u00FAltimo backup.",
                color
            );
            
        }//if
        
    }//download()


}//classe FetchTopics

