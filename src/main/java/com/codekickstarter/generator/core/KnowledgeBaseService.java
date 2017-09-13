package com.codekickstarter.generator.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KnowledgeBaseService {

    private SubjectKnowledgeBase knowledgeBase;
    private List<String> templates = new ArrayList<>();
    private String sourceDirectory;

    @Autowired
    public KnowledgeBaseService(@Value("${app.source.directory}") String sourceDirectory,
                                @Value("${app.template.directory}") String templateDirectory) throws IOException {

        this.knowledgeBase = buildKnowledgeBase(sourceDirectory);
        this.templates = buildTemplateList(templateDirectory);
        this.sourceDirectory = sourceDirectory;
    }

    private List<String> buildTemplateList(String templateDirectory) throws IOException {
        return Files.list(Paths.get(templateDirectory))
                .map(e -> e.getFileName().toString())
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
    }

    private SubjectKnowledgeBase buildKnowledgeBase(String sourceDirectory) throws IOException {
        SubjectKnowledgeBase knowledgeBase = new SubjectKnowledgeBase();
        knowledgeBase.build(sourceDirectory);
        return knowledgeBase;
    }

    public SubjectKnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    public List<String> getTemplates() {
        return templates;
    }

    public String getSourceDirectory() {
        return sourceDirectory;
    }
}
