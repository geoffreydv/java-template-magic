package com.codekickstarter.generator.api;

import com.codekickstarter.generator.core.ClassTemplateGenerator;
import com.codekickstarter.generator.core.KnowledgeBaseService;
import com.codekickstarter.generator.core.SourceClass;
import com.codekickstarter.generator.core.SubjectKnowledgeBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApiController {

    private final KnowledgeBaseService knowledgeBaseService;

    @Autowired
    public ApiController(KnowledgeBaseService knowledgeBaseService) {
        this.knowledgeBaseService = knowledgeBaseService;
    }

    @GetMapping("/list-templates")
    public List<String> listTemplates() throws IOException {
        return knowledgeBaseService.getTemplates();
    }

    @GetMapping("/list-known-classes")
    public List<String> listKnownClasses() throws IOException {
        return knowledgeBaseService.getKnowledgeBase().getKnownClassNames();
    }

    @PostMapping(value = "/generate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateCode(@RequestBody GenerateCommand command) throws IOException {

        SubjectKnowledgeBase knowledgeBase = knowledgeBaseService.getKnowledgeBase();

        Map<String, String> additionalProperties = new HashMap<>();
        additionalProperties.put("extPackage", "configuration");

        ClassTemplateGenerator generator = new ClassTemplateGenerator(knowledgeBaseService.getTemplateDirectory());
        SourceClass sourceClass = knowledgeBase.findClass(command.getClassName());
        List<GeneratedCode> output = generator.generate(sourceClass, command.getSelectedTemplates(), additionalProperties);

        return ResponseEntity.ok(output);
    }


}