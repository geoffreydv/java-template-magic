package com.codekickstarter.generator.api;

import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public class GenerateCommand {

    @NotBlank
    private String className;
    private List<String> selectedTemplates;

    public GenerateCommand() {
    }

    public GenerateCommand(String className, List<String> selectedTemplates) {
        this.className = className;
        this.selectedTemplates = selectedTemplates;
    }

    public String getClassName() {
        return className;
    }

    public List<String> getSelectedTemplates() {
        return selectedTemplates;
    }
}
