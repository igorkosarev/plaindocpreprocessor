package structure.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.HashMap;
import java.util.Map;

@Data
public class Template {
    @Getter
    private String name;

    @Getter
    private String fileName;

    @Getter
    private String extension;

    @Getter
    private String path;

    @Getter
    private String template;
    @Getter
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    private Map<String, TemplateVariable> variables;


    public Template(String fileName, String path, String template, Map<String, TemplateVariable> variables) {
        this.fileName = fileName;
        this.path = path;
        this.template = template;
        this.variables = variables;

        int fileExtensionIndex = fileName.lastIndexOf('.');
        if (fileExtensionIndex > 0 ){
            this.name = fileName.substring(0, fileExtensionIndex);
            this.extension = fileName.substring(fileExtensionIndex+1);
        } else {
            this.name = fileName;
            this.extension = null;
        }
    }

    @Override
    public String toString() {
        return "Template{" +
                "," + "\n"+ "name='" + name + '\'' +
                '}'+ "\n";
    }
}
