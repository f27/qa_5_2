package tests;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class FormTestsWithFaker {
    @BeforeAll
    static void setup() {
        Configuration.startMaximized = true;
    }

    @Test
    void formFillTest() {
        open("https://demoqa.com/automation-practice-form");

        Faker faker = new Faker();
        String firstname = faker.name().firstName(),
                lastname = faker.name().lastName(),
                email = faker.internet().emailAddress(),
                gender = faker.demographic().sex(),
                mobile = faker.phoneNumber().subscriberNumber(10),
                monthofbirth = faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                yearofbirth = String.valueOf(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).getYear()),
                dayofbirth = String.valueOf(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).getDayOfMonth()),
                subject1 = "English",
                subject2 = "Maths",
                hobby1 = "Sports",
                hobby2 = "Music",
                picture = "cat.png",
                address = faker.address().fullAddress(),
                state = "NCR",
                city = "Noida";

        $("#firstName").setValue(firstname);
        $("#lastName").setValue(lastname);
        $("#userEmail").setValue(email);
        $$("#genterWrapper label").findBy(text(gender)).click();
        $("#userNumber").setValue(mobile);
        $("#dateOfBirthInput").click();
        $(".react-datepicker__month-select").selectOption(monthofbirth);
        $(".react-datepicker__year-select").selectOption(yearofbirth);
        $$(".react-datepicker__day:not(.react-datepicker__day--outside-month)").findBy(text(dayofbirth)).click();
        $("#subjectsInput").setValue(subject1).pressEnter();
        $("#subjectsInput").setValue(subject2).pressEnter();
        $$("#hobbiesWrapper label").findBy(text(hobby1)).click();
        $$("#hobbiesWrapper label").findBy(text(hobby2)).click();
        $("#uploadPicture").uploadFromClasspath(picture);
        $("#currentAddress").setValue(address);
        $("#react-select-3-input").setValue(state).pressEnter();
        $("#react-select-4-input").setValue(city).pressEnter();
        $("#submit").click();

        $(".modal-content").shouldHave(text(firstname),
                text(lastname),
                text(email),
                text(gender),
                text(mobile),
                text(dayofbirth+" "+monthofbirth+","+yearofbirth),
                text(subject1+", "+subject2),
                text(hobby1+", "+hobby2),
                text(picture),
                text(address),
                text(state),
                text(city));


    }
}
