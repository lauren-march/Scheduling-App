package validator;

import java.time.LocalDateTime;

@FunctionalInterface
public interface BusinessHoursValidator {
    boolean validate(LocalDateTime start, LocalDateTime end);
}
