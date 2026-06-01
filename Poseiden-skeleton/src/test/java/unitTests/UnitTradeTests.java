package unitTests;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.TradeService;
import com.nnk.springboot.services.TradeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
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
public class UnitTradeTests {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private TradeServiceImpl tradeService;

    @Test
    public void findAll_shouldReturnList() {

        Trade trade1 = new Trade();
        trade1.setTradeId(1);
        trade1.setAccount("Account1");

        Trade trade2 = new Trade();
        trade2.setTradeId(2);
        trade2.setAccount("Account2");

        when(tradeRepository.findAll()).thenReturn(Arrays.asList(trade1, trade2));

        List<Trade> result = tradeService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    public void findById_shouldReturnTrade() {

        Trade trade = new Trade();
        trade.setTradeId(1);

        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        Trade result = tradeService.findById(1);

        assertThat(result).isNotNull();
        assertThat(result.getTradeId()).isEqualTo(1);
    }

    @Test
    public void findById_shouldThrowException_whenNotFound() {

        when(tradeRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tradeService.findById(999))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Trade not found for id: 999");
    }

    @Test
    public void save_shouldReturnSavedTrade() {

        Trade trade = new Trade();
        trade.setAccount("Test");

        when(tradeRepository.save(trade)).thenReturn(trade);

        Trade result = tradeService.save(trade);

        assertThat(result.getAccount()).isEqualTo("Test");
        verify(tradeRepository, times(1)).save(trade);
    }

    @Test
    public void delete_shouldCallRepository() {

        int TradeId = 1;

        when(tradeRepository.existsById(TradeId)).thenReturn(true);
        doNothing().when(tradeRepository).deleteById(TradeId);

        tradeService.deleteById(TradeId);

        verify(tradeRepository, times(1)).deleteById(1);
    }
}
