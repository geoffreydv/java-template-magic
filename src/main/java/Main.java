import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        SubjectKnowledgeBase knowledgeBase = new SubjectKnowledgeBase();

        List<String> sourceDirectories = new ArrayList<>();
        sourceDirectories.add("C:\\projects\\roots\\ifrs9\\core\\src\\main\\java\\be\\credo\\ifrs9\\core\\domain");
        knowledgeBase.build(sourceDirectories);

        ClassTemplateGenerator generator = new ClassTemplateGenerator();

        List<String> subjectNames = new ArrayList<>();
        subjectNames.add("OffloadedRun");


        subjectNames.forEach(sn -> {
            List<SourceClass> results = knowledgeBase.findClass(sn);

            if (results.size() == 1) {
                System.out.println("-------------------");
                System.out.println("Result for " + sn);
                System.out.println("-------------------");
                String output = generator.generate(results.get(0), "C:\\generator\\templates", "migration_classes.vm");
                System.out.println(output);
                System.out.println();
                System.out.println();
                System.out.println();
            } else {
                System.out.println("Too many results found");
            }
        });


    }
}
