package com.example.taskmanager.utils.validators;

import com.example.taskmanager.app.entities.ColorTypesEnum;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidColorValidator.class)
public @interface ValidColor {
    Class<? extends ColorTypesEnum> enumClass();
    String message() default "Invalid color value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
