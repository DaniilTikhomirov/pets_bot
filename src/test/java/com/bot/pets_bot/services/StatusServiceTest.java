package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.StatusDTO;
import com.bot.pets_bot.models.entity.Status;
import com.bot.pets_bot.repositories.StatusRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatusServiceTest {

    private Status status;

    private StatusDTO statusDTO;

    @BeforeEach
    public void setUp() {
        status = new Status();
        status.setId(1);
        status.setStatusName("Test");

        statusDTO = new StatusDTO();
        statusDTO.setStatusName("Test");
    }

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private StatusService statusService;

    @Test
    public void testGetAllStatus() {
        when(statusRepository.findAll()).thenReturn(List.of(status));
        Assertions.assertEquals(1, statusService.getAllStatus().getFirst().getId());
    }

    @Test
    public void testAddStatus() {
        when(statusRepository.save(any(Status.class))).thenReturn(status);

        Assertions.assertEquals(statusService.addStatus(statusDTO).getStatusName(), statusDTO.getStatusName());
    }
}
