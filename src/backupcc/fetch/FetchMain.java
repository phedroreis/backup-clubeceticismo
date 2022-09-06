package backupcc.fetch;

import static backupcc.fetch.FetchPages.specialPrintln;
import static backupcc.file.Util.readTextFile;
import static backupcc.net.Util.downloadUrl2Pathname;
import backupcc.pages.Header;
import backupcc.pages.UnexpectedHtmlFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe encarrega de baixar a página principal do forum, localizar (nesta
 * pagina) o endereco das paginas dos headers e disponibilizar estes 
 * enderecos atraves de um objeto TreeSet.
 * 
 * @author "Pedro Reis"
 * @since 23 de agosto de 2022
 * @version 1.0
 */
public final class FetchMain {
    
    private final backupcc.pages.Main main;
    
    private final int color;
    
    private static final Pattern HEADER_FINDER = 
        backupcc.pages.Header.getFinder();
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor da classe.
     * 
     * @param main Um objeto Main com os dados da pagina principal do forum.
     */
    public FetchMain(final backupcc.pages.Main main) {
        
        this.main = main;
        color = backupcc.tui.Tui.MAGENTA;
           
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/    
    /*
     * Baixa a pagina principal do forum para o diretorio onde todas as paginas
     * brutas (arquivos HTML nao editados pelo programa) serao gravadas.
     * 
     * @throws FileNotFoundException Se o caminho para o arquivo nao existir.
     * 
     * @throws IOException Se ocorrer algum erro de IO ao baixar ou na gravacao
     * do arquivo no disco local.
     */
    private void download() throws FileNotFoundException, IOException {
            
        downloadUrl2Pathname(
            main.getAbsoluteURL(),
            backupcc.file.Util.RAW_PAGES + '/' + main.getFilename(),
            main.getName(),
            color,
            100
        );
        
    }//download()
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Este metodo coleta dados (na pagina principal do forum) sobre as paginas
     * de HEADER do forum. Para cada HEADER apontado na pagina principal, este 
     * metodo ira criar um objeto backupcc.pages.Header com os dados deste 
     * header. No final, um TreeSet contendo todos estes objetos serah retornado
     * pelo metodo.
     * 
     * Uma pagina de HEADER eh aquela que aponta para paginas de secao do forum.
     *  
     * @return Um TreeSet (ordenado por ID do Header) com todos os Headers
     * apontados na pagina principal.
     * 
     * @throws FileNotFoundException Se a pagina principal nao for localizada.
     * 
     * @throws IOException Em caso de erro de IO.
     * 
     * @throws UnexpectedHtmlFormatException Se a regexp nao localizar os dados
     * de um HEADER no bloco HTML da pagina principal como seria de se esperar.
     * Sinalizando um bug no codigo deste programa ou que o padrao HTML das 
     * paginas do forum foi alterado.
     */
    public TreeSet<Header> getHeaders()
        throws 
            FileNotFoundException,
            IOException, 
            UnexpectedHtmlFormatException {
        
        download();
        
        String mainPage = readTextFile(
            backupcc.file.Util.RAW_PAGES + '/' + main.getFilename()
        );
        
        TreeSet <Header> headers; headers = new TreeSet<>();
        
        Matcher matcher = HEADER_FINDER.matcher(mainPage);
        
        specialPrintln(
            "Coletando dados de ", 
            "cabe\u00E7alhos",
            " na " + main.getName() + " ...", 
            color
        );
        
        while (matcher.find()) {
            
            Header header = new Header(matcher.group());
            headers.add(header);
            
        }//while
                
        return headers;
        
    }//getHeaders()
    
   
}//classe FetchMain
