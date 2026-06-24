package com.bajaj.bfhl;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;
import com.bajaj.bfhl.service.BfhlServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BfhlApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BfhlServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BfhlServiceImpl();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Unit tests – Service layer
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Example A: mixed numbers, alphabets, special chars")
    void testExampleA_service() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));
        BfhlResponse res = service.processData(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).containsExactly("1");
        assertThat(res.getEvenNumbers()).containsExactlyInAnyOrder("334", "4");
        assertThat(res.getAlphabets()).containsExactlyInAnyOrder("A", "R");
        assertThat(res.getSpecialCharacters()).containsExactly("$");
        assertThat(res.getSum()).isEqualTo("339");
        assertThat(res.getConcatString()).isEqualTo("Ra");
    }

    @Test
    @DisplayName("Example B: multiple numbers, alphabets, special chars")
    void testExampleB_service() {
        BfhlRequest req = new BfhlRequest(
                Arrays.asList("2", "a", "y", "4", "&", "-", "*", "5", "92", "b"));
        BfhlResponse res = service.processData(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).containsExactly("5");
        assertThat(res.getEvenNumbers()).containsExactlyInAnyOrder("2", "4", "92");
        assertThat(res.getAlphabets()).containsExactlyInAnyOrder("A", "Y", "B");
        assertThat(res.getSpecialCharacters()).containsExactlyInAnyOrder("&", "-", "*");
        assertThat(res.getSum()).isEqualTo("103");
        assertThat(res.getConcatString()).isEqualTo("ByA");
    }

    @Test
    @DisplayName("Example C: only alphabets (multi-char tokens)")
    void testExampleC_service() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("A", "ABCD", "DOE"));
        BfhlResponse res = service.processData(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getEvenNumbers()).isEmpty();
        assertThat(res.getAlphabets()).containsExactly("A", "ABCD", "DOE");
        assertThat(res.getSpecialCharacters()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
        assertThat(res.getConcatString()).isEqualTo("EoDdCbAa");
    }

    @Test
    @DisplayName("Empty data array returns zeros and empty lists")
    void testEmptyData_service() {
        BfhlRequest req = new BfhlRequest(Collections.emptyList());
        BfhlResponse res = service.processData(req);

        assertThat(res.isSuccess()).isTrue();
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getEvenNumbers()).isEmpty();
        assertThat(res.getAlphabets()).isEmpty();
        assertThat(res.getSpecialCharacters()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
        assertThat(res.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Only numbers in data")
    void testOnlyNumbers_service() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("10", "3", "7", "20"));
        BfhlResponse res = service.processData(req);

        assertThat(res.getEvenNumbers()).containsExactlyInAnyOrder("10", "20");
        assertThat(res.getOddNumbers()).containsExactlyInAnyOrder("3", "7");
        assertThat(res.getSum()).isEqualTo("40");
        assertThat(res.getAlphabets()).isEmpty();
        assertThat(res.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("Only special characters in data")
    void testOnlySpecialChars_service() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("@", "#", "!"));
        BfhlResponse res = service.processData(req);

        assertThat(res.getSpecialCharacters()).containsExactlyInAnyOrder("@", "#", "!");
        assertThat(res.getAlphabets()).isEmpty();
        assertThat(res.getOddNumbers()).isEmpty();
        assertThat(res.getEvenNumbers()).isEmpty();
        assertThat(res.getSum()).isEqualTo("0");
        assertThat(res.getConcatString()).isEmpty();
    }

    @Test
    @DisplayName("user_id follows full_name_ddmmyyyy pattern")
    void testUserIdFormat_service() {
        BfhlRequest req = new BfhlRequest(List.of("1"));
        BfhlResponse res = service.processData(req);

        // user_id must match pattern: lowercase_name_ddmmyyyy
        assertThat(res.getUserId()).matches("[a-z_]+_\\d{8}");
    }

    @Test
    @DisplayName("Numbers are returned as strings in the response")
    void testNumbersReturnedAsStrings_service() {
        BfhlRequest req = new BfhlRequest(Arrays.asList("1", "2"));
        BfhlResponse res = service.processData(req);

        // odd_numbers and even_numbers must be List<String>
        assertThat(res.getOddNumbers()).isInstanceOf(List.class);
        assertThat(res.getOddNumbers().get(0)).isInstanceOf(String.class);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Integration tests – Controller / HTTP layer
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /bfhl returns 200 for valid request")
    void testPostBfhl_returns200() throws Exception {
        BfhlRequest req = new BfhlRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.odd_numbers[0]").value("1"))
                .andExpect(jsonPath("$.sum").value("339"))
                .andExpect(jsonPath("$.concat_string").value("Ra"));
    }

    @Test
    @DisplayName("POST /bfhl returns 400 when data field is missing")
    void testPostBfhl_missingData_returns400() throws Exception {
        String body = "{}";

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl returns 400 for malformed JSON")
    void testPostBfhl_malformedJson_returns400() throws Exception {
        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not valid json}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success").value(false));
    }

    @Test
    @DisplayName("POST /bfhl Example C integration test")
    void testPostBfhl_exampleC() throws Exception {
        BfhlRequest req = new BfhlRequest(Arrays.asList("A", "ABCD", "DOE"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum").value("0"))
                .andExpect(jsonPath("$.concat_string").value("EoDdCbAa"))
                .andExpect(jsonPath("$.odd_numbers").isEmpty())
                .andExpect(jsonPath("$.even_numbers").isEmpty());
    }
}
