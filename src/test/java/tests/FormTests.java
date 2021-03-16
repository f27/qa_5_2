package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class FormTests {
    @BeforeAll
    static void setup() {
        Configuration.startMaximized = true;
    }

    @Test
    void formFillTest() {
        open("https://demoqa.com/automation-practice-form");

        $("#firstName").setValue("Alex");
        $("#lastName").setValue("Testoff");
        $("#userEmail").setValue("someemail@qaqaqaqa.qa");
        $("[for='gender-radio-1']").click();
        $("#userNumber").setValue("0123456789");
        $("#dateOfBirthInput").click();
        $(".react-datepicker__month-select").selectOptionByValue("2");
        $(".react-datepicker__year-select").selectOptionByValue("2011");
        $("[aria-label='Choose Monday, March 28th, 2011']").click();
        $("#subjectsInput").setValue("English").pressEnter();
        $("#subjectsInput").setValue("Maths").pressEnter();
        $("[for='hobbies-checkbox-1']").click();
        $("[for='hobbies-checkbox-3']").click();
        $("#uploadPicture").uploadFromClasspath("cat.png");
        $("#currentAddress").setValue("Moscow, Neverland");
        $("#react-select-3-input").setValue("NCR").pressEnter();
        $("#react-select-4-input").setValue("Noida").pressEnter();
        $("#submit").click();

        $(".modal-content").shouldHave(text("Alex"),
                text("Testoff"),
                text("someemail@qaqaqaqa.qa"),
                text("Male"),
                text("0123456789"),
                text("28 March,2011"),
                text("English, Maths"),
                text("Sports, Music"),
                text("cat.png"),
                text("Moscow, Neverland"),
                text("NCR"),
                text("Noida"));


    }
}
