package backupcc.incremental;

import static backupcc.file.Util.RAW_PAGES;
import static backupcc.file.Util.mkDirs;
import static backupcc.tui.OptionBox.abortBox;
import static backupcc.tui.OptionBox.warningBox;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
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
    /**
     * O nome do arquivo com a lista de todos os posts com os respectivos
     * arquivos ao qual pertencem.
     */
    private static final String ALLPOSTS_LIST_PATHNAME = "all-posts.dat";
   
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
        RAW_PAGES + '/' + POST_LIST_FILENAME;
    
    /*
    Path do arquivo com o hash de verificacao do arquivo de dados.
    */
    private static final String SHA256_PATHNAME =
        RAW_PAGES + '/' + SHA256_FILENAME;
    
    /*
    
    */
    private static final File RAW_PAGES_DIR = new File(RAW_PAGES);
    
    /*
    Uma lista com o numero de posts de cada topico ao fim do backup anterior.
    */    
    private static HashMap<Integer, Integer> previousLastPostsPerTopic;
    
    /*
    A lista sendo atualizada pelo backup que estiver executando no momento. 
    */
    private static StringBuilder updatedLastPostsList;
    
   /*
    Indica se o backup eh incremental ou full.
    */
    private static boolean backupIncremental;
    
    private static String lastBackupDatetime = null;
    
    private static final HashMap<Integer, int[]> POST_MAP = new HashMap<>();
    
    private static final List<String> POST_LIST = new LinkedList<>();
         
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Inicializa o processo incremental verificando se um backup previo jah
     * foi realizado. Se sim, le o arquivo e carrega os dados do ultimo backup
     * com o numero de posts em cada topico quando do backup anterior. Em caso 
     * contrario, se nao houve backup anterior, um full backup serah iniciado.
     * 
     */
    public static void init() {
        
        try {
            
            String allPosts =
                backupcc.file.Util.readTextFile(ALLPOSTS_LIST_PATHNAME);
            
            Scanner scanner = new Scanner(allPosts);
            
            while (scanner.hasNext()) {
                
               putFilenameOnMap(scanner.next());
               
            }
            
        }
        catch (IOException e) {
            
            String[] msgs = {
                e.toString() + "\n",
                "Erro ao ler o arquivo " + ALLPOSTS_LIST_PATHNAME + "\n",
                "O backup ainda pode prosseguir, mas o processo ser\u00E1 lento"
            };
                    
            backupcc.tui.OptionBox.warningBox(msgs);
            
        }//try-catch
        
        String tryToGetPreviousPostsList = null;
        String sha256File = null;
        String sha256 = null;
        
        try {
            
            tryToGetPreviousPostsList = 
                backupcc.file.Util.readTextFile(POST_LIST_PATHNAME);
            
            sha256 = backupcc.security.Util.sha256(tryToGetPreviousPostsList);
            
            sha256File = backupcc.file.Util.readTextFile(SHA256_PATHNAME);
            
        }        
        catch (NoSuchFileException e) {
            
            /*
            Se um dos arquivos nao puder ser lido e esta excexao for lancada,
            entao sha256 serah null ou sha256File serah null, ou ambos serao 
            null. Nesse caso o bloco "then" do proximo if serah executado.
            */
        }
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
                """
                Dados do backup anterior n\u00e3o existem ou est\u00e3o corrompidos
                """,
                "Se continuar ser\u00E1 iniciado um \"full backup\"\n",
                "Ou pode abortar e restaurar o backup destes arquivos"
            };

            warningBox(msgs);
                         
            previousLastPostsPerTopic = null;//backup full
            
            backupIncremental = false;
            
        }
        else {//backup incremental
             
            previousLastPostsPerTopic = new HashMap<>();

            Pattern delimiters = Pattern.compile(DELIMITER);

            String[] split = delimiters.split(tryToGetPreviousPostsList);

            for (String keyValue: split) {

                int separatorPosition = keyValue.indexOf(SEPARATOR);
                
                if (separatorPosition == -1) {//data do backup
                    
                    RAW_PAGES_DIR.renameTo(new File(RAW_PAGES + '-' + keyValue));
                    
                    lastBackupDatetime = keyValue;

                    try {

                        mkDirs(RAW_PAGES);
                    }
                    catch (IOException e) {

                        String[] msgs = {

                            "Imposs\u00EDvel criar diret\u00F3rio "  + RAW_PAGES

                        };

                        abortBox(msgs);                

                    }
                  
                }
                else {//ultimo post de um topico

                    String key = keyValue.substring(0, separatorPosition);

                    String value = keyValue.substring(
                        separatorPosition + 1, keyValue.length()
                    );

                    previousLastPostsPerTopic.put(
                        Integer.valueOf(key),
                        Integer.valueOf(value)
                    );
                    
                }//if-else

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
     * Acrescenta no arquivo com a lista de todos os posts (relacionados com o
     * arquivo em que estão publicados), os novos posts encontrados em um backup
     * full ou incremental.
     */
    private static void saveNewPostsList() throws IOException {
        
   
        try (    
                
            FileWriter writer = new FileWriter(ALLPOSTS_LIST_PATHNAME, true);
            BufferedWriter buffer = new BufferedWriter(writer);
        ) {
    
            for (String filename : POST_LIST) buffer.write(filename + '\n');
        }
   
    }//saveNewPostsList()
    
    /*[05]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * <p>Grava em disco a lista com o numero de posts em cada topico obtida no
     * backup corrente.</p>
     * 
     * <p>Também atualiza a lista de nomes de arquivos que
     * associa cada post ao arquivo em disco onde este post pode ser 
     * visualizado na cópia estática. Uma lista completa com estes nomes
     * de arquivos é necessária para agilizar o processo de edição de links
     * para posts. No entanto, obviamente, novos posts publicados desde o 
     * último backup não estão ainda presentes na lista, que portanto é
     * atualizada ao final do processo de backup</p>
     * 
     * <p>Este método deve ser chamado ao término do processo de backup, depois
     * que foi finalizada a edição de páginas que gera a cópia estática</p>
     *  
     * @throws IOException Em caso de erro de IO ao tentar gravar o arquivo.
     */
    public static void finish() throws IOException {
             
        updatedLastPostsList.append(backupcc.datetime.Util.now());
        
        String postList = updatedLastPostsList.toString();
        
        saveNewPostsList();
        
        backupcc.file.Util.writeTextFile(POST_LIST_PATHNAME, postList);
         
        backupcc.file.Util.writeTextFile(
            SHA256_PATHNAME, 
            backupcc.security.Util.sha256(postList)
        );
         
    }//finish()  
    
    /*[06]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna se o backup sendo realizado eh incremental ou full.
     * 
     * @return true se for incremental ou false se nao.
     */
    public static boolean isIncremental() {
        
        return backupIncremental;
        
    }//isIncremental()
    
    /*[07]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna a data hora do último backup ou se é full.
     * 
     * @return tData e hora do último backup.
     */
    public static String lastBackupDatetime() {
        
        if (lastBackupDatetime == null) return "Full backup.";
        
        return 
            "\u00DAltimo backup realizado em " +
            backupcc.datetime.Util.dateTime(lastBackupDatetime);
        
    }//lastBackupDatetime()
    
    private static final Pattern POST_MAP_PATTERN = 
        Pattern.compile("/t=(\\d+)(&start=(\\d+))?\\.html#p(\\d+)");
    
    /*[08]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * 
     * @param filename
     * @return 
     */
    public static boolean putFilenameOnMap(final String filename) {
        
        int postId;
        
        int[] topicStart = new int[2]; 
        
        Matcher matcher = POST_MAP_PATTERN.matcher(filename);
        
        if (matcher.find()) {
            
            postId = Integer.valueOf(matcher.group(4));
            topicStart[0] = Integer.valueOf(matcher.group(1));
            if (matcher.group(3) == null)
                topicStart[1] = 0;
            else
                topicStart[1] = Integer.valueOf(matcher.group(3));
            
        }
        else 
            throw new IllegalArgumentException(
                "Filename doesn't matches: " + filename
            );
        
        return (POST_MAP.put(postId, topicStart) == null);
        
    }//putFilenameOnMap()
    
    /*[09]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * 
     * @param postId
     * @return 
     */
    public static String getFilenameOnMap(final int postId) {
        
        int[] topicStart = POST_MAP.get(postId);
        
        if (topicStart == null) return null;
        
        String start = (topicStart[1] == 0) ? "" : ("&start=" + topicStart[1]);
        
        return "/t=" + topicStart[0] + start + ".html#p" + postId;
        
    }//getFilenameOnMap()
    
    /*[10]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Insere o nome de arquivo, juntamente com a referência para um post, na 
     * lista que cataloga novas referências para post encontradas no último 
     * backup incremental.
     * 
     * @param filename O nome do arquivo com uma referência para um post neste
     * arquivo.
     */
    public static void putFilenameOnList(final String filename) {
        
        POST_LIST.add(filename);
        
    }//putFilenameOnList()
    
      
}//classe Incremental
