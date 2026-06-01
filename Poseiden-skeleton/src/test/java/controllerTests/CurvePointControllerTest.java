package controllerTests;

import com.nnk.springboot.Application;
import com.nnk.springboot.configuration.SecurityConfig;
import com.nnk.springboot.controllers.CurveController;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CurveController.class)
@ContextConfiguration(classes = Application.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
public class CurvePointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurvePointService curvePointService;

    //TESTS LIST
    @Test
    @WithMockUser(username = "testuser")
    public void list_shouldReturnCurvePointListView() throws Exception {

        CurvePoint cp1 = new CurvePoint();
        cp1.setId(1);
        cp1.setCurveId(101);
        cp1.setTerm(0.5);
        cp1.setValue(1.2);
        cp1.setCreationDate(new Timestamp(System.currentTimeMillis()));

        CurvePoint cp2 = new CurvePoint();
        cp2.setId(2);
        cp2.setCurveId(102);
        cp2.setTerm(5.0);
        cp2.setValue(0.8);
        cp2.setCreationDate(new Timestamp(System.currentTimeMillis()));

        List<CurvePoint> mockCurvePoints = Arrays.asList(cp1, cp2);

        when(curvePointService.findAll()).thenReturn(mockCurvePoints);

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("curvePoints"))
                .andExpect(model().attribute("curvePoints", mockCurvePoints));
    }

    //TESTS VALIDATION
    @Test
    @WithMockUser(username = "testuser")
    public void validate_withValidData_shouldSaveCurvePointAndRedirect() throws Exception {

        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setCurveId(10);
        curvePoint.setTerm(1.5);
        curvePoint.setValue(2.0);

        when(curvePointService.save(Mockito.any(CurvePoint.class))).thenReturn(curvePoint);

        mockMvc.perform(post("/curvePoint/validate").with(csrf())
                        .param("curveId", "10")
                        .param("asOfDate", "2020-04-01")
                        .param("term", "1.5")
                        .param("value", "2.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void validate_withInvalidData_shouldNotSaveCurvePointAndReturnAddView() throws Exception {
        mockMvc.perform(post("/curvePoint/validate").with(csrf())
                        .param("asOfDate", "2026-04-01")
                        .param("term", "1.5")
                        .param("value", "2.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeHasFieldErrors("curvePoint", "curveId"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void validate_withCurveIdOutOfRange_shouldReturnAddViewWithCurveIdError() throws Exception {
        mockMvc.perform(post("/curvePoint/validate").with(csrf())
                        .param("curveId", "128")
                        .param("asOfDate", "2020-04-01")
                        .param("term", "1.5")
                        .param("value", "2.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeHasFieldErrors("curvePoint", "curveId"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void validate_withoutCsrf_shouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("curveId", "10")
                        .param("asOfDate", "2020-04-01")
                        .param("term", "1.5")
                        .param("value", "2.0"))
                .andExpect(status().isForbidden());
    }

    //TESTS UPDATE
    @Test
    @WithMockUser(username = "testuser")
    public void showUpdateForm_shouldReturnUpdateViewWithCurvePoint() throws Exception {

        CurvePoint mockCurvePoint = new CurvePoint();
        mockCurvePoint.setId(1);
        mockCurvePoint.setCurveId(10);
        mockCurvePoint.setAsOfDate(LocalDate.parse("2020-04-01"));
        mockCurvePoint.setTerm(1.5);
        mockCurvePoint.setValue(2.0);
        mockCurvePoint.setCreationDate(new Timestamp(System.currentTimeMillis()));

        when(curvePointService.findById(1)).thenReturn(mockCurvePoint);

        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"))
                .andExpect(model().attribute("curvePoint", mockCurvePoint));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void updateCurvePoint_withValidData_shouldUpdateAndRedirect() throws Exception {

        CurvePoint updatedCurvePoint = new CurvePoint();
        updatedCurvePoint.setId(1);
        updatedCurvePoint.setCurveId(10);
        updatedCurvePoint.setAsOfDate(LocalDate.parse("2020-04-01"));
        updatedCurvePoint.setTerm(2.5);
        updatedCurvePoint.setValue(3.0);

        when(curvePointService.save(Mockito.any(CurvePoint.class))).thenReturn(updatedCurvePoint);

        mockMvc.perform(post("/curvePoint/update/1").with(csrf())
                        .param("curveId", "10")
                        .param("asOfDate", "2020-04-01")
                        .param("term", "2.5")
                        .param("value", "3.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void updateCurvePoint_withInvalidData_shouldReturnUpdateView() throws Exception {

        mockMvc.perform(post("/curvePoint/update/1").with(csrf())
                        .param("curveId", "")
                        .param("asOfDate", "2020-04-01")
                        .param("term", "2.5")
                        .param("value", "3.0"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeHasFieldErrors("curvePoint", "curveId"));
    }

    //TESTS DELETE
    @Test
    @WithMockUser(username = "testuser")
    public void deleteCurvePoint_shouldDeleteAndRedirect() throws Exception {
        CurvePoint mockCurvePoint = new CurvePoint();
        when(curvePointService.findById(1)).thenReturn(mockCurvePoint);
        mockMvc.perform(post("/curvePoint/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
        Mockito.verify(curvePointService, Mockito.times(1)).deleteById(1);
    }

}
