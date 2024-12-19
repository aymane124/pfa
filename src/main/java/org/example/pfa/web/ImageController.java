package org.example.pfa.web;


import org.example.pfa.dao.entites.AnalysisResult;
import org.example.pfa.service.ImageAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageAnalysisService imageAnalysisService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        // Sauvegarder l'image sur le disque ou un dossier temporaire
        String imagePath = "/path/to/save/" + file.getOriginalFilename();

        try {
            file.transferTo(new java.io.File(imagePath));

            // Analyser l'image avec YOLO
            String analysisResult = imageAnalysisService.analyzeImage(imagePath);

            // Sauvegarder le résultat dans la base de données
            imageAnalysisService.saveAnalysisResult(file.getOriginalFilename(), analysisResult);

            return ResponseEntity.ok("Image reçue et analysée avec succès !");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erreur lors de l'analyse de l'image");
        }
    }

    @GetMapping("/results")
    public List<AnalysisResult> getAllResults() {
        return imageAnalysisService.getAllResults();
    }

}
