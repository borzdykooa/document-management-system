package com.borzdykooa.validator;

import com.borzdykooa.dto.SearchRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SearchRequestDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return SearchRequestDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SearchRequestDto searchRequestDto = (SearchRequestDto) o;
        if (searchRequestDto.getType() == null && searchRequestDto.getTitle() == null) {
            errors.reject("400", "At least one parameter should be set");
        }
    }
}
