package com.bloqly.api;

import com.bloqly.api.transaction.SignedTransaction;
import com.bloqly.api.transaction.Transaction;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KeyPairTest {

    private static final String PRIVATE_KEY = "x9oZ6jrUVEBDP5C0005fPseqPwLshQbb9io7Upg8sNM=";

    private static final String PUBLIC_KEY = "Ud8SI+nIX/XrPss2DEM1B2tdxu0v9NvuuAdABfUMrZk=";

    private static final String SPACE = "space1";

    private static final String KEY = "key1";

    @Test
    public void testPublicFromPrivateEncoded() {
        KeyPair keyPair = KeyPair.fromPrivateKeyEncoded(PRIVATE_KEY);

        Assert.assertEquals(PUBLIC_KEY, keyPair.getPublicKeyEncoded());
    }

    @Ignore
    @Test
    public void testSignTransaction() {

        BloqlyClient bloqlyClient = new BloqlyClient("http://localhost:8082");

        Optional<SignedTransaction> txOpt = bloqlyClient.getLastTransaction(SPACE, KEY);

        Long nonce = txOpt.map(tx -> tx.getNonce() + 1).orElse(1L);

        KeyPair keyPair = KeyPair.fromPrivateKeyEncoded(PRIVATE_KEY);

        Transaction tx = new Transaction();

        tx.setSpace(SPACE);
        tx.setKey(KEY);
        tx.setNonce(nonce);
        tx.setMemo("memo");
        tx.setTimestamp(Instant.now().toEpochMilli());
        tx.setValue("test-value");

        List<String> tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        tx.setTags(tags);

        SignedTransaction signedTx = keyPair.signTransaction(tx);

        bloqlyClient.submitTransaction(signedTx);

    }
}
