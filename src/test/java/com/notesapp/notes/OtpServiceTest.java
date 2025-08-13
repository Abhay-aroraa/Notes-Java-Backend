package com.notesapp.notes;

import com.notesapp.notes.model.OtpRequest;
import com.notesapp.notes.service.OtpService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OtpServiceTest {

    @Test
    void generateOtp_ShouldReturnSixDigitOtp() {
        OtpService otpService = new OtpService();
        String otp = otpService.generateOtp("abhayphutela08@gmail.com");

        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d+"));
    }
}
