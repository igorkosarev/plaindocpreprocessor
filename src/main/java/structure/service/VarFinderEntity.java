package structure.service;

import lombok.Getter;
import lombok.Setter;
import structure.entities.Variable;

import java.util.HashMap;
import java.util.Map;

public class VarFinderEntity {
    @Getter
    @Setter
    private Map<String, Variable> glogalVars = new HashMap<>();

    @Getter
    @Setter
    private Map<String, Variable> localVars = new HashMap<>();
}
