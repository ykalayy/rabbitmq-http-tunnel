package com.ykalay.rabbitmqtunnel.core;

import java.util.List;
import java.util.Objects;

/**
 * Controller store which is scanned & created by
 *
 * @see RabbitmqHttpTunnelServer
 */
public class HttpAmqpControllerModelStore {

    /**
     * Singleton lazy instance of {@link HttpAmqpControllerModelStore}
     */
    private static HttpAmqpControllerModelStore LAZY_HOLDER;

    /**
     * @return singleton lazy instance of {@link HttpAmqpControllerModelStore}
     */
    public static HttpAmqpControllerModelStore getInstance() {
        if(Objects.isNull(LAZY_HOLDER)) {
            LAZY_HOLDER = new HttpAmqpControllerModelStore();
        }
        return LAZY_HOLDER;
    }

    private HttpAmqpControllerModelStore() {}

    private List<HttpAmqpControllerModel> httpAmqpControllerModelList;

    public List<HttpAmqpControllerModel> getHttpAmqpControllerModelList() {
        return httpAmqpControllerModelList;
    }

    public void setHttpAmqpControllerModelList(List<HttpAmqpControllerModel> httpAmqpControllerModelList) {
        this.httpAmqpControllerModelList = httpAmqpControllerModelList;
    }
}

