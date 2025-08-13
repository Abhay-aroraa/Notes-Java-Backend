package com.notesapp.notes;

import com.notesapp.notes.controller.ForgotPasswordController;
import com.notesapp.notes.service.OtpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ForgotPasswordController.class)
class OtpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OtpService otpService;

    @Test
    void sendOtp_ShouldReturn200() throws Exception {
        when(otpService.generateOtp("abhayphutela08@gmail.com")).thenReturn("123456");

        mockMvc.perform(post("/api/auth/send-otp")
                        .param("email", "abhayphutela08@gmail.com"))
                .andExpect(status().isOk());
    }
}
