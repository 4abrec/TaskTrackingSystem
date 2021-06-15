package web.task.track.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(description = "Операции с feature")
public class RestFeatureController {

    private final FeatureService featureService;

    @Autowired
    public RestFeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }


    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping ("/add")
    @ApiOperation(value = "Добавление feature. Доступно только менеджеру")
    public ResponseEntity<Feature> addFeature(@Validated @RequestBody FeatureDto featureDto){
        return featureService.add(featureDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Удаление feature. Доступно только администратору")
    public void deleteFeature(@PathVariable Integer id){
        featureService.deleteById(id);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Получение feature по id")
    public ResponseEntity<Feature> getFeature(@PathVariable Integer id){
        Feature feature = featureService.findById(id);
        return new ResponseEntity<>(feature, HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "Получение всех feature")
    public ResponseEntity<List<Feature>> getAllFeature(){
        List<Feature> features = featureService.findAll();
        return new ResponseEntity<>(features, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/close/{id}")
    @ApiOperation(value = "Закрытие feature. Доступно только менеджеру")
    public ResponseEntity<Feature> closeFeature(@PathVariable Integer id){
        Feature feature = featureService.closeFeature(id);
        return new ResponseEntity<>(feature, HttpStatus.OK);
    }

}
