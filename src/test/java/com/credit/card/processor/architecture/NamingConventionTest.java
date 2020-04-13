package com.credit.card.processor.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.credit.card.processor")
public class NamingConventionTest {

    @ArchTest
    public static final ArchRule service_should_be_postfix = classes().that().resideInAnyPackage("..service..")
            .and().areAnnotatedWith(Service.class)
            .should().haveSimpleNameEndingWith("Service");

    /*@ArchTest
    public static final ArchRule validation_strategy_should_be_postfix = classes().that().implement(ValidationStrategy.class)
            .should().haveSimpleNameEndingWith("ValidationStrategy");*/


}
