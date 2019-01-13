package com.cloudcmr.app.sales.web.rest;

import com.cloudcmr.app.CloudcmrApp;
import com.cloudcmr.app.article.domain.Article;
import com.cloudcmr.app.article.repository.ArticleRepository;
import com.cloudcmr.app.domain.User;
import com.cloudcmr.app.member.domain.Member;
import com.cloudcmr.app.member.repository.MemberRepository;
import com.cloudcmr.app.repository.UserRepository;
import com.cloudcmr.app.sales.domain.PaymentType;
import com.cloudcmr.app.sales.domain.Sale;
import com.cloudcmr.app.sales.domain.SalesSession;
import com.cloudcmr.app.sales.repository.SalesSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudcmrApp.class)
@AutoConfigureMockMvc
public class SalesSessionResourceTest {

    public static final String USERNAME = "username";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private SalesSessionRepository salesSessionRepository;
    @MockBean
    private ArticleRepository articleRepository;
    @MockBean
    private MemberRepository memberRepository;

    private ObjectMapper mapper;
    private SalesSession defaultSalesSession;
    private Optional<User> defaultUserOptional;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        defaultUserOptional = Optional.of(new User());
        defaultSalesSession = new SalesSession(defaultUserOptional.get(), new BigDecimal(200));
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldReturnNothingAsNoSalesSessionIsOpen() throws Exception {
        when(userRepository.findOneByLogin(USERNAME)).thenReturn(defaultUserOptional);
        when(salesSessionRepository.findOpenBySeller(defaultUserOptional.get())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/sales-session/current")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldOpenANewSaleSession() throws Exception {
        when(userRepository.findOneByLogin(USERNAME)).thenReturn(defaultUserOptional);
        when(salesSessionRepository.findOpenBySeller(defaultUserOptional.get())).thenReturn(Optional.empty());

        OpenSalesSessionRequest openSalesSessionRequest = new OpenSalesSessionRequest();
        openSalesSessionRequest.setCash(new BigDecimal(200));
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String jsonContent = ow.writeValueAsString(openSalesSessionRequest);

        mockMvc.perform(post("/api/sales-session/current/open")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isCreated());

        verify(salesSessionRepository).save(any());
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldNotOpenANewSaleSessionWhenAnotherOneExists() throws Exception {
        when(userRepository.findOneByLogin(USERNAME)).thenReturn(defaultUserOptional);
        Optional<SalesSession> salesSessionOptional = Optional.of(defaultSalesSession);
        when(salesSessionRepository.findOpenBySeller(defaultUserOptional.get())).thenReturn(salesSessionOptional);

        OpenSalesSessionRequest openSalesSessionRequest = new OpenSalesSessionRequest();
        openSalesSessionRequest.setCash(new BigDecimal(200));
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String jsonContent = ow.writeValueAsString(openSalesSessionRequest);

        mockMvc.perform(post("/api/sales-session/current/open")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isCreated());

        verify(salesSessionRepository, times(0)).save(any());
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldReturnAnEmptyRunningSale() throws Exception {
        when(userRepository.findOneByLogin(USERNAME)).thenReturn(defaultUserOptional);
        Optional<SalesSession> salesSessionOptional = Optional.of(defaultSalesSession);
        when(salesSessionRepository.findOpenBySeller(defaultUserOptional.get())).thenReturn(salesSessionOptional);

        mockMvc.perform(get("/api/sales-session/current")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Sale.Status.NEW.toString()))
            .andExpect(jsonPath("$.soldArticles").value(empty()))
            .andExpect(jsonPath("$.payer").value(nullValue()));
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldAddSoldArticleToTheRunningSale() throws Exception {
        when(userRepository.findOneByLogin(USERNAME)).thenReturn(defaultUserOptional);
        Optional<SalesSession> salesSessionOptional = Optional.of(defaultSalesSession);
        when(salesSessionRepository.findOpenBySeller(defaultUserOptional.get())).thenReturn(salesSessionOptional);
        when(articleRepository.getOne(1l)).thenReturn(new Article());
        when(memberRepository.getOne(1l)).thenReturn(new Member());
        SellArticleRequest sellArticleRequest = new SellArticleRequest(1l, 1l, 1);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String jsonContent = ow.writeValueAsString(sellArticleRequest);

        mockMvc.perform(put("/api/sales-session/current/sellArticle")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isNoContent());
        verify(salesSessionRepository, times(1)).save(defaultSalesSession);
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldAssignAPayerToTheCurrentSalesSession() throws Exception {
        when(userRepository.findOneByLogin(USERNAME)).thenReturn(defaultUserOptional);
        Optional<SalesSession> salesSessionOptional = Optional.of(defaultSalesSession);
        when(salesSessionRepository.findOpenBySeller(defaultUserOptional.get())).thenReturn(salesSessionOptional);

        long payerId = 1l;
        AssignPayerRequest assignPayerRequest = new AssignPayerRequest();
        assignPayerRequest.setPayerId(payerId);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String jsonContent = ow.writeValueAsString(assignPayerRequest);


        mockMvc.perform(put("/api/sales-session/current/assignPayer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isNoContent());
        verify(salesSessionRepository, times(1)).save(defaultSalesSession);
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldAssignASecondPayerToTheCurrentSalesSession() throws Exception {
        when(userRepository.findOneByLogin(USERNAME)).thenReturn(defaultUserOptional);
        defaultSalesSession.assignPayer(new Member(), PaymentType.CASH);
        Optional<SalesSession> salesSessionOptional = Optional.of(defaultSalesSession);
        when(salesSessionRepository.findOpenBySeller(defaultUserOptional.get())).thenReturn(salesSessionOptional);

        final long payerId = 2l;
        final BigDecimal paymentAmount = new BigDecimal(10);
        AssignPayerRequest assignPayerRequest = new AssignPayerRequest();
        assignPayerRequest.setPayerId(payerId);
        assignPayerRequest.setPaymentAmount(paymentAmount);
        assignPayerRequest.setPaymentType(PaymentType.CARD);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String jsonContent = ow.writeValueAsString(assignPayerRequest);

        mockMvc.perform(put("/api/sales-session/current/assignSecondPayer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isNoContent());
        verify(salesSessionRepository, times(1)).save(defaultSalesSession);
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldReturnACompleteRunningSale() throws Exception {
        when(userRepository.findOneByLogin(USERNAME)).thenReturn(defaultUserOptional);
        Optional<SalesSession> salesSessionOptional = Optional.of(defaultSalesSession);
        when(salesSessionRepository.findOpenBySeller(defaultUserOptional.get())).thenReturn(salesSessionOptional);

        Member member = createDummyMember(1l);
        Member payer = createDummyMember(1l);
        Article article1 = new Article().code("ABC").description("DESCRIPTION").price(new BigDecimal(100));
        Article article2 = new Article().code("DEF").description("DESCRIPTION").price(new BigDecimal(50));

        defaultSalesSession.sellArticle(member, article1, 2);
        defaultSalesSession.sellArticle(member, article2, 1);
        defaultSalesSession.assignPayer(payer, PaymentType.CASH);

        mockMvc.perform(get("/api/sales-session/current")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Sale.Status.IN_PROGRESS.toString()))
            .andExpect(jsonPath("$.payer.firstName").value(payer.getFirstName()))
            .andExpect(jsonPath("$.payer.lastName").value(payer.getLastName()))
            .andExpect(jsonPath("$.payer.uscaNumber").value(payer.getUscaNumber()))
            .andExpect(jsonPath("$.payer.paymentType").value(PaymentType.CASH.toString()))
            .andExpect(jsonPath("$.payer.paymentAmount").value(250))
            .andExpect(jsonPath("$.payer2").value(nullValue()))
            .andExpect(jsonPath("$.total").value(250))
            .andExpect(jsonPath("$.soldArticles.[0].member.firstName").value(member.getFirstName()))
            .andExpect(jsonPath("$.soldArticles.[0].member.lastName").value(member.getLastName()))
            .andExpect(jsonPath("$.soldArticles.[0].article.code").value(article1.getCode()))
            .andExpect(jsonPath("$.soldArticles.[0].quantity").value(2))
            .andExpect(jsonPath("$.soldArticles.[1].member.firstName").value(member.getFirstName()))
            .andExpect(jsonPath("$.soldArticles.[1].member.lastName").value(member.getLastName()))
            .andExpect(jsonPath("$.soldArticles.[1].article.code").value(article2.getCode()))
            .andExpect(jsonPath("$.soldArticles.[1].quantity").value(1))
        ;
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldReturnACompleteRunningSaleWith2Payers() throws Exception {
        when(userRepository.findOneByLogin(USERNAME)).thenReturn(defaultUserOptional);
        Optional<SalesSession> salesSessionOptional = Optional.of(defaultSalesSession);
        when(salesSessionRepository.findOpenBySeller(defaultUserOptional.get())).thenReturn(salesSessionOptional);

        Member member = createDummyMember(1l);
        Member payer = createDummyMember(1l);
        Member payer2 = createDummyMember(2l);
        Article article1 = new Article().code("ABC").description("DESCRIPTION").price(new BigDecimal(100));
        Article article2 = new Article().code("DEF").description("DESCRIPTION").price(new BigDecimal(50));

        defaultSalesSession.sellArticle(member, article1, 2);
        defaultSalesSession.sellArticle(member, article2, 1);
        defaultSalesSession.assignPayer(payer, PaymentType.CASH);
        defaultSalesSession.assignSecondPayer(payer2, PaymentType.CARD, new BigDecimal(100));

        mockMvc.perform(get("/api/sales-session/current")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(Sale.Status.IN_PROGRESS.toString()))
            .andExpect(jsonPath("$.payer.firstName").value(payer.getFirstName()))
            .andExpect(jsonPath("$.payer.lastName").value(payer.getLastName()))
            .andExpect(jsonPath("$.payer.uscaNumber").value(payer.getUscaNumber()))
            .andExpect(jsonPath("$.payer.memberId").value(payer.getId()))
            .andExpect(jsonPath("$.payer.paymentType").value(PaymentType.CASH.toString()))
            .andExpect(jsonPath("$.payer.paymentAmount").value(150))
            .andExpect(jsonPath("$.payer2.firstName").value(payer2.getFirstName()))
            .andExpect(jsonPath("$.payer2.lastName").value(payer2.getLastName()))
            .andExpect(jsonPath("$.payer2.uscaNumber").value(payer2.getUscaNumber()))
            .andExpect(jsonPath("$.payer2.memberId").value(payer2.getId()))
            .andExpect(jsonPath("$.payer2.paymentType").value(PaymentType.CARD.toString()))
            .andExpect(jsonPath("$.payer2.paymentAmount").value(100))
            .andExpect(jsonPath("$.total").value(250))
            .andExpect(jsonPath("$.soldArticles.[0].member.firstName").value(member.getFirstName()))
            .andExpect(jsonPath("$.soldArticles.[0].member.lastName").value(member.getLastName()))
            .andExpect(jsonPath("$.soldArticles.[0].article.code").value(article1.getCode()))
            .andExpect(jsonPath("$.soldArticles.[0].quantity").value(2))
            .andExpect(jsonPath("$.soldArticles.[1].member.firstName").value(member.getFirstName()))
            .andExpect(jsonPath("$.soldArticles.[1].member.lastName").value(member.getLastName()))
            .andExpect(jsonPath("$.soldArticles.[1].article.code").value(article2.getCode()))
            .andExpect(jsonPath("$.soldArticles.[1].quantity").value(1))
        ;
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldDeleteTheArticleAtTheIndex() throws Exception {
        when(userRepository.findOneByLogin(USERNAME)).thenReturn(defaultUserOptional);
        Optional<SalesSession> salesSessionOptional = Optional.of(defaultSalesSession);
        when(salesSessionRepository.findOpenBySeller(defaultUserOptional.get())).thenReturn(salesSessionOptional);

        Member member = createDummyMember(1l);
        Article article1 = new Article().code("ABC").description("DESCRIPTION").price(new BigDecimal(100));
        Article article2 = new Article().code("DEF").description("DESCRIPTION").price(new BigDecimal(50));

        defaultSalesSession.sellArticle(member, article1, 2);
        defaultSalesSession.sellArticle(member, article2, 1);
        defaultSalesSession.runningSale().getSoldArticles().get(0).setId(1l);
        defaultSalesSession.runningSale().getSoldArticles().get(1).setId(2l);
        mockMvc.perform(delete("/api/sales-session/current/removeArticle/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(salesSessionRepository, times(1)).save(defaultSalesSession);
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldCloseTheCurrentRunningSale() throws Exception {
        when(userRepository.findOneByLogin(USERNAME)).thenReturn(defaultUserOptional);
        Optional<SalesSession> salesSessionOptional = Optional.of(defaultSalesSession);
        when(salesSessionRepository.findOpenBySeller(defaultUserOptional.get())).thenReturn(salesSessionOptional);

        Member member = createDummyMember(1l);
        Article article1 = new Article().code("ABC").description("DESCRIPTION").price(new BigDecimal(100));
        Article article2 = new Article().code("DEF").description("DESCRIPTION").price(new BigDecimal(50));

        defaultSalesSession.sellArticle(member, article1, 2);
        defaultSalesSession.sellArticle(member, article2, 1);
        defaultSalesSession.runningSale().getSoldArticles().get(0).setId(1l);
        defaultSalesSession.runningSale().getSoldArticles().get(1).setId(2l);
        mockMvc.perform(put("/api/sales-session/current/closeRunningSale")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(salesSessionRepository, times(1)).save(defaultSalesSession);
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldCloseTheSalesSession() throws Exception {
        when(userRepository.findOneByLogin(USERNAME)).thenReturn(defaultUserOptional);
        Optional<SalesSession> salesSessionOptional = Optional.of(defaultSalesSession);
        when(salesSessionRepository.findOpenBySeller(defaultUserOptional.get())).thenReturn(salesSessionOptional);
        CloseSalesSessionRequest closeSalesSessionRequest = new CloseSalesSessionRequest();
        closeSalesSessionRequest.setCash(new BigDecimal(20));
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String jsonContent = ow.writeValueAsString(closeSalesSessionRequest);

        mockMvc.perform(put("/api/sales-session/current/close")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isNoContent());

        verify(salesSessionRepository, times(1)).save(defaultSalesSession);
    }

    @Test
    @WithMockUser(username = USERNAME)
    public void shouldRemoveTheSecondPayerFromTheRunningSale() throws Exception {
        when(userRepository.findOneByLogin(USERNAME)).thenReturn(defaultUserOptional);
        Optional<SalesSession> salesSessionOptional = Optional.of(defaultSalesSession);
        when(salesSessionRepository.findOpenBySeller(defaultUserOptional.get())).thenReturn(salesSessionOptional);

        defaultSalesSession.assignPayer(new Member(), PaymentType.CASH);
        defaultSalesSession.assignSecondPayer(new Member(), PaymentType.CASH, new BigDecimal(10));

        mockMvc.perform(put("/api/sales-session/current/removeSecondPayer")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
        verify(salesSessionRepository, times(1)).save(defaultSalesSession);
        assertThat(defaultSalesSession.runningSale().getPayer2()).isNull();
    }

    private Member createDummyMember(Long id) {
        Member dummyMember = new Member().firstName("firstname").lastName("lastname").uscaNumber("1234");
        dummyMember.setId(id);
        return dummyMember;
    }

}
