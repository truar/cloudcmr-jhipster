package com.cloudcmr.app.member.web.rest;

import com.cloudcmr.app.member.service.MemberServiceCmr;
import com.cloudcmr.app.member.web.rest.errors.IncorrectExportFileException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class MemberResourceCmrTest {

    @Mock
    private MemberServiceCmr memberServiceCmr;

    @Test(expected = IncorrectExportFileException.class)
    public void shouldThrowIncorrectFileException() throws IOException {
        final MemberResource memberResource = new MemberResource(null, memberServiceCmr);
        MultipartFile file = mock(MultipartFile.class);
        doThrow(new IOException("I have to be thrown")).when(file).getInputStream();
        memberResource.readMemberFile(file);
    }

}
