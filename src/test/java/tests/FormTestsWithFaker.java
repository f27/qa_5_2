package tests;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class FormTestsWithFaker {
    @BeforeAll
    static void setup() {
        //Configuration.startMaximized = true;
        Configuration.browserSize = "800x200";
    }

    @Test
    void formFillTestWithFaker() {
        Faker faker = new Faker();
        Date dateOfBirth = faker.date().birthday();
        Map<String, String[]> statesAndCities = new HashMap<>();
        statesAndCities.put("NCR", new String[]{"Delhi", "Gurgaon", "Noida"});
        statesAndCities.put("Uttar Pradesh", new String[]{"Agra", "Lucknow", "Merrut"});
        statesAndCities.put("Haryana", new String[]{"Karnal", "Panipat"});
        statesAndCities.put("Rajasthan", new String[]{"Jaipur", "Jaiselmer"});

        List<String> statesList = new ArrayList<>(statesAndCities.keySet());

        String firstname = faker.name().firstName(),
                lastname = faker.name().lastName(),
                email = faker.internet().emailAddress(firstname.toLowerCase() + "." + lastname.toLowerCase()),
                gender = faker.demographic().sex(),
                mobile = faker.numerify("##########"), //10 Digits
                monthOfBirth = dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                yearOfBirth = String.valueOf(dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).getYear()),
                dayOfBirth = String.valueOf(dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).getDayOfMonth()),
                picture = "cat.png",
                address = faker.address().fullAddress(),
                state = statesList.get(faker.random().nextInt(statesList.size())),
                city = statesAndCities.get(state)[faker.random().nextInt(statesAndCities.get(state).length)],
                formLabel = "Student Registration Form",
                modalLabel = "Thanks for submitting the form";

        if (dayOfBirth.length() < 2) {
            dayOfBirth = "0" + dayOfBirth;
        }

        String[] subjects = {"English", "Maths", "Arts", "Accounting"}, hobbies = {"Sports", "Music"};

        open("https://demoqa.com/automation-practice-form");

        $(".practice-form-wrapper").shouldHave(text(formLabel));

        $("#firstName").setValue(firstname);
        $("#lastName").setValue(lastname);
        $("#userEmail").setValue(email);
        $("#genterWrapper").$(byText(gender)).scrollIntoView(true).click();
        $("#userNumber").setValue(mobile);
        $("#dateOfBirthInput").scrollIntoView(true).click();
        $(".react-datepicker__month-select").selectOption(monthOfBirth);
        $(".react-datepicker__year-select").selectOption(yearOfBirth);
        $(String.format(".react-datepicker__day--0%s:not(.react-datepicker__day--outside-month)", dayOfBirth)).scrollIntoView(true).click();
        for (String subject : subjects) {
            $("#subjectsInput").setValue(subject);
            $("#react-select-2-option-0").scrollIntoView(true).click();
        }
        Configuration.clickViaJs = true; //Click via javascript for headless
        for (String hobby : hobbies) {
            $("#hobbiesWrapper").$(byText(hobby)).scrollIntoView(true).click();
        }
        Configuration.clickViaJs = false;
        $("#uploadPicture").uploadFromClasspath(picture);
        $("#currentAddress").setValue(address);
        $("#state").scrollIntoView(true).click();
        $("#state").find(byText(state)).scrollIntoView(true).click();
        $("#city").scrollIntoView(true).click();
        $("#city").find(byText(city)).scrollIntoView(true).click();
        $("#submit").scrollIntoView(true).click();

        $("#example-modal-sizes-title-lg").shouldHave(text(modalLabel));

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

        $("#closeLargeModal").scrollIntoView(true).click();

        $(".modal-content").shouldNotBe(visible);
    }
}
