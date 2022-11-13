package com.gridnine.testing;

import org.junit.jupiter.api.Timeout;

import org.junit.Test;

public class MainTest {

        @Test
        @Timeout(value = 1)
        public void mainShouldBeExecutedInOneSecondOtherwiseFails() throws Exception {
            Main.main(null);
        }
}
