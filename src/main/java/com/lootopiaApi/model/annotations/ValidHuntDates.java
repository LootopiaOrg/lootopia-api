package com.lootopiaApi.model.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HuntDatesValidator.class)
public @interface ValidHuntDates {

    String message() default "La date de fin doit être postérieure à la date de début.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
