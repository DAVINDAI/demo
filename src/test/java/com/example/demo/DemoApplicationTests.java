package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        String s = "III";
        HashMap<Character, Integer> hashMap = new HashMap() {
            {
                //I             1
                //V             5
                //X             10
                //L             50
                //C             100
                //D             500
                //M             1000
                put('I', 1);
                put('V', 5);
                put('X', 10);
                put('L', 50);
                put('C', 100);
                put('D', 500);
                put('M', 1000);
            }
        };

        int length = s.length();

        int ret = 0;
        int last = 0;
        int cur = 0;
        Character c;
        for (int i = length; i > 0; i--) {
            c = s.charAt(i - 1);
            cur = hashMap.get(c);
            if (last < cur) {
                ret -= cur;
            } else {
                ret += cur;
            }
            last = cur;
        }

        System.out.println(ret);
    }

}
