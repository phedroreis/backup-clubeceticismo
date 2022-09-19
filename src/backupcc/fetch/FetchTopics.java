package backupcc.fetch;

import static backupcc.file.Util.readTextFile;
import backupcc.incremental.Incremental;
import backupcc.pages.Topic;
import backupcc.tui.ProgressBar;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe encarregada de baixar as paginas de topico.
 *
 * @author "Pedro Reis"
 * @since 1.0 (25 de agosto de 2022)
 * @version 1.1
 */
final class FetchTopics {
    /*
    Objeto com os dados da pagina principal do forum.
    */  
    private final backupcc.pages.Main main;
    
    private static final Pattern POST_FINDER = backupcc.pages.Post.getFinder();
    
    private static final int COLOR = backupcc.tui.Tui.GREEN;;
    
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
    private void showUpdatedInfo(
        final int updatedTopics, 
        final int totalOfNewPosts
    ) {
        
        String format = String.format(
            "%s%d t\u00F3pico%s com novas postagens desde " +
            "o \u00FAltimo backup",
            (updatedTopics > 0) ? "\n" : "",
            updatedTopics,
            ((updatedTopics == 1) ? "" : "s")
        );

        backupcc.tui.Tui.printlnc(format, COLOR);

        format = String.format(
            "%d post%s desde o \u00FAltimo backup",
            totalOfNewPosts,
            (totalOfNewPosts == 1) ? "" : "s"
        );

        backupcc.tui.Tui.printlnc(format, COLOR);

    }//showUpdatedInfo()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Baixa as paginas de tópicos.
     * 
     */
    private void downloadTopicsPages() {
        
        boolean isIncrementalBackup =
            (backupcc.incremental.Incremental.isIncremental());
        
        FetchSections sectionsPages = new FetchSections(main);
     
        topics = sectionsPages.getTopics();
           
        int total = topics.size(); 
        
        backupcc.tui.Tui.printlnc(
            "\n" + total + " t\u00F3picos p\u00FAblicos encontrados.\n", COLOR
        );
    
        backupcc.tui.Tui.printlnc(
            "Obtendo p\u00E1ginas de t\u00F3picos ...", COLOR
        );
         
        ProgressBar pBar = new ProgressBar(total, ProgressBar.LENGTH, COLOR);
        
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

                for (int i = firstPageToDownload-1; i<lastPageToDownload; i++) {
                    
                    String name = isIncrementalBackup ?
                        topic.getName() + " [" + (i+1) +"]" : null;
                                            
                    backupcc.net.Util.downloadUrlToPathname(
                        topic.getAbsoluteURL(i), 
                        backupcc.file.Util.RAW_PAGES + '/' + 
                        topic.getFilename(i),
                        name,
                        COLOR
                    );
                   
                }//for i
            
            }//if
            
            pBar.update(countTopics);
            
        }//for topic
        
        if (isIncrementalBackup) 
            showUpdatedInfo(updatedTopics, totalOfNewPosts);
        
    }//downloadTopicsPages()
    
    private static final Pattern TOPIC_FILENAME_PATTERN = 
        Pattern.compile("t=(\\d+)(&start=(\\d+))");
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    public void getPosts() {
        
        downloadTopicsPages();
        
        backupcc.tui.Tui.printlnc("\nColetando posts ...", COLOR);
        
        File rawPages = new File(backupcc.file.Util.RAW_PAGES);
        
        @SuppressWarnings("Convert2Lambda")
        File[] topicsFiles = rawPages.listFiles(
            new FilenameFilter(){
                @Override
                public boolean accept(File dir, String filename){
                    return filename.startsWith("t=");
                }
            }
        );
        
        String topicPage = null;
        
        int total = topicsFiles.length;
        
        int countTopics = 0;
              
        ProgressBar pBar = 
            new ProgressBar(
                total, 
                (total > ProgressBar.LENGTH) ? ProgressBar.LENGTH : total,
                COLOR
            );

        if (total > 0) pBar.show();
        
        
        for (File topicFile: topicsFiles) {

            try {

                topicPage = readTextFile(topicFile);
            }
            catch (IOException e) {

                backupcc.fetch.Util.readTextFileExceptionHandler(e);
            }

            Matcher matcher = POST_FINDER.matcher(topicPage);

            while (matcher.find()) {
                
                Matcher m = TOPIC_FILENAME_PATTERN.matcher(topicFile.getName());
                
                int topicId = -1;
                int start = 0;
                
                if (m.find()) {
                    
                    topicId = Integer.valueOf(m.group(1));
                    if (m.group(3) != null)
                        start = (Integer.valueOf(m.group(3)) / 50);
                    
                }

                backupcc.incremental.Incremental.putPostTopicPageOnMap(
                    String.format(
                        "%s:%d:%d", matcher.group(1), topicId, start
                    )
                );                    

            }//while
            
            pBar.update(++countTopics);

        }//for
        
    }//getPosts()

}//classe FetchTopics