package com.waldor.costcompass.dev.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DevCodeSearchTools {

    @Value("${dev.source-path}")
    private String sourcePath;

    @Value("${dev.frontend-source-path}")
    private String frontendSourcePath;

    @Tool(description = "Search for a text string (e.g. a class name or keyword) in the project's Java source code. Returns file path, line number and line content for each match.")
    public List<String> searchCode(
            @ToolParam(description = "Search term to look for, e.g. 'AiDevChatService' or 'memory'") String query) {
        List<String> hits = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(sourcePath))) {
            paths.filter(p -> p.toString().endsWith(".java")).forEach(p -> {
                try {
                    List<String> lines = Files.readAllLines(p);
                    for (int i = 0; i < lines.size(); i++) {
                        if (lines.get(i).toLowerCase().contains(query.toLowerCase())) {
                            hits.add(p + ":" + (i + 1) + " - " + lines.get(i).trim());
                        }
                    }
                } catch (IOException ignored) {
                }
            });
        } catch (IOException e) {
            hits.add("Could not search source code: " + e.getMessage());
        }
        return hits.stream().limit(30).toList();
    }

    @Tool(description = "List all Java and TSX files in the project (backend and frontend), to get an overview of the codebase structure.")
    public List<String> listProjectFiles() {
        List<String> files = new ArrayList<>();
        for (String root : List.of(sourcePath, frontendSourcePath)) {
            try (Stream<Path> paths = Files.walk(Paths.get(root))) {
                paths.filter(p -> p.toString().endsWith(".java") || p.toString().endsWith(".tsx"))
                        .forEach(p -> files.add(p.toString()));
            } catch (IOException e) {
                files.add("Could not list files in " + root + ": " + e.getMessage());
            }
        }
        return files;
    }

    @Tool(description = "Read the full contents of a specific file, to understand it better before suggesting improvements.")
    public String readFile(@ToolParam(description = "Full path to the file, from listProjectFiles") String path) {
        try {
            return Files.readString(Paths.get(path));
        } catch (IOException e) {
            return "Could not read file: " + e.getMessage();
        }
    }
}