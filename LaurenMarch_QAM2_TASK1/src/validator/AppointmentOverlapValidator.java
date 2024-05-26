package validator;

import java.time.LocalDateTime;

@FunctionalInterface
public interface AppointmentOverlapValidator {
    boolean validate(int customerId, LocalDateTime start, LocalDateTime end);
}

