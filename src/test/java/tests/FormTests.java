package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

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

        //First name
        $("#firstName").setValue("Alex");

        //Last name
        $("#lastName").setValue("Testoff");

        //Email
        $("#userEmail").setValue("someemail@qaqaqaqa.qa");

        //Gender (gender-radio-1 = Male; gender-radio-2 = Female; gender-radio-3 = Other)
        $("[for='gender-radio-1']").click();

        //Mobile
        $("#userNumber").setValue("0123456789");

        //Date Of Birth
        $("#dateOfBirthInput").click();
        $(".react-datepicker__month-select").selectOptionByValue("2");
        $(".react-datepicker__year-select").selectOptionByValue("2011");
        $("[aria-label='Choose Monday, March 28th, 2011']").click();

        //Subjects (Autocomplete form)
        $("#subjectsInput").setValue("English");
        $("#subjectsInput").pressEnter();
        $("#subjectsInput").setValue("Maths");
        $("#subjectsInput").pressEnter();

        //Hobbies (hobbies-checkbox-1 = Sports; hobbies-checkbox-2 = Reading; hobbies-checkbox-3 = Music)
        $("[for='hobbies-checkbox-1']").click();
        $("[for='hobbies-checkbox-3']").click();

        //Picture
        File picture = new File("src/test/resources/cat.png");
        $("#uploadPicture").uploadFile(picture);

        //Current address
        $("#currentAddress").setValue("Moscow, Neverland");

        //State and City
        $("#react-select-3-input").setValue("NCR");
        $("#react-select-3-input").pressEnter();
        $("#react-select-4-input").setValue("Noida");
        $("#react-select-4-input").pressEnter();

        //Submit
        $("#submit").click();

        //Check
        $(".modal-content").shouldHave(text("Alex"));
        $(".modal-content").shouldHave(text("Testoff"));
        $(".modal-content").shouldHave(text("someemail@qaqaqaqa.qa"));
        $(".modal-content").shouldHave(text("Male"));
        $(".modal-content").shouldHave(text("0123456789"));
        $(".modal-content").shouldHave(text("28 March,2011"));
        $(".modal-content").shouldHave(text("English, Maths"));
        $(".modal-content").shouldHave(text("Sports, Music"));
        $(".modal-content").shouldHave(text("cat.png"));
        $(".modal-content").shouldHave(text("Moscow, Neverland"));
        $(".modal-content").shouldHave(text("NCR"));
        $(".modal-content").shouldHave(text("Noida"));


    }
}
