package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.fauxspring.ModelMapImpl;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.services.OwnerService;
import jdk.jfr.BooleanFlag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {


    @Mock
    OwnerService ownerService;

    @Mock
    Model model;

    @InjectMocks
    OwnerController controller;

    @Mock
    BindingResult bindingResult;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp(){
        given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture()))
                .willAnswer(invocation->{
                    List<Owner> owners = new ArrayList<>();
            String name = invocation.getArgument(0);
            if(name.equals("%Buck%")){
                owners.add(new Owner(1l, "Joe", "Buck"));
                return owners;
            }else if(name.equals("%DontFindMe%")){
                return owners;
            }else if (name.equals("%FindMe%")){
                owners.add(new Owner(1l, "Joe", "Boek") );
                owners.add(new Owner(2l, "Harry", "Sjaal") );
                return owners;
            }
            throw new RuntimeException("Invalid Arguemnt");
        });
    }

    @Test
    void processFindFormWildcardStringAnnotation(){
        //given
        Owner owner = new Owner(1l, "Joe", "Buck");
        //when
        String viewName = controller.processFindForm(owner, bindingResult, null);

        //then
        assertThat(stringArgumentCaptor.getValue()).isEqualToIgnoringCase("%Buck%");
        assertThat(viewName).isEqualToIgnoringCase("redirect:/owners/1");
        verifyNoMoreInteractions(model);
    }
    @Test
    void processFindFormWildcardNotFound(){
        //given
        Owner owner = new Owner(1l, "Joe", "DontFindMe");
        //when
        String viewName = controller.processFindForm(owner, bindingResult, null);

        verifyNoMoreInteractions(ownerService);
        //then
        assertThat(stringArgumentCaptor.getValue()).isEqualToIgnoringCase("%DontFindMe%");
        assertThat(viewName).isEqualToIgnoringCase("owners/findOwners");
        verifyZeroInteractions(model);

    }

    @Test
    void processFindFormWildcardFound(){
        //given
        Owner owner = new Owner(1l, "Joe", "FindMe");
        InOrder inOrder = Mockito.inOrder(ownerService, model);

        //when
        String viewName = controller.processFindForm(owner, bindingResult, model);

        //then
        assertThat(stringArgumentCaptor.getValue()).isEqualToIgnoringCase("%FindMe%");
        assertThat(viewName).isEqualToIgnoringCase("owners/ownersList");

        // inorder asserts
        inOrder.verify(ownerService).findAllByLastNameLike(anyString());
        inOrder.verify(model, times(1)).addAttribute(anyString(), anyList());
        verifyZeroInteractions(model);
    }

//    @Test
//    void processCreationFormHasError() {
//        //given
//        Owner owner = new Owner(1l, "Jim", "Bob");
//        given(bindingResult.hasErrors()).willReturn(true);
//
//        //when
//        String viewName = controller.processCreationForm(owner, bindingResult);
//
//        //then
//        assertThat(viewName).isEqualTo("owners/createOrUpdateOwnerForm");
//    }
//    @Test
//    void processCreationFormNoError() {
//        //given
//        Owner owner = new Owner(1l, "Jim", "Bob");
//        Owner savedOwner = new Owner(1l, "Jim", "Bob");
//        given(bindingResult.hasErrors()).willReturn(false);
//        given(ownerService.save(any(Owner.class))).willReturn(savedOwner);
//        //when
//        String viewName = controller.processCreationForm(owner, bindingResult);
//
//        //then
//        assertThat(viewName).isEqualTo("redirect:/owners/1");
//        assertThat(savedOwner).isNotNull();
//        then(ownerService).should().save(any(Owner.class));
//    }
}