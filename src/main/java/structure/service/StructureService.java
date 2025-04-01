package structure.service;

import common.Common;
import lombok.Getter;
import org.codehaus.jackson.annotate.JsonIgnore;
import structure.entities.Item;
import structure.entities.Structure;
import structure.entities.Template;
import structure.entities.Variable;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static common.Common.TEMPLATES;
import static structure.service.VarFinder.getVarsFromFile;

public class StructureService {
    @Getter
    private final Structure structure = new Structure();

    private final Logger log;

    public StructureService(String rootDirectory, Logger log) {
        this.log = log;
        try {
            fillStructure(rootDirectory);
        } catch (IOException e) {
            log.warning("Error in filing structure: " + e.getMessage());
        }
    }

    @JsonIgnore
    public Item getRootItem() {

        return structure.getRootItem();
    }

    @JsonIgnore
    public Variable getGlobalVar(String varName) {
        return structure.getGlobalVar(varName);
    }

    @JsonIgnore
    public Template getTemplate(String templateName) {
        return structure.getTemplates().get(templateName);
    }

    @JsonIgnore
    public Item getItemByPath(String path) {

        if (path.length() < structure.getRootItem().getPath().length()) {
            path = (structure.getRootItem().getPath() + path).trim();
        }


        if (path.startsWith(structure.getRootItem().getPath().split(Common.getFileSeparator())[0])) {
            return getItemByPathHelper(structure.getRootItem(), path);
        } else {
            path = (structure.getRootItem().getFileName() + path).trim();
        }

        return getItemByPathHelper(structure.getRootItem(), path);
    }

    private Item getItemByPathHelper(Item currentItem, String path) {
        if (currentItem.getPath().equals(path)) {
            return currentItem;
        }

        for (Map.Entry<String, Item> entryItem : currentItem.getItems().entrySet()) {
            if (entryItem.getValue().getIsDirectory()) {

                if (entryItem.getValue().getPath().equals(path)) {
                    return entryItem.getValue();
                }

                Item item = getItemByPathHelper(entryItem.getValue(), path);
                if (item != null) {
                    return item;
                }
            }
        }

        return null;
    }

    private void fillStructure(String rootDirectory) throws IOException {
        File directory = new File(rootDirectory);

        log.info("Finding source directory...");
        if (!directory.isDirectory()) {
            return;
        }
        log.info("Starting to fill structure");
        Item rootItem = new Item(directory.getName(), directory.getPath());
        rootItem.setIsDirectory(true);
        structure.setRootItem(fillStructureHelper(directory, rootItem)); //тут фигня
        log.info("Structure created");
    }

    private Item fillStructureHelper(File directory, Item item) throws IOException {

        File[] files = directory.listFiles();

        assert files != null;
        if (files.length == 0) {
            return null;
        }

        Arrays.sort(files, new FileComparator());

        for (File file : files) {
            Item newItem = new Item(file.getName(), file.getPath());
            if (file.isDirectory()) {
                if (file.getName().equals(TEMPLATES)) {
                    fillTemplates(file);

                } else {
                    newItem.setIsDirectory(true);
                    Item tempItem = fillStructureHelper(new File(file.getPath()), newItem);
                    if (tempItem == null) {
                        item.addItem(newItem);
                    } else {
                        item.addItem(tempItem);
                    }

                }
            }

            if (file.isFile()) {
                VarFinderEntity varFinderEntity = getVarsFromFile(file);
                newItem.setVars(varFinderEntity.getLocalVars());
                newItem.setIsDirectory(false);
                structure.addGlobalVars(varFinderEntity.getGlogalVars());
                item.addItem(newItem);
            }
        }

        return item;
    }

    private void fillTemplates(File templateDirectory) throws IOException {
        File[] templates = templateDirectory.listFiles();
        for (File templateFile : templates) {

            InputStream inputStream = new FileInputStream(templateFile);

            StringBuilder templateStringBuilder = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                boolean isFirstLine = true;
                while ((line = reader.readLine()) != null) {

                    if (isFirstLine) {
                        templateStringBuilder.append(line);
                        isFirstLine = false;
                    } else {
                        templateStringBuilder.append("\n");
                        templateStringBuilder.append(line);
                    }

                }
            }

            structure.addTemplate(new Template(templateFile.getName(), templateFile.getPath(), templateStringBuilder.toString(), new HashMap<>()));
        }
    }

    private static class FileComparator implements Comparator<File> {
        @Override
        public int compare(File file1, File file2) {
            return file1.getName().compareTo(file2.getName());
        }
    }

}
