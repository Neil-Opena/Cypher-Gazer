package neil_opena.cyphergazer.Cyphers;


/**
 * This class is used to implement the Caesar cypher
 *
 * @author Neil Opena
 */
public class Caesar implements Cypher {
    /*
    To simplify the cypher:

    The key must be between the bounds 0 and 26, which correspond to a right shift
    during encryption. The negative is used for decryption.
     */

    @Override
    public String encrypt(String plainText, String key) {
        try{
            int shift = Integer.parseInt(key);
            return modify(plainText, shift);
        }catch (NumberFormatException ex){
            return null;
        }
    }

    @Override
    public String decrypt(String cypherText, String key) {
        try{
            int shift = Integer.parseInt(key);
            return  modify(cypherText, -shift);
        }catch (NumberFormatException ex){
            return null;
        }
    }

    @Override
    public String getArticleLink() {
        return "https://en.wikipedia.org/wiki/Caesar_cipher";
    }

    @Override
    public String toString(){
        return "Caesar";
    }

    private String modify(String text, int shift) {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int posFromA = (text.charAt(i) - 'A');
            char modifiedChar = (char) ('A' + ((posFromA + shift) % 26));

            //account for wrap around
            if(modifiedChar < 'A'){
                int posFromZ = ('A' - modifiedChar) - 1;
                modifiedChar = (char) ('Z' - posFromZ);
            }

            builder.append(modifiedChar);
        }

        return builder.toString();
    }
}
