package ee.bitweb.http.server.mock;

import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.http.HttpMethod;
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
        server = ClientAndServer.startClientAndServer(0);;
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        server.reset();
    }

    public void mock(HttpRequest request, HttpResponse response) {
        server.when(request).respond(response);
    }

    public HttpResponse responseBuilder(int statusCode) {
        return response()
                .withStatusCode(statusCode)
                .withHeaders(
                        new Header("Content-Type", "application/json; charset=utf-8")
                );
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
        Assertions.assertNotNull(method);
        Assertions.assertNotNull(path);

        return HttpRequest.request()
                .withPath(path)
                .withMethod(method.name());
    }
}
