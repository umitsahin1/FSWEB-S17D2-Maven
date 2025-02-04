package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.JuniorDeveloper;
import com.workintech.s17d2.model.MidDeveloper;
import com.workintech.s17d2.model.SeniorDeveloper;
import jakarta.annotation.PostConstruct;
import com.workintech.s17d2.model.Developer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.workintech.s17d2.tax.Taxable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    public Map<Integer, Developer> developers;
    private final Taxable taxable;


    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public void init() {

        this.developers = new HashMap<>();
        developers.put(1, new SeniorDeveloper(1, "umit", 2000d));
    }


    @GetMapping
    public List<Developer> getAllDevelopers() {
        return developers.values().stream().collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public Developer getUniqDevelopers(@PathVariable Integer id) {
        Developer developer = developers.get(id);

        if (developer == null) {
            throw new RuntimeException("Developer with ID " + id + " not found.");
        }
        return developer;
    }

    @PostMapping
    public ResponseEntity<Developer>addDeveloper(@RequestBody Developer developer) {
        Developer createdDeveloper;
        switch (developer.getExperience()) {
            case JUNIOR:
                createdDeveloper = new JuniorDeveloper(developer.getId(), developer.getName(), developer.getSalary());
                break;
            case MID:
                createdDeveloper = new MidDeveloper(developer.getId(), developer.getName(), developer.getSalary());
                break;
            case SENIOR:
                createdDeveloper = new SeniorDeveloper(developer.getId(), developer.getName(), developer.getSalary());
                break;
            default:
                throw new IllegalArgumentException("Unknown experience type");
        }


        return new ResponseEntity<>(createdDeveloper, HttpStatus.CREATED); // 201 Created
    }

    @PutMapping("/{id}")
    public void updateDeveloper(@PathVariable Integer id, @RequestBody Developer developer) {
        developers.put(id, developer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeveloper(@PathVariable Integer id) {

        if (developers.containsKey(id)) {
            developers.remove(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found
        }
    }


}



