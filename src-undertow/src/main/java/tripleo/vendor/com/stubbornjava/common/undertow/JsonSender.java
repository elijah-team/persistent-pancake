package tripleo.vendor.com.stubbornjava.common.undertow;

import java.nio.ByteBuffer;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import tripleo.vendor.com.stubbornjava.common.Json;

public interface JsonSender {

    default void sendJson(HttpServerExchange exchange, Object obj) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(ByteBuffer.wrap(Json.serializer().toByteArray(obj)));
    }
}
