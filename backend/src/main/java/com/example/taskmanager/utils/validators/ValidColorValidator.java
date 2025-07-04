package com.example.taskmanager.utils.validators;

import com.example.taskmanager.app.entities.ColorTypesEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class ValidColorValidator implements ConstraintValidator<ValidColor, String> {

    private Class<? extends ColorTypesEnum> enumClass;

    @Override
    public void initialize(ValidColor constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        } else {
            try {
                Enum.valueOf(enumClass.asSubclass(ColorTypesEnum.class), value);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    }
}
