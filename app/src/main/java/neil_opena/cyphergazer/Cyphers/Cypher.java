package neil_opena.cyphergazer.Cyphers;


public abstract class Cypher {

    public abstract String encrypt(String plainText, String key);

    public abstract String decrypt(String cypherText, String key);

    public abstract String getArticleLink();

    public abstract boolean hasNumericalKey();

    public String removeWhiteSpace(String text){
        String temp = "";
        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) < 'A' || text.charAt(i) > 'Z'){
                continue;
            }
            temp = temp + text.charAt(i);
        }
        return temp;
    }
}
