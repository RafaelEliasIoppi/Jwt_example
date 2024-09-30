package org.acme;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

@ApplicationScoped
public class KeyLoader {

    private static final Logger LOGGER = Logger.getLogger(KeyLoader.class);

    @Inject
    @ConfigProperty(name = "mp.jwt.sign.key.location")
    String keyStorePath; // Injetando a configuração diretamente

    @Inject
    @ConfigProperty(name = "mp.jwt.sign.key.store.password")
    String keyStorePassword; // Injetando a configuração diretamente

    @Inject
    @ConfigProperty(name = "mp.jwt.sign.key.alias")
    String keyAlias; // Injetando a configuração diretamente

    @Inject
    @ConfigProperty(name = "mp.jwt.sign.key.password")
    String keyPassword; // Injetando a configuração diretamente

    public PrivateKey loadPrivateKey() throws Exception {
        LOGGER.info("Iniciando o carregamento do KeyStore...");

        // Verifica se o arquivo do keystore existe
        File keyStoreFile = new File(keyStorePath);
        if (!keyStoreFile.exists()) {
            LOGGER.error("Keystore file not found: " + keyStorePath);
            throw new Exception("Keystore file not found: " + keyStorePath);
        } else {
            LOGGER.info("Keystore file encontrado: " + keyStorePath);
        }

        // Mantenha o formato "JKS"
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keyStorePath)) {
            LOGGER.info("Carregando KeyStore com a senha fornecida...");
            keyStore.load(fis, keyStorePassword.toCharArray());
            LOGGER.info("KeyStore carregado com sucesso.");
        } catch (Exception e) {
            LOGGER.error("Falha ao carregar o KeyStore: " + e.getMessage(), e);
            throw new Exception("Failed to load KeyStore", e);
        }

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, keyPassword.toCharArray());
        if (privateKey == null) {
            LOGGER.error("Private key not found for alias: " + keyAlias);
            throw new Exception("Private key not found for alias: " + keyAlias);
        }

        LOGGER.info("Private key carregada com sucesso.");
        return privateKey;
    }
}
