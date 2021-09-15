package com.thebois;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MainTests {

    @Test
    public void whenTrue() {
        assertThat(true).as("true is true").isEqualTo(true);
    }

}
