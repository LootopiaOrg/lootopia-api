package com.lootopiaApi.model.annotations;

import com.lootopiaApi.model.entity.Hunt;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class HuntDatesValidator implements ConstraintValidator<ValidHuntDates, Hunt> {
    @Override
    public boolean isValid(Hunt hunt, ConstraintValidatorContext constraintValidatorContext) {
        if (hunt == null) return true;

        LocalDateTime start = hunt.getStartDate();
        LocalDateTime end = hunt.getEndDate();

        return start == null || end == null || end.isAfter(start);
    }
}
