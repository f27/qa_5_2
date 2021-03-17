package tests;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class FormTestsWithFaker {
    @BeforeAll
    static void setup() {
        Configuration.startMaximized = true;
    }

    @Test
    void formFillTestWithFaker() {
        open("https://demoqa.com/automation-practice-form");

        Faker faker = new Faker();
        Date dateofbirth = faker.date().birthday();

        String firstname = faker.name().firstName(),
                lastname = faker.name().lastName(),
                email = faker.internet().emailAddress(),
                gender = faker.demographic().sex(),
                mobile = faker.phoneNumber().subscriberNumber(10),
                monthofbirth = dateofbirth.toInstant().atZone(ZoneId.systemDefault()).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                yearofbirth = String.valueOf(dateofbirth.toInstant().atZone(ZoneId.systemDefault()).getYear()),
                dayofbirth = String.valueOf(dateofbirth.toInstant().atZone(ZoneId.systemDefault()).getDayOfMonth()),
                picture = "cat.png",
                address = faker.address().fullAddress(),
                state = "NCR",
                city = "Noida";
        if (dayofbirth.length()<2){
            dayofbirth = "0" + dayofbirth;
        }

        String[] subjects = {"English", "Maths", "Arts", "Accounting"}, hobbies = {"Sports", "Music"};


        $("#firstName").setValue(firstname);
        $("#lastName").setValue(lastname);
        $("#userEmail").setValue(email);
        $$("#genterWrapper label").findBy(text(gender)).click();
        $("#userNumber").setValue(mobile);
        $("#dateOfBirthInput").click();
        $(".react-datepicker__month-select").selectOption(monthofbirth);
        $(".react-datepicker__year-select").selectOption(yearofbirth);
        $(".react-datepicker__day--0" + dayofbirth + ":not(.react-datepicker__day--outside-month)").click();
        for(String subject:subjects) {
            $("#subjectsInput").setValue(subject);
            $("#react-select-2-option-0").click();
        }
        for (String hobby:hobbies) {
            executeJavaScript("arguments[0].click();", $$("#hobbiesWrapper label").findBy(text(hobby)));
        }
        $("#uploadPicture").uploadFromClasspath(picture);
        $("#currentAddress").setValue(address);
        $("#state").click();
        $(byText(state)).click();
        $("#city").click();
        $(byText(city)).click();
        $("#submit").click();

        $(".modal-content").shouldHave(text(firstname),
                text(lastname),
                text(email),
                text(gender),
                text(mobile),
                text(dayofbirth + " " + monthofbirth + "," + yearofbirth),
                text(String.join(", ", subjects)),
                text(String.join(", ", hobbies)),
                text(picture),
                text(address),
                text(state),
                text(city));


    }
}
