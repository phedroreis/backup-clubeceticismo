package backupcc.pages;

import java.text.ParseException;

/**
 * Uma excecao desta classe deve ser lancada quando um bloco de codigo HTML, de
 * onde se esperava extrair algum dado, nao contiver nenhum texto que case com
 * as regexps aplicadas. Provavelmente sinalizando um bug no programa ou que os 
 * padroes esperados nas paginas HTML do forum foram alterados.
 * 
 * @author "Pedro Reis"
 * @since 23 de agosto de 2022
 * @version 1.0 
 */
public final class UnexpectedHtmlFormatException extends ParseException {
    /**
     * Construtor da classe.
     * 
     * @param msg A mensagem da excecao.
     */
    public UnexpectedHtmlFormatException(final String msg) {
        
        super(msg, 0);
        
    }//construtor
    
    
}//classe UnexpectedHtmlFormatException
