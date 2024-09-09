package com.itwray.iw.web.client;

/**
 * Feign Client Helper
 *
 * @author wray
 * @since 2024/9/9
 */
public abstract class ClientHelper {

    private static AuthClient authClient;

    public static void setAuthClient(AuthClient authClient) {
        if (ClientHelper.authClient == null) {
            ClientHelper.authClient = authClient;
        }
    }

    public static AuthClient getAuthClient() {
        return authClient;
    }
}
