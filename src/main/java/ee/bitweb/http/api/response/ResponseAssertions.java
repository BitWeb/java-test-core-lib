package ee.bitweb.http.api.response;

import lombok.*;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class ResponseAssertions {

    public static void assertUnauthorizedResponse(ResultActions actions) throws Exception {
        actions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").doesNotExist());
    }

    public static void assertForbidden(ResultActions actions) throws Exception {
        actions.andExpect(status().isForbidden())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", not(blankString())))
                .andExpect(jsonPath("$.message", is(nullValue())));
    }

    public static void assertAccessDeniedResponse(ResultActions actions) throws Exception {
        actions.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", not(blankString())))
                .andExpect(jsonPath("$.message", is("ACCESS_DENIED")));
    }

    public static void assertValidationErrorResponse(ResultActions actions, Error expectedError) throws Exception {
        assertValidationErrorResponse(actions, List.of(expectedError));
    }

    public static void assertValidationErrorResponse(ResultActions actions, List<Error> expectedErrors) throws Exception {
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.id", not(blankString())))
                .andExpect(jsonPath("$.message", is("INVALID_ARGUMENT")))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrors.size())));

        assertErrors(actions, expectedErrors);
    }

    public static void assertConstraintViolationErrorResponse(ResultActions actions, Error expectedError) throws Exception {
        assertConstraintViolationErrorResponse(actions, List.of(expectedError));
    }

    public static void assertConstraintViolationErrorResponse(ResultActions actions, List<Error> expectedErrors) throws Exception {
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.id", not(blankString())))
                .andExpect(jsonPath("$.message", is("CONSTRAINT_VIOLATION")))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrors.size())));

        assertErrors(actions, expectedErrors);
    }

    public static void assertConflictErrorResponse(
            ResultActions actions,
            String entity,
            String message,
            Criteria criteria
    ) throws Exception {
        assertConflictErrorResponse(
                actions,
                entity,
                message,
                List.of(criteria)
        );
    }

    public static void assertConflictErrorResponse(
            ResultActions actions,
            String entity,
            String message,
            List<Criteria> criterias
    ) throws Exception {
        actions.andExpect(status().isConflict())
                .andExpect(jsonPath("$", aMapWithSize(4)))
                .andExpect(jsonPath("$.id", not(blankString())))
                .andExpect(jsonPath("$.entity", is(entity)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.criteria", hasSize(criterias.size())));

        assertCriterias(actions, criterias);
    }

    public static void assertNotFoundResponse(
            ResultActions actions,
            String entity,
            String message,
            Criteria criteria
    ) throws Exception {
        assertNotFoundResponse(actions, entity, message, List.of(criteria));
    }

    public static void assertNotFoundResponse(
            ResultActions actions,
            String entity,
            String message,
            List<Criteria> criterias
    ) throws Exception {
        actions.andExpect(status().isNotFound())
                .andExpect(jsonPath("$", aMapWithSize(4)))
                .andExpect(jsonPath("$.id", not(blankString())))
                .andExpect(jsonPath("$.entity", is(entity)))
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.criteria", hasSize(criterias.size())));

        assertCriterias(actions, criterias);
    }

    public static void assertMessageNotReadableError(ResultActions actions) throws Exception {
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", not(blankString())))
                .andExpect(jsonPath("$.message", is("MESSAGE_NOT_READABLE")));
    }

    public static void assertMethodNotAllowedError(ResultActions actions) throws Exception {
        actions.andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", not(blankString())))
                .andExpect(jsonPath("$.message", is("METHOD_NOT_ALLOWED")));
    }

    public static void assertContentTypeInvalidError(ResultActions actions) throws Exception {
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", not(blankString())))
                .andExpect(jsonPath("$.message", is("CONTENT_TYPE_NOT_VALID")));
    }

    public static void assertInternalServerError(ResultActions actions) throws Exception {
        actions.andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", not(blankString())))
                .andExpect(jsonPath("$.message", is("INTERNAL_SERVER_ERROR")));
    }

    private static void assertCriterias(ResultActions actions, List<Criteria> criterias) throws Exception {
        for (int i = 0; i < criterias.size(); i++) {
            Criteria criteria = criterias.get(i);
            actions.andExpect(jsonPath("$.criteria[" + i + "]", aMapWithSize(2)))
                    .andExpect(jsonPath("$.criteria[" + i + "].field", is(criteria.getField())))
                    .andExpect(jsonPath("$.criteria[" + i + "].value", is(criteria.getValue())));
        }
    }

    private static void assertErrors(ResultActions actions, List<Error> errors) throws Exception {
        for (int i = 0; i < errors.size(); i++) {
            Error error = errors.get(i);
            actions.andExpect(jsonPath("$.errors[" + i + "]", aMapWithSize(3)))
                    .andExpect(jsonPath("$.errors[" + i + "].field", is(error.getField())))
                    .andExpect(jsonPath("$.errors[" + i + "].reason", is(error.getReason())))
                    .andExpect(jsonPath("$.errors[" + i + "].message", is(error.getMessage())));
        }
    }
}
