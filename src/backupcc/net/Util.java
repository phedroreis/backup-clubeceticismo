package backupcc.net;

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
 * @since 1.0 (21 de agosto de 2022)
 * @version 1.1
 */
public final class Util {
     
    /**
     * O protocolo utilizado na conexao com o servidor do forum.
     */
    public static final String FORUM_PROTOCOL = "https://";
    
    /**
     * O nome de dominio do forum.
     */
    public static final String FORUM_DOMAIN = "clubeceticismo.com.br";
    
    /**
     * A URL do forum
     */
    public static final String FORUM_URL = FORUM_PROTOCOL + FORUM_DOMAIN;   
     
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
     * @param name O nome (sem o caminho) do arquivo.
     * 
     * @param color A cor do texto com a qual o método vai escrever a mensagem
     * no terminal.
     * 
     */
    public static void downloadUrlToPathname(
        final String url, 
        final String pathname,
        final String name,
        final int color
    ) {
        
        if (name != null)
            backupcc.tui.Tui.printlnc("Obtendo " + name + " ...", color);
        
        URL theFile = null;
        try {
            
            theFile = new URL(url);
            
        } 
        catch (MalformedURLException e) {}
        
        boolean retry;
        
        do {
            retry = false;
            try (

                FileOutputStream fos = new FileOutputStream(pathname);

                @SuppressWarnings("null")
                ReadableByteChannel rbc = 
                    Channels.newChannel(theFile.openStream());

            ) {

                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
            catch (IOException e) {
                
                String[] msgs = {
                    e.getMessage() + "\n",
                    "Erro ao obter: " + pathname + "\n",
                    "Você pode desistir ou tentar novamente\n",
                    "Contudo, se pular um arquivo HTML, CSS ou JS, o backup " +
                    "ser\u00E1 abortado"
                };
                
                retry = backupcc.tui.OptionBox.retryBox(msgs);
                                
                if (!retry) {
                    
                    if (
                            (pathname.endsWith(".html")) ||
                            (pathname.endsWith(".js")) ||
                            (pathname.endsWith(".css"))
                    ) {
                        String[] abortMsgs = {
                            "Falha ao obter: " + pathname + "\n",
                            "C\u00F3digo fonte n\u00E3o pode ser descartado\n",
                            "Tente mais tarde realizar outro backup"
                        };
                        
                        backupcc.tui.OptionBox.abortBox(abortMsgs);
                        
                    } 
                    else {
                        
                        String[] warnMsgs = {
                            "Falha ao obter: " + pathname + "\n",
                            "O backup ainda pode prosseguir, mas a",
                            "aus\u00EAncia deste arquivo prejudicar a",
                            "visualiza\u00E3\u00E3o de algumas p\u00E1ginas"
                        };
                        
                        backupcc.tui.OptionBox.warningBox(warnMsgs);
                                    
                    }//if-else
                    
                }//if !retry    
                
            }//try-cache
            
        }while (retry);
       
    }//downloadUrlToPathname()   
        
}//classe Util