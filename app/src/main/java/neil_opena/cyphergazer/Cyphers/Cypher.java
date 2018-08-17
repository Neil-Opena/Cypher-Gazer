package neil_opena.cyphergazer.Cyphers;

public interface Cypher {

    String encrypt(String plainText, String key);

    String decrypt(String cypherText, String key);

    String getArticleLink();

    boolean hasNumericalKey();
}
