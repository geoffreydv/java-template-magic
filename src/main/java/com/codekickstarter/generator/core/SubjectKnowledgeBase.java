package com.codekickstarter.generator.core;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SubjectKnowledgeBase {

    // TODO: Classes with same name will get overwritten
    private Map<String, SourceClass> knownClasses = new HashMap<>();

    public void build(String sourceDirectory) throws IOException {

        Files.list(Paths.get(sourceDirectory))
                .parallel()
                .forEach(fileLocation -> {
                    if (Files.isDirectory(fileLocation)) {

                        try {
                            build(fileLocation.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (FilenameUtils.getExtension(fileLocation.getFileName().toString()).equals("java")) {
                            try {
                                CompilationUnit cu = JavaParser.parse(new File(fileLocation.toString()));
                                SourceClass classInformation = SourceClass.fromJavaFile(cu);
                                if (classInformation != null) {
                                    knownClasses.put(classInformation.getClassName(), classInformation);
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void build(List<String> sourceDirectories) throws IOException {
        sourceDirectories.parallelStream().forEach(sd -> {
            try {
                build(sd);
                appendFieldsOfParents();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void appendFieldsOfParents() {
        knownClasses.values()
                .forEach(cl -> {
                    if (cl.getSuperClassName() != null) {
                        SourceClass infoForParent = knownClasses.get(cl.getSuperClassName());
                        if (infoForParent != null) {
                            List<Field> finalFields = new ArrayList<>();
                            finalFields.addAll(infoForParent.getFields());
                            finalFields.addAll(cl.getFields());
                            cl.setFields(finalFields);
                        }

                    }
                });
    }

    public SourceClass findClass(String subjectName) {
        return knownClasses.get(subjectName);
    }

    public List<String> getKnownClassNames() {
        return knownClasses.keySet()
                .stream()
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
    }
}
