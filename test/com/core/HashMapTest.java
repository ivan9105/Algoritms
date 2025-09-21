package com.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class HashMapTest {

    @Test
    public void whenChangeKeyThenNodeDoesNotFound() {
        // given
        var key = new TestKey(1, "Test");

        var map = new HashMap<TestKey, String>();
        map.put(key, "1");

        // when
        assertThat(map.get(key)).isNotNull();

        //then
        key.setId(2);

        assertThat(map.get(key)).isNull();
    }

    @Test
    public void whenUseNullableKeyThenShouldSuccess() {
        // given
        TestKey key = null;

        var map = new HashMap<TestKey, String>();
        map.put(key, "1");

        // when
        // then
        assertThat(map.get(null)).isNotNull();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class TestKey {
        private long id;
        private String testField;
    }
}
