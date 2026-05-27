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
import java.util.Optional;

@WebMvcTest( controllers = RatingController.class )
@ContextConfiguration(classes = Application.class)
@AutoConfigureMockMvc(addFilters = false)
public class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    //TESTS LIST
    @Test
    @WithMockUser(username = "testuser")
    public void list_shouldReturnRatingListView() throws Exception {

        List<Rating> mockRatings = Arrays.asList(
                new Rating(1,"Aa1", "AA+", "AA+", 3),
                new Rating(2,"Aa2", "Aa2", "Aa2", 3)
        );

        Mockito.when(ratingService.findAll()).thenReturn(mockRatings);

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().attributeExists("ratings"))
                .andExpect(model().attribute("ratings", mockRatings));
    }

    //TESTS VALIDATION
    @Test
    @WithMockUser(username = "testuser")
    public void validate_withValidData_shouldSaveRatingAndRedirect() throws Exception {

        Rating rating = new Rating(null, "Aa1","AA+","AA+",3);

        Mockito.when(ratingService.save(Mockito.any(Rating.class))).thenReturn(rating);

        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", "Aa1")
                        .param("sandPRating", "AA+")
                        .param("fitchRating", "AA+")
                        .param("orderNumber", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void validate_withInvalidData_shouldNotSaveRatingAndReturnAddView() throws Exception {
        mockMvc.perform(post("/rating/validate")
                        .param("moodysRating", "Aa1")
                        .param("fitchRating", "AA+")
                        .param("orderNumber", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().attributeHasFieldErrors("rating", "orderNumber"));
    }

    //TESTS UPDATE
    @Test
    @WithMockUser(username = "testuser")
    public void showUpdateForm_shouldReturnUpdateViewWithRating() throws Exception {

        Rating mockRating = new Rating(1, "Aa1", "AA+", "AA+", 3);

        Mockito.when(ratingService.findById(1)).thenReturn(Optional.of(mockRating));

        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeExists("rating"))
                .andExpect(model().attribute("rating", mockRating));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void updateRating_withValidData_shouldUpdateAndRedirect() throws Exception {

        Rating updatedRating = new Rating(1, "Updated", "Updated", "Updated", 5);

        Mockito.when(ratingService.save(Mockito.any(Rating.class))).thenReturn(updatedRating);

        mockMvc.perform(post("/rating/update/1")
                        .param("moodysRating", "Aaa")
                        .param("sandPRating", "AAA")
                        .param("fitchRating", "BBB+")
                        .param("orderNumber", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void updateRating_withInvalidData_shouldReturnUpdateView() throws Exception {

        mockMvc.perform(post("/rating/update/1")
                        .param("moodysRating", "")
                        .param("orderNumber", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().attributeHasFieldErrors("rating", "moodysRating", "orderNumber"));
    }

    //TESTS DELETE
    @Test
    @WithMockUser(username = "testuser")
    public void deleteRating_shouldDeleteAndRedirect() throws Exception {

        Rating mockRating = new Rating(1, "Aa1", "AA+", "AA+", 3);

        Mockito.when(ratingService.findById(1)).thenReturn(Optional.of(mockRating));
        Mockito.doNothing().when(ratingService).deleteById(1);

        mockMvc.perform(post("/rating/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/rating/list"));

        Mockito.verify(ratingService, Mockito.times(1)).deleteById(1);
    }
}
