package com.stratio.cucumber.aspects;

import static org.assertj.core.api.Assertions.assertThat;

import com.stratio.exceptions.NonReplaceableException;
import org.testng.annotations.Test;
import com.stratio.tests.utils.ThreadProperty;

public class ReplacementAspectTest {

    @Test
    public void replaceEmptyPlaceholdersTest() throws NonReplaceableException {
        ThreadProperty.set("class", this.getClass().getCanonicalName());
        ReplacementAspect repAspect = new ReplacementAspect();
        assertThat(repAspect.replaceEnvironmentPlaceholders("")).as("Replacing an empty placeholded string should not modify it").isEqualTo("");
    }

    @Test
    public void replaceSinglePlaceholdersTest() throws NonReplaceableException {
        ThreadProperty.set("class", this.getClass().getCanonicalName());
        ReplacementAspect repAspect = new ReplacementAspect();
        System.setProperty("STRATIOBDD_ENV1", "33");
        System.setProperty("STRATIOBDD_ENV2", "aa");

        assertThat(repAspect.replaceEnvironmentPlaceholders("${STRATIOBDD_ENV1}"))
        	.as("Unexpected replacement").isEqualTo("33");
        assertThat(repAspect.replaceEnvironmentPlaceholders("${STRATIOBDD_ENV1}${STRATIOBDD_ENV2}"))
        	.as("Unexpected replacement").isEqualTo("33aa");
        assertThat(repAspect.replaceEnvironmentPlaceholders("${STRATIOBDD_ENV1}:${STRATIOBDD_ENV2}"))
        	.as("Unexpected replacement").isEqualTo("33:aa");
        assertThat(repAspect.replaceEnvironmentPlaceholders("|${STRATIOBDD_ENV1}|:|${STRATIOBDD_ENV2}|"))
        	.as("Unexpected replacement").isEqualTo("|33|:|aa|");
    }

    @Test
    public void replaceSinglePlaceholderCaseTest() throws NonReplaceableException {
        ThreadProperty.set("class", this.getClass().getCanonicalName());
        ReplacementAspect repAspect = new ReplacementAspect();
        System.setProperty("STRATIOBDD_ENV1", "33");
        System.setProperty("STRATIOBDD_ENV2", "aA");

        assertThat(repAspect.replaceEnvironmentPlaceholders("${STRATIOBDD_ENV1.toUpper}")).as("Unexpected replacement").isEqualTo("33");
        assertThat(repAspect.replaceEnvironmentPlaceholders("${STRATIOBDD_ENV1.toLower}")).as("Unexpected replacement").isEqualTo("33");
        assertThat(repAspect.replaceEnvironmentPlaceholders("${STRATIOBDD_ENV2.toUpper}")).as("Unexpected replacement").isEqualTo("AA");
        assertThat(repAspect.replaceEnvironmentPlaceholders("${STRATIOBDD_ENV2.toLower}")).as("Unexpected replacement").isEqualTo("aa");
        assertThat(repAspect.replaceEnvironmentPlaceholders("${STRATIOBDD_ENV1}${STRATIOBDD_ENV2.toLower}")).as("Unexpected replacement").isEqualTo("33aa");
        assertThat(repAspect.replaceEnvironmentPlaceholders("${STRATIOBDD_ENV1}:${STRATIOBDD_ENV2.toUpper}")).as("Unexpected replacement").isEqualTo("33:AA");
        assertThat(repAspect.replaceEnvironmentPlaceholders("|${STRATIOBDD_ENV2}.toUpper")).as("Unexpected replacement").isEqualTo("|aA.toUpper");
    }

    @Test (expectedExceptions = {NonReplaceableException.class})
    public void replaceUnexistantPlaceholdersTest() throws NonReplaceableException {
        ThreadProperty.set("class", this.getClass().getCanonicalName());
        ReplacementAspect repAspect = new ReplacementAspect();

        repAspect.replaceEnvironmentPlaceholders("${STRATIOBDD_ENVX}");
    }
}