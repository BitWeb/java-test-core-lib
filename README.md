# Bitweb Test Library 

Given library has helper functions that make it abit easier to test various functionality 

## Mock server

Introducing new class `ee.bitweb.http.server.mock.MockServer` which enables for easier handling of `org.mockserver.integration.ClientAndServer`.
If registered as a JUnit extension, it will control the typical lifecycle of the ClientAndServer class for each class.
Server instance is started on random port and it can be injected to your properties for seamless integration. 

### Example of general setup

```
@SpringBootTest(classes = {TestSpringApplication.class})
class HammasBookingApiCreateBookingTest {

    private static final String PATH = "/v1/booking";

    @RegisterExtension
    protected static MockServer server = new MockServer(HttpMethod.POST, PATH);

    @DynamicPropertySource
    static void initProperties(DynamicPropertyRegistry registry) {
        registry.add("booking.api.baseUrl", () -> "http://localhost:" + server.getPort());
    }
    ....
}
```

### Example definition of mock request

```
        server.mock(
                server.requestBuilder(HttpMethod.POST, "/v1/pre-booking")
                        .withBody(new JsonBody(getValidPreBookingCreateRequest(visitTime).toString())),
                server.responseBuilder(500).withBody(
                        expectedResponse.toString()
                )
        );
```

For reach request mock, you have specify the HTTP method and path where the request will be expected.
These parameters can be supplied to constructor as default values, then you don't have to specify these
values for every test.

## MockMvc Response expectation helpers

Given library provides a set of assertion helpers that correspond to the typical responses defined in
[Bitweb core library](https://bitbucket.bitweb.ee/projects/BITWEB/repos/java-core-lib/browse)

Structure of response objects are defined in class `ee.bitweb.http.api.response.ResponseAssertions`.
Different validation errors are defined in class `ee.bitweb.http.api.response.Error`

Example usage:

```
        ResultActions result = mockMvc.perform(mockMvcBuilder).andDo(print());
        ResponseAssertions.assertValidationErrorResponse(
                result,
                List.of(
                        Error.notBlank("complexProperty"),
                        Error.notNull("complexProperty"),
                        Error.notEmpty("objects"),
                        Error.notNull("objects")
                )
        );
```
### 1.2.0

Added new builder methods to MockServer class for more convenient usage. 

### 1.1.0
MockServer.mock(HttpRequest request, HttpResponse response, int times) for subsequent requests to same endpoint with different response expectations

### 1.0.0
Definitions of Error messages

 * Error.notNull - for when a value is not allowed to be null
 * Error.notBlank - for when a String is not allowed to be empty
 * Error.positiveNumber - for when a number must be positive
 * Error.genericInvalidFormat - for when there is a generic InvalidFormat error
 * Error.invalidTypeRequestParam - for when provided request parameter is of invalid type
 * Error.missingRequestParam - for when there is a missing required request parameter
 * Error.requestPartMissing - for missing multipart in files
 * Error.notEmpty - for when list should not be empty

Definitions of various response assertions 

 * ResponseAssertions.assertAccessDeniedResponse - for access denied response
 * ResponseAssertions.assertValidationErrorResponse - for when there is input validation errors
 * ResponseAssertions.assertConstraintViolationErrorResponse - for when there is validation exception of a model
 * ResponseAssertions.assertConflictErrorResponse - for when there is a ConflictException
 * ResponseAssertions.assertNotFoundResponse - for when there is a 404 error response
 * ResponseAssertions.assertMessageNotReadableError - for when there is a malformed input error
 * ResponseAssertions.assertMethodNotAllowedError - for when there is an invalid HTTP method error
 * ResponseAssertions.assertContentTypeInvalidError - for when provided content is of invalid type
 * ResponseAssertions.assertInternalServerError - for generic internal error cases.
