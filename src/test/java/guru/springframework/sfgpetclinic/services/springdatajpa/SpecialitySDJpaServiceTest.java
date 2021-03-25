package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService service;

    @Test
    void testDeleteByObject() {
        //given
        Speciality speciality = new Speciality();

        //when
        service.delete(speciality);

        //then
        then(specialtyRepository).should(timeout(100)).delete(any(Speciality.class));
//        verify(specialtyRepository, times(1)).delete(any(Speciality.class));

    }


    @Test
    void findByIdBddTest() {
        //given
        Speciality speciality = new Speciality();

        given(specialtyRepository.findById(1L)).willReturn(Optional.of(new Speciality()));

        //when
        Speciality foundSpeciality = service.findById(1L);

        //then
        assertThat(foundSpeciality).isNotNull();

        then(specialtyRepository).should().findById(anyLong());// times(1) <- is default
        then(specialtyRepository).should(times(1)).findById(anyLong());
        then(specialtyRepository).shouldHaveNoMoreInteractions();


    }


    @Test
    void findByIdTest() {
        //given
        Speciality speciality = new Speciality();

        when(specialtyRepository.findById(anyLong())).thenReturn(Optional.of(speciality));

        //when
        Speciality returnSpeciality1 = service.findById(1L);

        // then

        assertThat(returnSpeciality1).isNotNull();

        verify(specialtyRepository, times(1)).findById(anyLong());
    }


    @Test
    void deleteBDD() {
        //Given - none


        // when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        verify(specialtyRepository, times(2)).deleteById(anyLong());

        verify(specialtyRepository, atLeastOnce()).deleteById(anyLong());
        verify(specialtyRepository, atMost(5)).deleteById(anyLong());

        verify(specialtyRepository, never()).deleteById(5L);
    }

    @Test
    void delete() {
        service.deleteById(1L);
        verify(specialtyRepository, times(1)).deleteById(anyLong());
        service.deleteById(1L);
        verify(specialtyRepository, times(2)).deleteById(anyLong());

        verify(specialtyRepository, atLeastOnce()).deleteById(anyLong());
        verify(specialtyRepository, atMost(5)).deleteById(anyLong());

        verify(specialtyRepository, never()).deleteById(5L);
    }

    @Test
    void testDelete() {
        this.service.delete(new Speciality());
        verify(specialtyRepository, times(1)).delete(any(Speciality.class));
    }

    @Test
    void testDoThrow() {
        doThrow(new RuntimeException("boem")).when(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> this.service.delete(new Speciality()));

        verify(specialtyRepository).delete(any());
    }

    @Test
    void testFindByIdThrows() {
        given(specialtyRepository.findById(1L)).willThrow(new RuntimeException("boem"));

        assertThrows(RuntimeException.class, () -> service.findById(1L));

        then(specialtyRepository).should().findById(1L);
    }


    @Test
    void testDeleteBDD(){
        willThrow(new RuntimeException("boem")).given(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> service.delete(new Speciality()));

        then(specialtyRepository).should().delete(any());

    }


    @Test
    void testSaveLambda(){
        //given
        final String MATCH_ME = "MATCH_ME";
        Speciality speciality = new Speciality();
        speciality.setDescription(MATCH_ME);

        Speciality savedSpeciality = new Speciality();
        savedSpeciality.setId(1L);

        // need mock
        given(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(MATCH_ME)))).willReturn(savedSpeciality);

        //when
        Speciality returnedSpeciality = service.save(speciality);

        //then
        assertThat(returnedSpeciality.getId()).isEqualTo(1L);

    }

}