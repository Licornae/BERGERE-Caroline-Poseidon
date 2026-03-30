package controllerTests;


import com.nnk.springboot.controllers.RuleNameController;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.Application;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.Arrays;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest( controllers = RuleNameController.class)
@ContextConfiguration(classes = Application.class)
@AutoConfigureMockMvc(addFilters = false)
public class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameService ruleNameService;

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
}
