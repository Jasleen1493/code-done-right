package com.credit.card.processor.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.credit.card.processor")
public class LayeredArchitectureTest {
/*
    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture()

            .layer("Services").definedBy("com.credit.card.processor..")
            .layer("Utils").definedBy("com.credit.card.processor.utils..")

            .whereLayer("Utils").mayOnlyBeAccessedByLayers("Services");*/
}
