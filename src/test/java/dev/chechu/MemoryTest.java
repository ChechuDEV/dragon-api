package dev.chechu;

import dev.chechu.core.utils.ConfigChunk;
import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryTest {
    @Test
    public void chunkTest() {
        long memory0 = Runtime.getRuntime().freeMemory();
        List<ConfigChunk> chunks = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            ConfigChunk<Integer>
            chunks.add();

        }
        long memory1 = Runtime.getRuntime().freeMemory();
        System.out.println("Chunk: " + (memory0 - memory1));
    }

    @Test
    public void listTest() {
        long memory0 = Runtime.getRuntime().freeMemory();
        List<Object> objList = new ArrayList<>();
        List<String> keyList = new ArrayList<>();
        List<Object> defaultValList = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            objList.add("Test"+i);
            keyList.add("default"+i);
            defaultValList.add(i);
        }
        long memory1 = Runtime.getRuntime().freeMemory();
        System.out.println("List: " + (memory0 - memory1));
    }

    @Test
    public void hashMapTest() {
        long memory0 = Runtime.getRuntime().freeMemory();
        HashMap<String, Object[]> hashMap = new HashMap<>();
        for (int i = 0; i < 2000; i++) {
            Object[] obj = new Object[2];
            obj[0] = "Test"+i;
            obj[1] = i;
            hashMap.put("default"+i,obj);
        }
        long memory1 = Runtime.getRuntime().freeMemory();
        System.out.println("Hashmap: " + (memory0 - memory1));
    }
}
