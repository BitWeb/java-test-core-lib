package ee.bitweb.http.server.mock;

import lombok.RequiredArgsConstructor;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.verify.VerificationTimes;

@RequiredArgsConstructor
public class Verification {

    private final ClientAndServer server;
    private final HttpRequest request;

    public void calledNever() {
        calledExactly(0);
    }

    public void calledOnce() {
        calledExactly(1);
    }

    public void calledExactly(int times) {
        server.verify(request, VerificationTimes.exactly(times));
    }
}
