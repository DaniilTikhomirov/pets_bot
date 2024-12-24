package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.OwnerDTO;
import com.bot.pets_bot.models.entity.Owner;
import com.bot.pets_bot.repositories.OwnerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OwnerServiceTest {

    private Owner owner;

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerService ownerService;

    @BeforeEach
    public void setUp() {
        owner = new Owner();
        owner.setId(1L);
        owner.setName("Alice");
    }

    @Test
    public void testAddOwner_Success() {
        OwnerDTO ownerDTO = new OwnerDTO();
        ownerDTO.setName("Alice");

        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

        Owner savedOwner = ownerService.addOwner(ownerDTO);
        Assertions.assertNotNull(savedOwner);
        Assertions.assertEquals(owner.getName(), savedOwner.getName());
        verify(ownerRepository, times(1)).save(any(Owner.class));
    }

    @Test
    public void testGetAllOwners_Success() {
        when(ownerRepository.findAll()).thenReturn(List.of(owner));

        List<Owner> owners = ownerService.getAllOwners();
        Assertions.assertNotNull(owners);
        Assertions.assertEquals(1, owners.size());
        Assertions.assertEquals(owner.getName(), owners.getFirst().getName());
        verify(ownerRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllOwners_EmptyList() {
        when(ownerRepository.findAll()).thenReturn(List.of());

        List<Owner> owners = ownerService.getAllOwners();
        Assertions.assertNotNull(owners);
        Assertions.assertTrue(owners.isEmpty());
        verify(ownerRepository, times(1)).findAll();
    }
}
