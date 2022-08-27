package backupcc.pages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Um objeto desta classe coleta e retorna informacoes sobre uma pagina de 
 * topico do forum. Estes dados sao obtidos das paginas de Sections.
 * 
 * Um trecho de codigo HTML, contendo dados de um topico, deve ser enviado como
 * argumento ao construtor da classe. E deste bloco de codigo o construtor ira
 * obter a ID, NAME, FILENAME e URL ABSOLUTE deste topico.
 * 
 * Portanto um metodo deverah varrer o arquivo fonte de cada pagina de secao em 
 * busca destes blocos de codigo, criando objetos desta classe. Um para cada
 * topico apontado na pagina de secao que encontrar.
 *
 * @author "Pedro Reis"
 * @since 25 de agosto de 2022
 * @version 1.0
 */
public final class Topic extends Page {
    
    private final int numberOfPages;
    
    /*
     * Regexp para localizar os dados de um Topic na pagina de Section a qual
     * este topic pertence.
     */
    private static final Pattern FINDER_REGEXP = 
        Pattern.compile(
            "<a href=\"[.]/viewtopic[.]php[?].+?t=(\\d+).+?class=" +
            "\"topictitle\">(.+?)</a>[^ł]+?<dd class=\"posts\">(\\d+)"
        ); 
     
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor da classe extrai os dados de um topico a partir de um bloco
     * de codigo contido no codigo fonte de uma pagina de secao do forum.
     * 
     * @param htmlBlock O bloco de codigo HTML da pagina de secao do forum de
     * onde se pode extrair dados de um topico.
     *   
     * @throws backupcc.pages.UnexpectedHtmlFormatException No caso da
     * regexp nao conseguir fazer o parse desse bloco de codigo. Sinalizando um
     * bug do programa ou que o padrao das paginas do forum foi alterado.
     */
    public Topic(final String htmlBlock) throws UnexpectedHtmlFormatException{
         
        /*
        Localiza a id e o nome da Section no htmlBlock, que por sua vez foi 
        obtido de uma pagina de Header do forum com a regexp FINDER_REGEXP.
        */
        Matcher matcher = FINDER_REGEXP.matcher(htmlBlock);
        
        if (matcher.find()) {
            
            /* Estes campos sao declarados na super classe Page */
            id = Integer.valueOf(matcher.group(1));
            filename = "t=" + id + "&start=";
            name = matcher.group(2);
            absoluteURL = 
                backupcc.net.Util.FORUM_URL + "/viewtopic.php?t=" + id;
            int numberOfAnswers = Integer.valueOf(matcher.group(3));
            numberOfPages = (numberOfAnswers / MAX_ROWS_PER_PAGE) + 1;
            

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
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna o numero de paginas que tem esta secao.
     * 
     * @return O numero de paginas da secao menos um.
     */
    public int getNumberOfPages() {
        
        return numberOfPages;
        
    }//getNumberOfPages()
    
}//classe Topic

