package dev.chechu.core;

import dev.chechu.dragonapi.core.utils.ConfigChunk;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MemoryTest {
    private static long chunkMemory;
    private static long listMemory;
    private static long hashMapMemory;

    private static int chunkWins = 0;
    private static int listWins = 0;
    private static int hashMapWins = 0;

    private static final List<String> randomList = new ArrayList<>();

    private static final int size = 5000;

    public void chunkTest() {
        long memory0 = Runtime.getRuntime().freeMemory();
        List<ConfigChunk<String>> configChunkList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ConfigChunk<String> configChunk = new ConfigChunk<>("key"+i,randomList.get(i));
            configChunkList.add(configChunk);
            configChunk.setValue(randomList.get(i+size));
        }
        long memory1 = Runtime.getRuntime().freeMemory();
        chunkMemory = (memory0 - memory1);
    }

    public void listTest() {
        long memory0 = Runtime.getRuntime().freeMemory();
        List<String> keys = new ArrayList<>();
        List<String> defaultValues = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            keys.add("key"+i);
            defaultValues.add(randomList.get(i));
            values.add(randomList.get(i+size));
        }
        long memory1 = Runtime.getRuntime().freeMemory();
        listMemory = (memory0 - memory1);
    }

    public void hashMapTest() {
        long memory0 = Runtime.getRuntime().freeMemory();
        HashMap<String, Map.Entry<String, String>> configHashMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, String> defaultValueValue = new AbstractMap.SimpleEntry<>(randomList.get(i), randomList.get(i+size));
            configHashMap.put("key"+i,defaultValueValue);
        }
        long memory1 = Runtime.getRuntime().freeMemory();
        hashMapMemory = (memory0 - memory1);
    }

    private String getRandomString() {
        return RandomStringUtils.random(10,true,true);
    }

    @RepeatedTest(10)
    public void test() {
        for (int i = 0; i < size*2; i++) {
            randomList.add(getRandomString());
        }
        int random;
        boolean first = false;
        boolean second = false;
        boolean third = false;
        for (int i = 0; i < 2; i++) {
            boolean executed = false;
            do {
                random = new Random().nextInt(3);
                if (random == 0 && !first) {
                    hashMapTest();
                    first = true;
                    executed = true;
                }
                if (random == 1 && !second) {
                    listTest();
                    second = true;
                    executed = true;
                }
                if (random == 2 && !third) {
                    chunkTest();
                    third = true;
                    executed = true;
                }
            } while(!executed);
        }
        hashMapTest();
        chunkTest();
        listTest();
        if (hashMapMemory < chunkMemory && hashMapMemory < listMemory) {
            //System.out.println("HashMap wins");
            hashMapWins++;
        }
        if (listMemory < chunkMemory && listMemory < hashMapMemory) {
            //System.out.println("List wins");
            listWins++;
        }
        if (chunkMemory < listMemory && chunkMemory < hashMapMemory) {
            //System.out.println("ChunkConfig wins");
            chunkWins++;
        }
    }

    @AfterAll
    public static void assertThatChunkWins() {
        assertTrue(chunkWins > listWins && chunkWins > hashMapWins, "Chunk should win");
    }
}
