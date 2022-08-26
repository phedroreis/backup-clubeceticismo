package backupcc.net;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Metodos e campos estaticos relacionados com operacoes de conexao de internet.
 * 
 * @author "Pedro Reis"
 * @since 21 de agosto de 2022
 * @version 1.0
 */
public class Util {
     
    /**
     * O protocolo utilizado na conexao com o servidor do forum.
     */
    public static final String PROTOCOL = "https://";
    
    /**
     * O nome de dominio do forum.
     */
    public static final String DOMAIN = "clubeceticismo.com.br";
    
    /**
     * A URL do forum
     */
    public static final String FORUM_URL = PROTOCOL + DOMAIN;
     
    /*[01]---------------------------------------------------------------------

    -------------------------------------------------------------------------*/
    /**
     * Baixa um arquivo apontado por uma URL para um diretorio especificado que
     * jah deve indicar tambem o nome do arquivo que serah gravado.
     * 
     * @param url A url do arquivo a ser baixado.
     * 
     * @param pathname Caminho absoluto ou relativo para onde gravar o arquivo,
     * incluindo tambem o nome que serah dado ao arquivo baixado. Os diretorios
     * neste caminho devem existir ou uma excecao serah lancada. Se um arquivo 
     * com o nome indicado em pathname nao existir, serah criado um arquivo com
     * esse nome.
     * 
     * @throws java.net.MalformedURLException Caso a URL passada ao metodo seja
     * mal formada.
     * 
     * @throws java.io.FileNotFoundException Se o arquivo que se esta tentando
     * baixar nao existir no servidor ou se o pathname passado nao existir na
     * maquina cliente. Embora possa nao existir o arquivo com o nome indicado
     * para gravacao (neste caso um arquivo com este nome serah criado no 
     * diretorio indicado), os diretorios no caminho devem jah existir e nao 
     * serao criados por este metodo.
     * 
     * @throws java.io.IOException Em caso de erro de IO. 
     */
    public static void downloadUrl2Pathname(
        final String url, 
        final String pathname
    ) throws MalformedURLException, FileNotFoundException, IOException {
        
        try (FileOutputStream fos = new FileOutputStream(pathname)) {

            URL download = new URL(url);

            ReadableByteChannel rbc = 
                Channels.newChannel(download.openStream());

            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }

    }//downloadUrl2Pathname()
    
    
}//classe Util