package vn.edu.hcmute.fit.doan.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.GmailScopes;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleAuthService {

    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final java.io.File TOKENS_DIRECTORY_PATH = new java.io.File("tokens");
    private static GoogleAuthorizationCodeFlow flow;

    public static GoogleAuthorizationCodeFlow getFlow() throws GeneralSecurityException, IOException {
        if (flow == null) {
            InputStream in = GoogleAuthService.class.getResourceAsStream("/credentials.json");
            if (in == null) {
                throw new IOException("Không tìm thấy tệp credentials.json trong resources");
            }
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets,
                    Collections.singletonList(GmailScopes.GMAIL_SEND))
                    .setDataStoreFactory(new FileDataStoreFactory(TOKENS_DIRECTORY_PATH))
                    .setAccessType("offline")
                    .build();
        }
        return flow;
    }
}