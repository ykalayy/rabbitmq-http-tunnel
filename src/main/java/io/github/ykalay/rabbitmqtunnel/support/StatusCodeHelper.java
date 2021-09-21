package io.github.ykalay.rabbitmqtunnel.support;

import com.rabbitmq.client.LongString;

public class StatusCodeHelper {

    public static int getStatusCode(Object statusCode) {
        if(statusCode instanceof  Long) {
            return  ((Long) statusCode).intValue();
        } else if(statusCode instanceof LongString) {
            return  Integer.parseInt((statusCode).toString());
        }
        // 200 is default statusCode
        return 200;
    }
}
