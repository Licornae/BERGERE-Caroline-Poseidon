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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

        assertThat(result.get(1).getMoodysRating()).isEqualTo("Ba2");
        assertThat(result.get(1).getOrderNumber()).isEqualTo(8);
    }

    @Test
    public void findById_shouldReturnCorrespondingRating() {
        Rating rating = new Rating();
        rating.setId(1);
        rating.setMoodysRating("Aa1");
        rating.setOrderNumber(2);

        when(ratingRepository.findById(1)).thenReturn(java.util.Optional.of(rating));

        Optional<Rating> result = ratingService.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getMoodysRating()).isEqualTo("Aa1");
        assertThat(result.get().getOrderNumber()).isEqualTo(2);
    }

    @Test
    public void findById_shouldReturnExceptionWhenRatingDoesNotExist() {
        when(ratingRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ratingService.findById(999))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rating not found for id: 999");
    }

    @Test
    public void save_shouldReturnSavedRating() {
        Rating ratingToSave = new Rating();
        ratingToSave.setId(1);
        ratingToSave.setMoodysRating("Aa1");
        ratingToSave.setOrderNumber(2);

        Rating savedRating = new Rating();
        savedRating.setId(1);
        savedRating.setMoodysRating("Aa1");
        savedRating.setOrderNumber(2);

        when(ratingRepository.save(ratingToSave)).thenReturn(savedRating);

        Rating actualRating = ratingService.save(ratingToSave);

        assertThat(actualRating).isEqualTo(savedRating);
        assertThat(actualRating.getId()).isEqualTo(savedRating.getId());
        assertThat(actualRating.getMoodysRating()).isEqualTo(savedRating.getMoodysRating());
        assertThat(actualRating.getOrderNumber()).isEqualTo(savedRating.getOrderNumber());
    }
}
