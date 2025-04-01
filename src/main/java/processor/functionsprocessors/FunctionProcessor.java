package processor.functionsprocessors;

import structure.service.StructureService;

import java.io.IOException;

import static common.Common.*;
import static processor.functionsprocessors.TemplateFunctionProcessor.processUseTemplate;

public class FunctionProcessor {

    public static String useFunctionProcessor(StructureService structureService, String entry) throws IOException {
        String result = entry;
        String functionProcessorDefinitionString = removeWrapper(entry);
        String[] functionProcessorDefinition = functionProcessorDefinitionString.split(SPLIT_FUNCTION_PROCESSOR_DEFINITION_PATTERN);
        String function = functionProcessorDefinition[0].split("\\.")[0];



        switch (function){
            case FUNCTION_USE_TEMPLATE -> result = processUseTemplate(structureService, functionProcessorDefinition, variablePattern);
        }
        return result;
    }

}
