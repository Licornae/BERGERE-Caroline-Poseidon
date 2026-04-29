package unitTests;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.services.CurvePointServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnitCurvePointTests {

    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    private CurvePointServiceImpl curvePointService;

    @Test
    public void findAll_shouldReturnAllCurvePoint(){

        LocalDate asOfDate1 = LocalDate.parse("2026-04-15");
        LocalDate asOfDate2 = LocalDate.parse("2026-04-16");

        //Courbe EURIBOR au 15/04/2026
        CurvePoint cp1 = new CurvePoint();
        cp1.setId(1);
        cp1.setCurveId(101);
        cp1.setAsOfDate(asOfDate1);
        cp1.setTerm(0.5);
        cp1.setValue(0.5);
        cp1.setCreationDate(new Timestamp(System.currentTimeMillis()));

        CurvePoint cp2 = new CurvePoint();
        cp2.setId(2);
        cp2.setCurveId(101);
        cp2.setAsOfDate(asOfDate1);
        cp2.setTerm(5.0);
        cp2.setValue(1.2);
        cp2.setCreationDate(new Timestamp(System.currentTimeMillis()));

        CurvePoint cp3 = new CurvePoint();
        cp3.setId(3);
        cp3.setCurveId(101);
        cp3.setAsOfDate(asOfDate1);
        cp3.setTerm(2.0);
        cp3.setValue(0.9);
        cp3.setCreationDate(new Timestamp(System.currentTimeMillis()));

        // Courbe ID 102 (ex: Obligations d'État françaises) pour une autre date (16/04/2026)
        CurvePoint cp4 = new CurvePoint();
        cp4.setId(4);
        cp4.setCurveId(102);
        cp4.setAsOfDate(asOfDate2);
        cp4.setTerm(0.5);
        cp4.setValue(0.4);
        cp4.setCreationDate(new Timestamp(System.currentTimeMillis()));

        CurvePoint cp5 = new CurvePoint();
        cp5.setId(5);
        cp5.setCurveId(102);
        cp5.setAsOfDate(asOfDate2);
        cp5.setTerm(1.0);
        cp5.setValue(0.6);
        cp5.setCreationDate(new Timestamp(System.currentTimeMillis()));

        CurvePoint cp6 = new CurvePoint();
        cp6.setId(6);
        cp6.setCurveId(102);
        cp6.setAsOfDate(asOfDate2);
        cp6.setTerm(5.0);
        cp6.setValue(1.1);
        cp6.setCreationDate(new Timestamp(System.currentTimeMillis()));

        List<CurvePoint> mockCurvePoints = Arrays.asList(cp1, cp2, cp3, cp4, cp5, cp6);

        when(curvePointRepository.findAll()).thenReturn(mockCurvePoints);

        List<CurvePoint> result = curvePointService.findAll();

        assertThat(result).hasSize(6);

        assertThat(result.get(0).getCurveId()).isEqualTo(101);
        assertThat(result.get(0).getTerm()).isEqualTo(0.5);

        assertThat(result.get(1).getCurveId()).isEqualTo(101);
        assertThat(result.get(1).getValue()).isEqualTo(1.2);

        assertThat(result.get(4).getCurveId()).isEqualTo(102);
        assertThat(result.get(4).getValue()).isEqualTo(0.6);
        
    }

    @Test
    public void findById_shouldReturnCorrespondingCurvePoint() {
        CurvePoint cp = new CurvePoint();
        cp.setId(1);
        cp.setCurveId(101);
        cp.setTerm(0.5);
        cp.setValue(1.2);
        cp.setCreationDate(new Timestamp(System.currentTimeMillis()));

        when(curvePointRepository.findById(1)).thenReturn(Optional.of(cp));

        Optional<CurvePoint> result = curvePointService.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getCurveId()).isEqualTo(101);
        assertThat(result.get().getTerm()).isEqualTo(0.5);
        assertThat(result.get().getValue()).isEqualTo(1.2);
    }

    @Test
    public void findById_shouldThrowExceptionWhenCurvePointDoesNotExist() {
        when(curvePointRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> curvePointService.findById(999))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("CurvePoint not found for id: 999");
    }

    @Test
    public void save_shouldReturnSavedCurvePoint() {
        CurvePoint cpToSave = new CurvePoint();
        cpToSave.setCurveId(101);
        cpToSave.setTerm(1.0);
        cpToSave.setValue(0.8);
        cpToSave.setCreationDate(new Timestamp(System.currentTimeMillis()));

        CurvePoint savedCp = new CurvePoint();
        savedCp.setId(1);
        savedCp.setCurveId(101);
        savedCp.setTerm(1.0);
        savedCp.setValue(0.8);
        savedCp.setCreationDate(new Timestamp(System.currentTimeMillis()));

        when(curvePointRepository.save(cpToSave)).thenReturn(savedCp);

        CurvePoint result = curvePointService.save(cpToSave);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getCurveId()).isEqualTo(101);
        assertThat(result.getTerm()).isEqualTo(1.0);
        assertThat(result.getValue()).isEqualTo(0.8);
    }

    @Test
    public void deleteById_shouldDeleteCurvePoint() {
        Integer cpId = 1;

        when(curvePointRepository.existsById(cpId)).thenReturn(true);
        doNothing().when(curvePointRepository).deleteById(cpId);

        curvePointService.deleteById(cpId);

        verify(curvePointRepository, times(1)).deleteById(cpId);
    }

}
