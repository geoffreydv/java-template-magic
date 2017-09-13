package com.codekickstarter.generator.api;

public class GeneratedCode {
    private String templateName;
    private String generatedCode;

    public GeneratedCode(String templateName, String generatedCode) {
        this.templateName = templateName;
        this.generatedCode = generatedCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getGeneratedCode() {
        return generatedCode;
    }
}
