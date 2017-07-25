import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubjectKnowledgeBase {

    private List<SourceClass> knownClasses = new ArrayList<>();

    public void build(String sourceDirectory) throws IOException {

        // TODO: Classes with same name will get overwritten

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
                                    knownClasses.add(classInformation);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<SourceClass> getKnownClasses() {
        return knownClasses;
    }

    public List<SourceClass> findClass(String subjectName) {
        return knownClasses.stream()
                .filter(clazz -> clazz.getClassName().equalsIgnoreCase(subjectName))
                .collect(Collectors.toList());
    }
}
