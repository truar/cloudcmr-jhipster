package com.cloudcmr.app.member.web.rest.fileParser;

import com.cloudcmr.app.member.domain.Member;

import java.io.IOException;
import java.util.List;

public interface Parser {

    List<Member> parse() throws IOException;

}
