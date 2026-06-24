package com.bajaj.bfhl.service;

import com.bajaj.bfhl.dto.BfhlRequest;
import com.bajaj.bfhl.dto.BfhlResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class BfhlServiceImpl implements BfhlService {


    private static final String FULL_NAME   = "souradip pramanik";
    private static final String DOB         = "26012006";
    private static final String EMAIL       = "souradip1507.be23@chitkarauniversity.edu.in";
    private static final String ROLL_NUMBER = "2311981507";
    // ─────────────────────────────────────────────────────────

    @Override
    public BfhlResponse processData(BfhlRequest request) {

        List<String> data = request.getData();

        List<String> oddNumbers       = new ArrayList<>();
        List<String> evenNumbers      = new ArrayList<>();
        List<String> alphabets        = new ArrayList<>();
        List<String> specialChars     = new ArrayList<>();
        long         sum              = 0;
        StringBuilder rawAlphas       = new StringBuilder();

        for (String item : data) {
            if (isNumber(item)) {
                long num = Long.parseLong(item);
                sum += num;
                if (num % 2 == 0) {
                    evenNumbers.add(item);
                } else {
                    oddNumbers.add(item);
                }
            } else if (isAlpha(item)) {

                alphabets.add(item.toUpperCase());

                for (char c : item.toCharArray()) {
                    if (Character.isLetter(c)) {
                        rawAlphas.append(c);
                    }
                }
            } else {
                specialChars.add(item);
            }
        }

        String concatString = buildConcatString(rawAlphas.toString());

        return BfhlResponse.builder()
                .isSuccess(true)
                .userId(FULL_NAME + "_" + DOB)
                .email(EMAIL)
                .rollNumber(ROLL_NUMBER)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialChars)
                .sum(String.valueOf(sum))
                .concatString(concatString)
                .build();
    }


    private boolean isNumber(String token) {
        if (token == null || token.isEmpty()) return false;
        try {
            Long.parseLong(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private boolean isAlpha(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
        return true;
    }


    private String buildConcatString(String allAlphas) {
        if (allAlphas.isEmpty()) return "";


        String reversed = new StringBuilder(allAlphas).reverse().toString();

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (i % 2 == 0) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }
}
