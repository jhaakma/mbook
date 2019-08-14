package mbook.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements
ConstraintValidator<UsernameConstraint, String> {

  @Override
  public void initialize(UsernameConstraint contactNumber) {
  }

  @Override
  public boolean isValid(String usernameField,
    ConstraintValidatorContext cxt) {
      return usernameField.matches("[a-zA-Z0-9_]*");
  }

}