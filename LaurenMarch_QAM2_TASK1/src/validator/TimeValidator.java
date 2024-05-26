package validator;

import java.time.LocalDateTime;

@FunctionalInterface
public interface TimeValidator {
    boolean validate(LocalDateTime start, LocalDateTime end);
}
