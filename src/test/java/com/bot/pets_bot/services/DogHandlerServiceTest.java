package com.bot.pets_bot.services;

import com.bot.pets_bot.exeptions.BadPage;
import com.bot.pets_bot.models.entity.DogHandler;
import com.bot.pets_bot.repositories.DogHandlerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DogHandlerServiceTest {

    private DogHandler dogHandler;

    @Mock
    private DogHandlerRepository dogHandlerRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private DogHandlerService dogHandlerService;

    @BeforeEach
    public void setUp() {
        dogHandler = new DogHandler();
        dogHandler.setId(1L);
        dogHandler.setName("John");
    }

    @Test
    public void testGetDogHandler_Found() {
        when(dogHandlerRepository.findById(1L)).thenReturn(Optional.of(dogHandler));

        DogHandler foundHandler = dogHandlerService.getDogHandler(1L);
        Assertions.assertNotNull(foundHandler);
        Assertions.assertEquals(dogHandler.getId(), foundHandler.getId());
        verify(dogHandlerRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetDogHandler_NotFound() {
        when(dogHandlerRepository.findById(2L)).thenReturn(Optional.empty());

        DogHandler foundHandler = dogHandlerService.getDogHandler(2L);
        Assertions.assertNull(foundHandler);
        verify(dogHandlerRepository, times(1)).findById(2L);
    }

    @Test
    public void testAddPhoto_Success() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("photo.jpg");
        when(dogHandlerRepository.findById(1L)).thenReturn(Optional.of(dogHandler));
        when(s3Service.uploadFile(file, "dog_handler1")).thenReturn(true);
        when(s3Service.getExtension("photo.jpg")).thenReturn("jpg");
        when(dogHandlerRepository.save(any(DogHandler.class))).thenReturn(dogHandler);

        DogHandler updatedHandler = dogHandlerService.addPhoto(file, 1L);
        Assertions.assertNotNull(updatedHandler.getPhotoUrl());
        verify(dogHandlerRepository, times(1)).findById(1L);
        verify(dogHandlerRepository, times(1)).save(any(DogHandler.class));
    }

    @Test
    public void testAddPhoto_DogHandlerNotFound() {
        MultipartFile file = mock(MultipartFile.class);
        when(dogHandlerRepository.findById(1L)).thenReturn(Optional.empty());

        DogHandler result = dogHandlerService.addPhoto(file, 1L);
        Assertions.assertNull(result);
        verify(dogHandlerRepository, times(1)).findById(1L);
        verify(s3Service, times(0)).uploadFile(any(MultipartFile.class), anyString());
    }


    @Test
    public void testGetDogHandlerPage_ValidPage() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<DogHandler> page = new PageImpl<>(List.of(dogHandler));
        when(dogHandlerRepository.findAll(pageRequest)).thenReturn(page);

        List<DogHandler> handlers = dogHandlerService.getDogHandlerPage(1, 10);
        Assertions.assertEquals(1, handlers.size());
        Assertions.assertEquals(dogHandler.getName(), handlers.getFirst().getName());
        verify(dogHandlerRepository, times(1)).findAll(pageRequest);
    }

    @Test
    public void testGetDogHandlerPage_InvalidPage() {
        Assertions.assertThrows(BadPage.class, () -> dogHandlerService.getDogHandlerPage(0, 10));
        verify(dogHandlerRepository, times(0)).findAll(any(PageRequest.class));
    }
}
