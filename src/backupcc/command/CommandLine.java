package backupcc.command;

/**
 * Esta classe é responsável por fazer o parse da linha de comando que pode
 * selecionar o tipo de terminal. 
 * 
 * @author "Pedro Reis"
 * @since 1.0 (10 de setembro de 2022)
 * @version 1.0
 */
public final class CommandLine {
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * <p>Faz o parse dos argumentos passados na linha de comando para a 
     * aplicação.</p>
     * 
     * <p> Sem argumentos na linha de comando, o sistema irá usar o padrão de 
     * terminal de acordo com o sistema operacional onde estiver rodando.</p>
     * 
     * <ul>
     * <li>
     * -w : força que as saídas para o terminal sejam no padrão de um terminal
     * do sistema Windows
     * </li>
     * <li>
     * -u : força que as saídas para o terminal sejam no padrão de sistemas
     * Unix, reconhecendo comandos ANSI
     * </li>
     * </ul>
     * 
     * @param commands Linha de comando digitada ao executar o programa.
     */
    public CommandLine(final String[] commands) {
        
        switch (commands[0]) {
            case "-w" -> backupcc.tui.Tui.ansiCodesDisable();
            case "-u" -> backupcc.tui.Tui.ansiCodesEnable();
            default -> {
                String[] msgs = {
                    commands[0] + " <- flag n\u00E3o reconhecido!\n",
                    "Uso : [terminal]\n",
                    "terminal: -w , -u\n",
                    "-w: modo terminal Windows",
                    "-u: modo terminal Unix"
                };
                backupcc.tui.OptionBox.abortBox(msgs);
            }
        }//switch
              
    }//CommandLine()
        
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Informação textual sobre a classe.
     * 
     * @return Informação textual sobre a classe.
     */
    @Override
    public String toString() {
        
        String toString = "Interface de terminal = modo ";
        
        toString += backupcc.tui.Tui.isWindowsOS() ? "Windows" : "Unix";
                
        return toString;
        
    }//toString()
   
}//classe CommandLine
