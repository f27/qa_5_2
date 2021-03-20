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
        //Configuration.headless = true;
    }

    @Test
    void formFillTestWithFaker() {
        open("https://demoqa.com/automation-practice-form");

        Faker faker = new Faker();
        Date dateOfBirth = faker.date().birthday();

        String firstname = faker.name().firstName(),
                lastname = faker.name().lastName(),
                email = faker.internet().emailAddress(),
                gender = faker.demographic().sex(),
                mobile = faker.phoneNumber().subscriberNumber(10),
                monthOfBirth = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                yearOfBirth = String.valueOf(dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).getYear()),
                dayOfBirth = String.valueOf(dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).getDayOfMonth()),
                picture = "cat.png",
                address = faker.address().fullAddress(),
                state = "NCR",
                city = "Noida";
        if (dayOfBirth.length() < 2) {
            dayOfBirth = "0" + dayOfBirth;
        }

        String[] subjects = {"English", "Maths", "Arts", "Accounting"}, hobbies = {"Sports", "Music"};


        $("#firstName").setValue(firstname);
        $("#lastName").setValue(lastname);
        $("#userEmail").setValue(email);
        $$("#genterWrapper label").findBy(text(gender)).click();
        $("#userNumber").setValue(mobile);
        $("#dateOfBirthInput").click();
        $(".react-datepicker__month-select").selectOption(monthOfBirth);
        $(".react-datepicker__year-select").selectOption(yearOfBirth);
        $(".react-datepicker__day--0" + dayOfBirth + ":not(.react-datepicker__day--outside-month)").click();
        for (String subject : subjects) {
            $("#subjectsInput").setValue(subject);
            $("#react-select-2-option-0").click();
        }
        Configuration.clickViaJs = true; //Click via javascript for headless
        for (String hobby : hobbies) {
            $$("#hobbiesWrapper label").findBy(text(hobby)).click();
        }
        Configuration.clickViaJs = false;
        $("#uploadPicture").uploadFromClasspath(picture);
        $("#currentAddress").setValue(address);
        $("#state").click();
        $("#state").find(byText(state)).click();
        $("#city").click();
        $("#city").find(byText(city)).click();
        $("#submit").click();

        /*
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
        */

        $x("//td[text()='Student Name']").parent().shouldHave(text(firstname + " " + lastname));
        $x("//td[text()='Student Email']").parent().shouldHave(text(email));
        $x("//td[text()='Gender']").parent().shouldHave(text(gender));
        $x("//td[text()='Mobile']").parent().shouldHave(text(mobile));
        $x("//td[text()='Date of Birth']").parent().shouldHave(text(dayOfBirth + " " + monthOfBirth + "," + yearOfBirth));
        $x("//td[text()='Subjects']").parent().shouldHave(text(String.join(", ", subjects)));
        $x("//td[text()='Hobbies']").parent().shouldHave(text(String.join(", ", hobbies)));
        $x("//td[text()='Picture']").parent().shouldHave(text(picture));
        $x("//td[text()='Address']").parent().shouldHave(text(address));
        $x("//td[text()='State and City']").parent().shouldHave(text(state + " " + city));

        $("#closeLargeModal").click();


    }
}
