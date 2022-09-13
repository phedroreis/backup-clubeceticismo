package backupcc.strings;

/**
 *
 * @author "Pedro Reis"
 * @since 1.0 (12 de setembro de 2022)
 * @version 1.0
 */
public class StringEditor {
    
    private StringBuilder sb;
    
    public StringEditor(final String str) {
        
        sb = new StringBuilder(str);
        
    }
    
    public void replace(final String replacement, final String target) {
        
        int lengthTarget = target.length();
        int lengthReplacement = replacement.length();
        int indexOf = sb.indexOf(target);
        
        while (indexOf != -1) {
            
            sb = sb.replace(indexOf, indexOf + lengthTarget, replacement);
            
            indexOf = sb.indexOf(target, indexOf + lengthReplacement);
        }
        
    }
    
    @Override
    public String toString() {
        
        return sb.toString();
    }
    
    public static void main(String[] args) {
        
        StringEditor sm = new StringEditor("ana maça caqui banana laranja ana");
        
        sm.replace("xxx", "a");
        
        System.out.println(sm);
    }
    
}
