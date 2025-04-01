package structure.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class Variable {
    @Getter
    private String name;

    @Getter
    private String value;

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                "," + "\n"+ "value='" + value + '\'' +
                '}'+ "\n";
    }
}
