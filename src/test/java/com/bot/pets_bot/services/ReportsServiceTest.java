package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.ReportsDTO;
import com.bot.pets_bot.models.entity.Reports;
import com.bot.pets_bot.repositories.ReportsRepository;
import com.bot.pets_bot.telegram_utils.ModelsHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportsServiceTest {

    @Mock
    private ReportsRepository reportsRepository;

    @Mock
    private ModelsHelper modelsHelper;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private ReportsService reportsService;

    private Reports report;

    @BeforeEach
    public void setUp() {
        report = new Reports();
        report.setId(1L);
        report.setText("Test Report");
        report.setPhotoUrl(null);
    }

    @Test
    public void testAddReports_Success() {
        ReportsDTO reportsDTO = new ReportsDTO();
        reportsDTO.setText("Test Report DTO");

        when(modelsHelper.convertFromDTOReports(reportsDTO)).thenReturn(report);
        when(reportsRepository.save(any(Reports.class))).thenReturn(report);

        Reports savedReport = reportsService.addReports(reportsDTO);

        Assertions.assertNotNull(savedReport);
        Assertions.assertEquals("Test Report", savedReport.getText());
        verify(reportsRepository, times(1)).save(any(Reports.class));
    }

    @Test
    public void testGetAllReports_Success() {
        when(reportsRepository.findAll()).thenReturn(List.of(report));

        List<Reports> reportsList = reportsService.getAllReports();

        Assertions.assertNotNull(reportsList);
        Assertions.assertEquals(1, reportsList.size());
        Assertions.assertEquals("Test Report", reportsList.getFirst().getText());
        verify(reportsRepository, times(1)).findAll();
    }

    @Test
    public void testGetReportsById_Found() {
        when(reportsRepository.findById(1L)).thenReturn(Optional.of(report));

        Reports foundReport = reportsService.getReportsById(1L);

        Assertions.assertNotNull(foundReport);
        Assertions.assertEquals("Test Report", foundReport.getText());
        verify(reportsRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetReportsById_NotFound() {
        when(reportsRepository.findById(1L)).thenReturn(Optional.empty());

        Reports foundReport = reportsService.getReportsById(1L);

        Assertions.assertNull(foundReport);
        verify(reportsRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddPhoto_MultipartFile_Success() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("image.jpg");
        when(s3Service.uploadFile(file, "reports1")).thenReturn(true);
        when(reportsRepository.findById(1L)).thenReturn(Optional.of(report));
        when(reportsRepository.save(any(Reports.class))).thenReturn(report);

        Reports updatedReport = reportsService.addPhoto(file, 1L);

        Assertions.assertNotNull(updatedReport);
        Assertions.assertNotNull(updatedReport.getPhotoUrl());
        verify(s3Service, times(1)).uploadFile(file, "reports1");
        verify(reportsRepository, times(1)).save(report);
    }


    @Test
    public void testAddPhoto_File_Success() {
        File file = mock(File.class);
        when(file.getName()).thenReturn("image.jpg");
        when(s3Service.uploadFile(file, "reports1")).thenReturn(true);
        when(reportsRepository.findById(1L)).thenReturn(Optional.of(report));
        when(reportsRepository.save(any(Reports.class))).thenReturn(report);

        Reports updatedReport = reportsService.addPhoto(file, 1L);

        Assertions.assertNotNull(updatedReport);
        Assertions.assertNotNull(updatedReport.getPhotoUrl());
        verify(s3Service, times(1)).uploadFile(file, "reports1");
        verify(reportsRepository, times(1)).save(report);
    }

}
