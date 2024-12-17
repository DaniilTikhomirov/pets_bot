package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.VolunteersDTO;
import com.bot.pets_bot.models.entity.Volunteers;
import com.bot.pets_bot.services.VolunteersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/volunteers")
public class VolunteersController {

    private final VolunteersService volunteersService;

    public VolunteersController(VolunteersService volunteersService) {
        this.volunteersService = volunteersService;
    }

    @GetMapping()
    public ResponseEntity<List<Volunteers>> getVolunteers() {
        return ResponseEntity.ok(volunteersService.getVolunteers());
    }

    @PostMapping()
    public ResponseEntity<Volunteers> addVolunteer(@RequestBody VolunteersDTO volunteers) {
        return ResponseEntity.ok(volunteersService.addVolunteers(volunteers));
    }
}
