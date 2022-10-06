package backupcc.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Métodos e campos úteis para manipulação de arquivos e diretórios.
 * 
 * @author "Pedro Reis"
 * @since 1.0 (23 de agosto de 2022)
 * @version 1.0
 */
public final class Util {
   
    /**
     * Nome do arquivo com a pagina que deve informar que se trata de uma 
     * copia estatica do forum.
     */
    public static final String WARNING_FILENAME = "_warning.html";
    
    public static final String TOPICS_LIST_FILENAME = "_topics-list.html";
     
    /**
     * O diretorio que irah conter os arquivos e subdiretorios da copia 
     * estatica. Bem como arquivos de dados e log, e outros necessarios para a
     * construcao da copia estatica.
     */
    public static final String FORUM_HOME = 
        "./" + backupcc.net.Util.FORUM_DOMAIN; 
    
    public static final String INCREMENTAL = FORUM_HOME + "/incremental";
    
    /**
     * Para este diretorio serao baixados os arquivos com o codigo fonte das 
     * paginas do forum. Este diretorio soh ira conter arquivos nao editados.
     */
    public static final String RAW_PAGES = INCREMENTAL + "/raw-pages";
    
    /**
     * Nome do arquivo da pagina principal do forum na copia estatica.
     */
    public static final String INDEX_HTML = 
        backupcc.net.Util.FORUM_DOMAIN + ".html";
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Cria um diretorio se este ainda nao existir.Se necessario irah criar
     * tambem todos os subdiretorios do caminho.
     * 
     * @param path O caminho com os diretorios a serem criados.
     */
    public static void mkDirs(final String path) {
        
        File dir = new File(path);
        
        if (!dir.exists() && !dir.mkdirs()) {
            
            String[] msgs = {
               "Falha ao criar diret\u00F3rio:\n",
                path
            };
            
            backupcc.tui.OptionBox.abortBox(msgs);
        }
    
    }//mkDirs()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Le um arquivo de texto para dentro de um objeto String. O arquivo deve 
     * estar codificado em UTF8.
     * 
     * @param file O arquivo.
     * 
     * @return A String com o conteudo do arquivo.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public static String readTextFile(final File file) throws IOException {
        
        return 
            new String(
                Files.readAllBytes(file.toPath()), 
                StandardCharsets.UTF_8
            );
       
    }//readTextFile()
    
    /*[03]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Le um arquivo de texto para dentro de um objeto String. O arquivo deve 
     * estar codificado em UTF8.
     * 
     * @param file O arquivo.
     * 
     * @return A String com o conteudo do arquivo.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public static String readTextFile(final String file) throws IOException {
        
        return readTextFile(new File(file));
       
    }//readTextFile()
    
     /*[04]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Escreve o conteudo de uma String em um arquivo texto. Se o arquivo jah
     * existir seu conteudo serah substituido por esta String, e se nao existir
     * serah criado. A String deve ser UTF0.
     * 
     * @param file O arquivo.
     * 
     * @param content A String codificada em UTF-8.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public static void writeTextFile(final File file, final String content)
        throws IOException {
        
        try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
            
            fw.write(content);
            
        }
  
    }//writeTextFile()
    
    /*[05]---------------------------------------------------------------------
    
    -------------------------------------------------------------------------*/
    /**
     * Escreve o conteudo de uma String em um arquivo texto. Se o arquivo jah
     * existir seu conteudo serah substituido por esta String, e se nao existir
     * o arquivo serah criado. A String deve ser UTF8.
     * 
     * @param filename O arquivo.
     * 
     * @param content A String codificada em UTF-8.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public static void writeTextFile(
        final String filename,
        final String content
    )
        throws IOException {
        
        writeTextFile(new File(filename), content);
        
    }//writeTextFile()
    
    /*[06]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Cria o arquivo com a pagina informando se tratar de uma copia estatica do
     * forum.
     * 
     */
    public static void createWarningFile() {
        
        Path warningFileSource = new File("./" + WARNING_FILENAME).toPath();
        
        try {
            
            Files.copy(
                warningFileSource,
                new File(FORUM_HOME + '/' + WARNING_FILENAME).toPath()
            );

        }
        catch (IOException e) {


            String[] msgs = {

                e.getMessage() + '\n',
                "Imposs\u00EDvel criar arquivo : " + WARNING_FILENAME
            };

            backupcc.tui.OptionBox.abortBox(msgs);

        }//try-catch

        
    }//createWarningFile()
    
    /*[07]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Grava um arquivo com os nomes de todos os tópicos do fórum.
     * 
     * @param htmlBody Uma lista com links para todos os tópicos.
     */
    public static void createTopicsListFile(final String htmlBody) {
        
        String htmlHead = "<!DOCTYPE html>\n<html>\n<head>\n" +
            "<meta charset=\"utf-8\" />\n" +
            "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "<style>\nli:nth-child(odd) { background:#F2F3; }\n</style>\n" +
            "</head>\n<body>\n" +
            "<h2>T\u00D3picos de acesso p\u00FAblico " +
            "(por ordem de publica\u00E7\u00E3o)</h2><ul>\n";
                
        File topicsListFile = new File(RAW_PAGES + '/' + TOPICS_LIST_FILENAME);
       
        try {

            writeTextFile(
                topicsListFile, 
                htmlHead + htmlBody + "\n</ul>\n</body>\n</html>"
            );
        }
        catch (IOException e) {


            String[] msgs = {

                e.getMessage() + '\n',
                "Imposs\u00EDvel criar arquivo : " + TOPICS_LIST_FILENAME
            };

            backupcc.tui.OptionBox.warningBox(msgs);

        }//try-catch
        
    }//createTopicsListFile()
    
}//classe Util
