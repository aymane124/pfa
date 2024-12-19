package org.example.pfa.service;


import org.example.pfa.dao.entites.AnalysisResult;
import org.example.pfa.dao.repository.AnalysisResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ImageAnalysisService {

    @Autowired
    private AnalysisResultRepository repository;

    public String analyzeImage(String imagePath) throws IOException {
        // Appel à un script Python pour analyser l'image avec YOLO
        Process process = new ProcessBuilder("python", "analyze.py", imagePath).start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        StringBuilder result = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            result.append(line).append("\n");
        }
        return result.toString();
    }

    public void saveAnalysisResult(String imageName, String detectionDetails) {
        AnalysisResult result = new AnalysisResult();
        result.setImageName(imageName);
        result.setDetectionDetails(detectionDetails);
        result.setAnalysisDate(LocalDateTime.now());
        repository.save(result);
    }
    public List<AnalysisResult> getAllResults() {
        return repository.findAll();  // Utilise la méthode de Spring Data JPA pour récupérer tous les résultats
    }
}


