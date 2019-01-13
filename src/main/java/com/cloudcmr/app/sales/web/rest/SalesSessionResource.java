package com.cloudcmr.app.sales.web.rest;

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
import com.codahale.metrics.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Transactional
public class SalesSessionResource {

    private UserRepository userRepository;
    private SalesSessionRepository salesSessionRepository;
    private ArticleRepository articleRepository;
    private MemberRepository memberRepository;

    @Autowired
    public SalesSessionResource(UserRepository userRepository, SalesSessionRepository salesSessionRepository, ArticleRepository articleRepository, MemberRepository memberRepository) {
        this.userRepository = userRepository;
        this.salesSessionRepository = salesSessionRepository;
        this.articleRepository = articleRepository;
        this.memberRepository = memberRepository;
    }

    @Timed
    @GetMapping("/sales-session/current")
    public Sale getCurrentUserSalesSession(Principal principal) {
        SalesSession salesSession = getSalesSession(principal);
        return salesSession.runningSale();
    }

    @Timed
    @PostMapping("/sales-session/current/open")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void openNewSalesSession(Principal principal, @RequestBody OpenSalesSessionRequest openSalesSessionRequest) {
        User user = userRepository.findOneByLogin(principal.getName()).get();
        Optional<SalesSession> salesSession = salesSessionRepository.findOpenBySeller(user);
        if(!salesSession.isPresent()) {
            BigDecimal cash = openSalesSessionRequest.getCash();
            SalesSession openSalesSession = new SalesSession(user, cash);
            salesSessionRepository.save(openSalesSession);
        }
    }

    @Timed
    @PutMapping("/sales-session/current/sellArticle")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void sellArticleToCurrentSalesSession(Principal principal, @RequestBody SellArticleRequest sellArticleRequest) {
        SalesSession salesSession = getSalesSession(principal);
        Article article = articleRepository.getOne(sellArticleRequest.getArticleId());
        Member member = memberRepository.getOne(sellArticleRequest.getMemberId());
        salesSession.sellArticle(member, article, sellArticleRequest.getQuantity());
        salesSessionRepository.save(salesSession);
    }

    @Timed
    @PutMapping("/sales-session/current/assignPayer")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void assignPayerToCurrentSalesSession(Principal principal, @RequestBody AssignPayerRequest assignPayerRequest) {
        SalesSession salesSession = getSalesSession(principal);
        Member member = memberRepository.getOne(assignPayerRequest.getPayerId());
        salesSession.assignPayer(member, assignPayerRequest.getPaymentType());
        salesSessionRepository.save(salesSession);
    }

    @Timed
    @PutMapping("/sales-session/current/assignSecondPayer")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void assignSecondPayerToRunningSale(Principal principal, @RequestBody AssignPayerRequest assignPayerRequest) {
        SalesSession salesSession = getSalesSession(principal);
        Member member = memberRepository.getOne(assignPayerRequest.getPayerId());
        salesSession.assignSecondPayer(member, assignPayerRequest.getPaymentType(), assignPayerRequest.getPaymentAmount());
        salesSessionRepository.save(salesSession);
    }

    @Timed
    @PutMapping("/sales-session/current/removeSecondPayer")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeSecondPayerFromFromRunningSale(Principal principal) {
        SalesSession salesSession = getSalesSession(principal);
        salesSession.removeSecondPayer();
        salesSessionRepository.save(salesSession);
    }

    @Timed
    @DeleteMapping("/sales-session/current/removeArticle/{soldArticleId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void removeSoldArticleFromRunningSale(Principal principal, @PathVariable long soldArticleId) {
        SalesSession salesSession = getSalesSession(principal);
        salesSession.removeArticle(soldArticleId);
        salesSessionRepository.save(salesSession);
    }

    @Timed
    @PutMapping("/sales-session/current/closeRunningSale")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void closeRunningSale(Principal principal) {
        SalesSession salesSession = getSalesSession(principal);
        salesSession.closeRunningSale();
        salesSessionRepository.save(salesSession);
    }

    @Timed
    @PutMapping("/sales-session/current/close")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void closeSaleSession(Principal principal, @RequestBody CloseSalesSessionRequest closeSalesSessionRequest) {
        SalesSession salesSession = getSalesSession(principal);
        salesSession.close(closeSalesSessionRequest.getCash());
        salesSessionRepository.save(salesSession);
    }

    private SalesSession getSalesSession(Principal principal) {
        User user = userRepository.findOneByLogin(principal.getName()).get();
        Optional<SalesSession> salesSession = salesSessionRepository.findOpenBySeller(user);
        if(!salesSession.isPresent()) {
            throw new NoCurrentSalesSessionException();
        }
        return salesSession.get();
    }

}
