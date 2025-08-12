package com.notesapp.notes.service;

import com.notesapp.notes.model.OtpRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class OtpService {

    private final Map<String, OtpRequest> otpMap = new HashMap<>();

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
        OtpRequest request = new OtpRequest();
        request.setEmail(email);
        // Fixed expiry: 5 minutes (5 * 60 * 1000), you had 5 * 120 * 1000 (10 mins)
        request.setExpiryTime(System.currentTimeMillis() + 5 * 60 * 1000);
        request.setOtp(otp);
        otpMap.put(email, request);
        log.info("Generated OTP for email: {}", email);
        return otp;
    }

    public Boolean verifyOtp(String email, String inputOtp) {
        OtpRequest stored = otpMap.get(email);
        if (stored == null) {
            log.warn("No OTP found for email: {}", email);
            return false;
        }
        if (System.currentTimeMillis() > stored.getExpiryTime()) {
            log.warn("OTP expired for email: {}", email);
            otpMap.remove(email);
            return false;
        }
        boolean matched = stored.getOtp().equals(inputOtp);
        if (!matched) {
            log.warn("Invalid OTP entered for email: {}", email);
        } else {
            log.info("OTP verified for email: {}", email);
            otpMap.remove(email); // remove after successful verification
        }
        return matched;
    }

    public void removeOtp(String email) {
        otpMap.remove(email);
        log.info("Removed OTP for email: {}", email);
    }
}
