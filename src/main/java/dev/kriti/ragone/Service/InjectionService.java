package dev.kriti.ragone.Service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;

// Removing null characters from the PDF content before processing
import org.springframework.ai.document.Document;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InjectionService implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InjectionService.class);
    private final VectorStore vectorStore;

    @Value("classpath:/docs/pdf1.pdf")
    private Resource ragOnePdf;

    public InjectionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!ragOnePdf.exists()) {
            throw new IllegalStateException(
                    "PDF not found: " + ragOnePdf.getDescription());
        }
        var pdfReader = new PagePdfDocumentReader(ragOnePdf);
        var documents = pdfReader.get();
        logger.info("Documents extracted from PDF: {}", documents.size());

        List<Document> cleanedDocuments = documents.stream().map(doc -> new Document(
                doc.getText().replace("\u0000", ""),
                doc.getMetadata()))
                .collect(Collectors.toList());

        TextSplitter textSplitter = TokenTextSplitter.builder().build();

        var chunks = textSplitter.apply(cleanedDocuments);

        logger.info("Chunks generated: {}", chunks.size());

        if (chunks.isEmpty()) {
            logger.warn("No chunks generated. Check whether the PDF contains extractable text.");
            return;
        }

        String fileName = ragOnePdf.getFilename();
        if (fileName == null) {
            throw new IllegalStateException("Unable to determine the PDF file name.");
        }

        var existingChunks = vectorStore.similaritySearch(SearchRequest.builder()
                .query(chunks.get(0).getText())
                .topK(1)
                .filterExpression("file_name == '" + fileName.replace("'", "\\'") + "'")
                .build());
        if (!existingChunks.isEmpty()) {
            logger.info("PDF '{}' is already present in the vector store; skipping ingestion.", fileName);
            return;
        }

        vectorStore.add(chunks);
        logger.info("Successfully submitted {} chunks to the vector store.", chunks.size());

        // This method will be executed after the application context is loaded and
        // right before the Spring Application run method is completed.
        // You can add any initialization logic here if needed.
    }

}
