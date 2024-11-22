
import astrea.generators.OwlGenerator;
import astrea.model.ShaclFromOwl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

public class AstreaBFOProcessor {

    public static void main(String[] args) {
        try {
            // Load BFO ontology from the specified file path
            //owl file
            String bfoPath = "";

            //todo load the owl file into a buffer
            Model bfoModel = loadOntology(bfoPath);
            System.out.println("BFO Ontology Loaded Successfully.");

            // Generate SHACL shapes using Astrea and the BFO path
            File outputShapes = runAstrea(bfoModel);
            System.out.println("Generated SHACL Shapes at: " + outputShapes.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Model loadOntology(String filePath) throws Exception {
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(filePath);
        if (in == null) {
            throw new IllegalArgumentException("File: " + filePath + " not found.");
        }
        model.read(in, null);
        return model;
    }

    private static File runAstrea(Model ontologyModel) throws Exception {
        // Set up the directory to save SHACL shapes
        String outputPath = "output/shapes.ttl";
        File outputFile = new File(outputPath);

        // Ensure parent directories exist
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }

        // Initialize the SHACL generator
        ShaclFromOwl sharper = new OwlGenerator();

        // Generate SHACL shapes using the Jena model
        //Model shapes = sharper.fromModel(ontologyModel);
        Model shapes = sharper.fromURL("");
        // Model shapes = sharper.fromOwl()

        // Save the generated SHACL shapes to a file
        try (FileWriter writer = new FileWriter(outputFile)) {
            shapes.write(writer, "TURTLE");
        }

        return outputFile;
    }
}