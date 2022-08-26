package backupcc.pages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Um objeto desta classe coleta e retorna informacoes sobre uma pagina de 
 * header do forum. Estes dados sao obtidos da pagina principal.
 * 
 * Um trecho de codigo HTML, contendo dados de um HEADER, deve ser enviado como
 * argumento ao construtor da classe. E deste bloco de codigo o construtor ira
 * obter a ID, NAME, FILENAME e URL ABSOLUTE do HEADER.
 * 
 * Portanto, um metodo deverah varrer o arquivo fonte da pagina principal em 
 * busca destes blocos de codigo, criando objetos desta classe. Um para cada
 * HEADER apontado na pagina principal que encontrar.
 * 
 * @author "Pedro Reis"
 * @since 23 de agosto de 2022
 * @version 1.0
 */
public final class Header extends Page {
    /*
     * Regexp para localizar os dados de um Header na pagina principal do 
     * forum. Estes dados estao sempre inseridos no escopo de uma tag li 
     * (item list) cujo class ="header".
     */
    private static final Pattern FINDER_REGEXP = 
        Pattern.compile("<li class=\"header\">(.|\\n)+?</li>");
    /*
    Localiza ID e NOME do HEADER no bloco de codigo HTML localizado pelo 
    Pattern FINDER_REGEXP
    */
    private static final Pattern ID_AND_NAME = 
        Pattern.compile("data-id=\"(\\d+)\">(.+)</a>");
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * @param htmlBlock O bloco de codigo HTML da pagina principal do forum de
     * onde se pode extrair dados de um HEADER. (O Header eh a pagina que aponta
     * para uma lista de secoes do forum). Serao obtidos deste bloco a id do 
     * header, o endereco absoluto da sua pagina, o nome do arquivo com o qual 
     * esta pagina deve ser gravada no disco local e tambem o nome do header.
     * 
     * No momento em que estas linhas sao escritas ha 3 headers no forum:
     * 
     * AVISOS E TESTES
     * DEBATES
     * DIVERSAO
     *   
     * @throws backupcc.pages.UnexpectedHtmlFormatException No caso da
     * regexp nao conseguir fazer o parse desse bloco de codigo. Sinalizando um
     * bug do programa ou que o padrao das paginas do forum foi alterado.
     */
    public Header(final String htmlBlock) throws UnexpectedHtmlFormatException {
          
        /*
        Localiza a id e o nome do HEADER no htmlBlock, que por sua vez foi 
        obtido da pagina principal do forum com a regexp FINDER_REGEXP.
        */
        Matcher matcher = ID_AND_NAME.matcher(htmlBlock);
        
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
            "Can't parse HTML to find header data"
        );
         
    }//Header()
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna um objeto Pattern com a regexp que permite localizar blocos de
     * codigo HTML (da pagina principal do forum) de onde os dados de um header
     * podem ser extraidos.
     * 
     * @return A regexp para localizar blocos de codigo HTML na pagina principal
     * do forum que contem os dados das secoes de HEADER do forum.
     */
    public static Pattern getFinder() {
        
        return FINDER_REGEXP;
        
    }//getFinder()
    
}//classe Header
