package restapi.restdocs.restdocsTest.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import restapi.restdocs.dto.PostRequest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class ConstraintFields<T> {

    private final ConstraintDescriptions constraintDescriptions;

    public ConstraintFields(Class<T> clazz) {
        this.constraintDescriptions = new ConstraintDescriptions(clazz);
    }

    public FieldDescriptor withPath(String path) {
        return fieldWithPath(path).attributes(
                Attributes.key("constraints").value(
                        this.constraintDescriptions.descriptionsForProperty(path).stream()
                                .map(description -> "- " + description)
                                .collect(Collectors.joining("\n"))
                )
        );
    }

}

