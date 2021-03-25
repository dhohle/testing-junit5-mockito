package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    private VisitRepository visitRepository;

    @InjectMocks
    private VisitSDJpaService service;


    @DisplayName("Visit: Find All Visits")
    @Test
    void findAll() {
        //given
        given(visitRepository.findAll()).willReturn(Arrays.asList(new Visit(), new Visit()));

        //when
        final Set<Visit> visits = this.service.findAll();

        // then
        assertThat(visits.size()).isEqualTo(2);
        then(this.visitRepository).should(times(1)).findAll();
    }


    @Test
    void findById() {
        // given
        given(visitRepository.findById(anyLong())).willReturn(Optional.of(new Visit()));

        //when
        final Visit visit = service.findById(1L);

        // then
        assertThat(visit).isNotNull();
        then(visitRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void save() {
        // given
        given(visitRepository.save(any(Visit.class))).willReturn(new Visit());

        // when
        Visit returnVisit = service.save(new Visit());

        // then
        then(visitRepository).should(times(1)).save(any(Visit.class));
        assertThat(returnVisit).isNotNull();
    }

    @Test
    void delete() {
        //given - none

        //when
        service.delete(new Visit());

        //then
        then(visitRepository).should(times(1)).delete(any(Visit.class));
    }

    @Test
    void deleteById() {
        //given

        //when
        service.deleteById(1L);

        //then
        then(visitRepository).should().deleteById(anyLong());
    }
}