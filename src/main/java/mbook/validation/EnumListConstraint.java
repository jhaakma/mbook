package mbook.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumListConstraint implements ConstraintValidator<EnumListValidator, ArrayList<String>> {

  List<String> valueList = null;

  @Override
  public boolean isValid(ArrayList<String> list, ConstraintValidatorContext context) {
      for ( String value : list ) {
          if(!valueList.contains(value)) {
              return false;
            }
      }
    return true;
  }

  @Override
  public void initialize(EnumListValidator constraintAnnotation) {
    valueList = new ArrayList<String>();
    Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();

    @SuppressWarnings("rawtypes")
    Enum[] enumValArr = enumClass.getEnumConstants();

    for(@SuppressWarnings("rawtypes")
    Enum enumVal : enumValArr) {
      valueList.add(enumVal.toString());
    }

    
  }

}