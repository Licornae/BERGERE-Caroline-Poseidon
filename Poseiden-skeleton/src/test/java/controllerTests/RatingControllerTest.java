package controllerTests;

import com.nnk.springboot.Application;
import com.nnk.springboot.controllers.RatingController;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

@WebMvcTest( controllers = RatingController.class )
@ContextConfiguration(classes = Application.class)
@AutoConfigureMockMvc(addFilters = false)
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @Test
    @WithMockUser(username = "testuser")
    public void list_shouldReturnRatingListView() throws Exception {

        List<Rating> mockRatings = Arrays.asList(
                new Rating(1,"Aa1", "Aa2", "Aa2", 3),
                new Rating(2,"Aa2", "Aa2", "Aa2", 3)
        );

        Mockito.when(ratingService.findAll()).thenReturn(mockRatings);

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("ratings", mockRatings));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void validate_withValidData_shouldSaveRatingAndRedirect() throws Exception {

        Rating rating = new Rating(null, "Aa1","Aa2","Aa2",3);

        Mockito.when(ratingService.save(Mockito.any(Rating.class))).thenReturn(rating);

        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", "Aa1")
                        .param("sandPRating", "Aa2")
                        .param("fitchRating", "Aa2")
                        .param("orderNumber", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

}
