package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.ShelterInfoDTO;
import com.bot.pets_bot.models.entity.ShelterInfo;
import com.bot.pets_bot.services.ShelterInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shelters")
public class SheltersInfoController {
    private final ShelterInfoService shelterInfoService;

    public SheltersInfoController(ShelterInfoService shelterInfoService) {
        this.shelterInfoService = shelterInfoService;
    }

    @GetMapping()
    public ResponseEntity<List<ShelterInfo>> getShelters() {
        return ResponseEntity.ok(shelterInfoService.getSheltersInfo());
    }

    @PostMapping()
    public ResponseEntity<ShelterInfo> addShelter(@RequestBody ShelterInfoDTO shelterInfo) {
        return ResponseEntity.ok(shelterInfoService.addShelterInfo(shelterInfo));
    }

}
