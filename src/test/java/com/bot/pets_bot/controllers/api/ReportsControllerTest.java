package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.ReportsDTO;
import com.bot.pets_bot.models.entity.Reports;
import com.bot.pets_bot.services.ReportsService;
import com.bot.pets_bot.repositories.ReportsRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReportsController.class)
public class ReportsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportsService reportsService;

    @MockBean
    private ReportsRepository reportsRepository;

    @InjectMocks
    private ReportsController reportsController;

    private Reports generateReport() {
        Reports report = new Reports();
        report.setId(1L);
        report.setText("Test Report");
        report.setAccepted(true);
        return report;
    }

    private JSONObject getReportsJSObject() throws JSONException {
        JSONObject jsObject = new JSONObject();
        jsObject.put("text", "Test Report");
        jsObject.put("adopterId", 1L);
        jsObject.put("animalId", 1L);
        jsObject.put("volunteerId", 1L);
        return jsObject;
    }

    @Test
    public void testAddReport() throws Exception {
        Reports report = generateReport();
        ReportsDTO reportsDTO = new ReportsDTO();
        reportsDTO.setText("Test Report");
        reportsDTO.setAdopterId(1L);
        reportsDTO.setAnimalId(1L);
        reportsDTO.setVolunteerId(1L);

        when(reportsService.addReports(any(ReportsDTO.class))).thenReturn(report);

        mockMvc.perform(MockMvcRequestBuilders.post("/bot/reports")
                        .contentType("application/json")
                        .content(getReportsJSObject().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(report.getText()))
                .andExpect(jsonPath("$.accepted").value(report.isAccepted()));
    }

    @Test
    public void testGetAllReports() throws Exception {
        Reports report = generateReport();
        when(reportsService.getAllReports()).thenReturn(List.of(report));

        mockMvc.perform(MockMvcRequestBuilders.get("/bot/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value(report.getText()))
                .andExpect(jsonPath("$[0].accepted").value(report.isAccepted()));
    }
}
