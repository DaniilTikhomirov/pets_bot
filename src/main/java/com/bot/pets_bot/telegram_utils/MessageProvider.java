package com.bot.pets_bot.telegram_utils;

import com.bot.pets_bot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

/**
 * Класс для обработки сообщений в Telegram-боте.
 * Этот класс расширяет {@link TelegramLongPollingBot} и предоставляет методы для отправки сообщений,
 * работы с клавишами, отправки фотографий и контактов, а также для получения и удаления сообщений.
 */
@Component
@Slf4j
public class MessageProvider extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    /**
     * Конструктор для инициализации бота с использованием конфигурации.
     *
     * @param botConfig Конфигурация бота, содержащая токен.
     */
    public MessageProvider(BotConfig botConfig) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
    }

    /**
     * Метод для обработки входящих обновлений. Пока не используется.
     *
     * @param update Объект {@link Update}, содержащий информацию об обновлении.
     */
    @Override
    public void onUpdateReceived(Update update) {
        // Пока не используется
    }

    /**
     * Получает имя бота.
     *
     * @return Имя бота.
     */
    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    /**
     * Отправляет текстовое сообщение в чат.
     *
     * @param message Сообщение, которое необходимо отправить.
     * @return Отправленное сообщение.
     */
    private Message sendMessage(SendMessage message) {
        Message mess = null;
        try {
            mess = execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
        return mess;
    }

    /**
     * Отправляет сообщение с текстом и клавишами.
     *
     * @param chatId Идентификатор чата, в который отправляется сообщение.
     * @param text   Текст сообщения.
     * @return Отправленное сообщение.
     */
    public Message putMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode(ParseMode.HTML);
        return sendMessage(message);
    }

    /**
     * Отправляет сообщение с текстом и удаляет клавиатуру.
     *
     * @param chatId      Идентификатор чата.
     * @param text        Текст сообщения.
     * @param delKeyBoard Флаг, удалять ли клавиатуру.
     * @return Отправленное сообщение.
     */
    public Message putMessage(long chatId, String text, boolean delKeyBoard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode(ParseMode.HTML);
        if (delKeyBoard) {
            ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
            replyKeyboardRemove.setRemoveKeyboard(true);
            message.setReplyMarkup(replyKeyboardRemove);
        }
        return sendMessage(message);
    }

    /**
     * Отправляет сообщение с текстом и инлайн клавишами.
     *
     * @param chatId               Идентификатор чата.
     * @param text                 Текст сообщения.
     * @param inlineKeyboardMarkup Инлайн клавиши.
     * @return Отправленное сообщение.
     */
    public Message putMessageWithMarkUps(long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(inlineKeyboardMarkup);
        message.setParseMode(ParseMode.HTML);
        return sendMessage(message);
    }

    /**
     * Отправляет сообщение с текстом, инлайн клавишами и опцией удаления клавиатуры.
     *
     * @param chatId               Идентификатор чата.
     * @param text                 Текст сообщения.
     * @param inlineKeyboardMarkup Инлайн клавиши.
     * @param delKeyBoard          Флаг, удалять ли клавиатуру.
     * @return Отправленное сообщение.
     */
    public Message putMessageWithMarkUps(long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup, boolean delKeyBoard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(inlineKeyboardMarkup);
        message.setParseMode(ParseMode.HTML);
        if (delKeyBoard) {
            delKeyBoard(chatId);
        }
        return sendMessage(message);
    }

    /**
     * Удаляет клавиатуру в чате.
     *
     * @param chatId Идентификатор чата, в котором нужно удалить клавиатуру.
     */
    public void delKeyBoard(long chatId) {
        SendMessage message1 = new SendMessage();
        message1.setChatId(chatId);
        message1.setText("load...");
        ReplyKeyboardRemove replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        message1.setReplyMarkup(replyKeyboardRemove);
        Message mess = sendMessage(message1);
        deleteMessage(chatId, mess.getMessageId());
    }

    /**
     * Отправляет контакт в чат.
     *
     * @param chatId               Идентификатор чата.
     * @param number               Номер телефона.
     * @param name                 Имя контакта.
     * @param lastName             Фамилия контакта.
     * @param inlineKeyboardMarkup Инлайн клавиши.
     */
    public void sendContact(long chatId, String number, String name, String lastName, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendContact sendContact = new SendContact();
        sendContact.setChatId(chatId);
        sendContact.setReplyMarkup(inlineKeyboardMarkup);
        sendContact.setFirstName(name);
        sendContact.setLastName(lastName);
        sendContact.setPhoneNumber(number);

        try {
            execute(sendContact);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Отправляет фотографию в чат.
     *
     * @param chatId               Идентификатор чата.
     * @param text                 Текст сообщения.
     * @param photoUrl             URL фотографии.
     * @param inlineKeyboardMarkup Инлайн клавиши.
     */
    public void sendPhoto(long chatId, String text, String photoUrl, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        sendPhoto.setParseMode(ParseMode.HTML);
        sendPhoto.setCaption(text);
        sendPhoto.setPhoto(new InputFile(photoUrl));

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Отправляет фотографию в чат.
     *
     * @param chatId               Идентификатор чата.
     * @param text                 Текст сообщения.
     * @param photo                фотография.
     * @param inlineKeyboardMarkup Инлайн клавиши.
     */
    public Message sendPhoto(long chatId, String text, InputFile photo, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        sendPhoto.setParseMode(ParseMode.HTML);
        sendPhoto.setCaption(text);
        sendPhoto.setPhoto(photo);

        Message message = null;
        try {
            message = execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }

        return message;
    }

    /**
     * Отправляет сообщение с текстом и клавишами.
     *
     * @param chatId Идентификатор чата.
     * @param text   Текст сообщения.
     * @param button Клавиши для сообщения.
     * @return Отправленное сообщение.
     */
    public Message putKeyBoard(long chatId, String text, ReplyKeyboardMarkup button) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setParseMode(ParseMode.HTML);
        sendMessage.setReplyMarkup(button);

        return sendMessage(sendMessage);
    }

    /**
     * Удаляет сообщение из чата.
     *
     * @param chatId    Идентификатор чата.
     * @param messageId Идентификатор сообщения, которое нужно удалить.
     */
    public void deleteMessage(long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);

        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Изменяет инлайн клавиши в сообщении.
     *
     * @param chatID         Идентификатор чата.
     * @param messageID      Идентификатор сообщения.
     * @param keyboardMarkup Новые инлайн клавиши.
     * @return Обновленное сообщение.
     */
    public Message changeInline(long chatID, int messageID, InlineKeyboardMarkup keyboardMarkup) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setMessageId(messageID);
        editMessageReplyMarkup.setChatId(chatID);
        editMessageReplyMarkup.setReplyMarkup(keyboardMarkup);

        Message message;
        try {
            message = (Message) execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return message;
    }

    /**
     * Изменяет текст в сообщении.
     *
     * @param chatID    Идентификатор чата.
     * @param messageID Идентификатор сообщения.
     * @param text      Новый текст сообщения.
     */
    public void changeText(long chatID, int messageID, String text) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(messageID);
        editMessageText.setChatId(chatID);
        editMessageText.setText(text);
        editMessageText.setParseMode(ParseMode.HTML);

        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Получает файл по его ID и сохраняет локально.
     *
     * @param fileId   ID файла.
     * @param fileName Имя для сохраненного файла.
     * @return Локальный файл.
     */
    public File getFile(String fileId, String fileName) {
        GetFile getFile = new GetFile(fileId);

        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
            File localFile = new File("./data", fileName + ".jpg");
            downloadFile(file.getFilePath(), localFile);
            return localFile;
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
