package ee.bitweb.http.server.mock;

import io.netty.handler.codec.http.HttpMethod;
import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.*;

import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpResponse.response;

@NoArgsConstructor
public class MockServer implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback {

    private String path;
    private HttpMethod method;
    private ClientAndServer server;

    public MockServer(String path) {
        this.path = path;
    }

    public MockServer(HttpMethod method) {
        this.method = method;
    }

    public MockServer(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public static MockServer post(String path) {
        return new MockServer(HttpMethod.POST, path);
    }

    public static MockServer get(String path) {
        return new MockServer(HttpMethod.GET, path);
    }

    public static MockServer put(String path) {
        return new MockServer(HttpMethod.PUT, path);
    }

    public static MockServer patch(String path) {
        return new MockServer(HttpMethod.PATCH, path);
    }

    public static MockServer delete(String path) {
        return new MockServer(HttpMethod.DELETE, path);
    }

    public static MockServer head(String path) {
        return new MockServer(HttpMethod.HEAD, path);
    }

    public Integer getPort() {
        return server.getPort();
    }

    public ClientAndServer get() {
        return server;
    }

    @Override
    public void afterAll(ExtensionContext context) {
        server.stop();
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        server = ClientAndServer.startClientAndServer(0);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        server.reset();
    }

    public Verification mock(HttpRequest request, HttpResponse response, int times) {
        server.when(request, exactly(times)).respond(response);

        return new Verification(server, request);
    }

    public Verification mock(HttpRequest request, HttpResponse response) {
        server.when(request).respond(response);

        return new Verification(server, request);
    }

    public Verification mock(HttpResponse response) {
        return mock(requestBuilder(), response);
    }

    public HttpResponse responseBuilder(int statusCode) {
        return responseBuilder(statusCode, null);
    }

    public HttpResponse responseBuilder(int statusCode, JSONObject body) {
        HttpResponse response = response()
                .withStatusCode(statusCode)
                .withHeaders(new Header("Content-Type", MediaType.APPLICATION_JSON_UTF_8.toString()));

        if (body != null) {
            response.withBody(body.toString());
        }

        return response;
    }

    public HttpRequest requestBuilder() {
        return requestBuilder(method, path);
    }

    public HttpRequest requestBuilder(HttpMethod method) {
        return requestBuilder(method, path);
    }

    public HttpRequest requestBuilder(String path) {
        return requestBuilder(method, path);
    }

    public HttpRequest requestBuilder(HttpMethod method, String path) {
        return requestBuilder(method, path, null);
    }

    public HttpRequest requestBuilder(JSONObject body) {
        return requestBuilder(method, path, body);
    }

    public HttpRequest requestBuilder(HttpMethod method, String path, JSONObject body) {
        Assertions.assertNotNull(method);
        Assertions.assertNotNull(path);

        HttpRequest request = HttpRequest.request().withPath(path).withMethod(method.name());

        if (body != null) {
            request.withBody(new JsonBody(body.toString()));
        }

        return request;
    }
}
