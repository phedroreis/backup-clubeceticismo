package backupcc.strings;

/**
 * Esta classe disponibiliza metodos estaticos uteis para manipulacao de
 * Strings. 
 * 
 * @since 16 de agosto de 2022
 * @version 1.0
 * @author "Pedro Reis"
 */
public class Util {
        
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna um objeto String com o caractere c repetindo count vezes. Se 
     * count = 0 serah retornada uma string vazia.
     * 
     * @param c O caractere a ser repetido na string.
     * @param count O num. de repeticoes do caractere. Sera tambem o comprimento
     * da string retornada.
     * @return Um objeto String com o char c repetido count vezes.
     */
    public static String repeatChar(final char c, final int count) {
        
        StringBuilder sb = new StringBuilder(256);
        
        for (int i = 1; i <= count; i++) sb.append(c);
        
        return new String(sb);
    }//repeatChar()
    
    /*[--]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Metodo main() para testar a classe.
     * 
     * @param args Nao usado
     */
    public static void main(String[] args) {
        String s = repeatChar('x', 0);
        System.out.println("" + s.isEmpty());
        System.out.println(repeatChar('x', 20));
    }//main()
       
    
}//classe Util
