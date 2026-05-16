package controllerTests;

import com.nnk.springboot.Application;
import com.nnk.springboot.controllers.TradeController;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TradeController.class)
@ContextConfiguration(classes = Application.class)
@AutoConfigureMockMvc(addFilters = false)
public class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @Test
    @WithMockUser
    public void list_shouldReturnTradeListView() throws Exception {

        Trade trade1 = new Trade();
        trade1.setTradeId(1);
        trade1.setAccount("Account1");

        Trade trade2 = new Trade();
        trade2.setTradeId(2);
        trade2.setAccount("Account2");

        when(tradeService.findAll()).thenReturn(Arrays.asList(trade1, trade2));

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attributeExists("trades"));
    }

    @Test
    @WithMockUser
    public void addTradeForm_shouldReturnAddView() throws Exception {

        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    @WithMockUser
    public void validateTrade_withValidData_shouldRedirect() throws Exception {

        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("Account");
        trade.setType("Type");
        trade.setBuyQuantity(10.0);

        when(tradeService.save(any(Trade.class))).thenReturn(trade);

        mockMvc.perform(post("/trade/validate")
                        .param("account", "Account")
                        .param("type", "Type")
                        .param("buyQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    @WithMockUser
    public void showUpdateForm_shouldReturnUpdateView() throws Exception {

        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("Account");

        when(tradeService.findById(1)).thenReturn((trade));

        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    @WithMockUser
    public void updateTrade_withValidData_shouldRedirect() throws Exception {

        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("Updated");

        when(tradeService.save(any(Trade.class))).thenReturn(trade);

        mockMvc.perform(post("/trade/update/1")
                        .param("account", "Updated")
                        .param("type", "Type")
                        .param("buyQuantity", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    @WithMockUser
    public void updateTrade_withInvalidData_shouldReturnUpdateView() throws Exception {

        mockMvc.perform(post("/trade/update/1")
                        .param("account", "")
                        .param("type", "")
                        .param("buyQuantity", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attributeHasFieldErrors("trade", "account", "type"));
    }

    @Test
    @WithMockUser
    public void deleteTrade_shouldRedirectToList() throws Exception {

        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).deleteById(1);
    }
}
