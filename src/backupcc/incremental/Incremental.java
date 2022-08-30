package backupcc.incremental;

import static backupcc.file.Util.INCREMENTAL;
import static backupcc.file.Util.INCREMENTAL_DATA_BKP;
import static backupcc.tui.OptionBox.abortBox;
import static backupcc.tui.OptionBox.warningBox;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Um arquivo com o numero de posts de cada topico eh gravado ao fim de cada 
 * backup. No proximo backup, estes dados serao comparados com o numero de 
 * posts de cada  topico indicado nas paginas de Section do forum. Se entre um
 * backup e outro, um topico tiver recebido novos posts, as paginas destes 
 * novos posts (e apenas estas) serao baixadas.
 * 
 * Esta classe eh responsavel por ler e gravar o arquivo com o numero de posts
 * de cada topico alem de fornecer os metodos estaticos necessarios ao 
 * gerenciamento do backup incremental.
 * 
 * @author "Pedro Reis"
 * @since 27 de agosto de 2022
 * @version 1.0
 */
public final class Incremental {
    
    private static final String DELIMITER = ",";
    
    private static final char SEPARATOR = ' ';
   
    /*
    Nome do arquivo com a lista de numero de posts por topico.
    */
    private static final String POST_LIST_FILENAME =  "last-posts.dat";
    
    /*
    Arquivo com o hash de verificacao de POST_LIST_FILENAME
    */
    private static final String SHA256_FILENAME = POST_LIST_FILENAME + ".sha";
    
    /*
    O pathname do arquivo onde eh gravado o numero de posts de cada topico.
    */
    private static final String POST_LIST_PATHNAME =
        INCREMENTAL + '/' + POST_LIST_FILENAME;
    
    private static final File POST_LIST_FILE = new File(POST_LIST_PATHNAME);
    
    /*
    Path do arquivo com o hash de verificacao do arquivo de dados.
    */
    private static final String SHA256_PATHNAME =
        INCREMENTAL + '/' + SHA256_FILENAME;
    
    private static final File SHA256_FILE = new File(SHA256_PATHNAME);
    
    /*
    Uma lista com o numero de posts de cada topico ao fim do backup anterior.
    */    
    private static HashMap<Integer, Integer> previousLastPostsPerTopic;
    
    /*
    A lista sendo atualizada pelo backup que estiver executando no momento. 
    */
    private static StringBuilder updatedLastPostsList;
    
    private static boolean backupIncremental;
         
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Inicializa o processo incremental verificando se um backup previo jah
     * foi realizado. Se sim, le o arquivo e carrega os dados do ultimo backup
     * com o numero de posts em cada topico quando do backup anterior. Em caso 
     * contrario, se nao houve backup anterior, estes arquivos e o diretorio 
     * que o contem nao existem e serao entao criados.
     * 
     */
    public static void init() {
        
        String tryToGetPreviousPostsList = null;
        String sha256File = null;
        String sha256 = null;
        
        try {
            
            tryToGetPreviousPostsList = 
                backupcc.file.Util.readTextFile(POST_LIST_PATHNAME);
            
            sha256 = backupcc.security.Util.sha256(tryToGetPreviousPostsList);
            
            sha256File = backupcc.file.Util.readTextFile(SHA256_PATHNAME);
            
        }        
        catch (NoSuchFileException e) { }
        catch (IOException e) {
            
            String[] msgs = { e.getMessage() };
            
            abortBox(msgs);
                    
        }//try-catch
        
        if ( 
            (sha256 == null) || 
            (sha256File == null) || 
            (!sha256.equals(sha256File))
        ) {
            
            String[] msgs = {
                "Dados do backup anterior n\u00E3o existem ou est\u00E3o corrompidos\n",
                "Se continuar ser\u00E1 iniciado um \"full backup\"\n",
                "Ou pode abortar e restaurar o backup destes arquivos"
            };

            warningBox(msgs);
            
            /*
            Deleta arquivos corrompidos antes de fazer full backup
            */
            if (SHA256_FILE.exists()) SHA256_FILE.delete();
            if (POST_LIST_FILE.exists()) POST_LIST_FILE.delete();
             
            previousLastPostsPerTopic = null;//backup full
            
            backupIncremental = false;
            
        }
        else {//backup incremental
            
            previousLastPostsPerTopic = new HashMap<>();

            Pattern delimiters = Pattern.compile(DELIMITER);

            String[] split = delimiters.split(tryToGetPreviousPostsList);

            for (String keyValue: split) {

                int separatorPosition = keyValue.indexOf(SEPARATOR);
                
                if (separatorPosition == -1) break;

                String key = keyValue.substring(0, separatorPosition);

                String value = keyValue.substring(
                        separatorPosition + 1, keyValue.length()
                );

                previousLastPostsPerTopic.put(
                    Integer.valueOf(key),
                    Integer.valueOf(value)
                );

            }//for
            
            backupIncremental = true;
             
        }//if-else
                
        updatedLastPostsList = new StringBuilder(16384);        
               
    }//init()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Recebe a Id de um topico e retorna quantos posts havia neste topico 
     * quando foi realizado o ultimo backup.
     * 
     * @param topicId A id do topico.
     * 
     * @return Quantos posts havia neste topico no ultimo backup.
     */
    public static int lastPostOnPreviousBackup(final int topicId) {
        /*
        Nao existe backup anterior a este.
        */
        if (previousLastPostsPerTopic == null) return 0;
        
        Integer lastPost = previousLastPostsPerTopic.get(topicId);
        
        /*
        O topico ainda nao havia sido criado quando do backup anterior.
        */
        if (lastPost == null) return 0;
        
        return lastPost;
                
    }//lastPostOnPreviousBakcup()
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Insere na lista com o numero de posts de cada topico, o numero de posts
     * (postNumber) no topico de id = topicId.
     * 
     * @param topicId A Id do topico que tera seu numero de posts atualizado.
     * 
     * @param postNumber A indice do ultimo post neste topico ou o numero de
     * posts no topico, o que eh a mesma coisa.   
     */
    public static void updateLastPostNumberList(
        final int topicId, 
        final int postNumber
    ) {
        
        updatedLastPostsList.append(topicId).append(SEPARATOR).
            append(postNumber).append(DELIMITER);
        
    }//updateLastPostNumberList()
    
    /*[04]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Grava em disco a lista com o numero de posts em cada topico obtida no
     * backup corrente.
     * 
     * @throws IOException Em caso de erro de IO ao tentar gravar o arquivo.
     */
    public static void saveUpdatedLastPostsList() throws IOException {
        
        if (SHA256_FILE.exists()) 
            SHA256_FILE.renameTo(
                new File(INCREMENTAL_DATA_BKP + '/' + SHA256_FILENAME)
            );
        
        if (POST_LIST_FILE.exists()) 
            POST_LIST_FILE.renameTo(
                new File(INCREMENTAL_DATA_BKP + '/' + POST_LIST_FILENAME)
            );
        
        LocalDateTime localDateTime = LocalDateTime.now();
  
        DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern("dd-MM-yyyy(HH:mm:ss)");

        updatedLastPostsList.append(localDateTime.format(formatter));
        
        String postList = updatedLastPostsList.toString();
         
        backupcc.file.Util.writeTextFile(
            SHA256_PATHNAME, 
            backupcc.security.Util.sha256(postList)
        );
                
        backupcc.file.Util.writeTextFile(POST_LIST_PATHNAME, postList);
          
    }//saveUpdatedLastPostsList()  
    
    /*[05]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna se o backup sendo realizado eh incremental ou full.
     * 
     * @return true se for incremental ou false se nao.
     */
    public static boolean isIncremental() {
        
        return backupIncremental;
        
    }
       
}//classe Incremental
