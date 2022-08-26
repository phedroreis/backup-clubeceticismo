package backupcc.net;

/**
 * Esta classe deve recuperar informacoes de uma URL que aponte para pagina ou 
 * arquivo no dominio do forum ClubeCeticismo.
 * As informacoes devem ser obtidas da propria String da URL, sem necessidade de
 * acesso ao servidor do forum.
 * 
 * @since 14 de agosto de 2022
 * @version 1.0
 * @author "Pedro Reis"
 */
public class URLInfo {
    
    private final String url;
    
  
    /*[00]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Construtor que inicializa a classe com a URL que poderah ser processada
     * pelos metodos da classe para retornar informacoes uteis.
     * 
     * @param url Um objeto String com a Url.
     */
    public URLInfo(final String url) {
        
        this.url = url;
        
    }//construtor
    
    /*[01]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna se a URL eh um endereco absoluto ou relativo. True se for 
     * relativo e apontando para algum arquivo no servidor do forum. False se 
     * for absoluto e neste caso pode estar apontando para pagina do forum, ou
     * um arquivo no servidor do forum, ou pagina em outro servidor web ou mesmo
     * um arquivo em outro servidor que não o do forum ClubeCeticismo.
     * 
     * @return true se o endereco for relativo e false se for absoluto.
     */
    public boolean isRelativeAddress() {
        
        return (url.startsWith(".") || url.startsWith("/"));
        
    }//isRelativeAdress()
    
    /*[02]----------------------------------------------------------------------
    
    --------------------------------------------------------------------------*/
    /**
     * Retorna a url como endereço absoluto. Se já for um endereco absoluto, a 
     * url eh retornada sem alteracao.
     * 
     * @return A url no formato de endereco absoluto.
     */
    public String getAbsoluteUrl() {
        
        if (isRelativeAddress()) {
            
            if (url.startsWith("/")) return (Util.FORUM_URL + url);
            return (Util.FORUM_URL + url.substring(1));
                
        }
         
        return url;
     
    }//getAbsoluteUrl()
    
    public static void main(String[] args) {
        
        URLInfo u = new URLInfo("/bla");
        
        System.out.println(u.getAbsoluteUrl());
        
    }
    
}//classe URLInfo
