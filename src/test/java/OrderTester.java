import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTester {

    @BeforeEach
    public void openPage() throws SQLException {
        open("http://localhost:8080/");
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName(value = "01. Order should be approved with the card number 41")
    void orderShouldBeApprovedWithCardFortyOne() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choicePayment();
        orderPage.inputCardNumber(DataHelper.getFirstCardNumber());
        orderPage.inputCardMonth(DataHelper.getThisMonth());
        orderPage.inputCardYear(DataHelper.getNextYear());
        orderPage.inputCustomerName(DataHelper.getCorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getCorrectCVV());
        orderPage.continueClick();
        orderPage.successMessageCheck();
        assertEquals(rowCountFirst + 1, sqlHelper.rowCountChecker());
        assertEquals(DataHelper.getApproved(), sqlHelper.checkPaymentStatus());
        assertEquals(sqlHelper.checkTransactionId(), sqlHelper.checkPaymentId());
        assertEquals(DataHelper.getOrderAmountActual(), sqlHelper.checkOrderAmount());
    }

    @Test
    @DisplayName(value = "02. Order should be approved with the card number 42")
    void orderShouldBeApprovedWithCardFortyTwo() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        sqlHelper.rowCount();
        orderPage.choicePayment();
        orderPage.inputCardNumber(DataHelper.getSecondCardNumber());
        orderPage.inputCardMonth(DataHelper.getThisMonth());
        orderPage.inputCardYear(DataHelper.getNextYear());
        orderPage.inputCustomerName(DataHelper.getCorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getCorrectCVV());
        orderPage.continueClick();
        orderPage.successMessageCheck();
        assertEquals(rowCountFirst + 1, sqlHelper.rowCountChecker());
        assertEquals("DECLINED", sqlHelper.checkPaymentStatus());
        assertEquals(sqlHelper.checkTransactionId(), sqlHelper.checkPaymentId());
        assertEquals(DataHelper.getOrderAmountActual(), sqlHelper.checkOrderAmount());
    }

    @Test
    @DisplayName(value = "03. Order should be denied with the card number 43")
    void orderShouldBeDeniedWithCardFortyThree() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        sqlHelper.rowCount();
        orderPage.choicePayment();
        orderPage.inputCardNumber(DataHelper.getThirdCardNumber());
        orderPage.inputCardMonth(DataHelper.getThisMonth());
        orderPage.inputCardYear(DataHelper.getNextYear());
        orderPage.inputCustomerName(DataHelper.getCorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getCorrectCVV());
        orderPage.continueClick();
        orderPage.checkErrorMessage();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }

    @Test
    @DisplayName(value = "04. Incorrect values will not be entered")
    void incorrectValuesWillNotBeEntered() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choicePayment();
        orderPage.inputCardNumber(DataHelper.getIncorrectCardNumber());
        orderPage.inputCardMonth(DataHelper.getIncorrectMonth());
        orderPage.inputCardYear(DataHelper.getIncorrectYear());
        orderPage.inputCustomerName(DataHelper.getIncorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getIncorrectCVV());
        orderPage.continueClick();
        orderPage.checkEmptyCardNumberField();
        orderPage.checkEmptyCardMonthField();
        orderPage.checkEmptyCardYearField();
        orderPage.checkEmptyCustomerNameField();
        orderPage.checkEmptyCVVNumberField();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }

    @Test
    @DisplayName(value = "05. Incorrect values(specials) will not be entered")
    void specialsWillNotBeEntered() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choicePayment();
        orderPage.inputCardNumber(DataHelper.getSpecialsCardNumber());
        orderPage.inputCardMonth(DataHelper.getSpecialsCardMonth());
        orderPage.inputCardYear(DataHelper.getSpecialsCardYear());
        orderPage.inputCustomerName(DataHelper.getSpecialsCustomerName());
        orderPage.inputCardCVV(DataHelper.getSpecialsCardCVV());
        orderPage.continueClick();
        orderPage.checkEmptyCardNumberField();
        orderPage.checkEmptyCardMonthField();
        orderPage.checkEmptyCardYearField();
        orderPage.checkEmptyCustomerNameField();
        orderPage.checkEmptyCVVNumberField();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }


    @Test
    @DisplayName(value = "06. Empty fields should be highlighted")
    void emptyFieldsShouldBeHighlighted() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choicePayment();
        orderPage.continueClick();
        orderPage.checkCardNumberFieldEmptyMessage();
        orderPage.checkCardMonthFieldEmptyMessage();
        orderPage.checkCardYearFieldEmptyMessage();
        orderPage.checkCustomerNameFieldEmptyMessage();
        orderPage.checkCVVNumberFieldEmptyMessage();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }


    @Test
    @DisplayName(value = "07. Nonexistent month should be allocated")
    void nonexistentMonthShouldBeAllocated() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choicePayment();
        orderPage.continueClick();
        orderPage.inputCardNumber(DataHelper.getSecondCardNumber());
        orderPage.inputCardMonth(DataHelper.getFalseMonth());
        orderPage.inputCardYear(DataHelper.getNextYear());
        orderPage.inputCustomerName(DataHelper.getCorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getCorrectCVV());
        orderPage.continueClick();
        orderPage.falseMonthMessage();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }

    @Test
    @DisplayName(value = "08. Expired month card should be allocated")
    void expiredMonthCardShouldBeAllocated() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choicePayment();
        orderPage.continueClick();
        orderPage.inputCardNumber(DataHelper.getSecondCardNumber());
        orderPage.inputCardMonth(DataHelper.getLastMonth());
        orderPage.inputCardYear(DataHelper.getThisYear());
        orderPage.inputCustomerName(DataHelper.getCorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getCorrectCVV());
        orderPage.continueClick();
        orderPage.falseMonthMessage();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }

    @Test
    @DisplayName(value = "09. Expired year card should be allocated")
    void expiredYearCardShouldBeAllocated() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choicePayment();
        orderPage.continueClick();
        orderPage.inputCardNumber(DataHelper.getSecondCardNumber());
        orderPage.inputCardMonth(DataHelper.getThisMonth());
        orderPage.inputCardYear(DataHelper.getLastYear());
        orderPage.inputCustomerName(DataHelper.getCorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getCorrectCVV());
        orderPage.continueClick();
        orderPage.lastYearMessage();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }

    @Test
    @DisplayName(value = "10. Credit should be approved with the card number 41")
    void creditShouldBeApprovedWithCardFortyOne() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choiceCredit();
        orderPage.inputCardNumber(DataHelper.getFirstCardNumber());
        orderPage.inputCardMonth(DataHelper.getThisMonth());
        orderPage.inputCardYear(DataHelper.getNextYear());
        orderPage.inputCustomerName(DataHelper.getCorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getCorrectCVV());
        orderPage.continueClick();
        orderPage.successMessageCheck();
        assertEquals(rowCountFirst + 1, sqlHelper.rowCountChecker());
        assertEquals(DataHelper.getApproved(), sqlHelper.checkCreditStatus());
        assertEquals(sqlHelper.checkBankId(), sqlHelper.checkPaymentId());
    }

    @Test
    @DisplayName(value = "11. Credit should be approved with the card number 42")
    void creditShouldBeApprovedWithCardFortyTwo() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choiceCredit();
        orderPage.inputCardNumber(DataHelper.getSecondCardNumber());
        orderPage.inputCardMonth(DataHelper.getThisMonth());
        orderPage.inputCardYear(DataHelper.getNextYear());
        orderPage.inputCustomerName(DataHelper.getCorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getCorrectCVV());
        orderPage.continueClick();
        orderPage.successMessageCheck();
        assertEquals(rowCountFirst + 1, sqlHelper.rowCountChecker());
        assertEquals(DataHelper.getDeclined(), sqlHelper.checkCreditStatus());
        assertEquals(sqlHelper.checkBankId(), sqlHelper.checkPaymentId());
    }

    @Test
    @DisplayName(value = "12. Credit should be denied with the card number 43")
    void creditShouldBeDeniedWithCardFortyThree() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choiceCredit();
        orderPage.inputCardNumber(DataHelper.getThirdCardNumber());
        orderPage.inputCardMonth(DataHelper.getThisMonth());
        orderPage.inputCardYear(DataHelper.getNextYear());
        orderPage.inputCustomerName(DataHelper.getCorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getCorrectCVV());
        orderPage.continueClick();
        orderPage.checkErrorMessage();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }

    @Test
    @DisplayName(value = "13. Incorrect values will not be entered credit")
    void incorrectValuesWillNotBeEnteredCredit() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choiceCredit();
        orderPage.inputCardNumber(DataHelper.getIncorrectCardNumber());
        orderPage.inputCardMonth(DataHelper.getIncorrectMonth());
        orderPage.inputCardYear(DataHelper.getIncorrectYear());
        orderPage.inputCustomerName(DataHelper.getIncorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getIncorrectCVV());
        orderPage.continueClick();
        orderPage.checkEmptyCardNumberField();
        orderPage.checkEmptyCardMonthField();
        orderPage.checkEmptyCardYearField();
        orderPage.checkEmptyCustomerNameField();
        orderPage.checkEmptyCVVNumberField();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }

    @Test
    @DisplayName(value = "14. Incorrect values(specials) will not be entered credit")
    void specialsWillNotBeEnteredCredit() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choiceCredit();
        orderPage.inputCardNumber(DataHelper.getSpecialsCardNumber());
        orderPage.inputCardMonth(DataHelper.getSpecialsCardMonth());
        orderPage.inputCardYear(DataHelper.getSpecialsCardYear());
        orderPage.inputCustomerName(DataHelper.getSpecialsCustomerName());
        orderPage.inputCardCVV(DataHelper.getSpecialsCardCVV());
        orderPage.continueClick();
        orderPage.checkEmptyCardNumberField();
        orderPage.checkEmptyCardMonthField();
        orderPage.checkEmptyCardYearField();
        orderPage.checkEmptyCustomerNameField();
        orderPage.checkEmptyCVVNumberField();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }


    @Test
    @DisplayName(value = "15. Empty fields should be highlighted credit")
    void emptyFieldsShouldBeHighlightedCredit() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choiceCredit();
        orderPage.continueClick();
        orderPage.checkCardNumberFieldEmptyMessage();
        orderPage.checkCardMonthFieldEmptyMessage();
        orderPage.checkCardYearFieldEmptyMessage();
        orderPage.checkCustomerNameFieldEmptyMessage();
        orderPage.checkCVVNumberFieldEmptyMessage();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }


    @Test
    @DisplayName(value = "16. Nonexistent month should be allocated credit")
    void nonexistentMonthShouldBeAllocatedCredit() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choiceCredit();
        orderPage.continueClick();
        orderPage.inputCardNumber(DataHelper.getSecondCardNumber());
        orderPage.inputCardMonth(DataHelper.getFalseMonth());
        orderPage.inputCardYear(DataHelper.getNextYear());
        orderPage.inputCustomerName(DataHelper.getCorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getCorrectCVV());
        orderPage.continueClick();
        orderPage.falseMonthMessage();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }

    @Test
    @DisplayName(value = "17. Expired month credit card should be allocated")
    void expiredMonthCreditCardShouldBeAllocated() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choiceCredit();
        orderPage.continueClick();
        orderPage.inputCardNumber(DataHelper.getSecondCardNumber());
        orderPage.inputCardMonth(DataHelper.getLastMonth());
        orderPage.inputCardYear(DataHelper.getThisYear());
        orderPage.inputCustomerName(DataHelper.getCorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getCorrectCVV());
        orderPage.continueClick();
        orderPage.falseMonthMessage();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }

    @Test
    @DisplayName(value = "18. Expired year credit card should be allocated")
    void expiredYearCreditCardShouldBeAllocated() throws SQLException {
        val orderPage = new OrderPage();
        val sqlHelper = new SQLHelper();
        long rowCountFirst = sqlHelper.rowCount();
        orderPage.choiceCredit();
        orderPage.continueClick();
        orderPage.inputCardNumber(DataHelper.getSecondCardNumber());
        orderPage.inputCardMonth(DataHelper.getThisMonth());
        orderPage.inputCardYear(DataHelper.getLastYear());
        orderPage.inputCustomerName(DataHelper.getCorrectCustomerName());
        orderPage.inputCardCVV(DataHelper.getCorrectCVV());
        orderPage.continueClick();
        orderPage.lastYearMessage();
        assertEquals(rowCountFirst, sqlHelper.rowCountChecker());
    }
}
