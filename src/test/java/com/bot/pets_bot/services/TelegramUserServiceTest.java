package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.TelegramUserDTO;
import com.bot.pets_bot.models.entity.TelegramUser;
import com.bot.pets_bot.repositories.TelegramUsersRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TelegramUserServiceTest {

    @Mock
    private TelegramUsersRepository telegramUsersRepository;

    @InjectMocks
    private TelegramUserService telegramUserService;

    private TelegramUser telegramUser;

    @BeforeEach
    public void setUp() {
        telegramUser = new TelegramUser();
        telegramUser.setId(1L);
        telegramUser.setTelegramId(123456L);
        telegramUser.setName("test_user");
    }

    @Test
    public void testGetTelegramUsers_Success() {
        when(telegramUsersRepository.findAll()).thenReturn(List.of(telegramUser));

        List<TelegramUser> users = telegramUserService.getTelegramUsers();

        Assertions.assertNotNull(users);
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(123456L, users.getFirst().getTelegramId());
        verify(telegramUsersRepository, times(1)).findAll();
    }

    @Test
    public void testAddTelegramUser_Success() {
        TelegramUserDTO dto = new TelegramUserDTO();
        dto.setTelegramId(789012L);
        dto.setName("new_user");

        TelegramUser newUser = new TelegramUser();
        newUser.setId(2L);
        newUser.setTelegramId(789012L);
        newUser.setName("new_user");

        when(telegramUsersRepository.save(any(TelegramUser.class))).thenReturn(newUser);

        TelegramUser savedUser = telegramUserService.addTelegramUser(dto);

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(789012L, savedUser.getTelegramId());
        Assertions.assertEquals("new_user", savedUser.getName());
        verify(telegramUsersRepository, times(1)).save(any(TelegramUser.class));
    }

    @Test
    public void testPutTelegramUser_Success() {
        telegramUser.setName("updated_user");

        when(telegramUsersRepository.save(any(TelegramUser.class))).thenReturn(telegramUser);

        TelegramUser updatedUser = telegramUserService.putTelegramUser(telegramUser);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("updated_user", updatedUser.getName());
        verify(telegramUsersRepository, times(1)).save(any(TelegramUser.class));
    }

    @Test
    public void testGetTelegramUserByTelegramId_Found() {
        when(telegramUsersRepository.findByTelegramId(123456L)).thenReturn(Optional.of(telegramUser));

        TelegramUser foundUser = telegramUserService.getTelegramUserByTelegramId(123456L);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals("test_user", foundUser.getName());
        verify(telegramUsersRepository, times(1)).findByTelegramId(123456L);
    }

    @Test
    public void testGetTelegramUserByTelegramId_NotFound() {
        when(telegramUsersRepository.findByTelegramId(123456L)).thenReturn(Optional.empty());

        TelegramUser foundUser = telegramUserService.getTelegramUserByTelegramId(123456L);

        Assertions.assertNull(foundUser);
        verify(telegramUsersRepository, times(1)).findByTelegramId(123456L);
    }

    @Test
    public void testIsNewUserByTelegramId_NewUser() {
        when(telegramUsersRepository.findByTelegramId(123456L)).thenReturn(Optional.empty());

        boolean isNewUser = telegramUserService.isNewUserByTelegramId(123456L);

        Assertions.assertTrue(isNewUser);
        verify(telegramUsersRepository, times(1)).findByTelegramId(123456L);
    }

    @Test
    public void testIsNewUserByTelegramId_ExistingUser() {
        when(telegramUsersRepository.findByTelegramId(123456L)).thenReturn(Optional.of(telegramUser));

        boolean isNewUser = telegramUserService.isNewUserByTelegramId(123456L);

        Assertions.assertFalse(isNewUser);
        verify(telegramUsersRepository, times(1)).findByTelegramId(123456L);
    }
}
