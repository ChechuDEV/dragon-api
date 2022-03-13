package dev.chechu.core.utils;

import dev.chechu.dragonapi.core.Core;
import dev.chechu.dragonapi.core.utils.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {
    @BeforeAll
    public static void setUp() {
        new Core(false);
    }

    @Test
    public void englishUpdateSearch() {
        assertEquals("Searching for updates...", Message.get("en_US","update_search"), "English update_search message should return 'Searching for updates...'");
    }

    @Test
    public void noLanguageUpdateSearch() {
        assertEquals("Searching for updates...", Message.get("in_EN","update_search"), "No language update_search message should return 'Searching for updates...'");
    }

    @Test
    public void englishNonExistentMessageShouldReturnError() {
        assertEquals("LANGUAGE ERROR", Message.get("en_US","hahaha"), "English non existent message should return 'LANGUAGE ERROR'");
    }

    @Test
    public void nolanguageNonExsitentMessageShouldReturnError() {
        assertEquals("LANGUAGE ERROR", Message.get("in_EN","hahaha"), "No language non existent message should return 'LANGUAGE ERROR'");
    }
}
