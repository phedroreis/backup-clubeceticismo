package backupcc.exception;

import java.io.IOException;

/**
 *
 * @author "Pedro Reis"
 * @since 1.0 (16 de setembro de 2022)
 * @version 1.0
 */
public class IOExceptionX extends IOException {
    /**
     * A exceção deve ser tratada com um warningBox()
     */
    public static final int WARNING = 0;
    /**
     * A exceção deve ser tratada com um retryBox()
     */
    public static final int RETRY = 1;
    /**
     * A exceção deve ser tratada com um abortBox()
     */
    public static final int ABORT = 2;
    
    /**
     * Mensagens que devem aparecer no OptionBox
     */
    private final String[] msgs;
    /**
     * O tipo de tratamento para a exceção
     */
    private final int handlerType;
    
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor.
     * 
     * @param msgs Mensagens de erro.
     * 
     * @param handlerType Como deve ser tratada: aviso e continua, pergunta se
     * quer tentar de novo ou aborta aplicação.
     */
    public IOExceptionX(final String[] msgs, final int handlerType) {
        
        this.msgs = msgs;
        
        this.handlerType = handlerType;
                
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Método para ser chamado para tratar a exceção.
     * 
     * @return O que o usuário selecionar (false ou true) se for do tipo RETRY. 
     * Sempre true para WARNING. Não retorna e aborta quando ABORT.
     */
    public boolean handler() {
        
        switch (handlerType) {
            
            case WARNING ->  {
                backupcc.tui.OptionBox.warningBox(msgs);
            }
            case RETRY -> {
                return backupcc.tui.OptionBox.retryBox(msgs);
            }
            case ABORT -> {
                backupcc.tui.OptionBox.abortBox(msgs);
            }
             
        }//switch
        
        return true;
        
    }//handler()
    
}//classe IOExceptionX
