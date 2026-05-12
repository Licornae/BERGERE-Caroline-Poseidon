package unitTests;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.BidListServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitBidListTests {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListServiceImpl bidListService;

    @Test
    public void findAll_shouldReturnAllBidLists() {

        BidList bid1 = new BidList();
        bid1.setBidListId(1);
        bid1.setAccount("Account1");

        BidList bid2 = new BidList();
        bid2.setBidListId(2);
        bid2.setAccount("Account2");

        List<BidList> bidLists = Arrays.asList(bid1, bid2);

        when(bidListRepository.findAll()).thenReturn(bidLists);

        List<BidList> result = bidListService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getAccount()).isEqualTo("Account1");
    }

    @Test
    public void findById_shouldReturnBidList() {

        BidList bid = new BidList();
        bid.setBidListId(1);
        bid.setAccount("Test");

        when(bidListRepository.findById(1)).thenReturn(Optional.of(bid));

        BidList result = bidListService.findById(1);

        assertThat(result).isNotNull();
        assertThat(result.getAccount()).isEqualTo("Test");
    }

    @Test
    public void save_shouldSaveBidList() {

        BidList bid = new BidList();
        bid.setAccount("SaveTest");

        when(bidListRepository.save(bid)).thenReturn(bid);

        BidList result = bidListService.save(bid);

        assertThat(result.getAccount()).isEqualTo("SaveTest");
        verify(bidListRepository, times(1)).save(bid);
    }

    @Test
    public void delete_shouldDeleteBidList() {

        when(bidListRepository.existsById(1)).thenReturn(true);
        doNothing().when(bidListRepository).deleteById(1);

        bidListService.deleteById(1);

        verify(bidListRepository, times(1)).deleteById(1);
    }
}
