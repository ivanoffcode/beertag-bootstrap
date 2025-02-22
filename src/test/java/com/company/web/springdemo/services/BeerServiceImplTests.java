package com.company.web.springdemo.services;

import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.exceptions.AuthorizationException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.FilterOptions;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.repositories.BeerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.company.web.springdemo.Helpers.*;

@ExtendWith(MockitoExtension.class)
public class BeerServiceImplTests {

    @Mock
    BeerRepository mockRepository;

    @InjectMocks
    BeerServiceImpl service;

    @Test
    void get_Should_CallRepository() {
        // Arrange
        FilterOptions mockFilterOptions = createMockFilterOptions();
        Mockito.when(mockRepository.get(mockFilterOptions))
                .thenReturn(null);

        // Act
        service.get(mockFilterOptions);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .get(mockFilterOptions);
    }

    @Test
    public void get_Should_ReturnBeer_When_MatchByIdExist() {
        // Arrange
        Beer mockBeer = createMockBeer();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);

        // Act
        Beer result = service.get(mockBeer.getId());

        // Assert
        Assertions.assertEquals(mockBeer, result);
    }

    @Test
    public void create_Should_CallRepository_When_BeerWithSameNameDoesNotExist() {
        // Arrange
        Beer mockBeer = createMockBeer();
        User mockUser = createMockUser();

        Mockito.when(mockRepository.get(mockBeer.getName()))
                .thenThrow(EntityNotFoundException.class);

        // Act
        service.create(mockBeer, mockUser);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .create(mockBeer);
    }

    @Test
    public void create_Should_Throw_When_BeerWithSameNameExists() {
        // Arrange
        Beer mockBeer = createMockBeer();
        User mockUser = createMockUser();

        Mockito.when(mockRepository.get(mockBeer.getName()))
                .thenReturn(mockBeer);

        // Act, Assert
        Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> service.create(mockBeer, mockUser));
    }

    @Test
    void update_Should_CallRepository_When_UserIsCreator() {
        // Arrange
        Beer mockBeer = createMockBeer();
        User mockUserCreator = mockBeer.getCreatedBy();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);

        Mockito.when(mockRepository.get(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);

        // Act
        service.update(mockBeer, mockUserCreator);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockBeer);
    }

    @Test
    void update_Should_CallRepository_When_UserIsAdmin() {
        // Arrange
        User mockUserAdmin = createMockAdmin();
        Beer mockBeer = createMockBeer();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);

        Mockito.when(mockRepository.get(Mockito.anyString()))
                .thenThrow(EntityNotFoundException.class);

        // Act
        service.update(mockBeer, mockUserAdmin);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockBeer);
    }

    @Test
    public void update_Should_CallRepository_When_UpdatingExistingBeer() {
        // Arrange
        Beer mockBeer = createMockBeer();
        User mockUserCreator = mockBeer.getCreatedBy();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);

        Mockito.when(mockRepository.get(Mockito.anyString()))
                .thenReturn(mockBeer);

        // Act
        service.update(mockBeer, mockUserCreator);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .update(mockBeer);
    }

    @Test
    public void update_Should_ThrowException_When_UserIsNotCreatorOrAdmin() {
        // Arrange
        Beer mockBeer = createMockBeer();

        Mockito.when(mockRepository.get(mockBeer.getId()))
                .thenReturn(mockBeer);

        // Act, Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.update(mockBeer, Mockito.mock(User.class)));
    }

    @Test
    public void update_Should_ThrowException_When_BeerNameIsTaken() {
        // Arrange
        Beer mockBeer = createMockBeer();
        User mockUserCreator = mockBeer.getCreatedBy();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);

        Beer mockExistingBeerWithSameName = createMockBeer();
        mockExistingBeerWithSameName.setId(2);

        Mockito.when(mockRepository.get(mockBeer.getName()))
                .thenReturn(mockExistingBeerWithSameName);

        // Act, Assert
        Assertions.assertThrows(
                EntityDuplicateException.class,
                () -> service.update(mockBeer, mockUserCreator));
    }

    @Test
    void delete_Should_CallRepository_When_UserIsCreator() {
        // Arrange
        Beer mockBeer = createMockBeer();
        User mockUserCreator = mockBeer.getCreatedBy();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);

        // Act
        service.delete(1, mockUserCreator);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(1);
    }

    @Test
    void delete_Should_CallRepository_When_UserIsAdmin() {
        // Arrange
        User mockUserAdmin = createMockAdmin();
        Beer mockBeer = createMockBeer();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);

        // Act
        service.delete(1, mockUserAdmin);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1))
                .delete(1);
    }

    @Test
    void delete_Should_ThrowException_When_UserIsNotAdminOrCreator() {
        // Arrange
        Beer mockBeer = createMockBeer();

        Mockito.when(mockRepository.get(Mockito.anyInt()))
                .thenReturn(mockBeer);

        User mockUser = Mockito.mock(User.class);

        // Act, Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> service.delete(1, mockUser));
    }

}
