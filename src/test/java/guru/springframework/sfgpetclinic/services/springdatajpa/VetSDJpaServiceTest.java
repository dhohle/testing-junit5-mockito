package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Vet;
import guru.springframework.sfgpetclinic.repositories.VetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VetSDJpaServiceTest {

    @Mock
    private VetRepository vetRepository;

    @InjectMocks
    private VetSDJpaService service;

    private Vet vet1 = new Vet(1L,"A", "B", null);
    @Test
    void deleteById() {
        //given - none

        // when
        this.service.deleteById(1L);

        // then
        then(this.vetRepository).should().deleteById(anyLong());
    }

    @Test
    void findAll() {
        //given
        given(vetRepository.findAll()).willReturn(Arrays.asList(vet1, new Vet(2L, "c", "D", null)));
        //when
        final Set<Vet> foundVets = service.findAll();
        //then
        assertThat(foundVets.size()).isEqualTo(2);
        then(vetRepository).should().findAll();

    }

    @Test
    void findById() {
        // given
        given(vetRepository.findById(anyLong())).willReturn(Optional.of(vet1));
        //when
        final Vet foundVet = service.findById(anyLong());
        //then
        assertThat(foundVet).isNotNull();
        then(vetRepository).should().findById(anyLong());
    }

    @Test
    void save() {
        //given - none
        given(vetRepository.save(any(Vet.class))).willReturn(vet1);
        //when
        service.save(vet1);
        // then
        then(vetRepository).should().save(any(Vet.class));
    }

    @Test
    void delete() {
        //given - none

        //when
        service.delete(vet1);
        // then
        then(vetRepository).should().delete(any(Vet.class));
    }

    @Test
    void testDeleteById() {
        //given - none

        //when
        service.deleteById(1L);
        // then
        then(vetRepository).should().deleteById(anyLong());

    }
}