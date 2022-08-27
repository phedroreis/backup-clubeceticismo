package backupcc.fetch;

import static backupcc.file.Util.readTextFile;
import static backupcc.net.Util.downloadUrl2Pathname;
import backupcc.pages.Header;
import backupcc.pages.Section;
import backupcc.pages.UnexpectedHtmlFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe encarregada de localizar (na pagina principal) o endereco das paginas
 * dos headers e disponibilizar estes enderecos atraves de um objeto TreeSet.
 * 
 * @author "Pedro Reis"
 * @since 23 de agosto de 2022
 * @version 1.0
 */
public final class FetchHeaders {
    
    private final backupcc.pages.Main main;
    
    private static final Pattern SECTION_FINDER = 
        backupcc.pages.Section.getFinder();
    
    private TreeSet<Header> headers;
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor da classe.
     * 
     * @param main Objeto com os dados da pagina principal do forum.
     */
    public FetchHeaders(final backupcc.pages.Main main) {
        
        this.main = main;
        
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /*
     * Obtem da pagina  principal a lista de todos os headers do forum e baixa
     * as paginas de HEADER.
     * 
     * @throws FileNotFoundException Se o caminho para o arquivo nao existir.
     * 
     * @throws IOException Se ocorrer algum erro de IO ao baixar ou na gravacao
     * do arquivo no disco local.
     * 
     * @throws backupcc.pages.UnexpectedHtmlFormatException
     */
    private void download() throws
        FileNotFoundException, IOException, UnexpectedHtmlFormatException {
        
        FetchMain mainPage = new FetchMain(main);
                      
        headers = mainPage.getHeaders();
        
        for (Header header: headers)    
            downloadUrl2Pathname(
                header.getAbsoluteURL(), 
                backupcc.file.Util.RAW_PAGES + '/' + header.getFilename()
            );
        
    }//download()
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Obtem os dados das paginas de secao a partir da lista de todas as paginas
     * de headers.
     * 
     * @return Um TreeSet com objetos Section com os dados de todas as paginas
     * de secao do forum.
     * 
     * @throws FileNotFoundException Se uma pagina do forum nao for encontrada.
     * Ou se um arquivo nao existir no disco.
     * 
     * @throws IOException Em caso de erro de IO.
     * 
     * @throws UnexpectedHtmlFormatException Se a regexp nao localizar um tipo
     * de dado que deveria constar em um determinado bloco de codigo HTML.
     */
    public TreeSet<Section> getSections()
        throws 
            FileNotFoundException,
            IOException, 
            UnexpectedHtmlFormatException {
        
        download();
        
        TreeSet<Section> sections = new TreeSet<>();
        
                
        for (Header header: headers) {
        
            String headerPage = 
                readTextFile(
                    backupcc.file.Util.RAW_PAGES + '/' + header.getFilename()
                );

            Matcher matcher = SECTION_FINDER.matcher(headerPage);
  
            while (matcher.find()) {
                
                Section section = new Section(matcher.group());
                sections.add(section);

            }//while
        
        }
               
        return sections;
        
    }//getSections()
    
    
}//classe FetchHeaders
