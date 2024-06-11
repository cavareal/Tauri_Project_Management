package fr.eseo.tauri.unit.service;

import fr.eseo.tauri.model.*;
import fr.eseo.tauri.model.enumeration.RoleType;
import fr.eseo.tauri.repository.StudentRepository;
import fr.eseo.tauri.repository.ValidationFlagRepository;
import fr.eseo.tauri.service.AuthService;
import fr.eseo.tauri.service.TeamService;
import fr.eseo.tauri.service.UserService;
import fr.eseo.tauri.service.ValidationFlagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidationFlagServiceTest {

    @Mock
    AuthService authService;

    @Mock
    ValidationFlagRepository validationFlagRepository;

    @Mock
    UserService userService;

    @Mock
    TeamService teamService;

    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    ValidationFlagService validationFlagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getValidationFlagByAuthorIdShouldReturnFlagWhenAuthorized() {
        when(validationFlagRepository.findByAuthorIdAndFlagId(anyInt(), anyInt())).thenReturn(new ValidationFlag());

        ValidationFlag result = validationFlagService.getValidationFlagByAuthorId(1, 1);

        assertNotNull(result);
    }

    @Test
    void getAllValidationFlagsShouldReturnFlagsWhenAuthorized() {
        when(validationFlagRepository.findAllByFlag(anyInt())).thenReturn(Arrays.asList(new ValidationFlag(), new ValidationFlag()));

        List<ValidationFlag> result = validationFlagService.getAllValidationFlags( 1);

        assertEquals(2, result.size());
    }

    @Test
    void getAllValidationFlagsShouldReturnEmptyListWhenNoFlags() {
        when(validationFlagRepository.findAllByFlag(anyInt())).thenReturn(Collections.emptyList());

        List<ValidationFlag> result = validationFlagService.getAllValidationFlags(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void updateValidationFlagShouldUpdateFlagWhenAuthorizedAndConfirmedIsNotNull() {
        Integer flagId = 1;
        Integer authorId = 1;
        ValidationFlag updatedValidationFlag = new ValidationFlag();
        updatedValidationFlag.confirmed(true);

        when(validationFlagService.getValidationFlagByAuthorId(flagId, authorId)).thenReturn(new ValidationFlag());

        validationFlagService.updateValidationFlag(flagId, authorId, updatedValidationFlag);

        verify(validationFlagRepository, times(1)).save(any(ValidationFlag.class));
    }


    @Test
    void createValidationFlagsShouldNotCreateFlagsWhenUserIsNotOptionStudent() {
        Flag flag = new Flag().author(new User().id(1));
        List<RoleType> roles = Collections.singletonList(RoleType.TEAM_MEMBER);

        when(userService.getRolesByUserId(flag.author().id())).thenReturn(roles);

        validationFlagService.createValidationFlags(flag);

        verify(validationFlagRepository, times(0)).save(any(ValidationFlag.class));
    }

    @Test
    void createValidationFlagShouldCreateFlagWhenUserExists() {
        Integer flagId = 1;
        Integer authorId = 1;
        ValidationFlag validationFlag = new ValidationFlag().authorId(authorId);
        User user = new User().id(authorId);

        when(userService.getUserById(authorId)).thenReturn(user);

        validationFlagService.createValidationFlag(flagId, validationFlag);

        verify(validationFlagRepository, times(1)).save(any(ValidationFlag.class));
    }

    @Test
    void createValidationFlagShouldThrowExceptionWhenUserDoesNotExist() {
        Integer flagId = 1;
        Integer authorId = 1;
        ValidationFlag validationFlag = new ValidationFlag().authorId(authorId);

        when(userService.getUserById(authorId)).thenReturn(null);

       assertDoesNotThrow(() -> validationFlagService.createValidationFlag(flagId, validationFlag));
    }

    @Test
    void createValidationFlagsShouldNotCreateFlagsWhenNoStudentsInTeams() {
        Flag flag = new Flag().author(new User().id(1));
        Student firstStudent = new Student().team(new Team().id(1));
        Student secondStudent = new Student().team(new Team().id(2));
        flag.firstStudent(firstStudent).secondStudent(secondStudent);


        when(userService.getRolesByUserId(flag.author().id())).thenReturn(Collections.singletonList(RoleType.OPTION_STUDENT));
        when(teamService.getStudentsByTeamId(flag.firstStudent().team().id())).thenReturn(Collections.emptyList());
        when(teamService.getStudentsByTeamId(flag.secondStudent().team().id())).thenReturn(Collections.emptyList());

        validationFlagService.createValidationFlags(flag);

        verify(validationFlagRepository, times(0)).save(any(ValidationFlag.class));
    }

}
