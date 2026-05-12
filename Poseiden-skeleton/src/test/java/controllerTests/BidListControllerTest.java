package controllerTests;

import com.nnk.springboot.Application;
import com.nnk.springboot.controllers.BidListController;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BidListController.class)
@ContextConfiguration(classes = Application.class)
@AutoConfigureMockMvc(addFilters = false)
public class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidListService bidListService;

    @Test
    @WithMockUser(username = "testuser")
    public void home_shouldReturnListView() throws Exception {

        BidList bid1 = new BidList();
        bid1.setBidListId(1);
        bid1.setAccount("Account1");
        bid1.setType("Type1");
        bid1.setBidQuantity(10.0);

        BidList bid2 = new BidList();
        bid2.setBidListId(2);
        bid2.setAccount("Account2");
        bid2.setType("Type2");
        bid2.setBidQuantity(20.0);

        Mockito.when(bidListService.findAll()).thenReturn(Arrays.asList(bid1, bid2));

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attributeExists("bidLists"));
    }

    @Test
    @WithMockUser
    public void addBidForm_shouldReturnAddView() throws Exception {

        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }

    @Test
    @WithMockUser
    public void validate_withValidData_shouldSaveAndRedirect() throws Exception {

        BidList savedBid = new BidList();
        savedBid.setBidListId(1);

        Mockito.when(bidListService.save(Mockito.any(BidList.class))).thenReturn(savedBid);

        mockMvc.perform(post("/bidList/validate")
                        .param("account", "Account Test")
                        .param("type", "Type Test")
                        .param("bidQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser
    public void validate_withInvalidData_shouldReturnAddView() throws Exception {

        mockMvc.perform(post("/bidList/validate")
                        .param("account", "") // invalide
                        .param("type", "Type Test")
                        .param("bidQuantity", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeHasFieldErrors("bidList", "account"));
    }

    @Test
    @WithMockUser
    public void showUpdateForm_shouldReturnUpdateView() throws Exception {

        BidList bid = new BidList();
        bid.setBidListId(1);
        bid.setAccount("Account");
        bid.setType("Type");
        bid.setBidQuantity(10.0);

        Mockito.when(bidListService.findById(1)).thenReturn((bid));

        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attributeExists("bidList"));
    }

    @Test
    @WithMockUser
    public void updateBid_withValidData_shouldRedirect() throws Exception {

        BidList updatedBid = new BidList();
        updatedBid.setBidListId(1);

        Mockito.when(bidListService.save(Mockito.any(BidList.class))).thenReturn(updatedBid);

        mockMvc.perform(post("/bidList/update/1")
                        .param("account", "Updated")
                        .param("type", "Updated")
                        .param("bidQuantity", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    @WithMockUser
    public void deleteBid_shouldRedirectToList() throws Exception {

        mockMvc.perform(get("/bidList/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        Mockito.verify(bidListService).deleteById(1);
    }
}
