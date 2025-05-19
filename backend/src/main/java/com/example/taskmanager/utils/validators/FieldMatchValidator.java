package com.example.taskmanager.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

public class FieldMatchValidator implements ConstraintValidator<FieldsMatch, Object> {
    private String fieldName;
    private String fieldMatchName;

    @Override
    public void initialize(FieldsMatch constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.fieldMatchName = constraintAnnotation.fieldMatchName();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        try {
            Object fieldValue = new BeanWrapperImpl(o).getPropertyValue(fieldName);
            Object fieldMatchValue = new BeanWrapperImpl(o).getPropertyValue(fieldMatchName);

            boolean isValid = fieldValue != null && fieldValue.equals(fieldMatchValue);

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(fieldMatchName)
                        .addConstraintViolation();
            }

            return isValid;
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }
}
