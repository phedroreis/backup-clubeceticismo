package backupcc.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Metodos e campos uteis para manipulacao de arquivos e diretorios.
 * 
 * @author "Pedro Reis"
 * @since 23 de agosto de 2022
 * @version 1.0
 */
public final class Util {
     
    /**
     * O diretorio que irah conter os arquivos e subdiretorios da copia 
     * estatica. Bem como arquivos de dados e log, e outros necessarios para a
     * construcao da copia estatica.
     */
    public static final String FORUM_HOME = "./clubeceticismo.com.br";
    
    /**
     * Para este diretorio serao baixados os arquivos com o codigo fonte das 
     * paginas do forum. Este diretorio soh ira conter arquivos nao editados.
     */
    public static final String RAW_PAGES = FORUM_HOME + "/raw-pages";
    
    /**
     * Diretorio para gravar os arquivos de log e dados.
     */
    public static final String LOG_DIR = FORUM_HOME + "/log";
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Cria um diretorio se este ainda nao existir.Se necessario irah criar
     * tambem todos os subdiretorios do caminho.
     * 
     * @param path O caminho com os diretorios a serem criados.
     * 
     * @throws IOException Se o sistema nao permitir criar o diretorio.
     */
    public static void mkDirs(final String path) throws IOException {
        
        File dir = new File(path);
        
        if (!dir.exists() && !dir.mkdirs())
                
            throw new IOException("Can't create directory: " + path);
    
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
     * @throws FileNotFoundException Se o arquivo nao existir.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public static String readTextFile(final File file) throws
        FileNotFoundException, IOException {
        
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
     * @throws FileNotFoundException Se o arquivo nao existir.
     * 
     * @throws IOException Em caso de erro de IO.
     */
    public static String readTextFile(final String file) throws
        FileNotFoundException, IOException {
        
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
    
}//classe Util
