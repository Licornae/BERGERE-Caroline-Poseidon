package controllerTests;


import com.nnk.springboot.controllers.RuleNameController;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.Application;
import com.nnk.springboot.configuration.SecurityConfig;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest( controllers = RuleNameController.class)
@ContextConfiguration(classes = Application.class)
@Import(SecurityConfig.class)
@AutoConfigureMockMvc
public class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameService ruleNameService;

    //TESTS LIST
    @Test
    @WithMockUser(username = "testuser")
    public void list_shouldReturnRuleNameListView() throws Exception {

        List<RuleName> mockRuleNames = Arrays.asList(
                new RuleName(1, "Rule1", "Description1", "Json1", "Template1", "SqlStr1", "SqlPart1"),
                new RuleName(2, "Rule2", "Description2", "Json2", "Template2", "SqlStr2", "SqlPart2")
        );

        Mockito.when(ruleNameService.findAll()).thenReturn(mockRuleNames);

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"))
                .andExpect(model().attribute("ruleNames", mockRuleNames));
    }

    //TESTS VALIDATION
    @Test
    @WithMockUser(username = "testuser")
    public void validate_withValidData_shouldSaveAndRedirect() throws Exception {
        RuleName ruleName = new RuleName(
                null,
                "Rule1",
                "Description1",
                "Json1",
                "Template1",
                "SqlStr1",
                "SqlPart1");

        Mockito.when(ruleNameService.save(Mockito.any(RuleName.class))).thenReturn(ruleName);

        mockMvc.perform(post("/ruleName/validate").with(csrf())
                        .param("name", "Rule1")
                        .param("description", "Description1")
                        .param("json", "Json1")
                        .param("template", "Template1")
                        .param("sqlStr", "SqlStr1")
                        .param("sqlPart", "SqlPart1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void validate_withInvalidData_shouldReturnAddView() throws Exception {
        mockMvc.perform(post("/ruleName/validate").with(csrf())
                        .param("name", "")
                        .param("description", "Description1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeHasFieldErrors("ruleName", "name"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void validate_withoutCsrf_shouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .param("name", "Rule1")
                        .param("description", "Description1"))
                .andExpect(status().isForbidden());
    }

    //TESTS UPDATE
    @Test
    @WithMockUser(username = "testuser")
    public void showUpdateForm_shouldReturnUpdateViewWithRuleName() throws Exception {
        RuleName mockRuleName = new RuleName(1, "Rule1", "Description1", "Json1", "Template1", "SqlStr1", "SqlPart1");

        Mockito.when(ruleNameService.findById(1)).thenReturn(Optional.of(mockRuleName));

        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeExists("ruleName"))
                .andExpect(model().attribute("ruleName", mockRuleName));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void updateRuleName_withValidData_shouldUpdateAndRedirect() throws Exception {
        RuleName updatedRuleName = new RuleName(
                1,
                "UpdatedRule",
                "UpdatedDescription",
                "UpdatedJson",
                "UpdatedTemplate",
                "UpdatedSqlStr",
                "UpdatedSqlPart");

        Mockito.when(ruleNameService.save(Mockito.any(RuleName.class))).thenReturn(updatedRuleName);

        mockMvc.perform(post("/ruleName/update/1").with(csrf())
                        .param("name", "UpdatedRule")
                        .param("description", "UpdatedDescription")
                        .param("json", "UpdatedJson")
                        .param("template", "UpdatedTemplate")
                        .param("sqlStr", "UpdatedSqlStr")
                        .param("sqlPart", "UpdatedSqlPart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void updateRuleName_withInvalidData_shouldReturnUpdateView() throws Exception {
        mockMvc.perform(post("/ruleName/update/1").with(csrf())
                        .param("name", "")
                        .param("description", "UpdatedDescription"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeHasFieldErrors("ruleName", "name"));
    }

    //TESTS DELETE
    @Test
    @WithMockUser(username = "testuser")
    public void deleteRuleName_shouldDeleteAndRedirect() throws Exception {
        RuleName mockRuleName = new RuleName(
                1, "Rule1",
                "Description1",
                "Json1",
                "Template1",
                "SqlStr1",
                "SqlPart1");

        Mockito.when(ruleNameService.findById(1)).thenReturn(java.util.Optional.of(mockRuleName));
        Mockito.doNothing().when(ruleNameService).deleteById(1);

        mockMvc.perform(post("/ruleName/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        Mockito.verify(ruleNameService, Mockito.times(1)).deleteById(1);
    }

}
