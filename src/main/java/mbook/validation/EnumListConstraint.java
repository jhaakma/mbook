package mbook.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumListConstraint implements ConstraintValidator<EnumListValidator, Map<String, Integer>> {

 ArrayList<String> valueList = null;

  @Override
  public boolean isValid(Map<String, Integer> list, ConstraintValidatorContext context) {

    for (Map.Entry<String, Integer> entry : list.entrySet() ) {
        if ( !valueList.contains(entry.getKey()) ) {
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