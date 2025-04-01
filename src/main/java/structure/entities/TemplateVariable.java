package structure.entities;

import lombok.Data;
import lombok.Getter;

@Data
public class TemplateVariable {
    @Getter
    String name;

    @Getter
    int position;
}
