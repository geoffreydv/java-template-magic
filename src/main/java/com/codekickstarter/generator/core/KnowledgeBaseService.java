package com.codekickstarter.generator.core;

import com.google.common.collect.Lists;
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
    private String templateDirectory;

    @Autowired
    public KnowledgeBaseService(@Value("${app.source.directory}") String sourceDirectory,
                                @Value("${app.template.directory}") String templateDirectory) throws IOException {

        this.knowledgeBase = buildKnowledgeBase(sourceDirectory);
        this.templates = buildTemplateList(templateDirectory);
        this.templateDirectory = templateDirectory;
    }

    private List<String> buildTemplateList(String templateDirectory) throws IOException {
        return Files.list(Paths.get(templateDirectory))
                .map(e -> e.getFileName().toString())
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
    }

    private SubjectKnowledgeBase buildKnowledgeBase(String sourceDirectory) throws IOException {
        SubjectKnowledgeBase knowledgeBase = new SubjectKnowledgeBase();
        knowledgeBase.build(Lists.newArrayList(sourceDirectory));
        return knowledgeBase;
    }

    public SubjectKnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    public List<String> getTemplates() {
        return templates;
    }

    public String getTemplateDirectory() {
        return templateDirectory;
    }
}
