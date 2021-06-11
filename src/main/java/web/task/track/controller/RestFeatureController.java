package web.task.track.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import web.task.track.domain.Feature;
import web.task.track.dto.FeatureDto;
import web.task.track.service.FeatureService;
import java.util.List;

@RestController
@RequestMapping("api/feature")
public class RestFeatureController {

    private final FeatureService featureService;

    @Autowired
    public RestFeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    /*
    Добавление Feature. Доступно только MANAGER
     */
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping ("/add")
    public ResponseEntity<Feature> addFeature(@Validated @RequestBody FeatureDto featureDto){
        return featureService.add(featureDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteFeature(@PathVariable Integer id){
        featureService.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feature> getFeature(@PathVariable Integer id){
        Feature feature = featureService.findById(id);
        return new ResponseEntity<>(feature, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Feature>> getAllFeature(){
        List<Feature> features = featureService.findAll();
        return new ResponseEntity<>(features, HttpStatus.OK);
    }

    /*
    Закрытие Feature. Доступно только MANAGER
     */
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/close/{id}")
    public ResponseEntity<Feature> closeFeature(@PathVariable Integer id){
        Feature feature = featureService.closeFeature(id);
        return new ResponseEntity<>(feature, HttpStatus.OK);
    }

}
