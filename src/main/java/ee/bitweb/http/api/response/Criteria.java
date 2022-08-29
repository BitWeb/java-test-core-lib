package ee.bitweb.http.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Criteria {

    private final String field;
    private final String value;
}
