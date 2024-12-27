package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.SchedulesDTO;
import com.bot.pets_bot.models.dto.ShelterInfoDTO;
import com.bot.pets_bot.models.entity.ShelterInfo;
import com.bot.pets_bot.repositories.SheltersInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShelterInfoServiceTest {

    @Mock
    private SheltersInfoRepository shelterInfoRepository;

    @InjectMocks
    private ShelterInfoService shelterInfoService;

    private ShelterInfo shelterInfo;

    @BeforeEach
    public void setUp() {
        shelterInfo = new ShelterInfo();
        shelterInfo.setId(1L);
        shelterInfo.setName("Test Shelter");
        shelterInfo.setDescription("Test Description");
        shelterInfo.setAddress("123 Test Street");
    }

    @Test
    public void testGetSheltersInfo_Success() {
        when(shelterInfoRepository.findAll()).thenReturn(List.of(shelterInfo));

        List<ShelterInfo> shelters = shelterInfoService.getSheltersInfo();

        Assertions.assertNotNull(shelters);
        Assertions.assertEquals(1, shelters.size());
        Assertions.assertEquals("Test Shelter", shelters.getFirst().getName());
        verify(shelterInfoRepository, times(1)).findAll();
    }

    @Test
    public void testAddShelterInfo_Success() {
        SchedulesDTO schedulesDTO = new SchedulesDTO();

        schedulesDTO.setMonday("11-11");
        schedulesDTO.setTuesday("22-22");
        schedulesDTO.setWednesday("33-33");
        schedulesDTO.setThursday("44-44");
        schedulesDTO.setFriday("55-55");
        schedulesDTO.setSaturday("66-66");
        schedulesDTO.setSunday("77-77");

        ShelterInfoDTO dto = new ShelterInfoDTO();
        dto.setName("New Shelter");
        dto.setDescription("New Description");
        dto.setAddress("456 New Street");
        dto.setSchedules(schedulesDTO);

        ShelterInfo newShelterInfo = new ShelterInfo();
        newShelterInfo.setId(2L);
        newShelterInfo.setName("New Shelter");
        newShelterInfo.setDescription("New Description");
        newShelterInfo.setAddress("456 New Street");

        when(shelterInfoRepository.save(any(ShelterInfo.class))).thenReturn(newShelterInfo);

        ShelterInfo savedShelter = shelterInfoService.addShelterInfo(dto);

        Assertions.assertNotNull(savedShelter);
        Assertions.assertEquals("New Shelter", savedShelter.getName());
        verify(shelterInfoRepository, times(1)).save(any(ShelterInfo.class));
    }

    @Test
    public void testGetShelterInfoById_Found() {
        when(shelterInfoRepository.findById(1L)).thenReturn(Optional.of(shelterInfo));

        ShelterInfo foundShelter = shelterInfoService.getShelterInfoById(1L);

        Assertions.assertNotNull(foundShelter);
        Assertions.assertEquals("Test Shelter", foundShelter.getName());
        verify(shelterInfoRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetShelterInfoById_NotFound() {
        when(shelterInfoRepository.findById(1L)).thenReturn(Optional.empty());

        ShelterInfo foundShelter = shelterInfoService.getShelterInfoById(1L);

        Assertions.assertNull(foundShelter);
        verify(shelterInfoRepository, times(1)).findById(1L);
    }
}
