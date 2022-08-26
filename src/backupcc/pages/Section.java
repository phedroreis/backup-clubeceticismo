package backupcc.pages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Um objeto desta classe coleta e retorna informacoes sobre uma pagina de 
 * secao do forum. Estes dados sao obtidos das paginas de Header.
 * 
 * Um trecho de codigo HTML, contendo dados de uma secao, deve ser enviado como
 * argumento ao construtor da classe. E deste bloco de codigo o construtor ira
 * obter a ID, NAME, FILENAME e URL ABSOLUTE desta section.
 * 
 * Portanto um metodo deverah varrer o arquivo fonte de cada pagina de header em 
 * busca destes blocos de codigo, criando objetos desta classe. Um para cada
 * secao apontada na pagina de header que encontrar.
 *
 * @author "Pedro Reis"
 * @since 24 de agosto de 2022
 * @version 1.0
 */
public final class Section extends Page {
    
    /*
     * Regexp para localizar os dados de uma Section na pagina do Header ao qual
     * essa Section pertence. Estes dados estao sempre inseridos no escopo de
     * uma tag a localizavel por este Pattern.
     */
    private static final Pattern FINDER_REGEXP = 
        Pattern.compile(
            "<a href=\"[.]/viewforum[.]php[?]f=.+?" +
            "class=\"forumtitle\" data-id=\"(\\d+)\">(.+)</a>"
        ); 
 
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor da classe extrai os dados de uma Section a partir de um bloco
     * de codigo contido no codigo fonte de uma pagina de Header do forum.
     * 
     * @param htmlBlock O bloco de codigo HTML da pagina Header do forum de
     * onde se pode extrair dados de uma Section.
     *   
     * @throws backupcc.pages.UnexpectedHtmlFormatException No caso da
     * regexp nao conseguir fazer o parse desse bloco de codigo. Sinalizando um
     * bug do programa ou que o padrao das paginas do forum foi alterado.
     */
    public Section(final String htmlBlock) throws UnexpectedHtmlFormatException{
         
        /*
        Localiza a id e o nome da Section no htmlBlock, que por sua vez foi 
        obtido de uma pagina de Header do forum com a regexp FINDER_REGEXP.
        */
        Matcher matcher = FINDER_REGEXP.matcher(htmlBlock);
        
        if (matcher.find()) {
            
            /* Estes campos sao declarados na super classe Page */
            id = Integer.valueOf(matcher.group(1));
            filename = "f=" + id + ".html";
            name = matcher.group(2);
            absoluteURL = 
                backupcc.net.Util.FORUM_URL + "/viewforum.php?f=" + id;
        }
        /*
        Se os dados do bloco if nao puderam ser localizados, ha um bug no
        programa ou o padrao do HTML das paginas do forum foi alterado desde que
        este codigo foi escrito.
        */
        else throw new UnexpectedHtmlFormatException(
            "Can't parse HTML to find section data"
        );
         
    }//Header()
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna um objeto Pattern com a regexp que permite localizar blocos de
     * codigo HTML (em uma pagina de Header) de onde os dados de uma 
     * Section podem ser extraidos.
     * 
     * @return A regexp para localizar blocos de codigo HTML na pagina principal
     * do forum que contem os dados das secoes do forum.
     */
    public static Pattern getFinder() {
        
        return FINDER_REGEXP;
        
    }//getFinder()
    
}//classe Section
