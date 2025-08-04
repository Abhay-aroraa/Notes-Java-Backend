package com.notesapp.notes.service;

import com.notesapp.notes.model.OtpRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, OtpRequest> otpMap = new HashMap<>();

    public String generateOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
        OtpRequest request = new OtpRequest();
        request.setEmail(email);
        request.setExpiryTime(System.currentTimeMillis() + 5 * 120 * 1000);
        request.setOtp(otp);
        otpMap.put(email,request);
        return  otp;
    }

    public Boolean verifyOtp(String email, String inputOtp){
        OtpRequest stored = otpMap.get(email);
        if(stored == null) return false;
        if(System.currentTimeMillis() > stored.getExpiryTime()) return false;
        return stored.getOtp().equals(inputOtp);
    }
    public void removeOtp(String email){
        otpMap.remove(email);
    }
}
