package structure.entities;

import lombok.Data;
import lombok.Getter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.File;
import java.util.*;

@Data
public class Structure{
    @Getter
    private Map<String, Variable> globalVars = new HashMap<>();


    @Getter
    private Item rootItem;

    @Getter
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    private Map<String, Template> templates = new HashMap<>();

    public void addGlobalVars(Map<String, Variable> vars){
        globalVars.putAll(vars);
    }
    public Variable getGlobalVar(String varName){
        if (globalVars.isEmpty()){
            return null;
        }

        if (globalVars.containsKey(varName)){
            return globalVars.get(varName);
        }
        return null;
    }


    public void addTemplate(Template template){
        templates.put(template.getName(), template);
    }




}
