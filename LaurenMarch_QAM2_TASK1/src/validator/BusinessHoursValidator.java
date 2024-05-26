package validator;

import java.time.ZonedDateTime;

@FunctionalInterface
public interface BusinessHoursValidator {
    boolean validate(ZonedDateTime start, ZonedDateTime end);
}
