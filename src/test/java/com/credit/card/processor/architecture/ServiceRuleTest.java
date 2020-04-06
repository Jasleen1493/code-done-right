package com.credit.card.processor.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "java.com.credit.card.processor")
public class ServiceRuleTest {


    @ArchTest
    public static final ArchRule service_should_be_accessed = classes().that().resideInAnyPackage("..service..")
            .should().onlyBeAccessed().byAnyPackage("..service..", "..controller..");


    @ArchTest
    public static final ArchRule service_should_be_annotated_with_service = classes().that().resideInAnyPackage("..service..")
            .should().beAnnotatedWith(Service.class);

    @ArchTest
    public static final ArchRule classes_named_cservice_should_be_in_a_service_package = classes().that().areAnnotatedWith(Service.class).should()
            .resideInAPackage("..service..");
}

