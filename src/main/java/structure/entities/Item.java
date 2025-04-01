package structure.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Item {
    @Getter
    String fileName;

    @Getter
    String name;

    @Getter
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    String extension;

    @Getter
    @Setter
    Boolean isDirectory;

    @Getter
    @Setter
    String path;

    @Getter
    @Setter
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    Map<String, Variable> vars = new HashMap<>();
    @Getter
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    Map<String, Item> items = new LinkedHashMap<>();

    public Item(String fileName, String path) {
        this.fileName = fileName;
        this.path = path;

        int fileExtensionIndex = fileName.lastIndexOf('.');
        if (fileExtensionIndex > 0) {
            this.name = fileName.substring(0, fileExtensionIndex);
            this.extension = fileName.substring(fileExtensionIndex + 1);
        } else {
            this.name = fileName;
            this.extension = null;
        }
    }

    public void addItem(Item item) {
        items.put(item.getName(), item);
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", vars=" + vars +
                ", items=" + items +
                '}';
    }

}
