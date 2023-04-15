package com.ahao;

import com.sun.istack.internal.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpingOriginApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void notNull() {
        cantNullParams("");
    }

    void cantNullParams(@NotNull String str) {
        System.out.println(str);
    }


}
