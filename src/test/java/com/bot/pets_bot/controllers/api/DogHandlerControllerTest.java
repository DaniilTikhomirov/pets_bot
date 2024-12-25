package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.entity.DogHandler;
import com.bot.pets_bot.repositories.DogHandlerRepository;
import com.bot.pets_bot.services.DogHandlerService;
import com.bot.pets_bot.services.S3Service;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DogHandlerController.class)
public class DogHandlerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private DogHandlerService dogHandlerService;

    @MockBean
    private DogHandlerRepository dogHandlerRepository;

    @MockBean
    private S3Service s3Service;

    private DogHandler generateDogHandler() {
        DogHandler dogHandler = new DogHandler();
        dogHandler.setId(1L);
        dogHandler.setName("John Doe");
        dogHandler.setPhotoUrl("http://example.com/photo.jpg");
        return dogHandler;
    }

    private JSONObject getDogHandlerJson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "John Doe");
        return jsonObject;
    }

    @Test
    public void testGetDogHandlerById() throws Exception {
        DogHandler dogHandler = generateDogHandler();

        when(dogHandlerRepository.findById(anyLong())).thenReturn(Optional.of(dogHandler));

        mockMvc.perform(get("/bot/dog_handler/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(dogHandler.getName()))
                .andExpect(jsonPath("$.photoUrl").value(dogHandler.getPhotoUrl()));
    }

    @Test
    public void testGetDogHandlerByPage() throws Exception {
        DogHandler dogHandler = generateDogHandler();

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<DogHandler> dogHandlers = new PageImpl<>(Collections.singletonList(dogHandler));

        when(dogHandlerRepository.findAll(pageRequest)).thenReturn(dogHandlers);

        mockMvc.perform(get("/bot/dog_handler/get_page")
                        .param("page", "1")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(dogHandler.getName()))
                .andExpect(jsonPath("$[0].photoUrl").value(dogHandler.getPhotoUrl()));
    }

    @Test
    public void testAddPhoto() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "photo.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image".getBytes()
        );

        DogHandler dogHandler = generateDogHandler();

        when(dogHandlerRepository.findById(anyLong())).thenReturn(Optional.of(dogHandler));
        when(s3Service.uploadFile(any(MultipartFile.class), anyString())).thenReturn(true);
        when(dogHandlerRepository.save(any(DogHandler.class))).thenReturn(dogHandler);

        mockMvc.perform(multipart("/bot/dog_handler/add_photo/1")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.photoUrl").value(dogHandler.getPhotoUrl()));
    }
}
