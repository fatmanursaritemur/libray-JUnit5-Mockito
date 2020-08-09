package com.fatmanur.unittest.mockito;

import com.fatmanur.unittest.model.Member;
import com.fatmanur.unittest.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimeoutVerificationModeDemo {

    @Mock
    private MemberRepository memberRepository;

    @Test
    void readStudent() {
        when(memberRepository.findById("id1")).thenReturn(Optional.of(new Member("", "", "")));

        new Thread(() -> {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            memberRepository.findById("id1");
        }).start();

        verify(memberRepository, timeout(100).times(1)).findById("id1");

    }

}