package ee.bitweb.http.api.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Error {
    private final String field;
    private final String reason;
    private final String message;

    public static Error notNull(String field) {
        return new Error(
                field,
                "NotNull",
                "must not be null"
        );
    }

    public static Error notBlank(String field) {
        return new Error(
                field,
                "NotBlank",
                "must not be blank"
        );
    }

    public static Error positiveNumber(String field) {
        return new Error(
                field,
                "Positive",
                "must be greater than 0"
        );
    }

    public static Error genericInvalidFormat(String field, String value) {
        return new Error(
                field,
                "InvalidFormat",
                String.format("Value not recognized (%s), please refer to specification for available values.", value)
        );
    }

    public static Error invalidTypeRequestParam(String field) {
        return new Error(
                field,
                "InvalidType",
                "Request parameter is invalid"
        );
    }

    public static Error missingRequestParam(String field) {
        return new Error(
                field,
                "MissingValue",
                "Request parameter is required"
        );
    }

    public static Error requestPartMissing(String field) {
        return new Error(
                field,
                "RequestPartPresent",
                String.format("Required request part '%s' is not present", field)
        );
    }

    public static Error notEmpty(String field) {
        return new Error(
                field,
                "NotEmpty",
                "must not be empty"
        );
    }
}
