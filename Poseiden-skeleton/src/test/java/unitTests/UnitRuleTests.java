package unitTests;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.services.RuleNameServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitRuleTests {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameServiceImpl ruleNameService;

    @Test
    public void findAll_shouldReturnAllRuleNames(){

        RuleName rule1 = new RuleName();
        rule1.setId(1);
        rule1.setName("Rule 1");
        RuleName rule2 = new RuleName();
        rule2.setId(2);
        rule2.setName("Rule 2");
        List<RuleName> ruleList = Arrays.asList(rule1, rule2);

        when(ruleNameRepository.findAll()).thenReturn(ruleList);

        List<RuleName> result = ruleNameService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Rule 1");
        assertThat(result.get(1).getName()).isEqualTo("Rule 2");
    }

    @Test
    public void findById_shouldReturnCorrespondingRuleName(){
        RuleName ruleName = new RuleName();
        ruleName.setId(1);
        ruleName.setName("Rule 1");

        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName));

        Optional<RuleName> result = ruleNameService.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Rule 1");
    }

    @Test
    public void findById_shouldReturnExceptionWhenRuleDoesNotExist() {

        when(ruleNameRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ruleNameService.findById(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("RuleName not found for id: 999");
    }

    @Test
    public void save_shouldReturnSavedRuleName() {

        RuleName ruleToSave = new RuleName();
        ruleToSave.setName("New rule");

        RuleName savedRule = new RuleName();
        savedRule.setId(1);
        savedRule.setName("New rule");

        when(ruleNameRepository.save(ruleToSave)).thenReturn(savedRule);

        RuleName actualrule = ruleNameService.save(ruleToSave);

        assertThat(actualrule.getId()).isNotNull();
        assertThat(actualrule.getName()).isEqualTo(savedRule.getName());
    }

    @Test
    public void deleteById_shouldDeleteRuleName() {

        int ruleId = 1;

        when(ruleNameRepository.existsById(ruleId)).thenReturn(true);
        doNothing().when(ruleNameRepository).deleteById(ruleId);

        ruleNameService.deleteById(ruleId);

        verify(ruleNameRepository, times(1)).deleteById(ruleId);
    }
}
