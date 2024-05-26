package validator;

import java.time.LocalTime;

@FunctionalInterface
public interface AppointmentOverlapValidator {
    boolean validate(int customerId, LocalTime start, LocalTime end);
}

