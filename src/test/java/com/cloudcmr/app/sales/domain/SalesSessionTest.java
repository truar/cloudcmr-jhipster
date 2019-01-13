package com.cloudcmr.app.sales.domain;

import com.cloudcmr.app.article.domain.Article;
import com.cloudcmr.app.domain.User;
import com.cloudcmr.app.member.domain.Member;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesSessionTest {

    private User user = new User();
    private BigDecimal cashBeforeSale = new BigDecimal(100);

    @Test
    public void shouldBeNewWhenJustCreated() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        assertThat(salesSession.status()).isEqualTo(SalesSession.Status.NEW);
    }

    @Test
    public void shouldKnowTheAmountInTheCashRegister() {
        BigDecimal cashBeforeSale = new BigDecimal(200);
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        assertThat(salesSession.cashWhenNew()).isEqualTo(cashBeforeSale);
    }

    @Test
    public void shouldBeStartedByAUser() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        assertThat(salesSession.seller()).isEqualTo(user);
    }

    @Test
    public void shouldHaveANewRunningSale() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        assertThat(salesSession.runningSale()).isNotNull();
        assertThat(salesSession.runningSale().getStatus()).isEqualTo(Sale.Status.NEW);
        assertThat(salesSession.runningSale().getSoldArticles()).isEmpty();
    }

    @Test
    public void shouldSellAnArticleToAMember() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        salesSession.sellArticle(new Member(), new Article(), 1);
        assertThat(salesSession.status()).isEqualTo(SalesSession.Status.IN_PROGRESS);
        assertThat(salesSession.runningSale().getStatus()).isEqualTo(Sale.Status.IN_PROGRESS);
        assertThat(salesSession.runningSale().getSoldArticles()).hasSize(1);
    }

    @Test
    public void shouldMergeTheSoldArticleWhenSellingSameSoldArticle() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        Member member = new Member();
        member.setId(1l);
        Article article  = new Article();
        article.setId(2l);
        salesSession.sellArticle(member, article, 1);
        salesSession.sellArticle(member, article, 2);
        assertThat(salesSession.status()).isEqualTo(SalesSession.Status.IN_PROGRESS);
        assertThat(salesSession.runningSale().getStatus()).isEqualTo(Sale.Status.IN_PROGRESS);
        assertThat(salesSession.runningSale().getSoldArticles()).hasSize(1);
        assertThat(salesSession.runningSale().getSoldArticles().get(0).getQuantity()).isEqualTo(3);
    }

    @Test
    public void shouldDeleteTheSoldArticleGivenItsID() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        salesSession.sellArticle(new Member(), new Article(), 1);
        salesSession.runningSale().getSoldArticles().get(0).setId(1l);
        salesSession.removeArticle(1l);
        assertThat(salesSession.status()).isEqualTo(SalesSession.Status.IN_PROGRESS);
        assertThat(salesSession.runningSale().getStatus()).isEqualTo(Sale.Status.IN_PROGRESS);
        assertThat(salesSession.runningSale().getSoldArticles()).hasSize(0);
    }

    @Test
    public void shouldHaveAPayerWithAPaymentType() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        salesSession.assignPayer(new Member(), PaymentType.CARD);
        assertThat(salesSession.runningSale().getPayer()).isNotNull();
        assertThat(salesSession.runningSale().getPayer().getPaymentType()).isEqualTo(PaymentType.CARD);
        assertThat(salesSession.runningSale().getStatus()).isEqualTo(Sale.Status.IN_PROGRESS);
    }

    @Test
    public void shouldCloseASale() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        salesSession.sellArticle(new Member(), new Article(), 1);
        salesSession.closeRunningSale();
        assertThat(salesSession.closedSales()).hasSize(1);
        assertThat(salesSession.closedSales().get(0).getStatus()).isEqualTo(Sale.Status.CLOSED);
        assertThat(salesSession.runningSale().getStatus()).isEqualTo(Sale.Status.NEW);
    }

    @Test
    public void shouldNotCloseANewSale() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        salesSession.closeRunningSale();
        assertThat(salesSession.closedSales()).hasSize(0);
        assertThat(salesSession.runningSale().getStatus()).isEqualTo(Sale.Status.NEW);
    }

    @Test
    public void shouldCalculateTheTotal() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        Member member = new Member();
        member.setId(1l);
        Article article1  = new Article().price(new BigDecimal(10));
        article1.setId(1l);
        article1.setPrice(new BigDecimal(10));
        Article article2  = new Article().price(new BigDecimal(30));
        article2.setId(2l);
        salesSession.sellArticle(member, article1, 1);
        salesSession.sellArticle(member, article2, 2);
        assertThat(salesSession.runningSale().getTotal()).isEqualTo(new BigDecimal(70));
    }

    @Test
    public void shouldNotCloseTheSalesSessionIfARunningSaleIsNotCompleted() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        salesSession.sellArticle(new Member(), new Article(), 1);
        BigDecimal cashWhenClosed = new BigDecimal(20);
        salesSession.close(cashWhenClosed);

        assertThat(salesSession.status()).isEqualTo(SalesSession.Status.IN_PROGRESS);
        assertThat(salesSession.runningSale()).isNotNull();
        assertThat(salesSession.getCashWhenClosed()).isNull();
    }

    @Test
    public void shouldCloseTheSalesSession() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        BigDecimal cashWhenClosed = new BigDecimal(20);
        salesSession.close(cashWhenClosed);

        assertThat(salesSession.status()).isEqualTo(SalesSession.Status.CLOSED);
        assertThat(salesSession.runningSale()).isNull();
        assertThat(salesSession.getCashWhenClosed()).isEqualTo(cashWhenClosed);
    }

    @Test
    public void shouldCloseTheSalesSessionWithAnEmptyRunningSale() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        salesSession.sellArticle(new Member(), new Article(), 1);
        salesSession.runningSale().getSoldArticles().get(0).setId(1l);
        salesSession.removeArticle(1l);
        BigDecimal cashWhenClosed = new BigDecimal(20);
        salesSession.close(cashWhenClosed);

        assertThat(salesSession.status()).isEqualTo(SalesSession.Status.CLOSED);
        assertThat(salesSession.runningSale()).isNull();
        assertThat(salesSession.getCashWhenClosed()).isEqualTo(cashWhenClosed);
    }

    @Test
    public void thePayerShouldPayNothingIfNothingToPay() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        salesSession.assignPayer(new Member(), PaymentType.CASH);
        assertThat(salesSession.runningSale().getPayer().getPaymentAmount()).isEqualTo(new BigDecimal(0));
    }

    @Test
    public void thePayerShouldPayTheTotalOfTheRunningSaleIfArticleAreAlreadySold() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        Article article = new Article().price(new BigDecimal(30));
        article.setId(1l);
        salesSession.sellArticle(new Member(), article, 1);

        salesSession.assignPayer(new Member(), PaymentType.CASH);
        assertThat(salesSession.runningSale().getPayer().getPaymentAmount()).isEqualTo(new BigDecimal(30));
    }

    @Test
    public void thePayerMustPayTheEntireTotalWhenAlone() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        salesSession.assignPayer(new Member(), PaymentType.CASH);
        salesSession.sellArticle(new Member(), new Article().price(new BigDecimal(30)), 1);
        salesSession.runningSale().getSoldArticles().get(0).setId(1l);
        assertThat(salesSession.runningSale().getPayer().getPaymentAmount()).isEqualTo(new BigDecimal(30));
        salesSession.removeArticle(1l);
        assertThat(salesSession.runningSale().getPayer().getPaymentAmount()).isEqualTo(new BigDecimal(0));
    }

    @Test
    public void theRunningSaleCanHave2PayersPayingTogether() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        salesSession.assignPayer(new Member(), PaymentType.CASH);
        salesSession.assignSecondPayer(new Member(), PaymentType.CASH, new BigDecimal(10));
        salesSession.sellArticle(new Member(), new Article().price(new BigDecimal(30)), 1);
        assertThat(salesSession.runningSale().getPayer().getPaymentAmount()).isEqualTo(new BigDecimal(20));
        assertThat(salesSession.runningSale().getPayer2().getPaymentAmount()).isEqualTo(new BigDecimal(10));
    }

    @Test
    public void theSecondPayerIsOptionalAndCanBeRemoved() {
        SalesSession salesSession = new SalesSession(user, cashBeforeSale);
        salesSession.assignPayer(new Member(), PaymentType.CASH);
        salesSession.assignSecondPayer(new Member(), PaymentType.CASH, new BigDecimal(10));
        salesSession.sellArticle(new Member(), new Article().price(new BigDecimal(30)), 1);
        salesSession.removeSecondPayer();
        assertThat(salesSession.runningSale().getPayer2()).isNull();
        assertThat(salesSession.runningSale().getPayer().getPaymentAmount()).isEqualTo(new BigDecimal(30));
    }
}
