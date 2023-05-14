package restapi.restdocs.restdocsTest.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import  org.springframework.restdocs.snippet.Attributes.Attribute;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class RestDocsConfig {

    public static Attribute field(
            final String key,
            final String value){
        return new Attribute(key,value);
    }

}
