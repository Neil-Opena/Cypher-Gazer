package neil_opena.cyphergazer.Cyphers;

public interface Cypher {

    String encrypt(String plainText, String configuration);

    String decrypt(String cypherText, String configuration);
}
