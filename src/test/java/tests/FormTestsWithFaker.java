package tests;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class FormTestsWithFaker {
    Faker faker = new Faker();
    Date dateOfBirth = faker.date().birthday();

    static HashMap<String, String[]> statesAndCities = new HashMap<>();

    static {
        statesAndCities.put("NCR", new String[]{"Delhi", "Gurgaon", "Noida"});
        statesAndCities.put("Uttar Pradesh", new String[]{"Agra", "Lucknow", "Merrut"});
        statesAndCities.put("Haryana", new String[]{"Karnal", "Panipat"});
        statesAndCities.put("Rajasthan", new String[]{"Jaipur", "Jaiselmer"});
    }

    List<String> statesList = new ArrayList<>(statesAndCities.keySet());

    String firstname = faker.name().firstName(),
            lastname = faker.name().lastName(),
            email = faker.internet().emailAddress(firstname.toLowerCase() + "." + lastname.toLowerCase()),
            gender = faker.demographic().sex(),
            mobile = faker.numerify("##########"), //10 Digits
            yearOfBirth = new SimpleDateFormat("yyyy").format(dateOfBirth),
            monthOfBirth = new SimpleDateFormat("MMMM", Locale.ENGLISH).format(dateOfBirth),
            dayOfBirth = new SimpleDateFormat("dd").format(dateOfBirth),
            picture = "cat.png",
            address = faker.address().fullAddress(),
            state = statesList.get(faker.random().nextInt(statesList.size())),
            city = statesAndCities.get(state)[faker.random().nextInt(statesAndCities.get(state).length)],
            formLabel = "Student Registration Form",
            modalLabel = "Thanks for submitting the form";

    String[] subjects = {"English", "Maths", "Arts", "Accounting"}, hobbies = {"Sports", "Music"};


    @BeforeAll
    static void setup() {
        Configuration.startMaximized = true;
    }

    @Test
    void formFillTestWithFaker() {
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
