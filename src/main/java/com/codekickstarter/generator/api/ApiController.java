package com.codekickstarter.generator.api;

import com.codekickstarter.generator.core.ClassTemplateGenerator;
import com.codekickstarter.generator.core.Configuration;
import com.codekickstarter.generator.core.SourceClass;
import com.codekickstarter.generator.core.SubjectKnowledgeBase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    @GetMapping("/list-templates")
    public List<String> listTemplates() throws IOException {
        return Files.list(Paths.get(Configuration.TEMPLATES_DIRECTORY))
                .map(e -> e.getFileName().toString())
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/generate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateCode(@RequestBody GenerateCommand command) throws IOException {

        SubjectKnowledgeBase knowledgeBase = buildKnowledgeBase();

        Map<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put("extPackage", "configuration");

        ClassTemplateGenerator generator = new ClassTemplateGenerator(Configuration.TEMPLATES_DIRECTORY);
        SourceClass sourceClass = knowledgeBase.findClass(command.getClassName());
        List<GeneratedCode> output = generator.generate(sourceClass, command.getSelectedTemplates(), additionalProperties);

        return ResponseEntity.ok(output);
    }

    private SubjectKnowledgeBase buildKnowledgeBase() throws IOException {
        SubjectKnowledgeBase knowledgeBase = new SubjectKnowledgeBase();
        knowledgeBase.build(Configuration.SOURCE_DIRECTORIES);
        return knowledgeBase;
    }
}