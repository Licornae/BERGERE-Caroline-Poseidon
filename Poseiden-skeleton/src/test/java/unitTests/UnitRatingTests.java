package unitTests;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.ratingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UnitRatingTests {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private ratingServiceImpl ratingService;

    @Test
    public void findAll_shouldReturnAllRating(){

        Rating rating1 = new Rating();
        rating1.setId(1);
        rating1.setMoodysRating("Aa1");
        rating1.setOrderNumber(2);

        Rating rating2 = new Rating();
        rating2.setId(2);
        rating2.setMoodysRating("Ba2");
        rating2.setOrderNumber(8);

        List<Rating> ratingList = Arrays.asList(rating1, rating2);

        when(ratingRepository.findAll()).thenReturn(ratingList);

        List<Rating> result = ratingService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getMoodysRating()).isEqualTo("Aa1");
        assertThat(result.get(0).getOrderNumber()).isEqualTo(2);

        assertThat(result.get(1).getMoodysRating()).isEqualTo("Ba1");
        assertThat(result.get(1).getOrderNumber()).isEqualTo(8);
    }
}
