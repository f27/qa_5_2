package tests;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.FileUtils.readStringFromFile;
import static utils.JsonUtils.mapFromJson;

public class FormWithFakerAndJavaScriptTests {
    static Faker faker = new Faker();
    static Date dateOfBirth = faker.date().birthday();

    static Map<String, String[]> statesAndCities = new HashMap<String, String[]>() {{
        put("NCR", new String[]{"Delhi", "Gurgaon", "Noida"});
        put("Uttar Pradesh", new String[]{"Agra", "Lucknow", "Merrut"});
        put("Haryana", new String[]{"Karnal", "Panipat"});
        put("Rajasthan", new String[]{"Jaipur", "Jaiselmer"});
    }};

    static List<String> statesList = new ArrayList<>(statesAndCities.keySet());

    static String firstName = faker.name().firstName(),
            lastName = faker.name().lastName(),
            email = faker.internet().emailAddress(firstName.toLowerCase() + "." + lastName.toLowerCase()),
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

    static String[] subjects = {"English", "Maths", "Arts", "Accounting"}, hobbies = {"Sports", "Music"};

    static Map<String, String> expectedData  = new HashMap<String, String>() {{
        put("Student Name", firstName + " " + lastName);
        put("Student Email", email);
        put("Gender", gender);
        put("Mobile", mobile);
        put("Date of Birth", dayOfBirth + " " + monthOfBirth + "," + yearOfBirth);
        put("Subjects", String.join(", ", subjects));
        put("Hobbies", String.join(", ", hobbies));
        put("Picture", picture);
        put("Address", address);
        put("State and City", state + " " + city);
    }};

    @BeforeAll
    static void setup() {
        Configuration.startMaximized = true;
        openForm();
        fillForm();
    }

    @AfterAll
    static void closeModal() {
        checkCloseButton();
    }

    @ParameterizedTest
    @MethodSource("getTableDataAsStream")
    void formFillTestWithFaker(String key, String actualValue) {
        assertThat(expectedData.get(key), is(actualValue));
    }

    public static Stream<Arguments> getTableDataAsStream() {
        String js = readStringFromFile("./src/test/resources/js/get_table_data.js");
        Map<String, String> actualData = mapFromJson(executeJavaScript(js));
        return createList(actualData).stream();
    }

    private static List<Arguments> createList(Map<String, String> data) {
        return data.entrySet()
                .stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public static void openForm() {
        open("https://demoqa.com/automation-practice-form");
        $(".practice-form-wrapper").shouldHave(text(formLabel));
    }

    public static void checkCloseButton() {
        $("#closeLargeModal").scrollIntoView(true).click();
        $(".modal-content").shouldNotBe(visible);
    }

    public static void fillForm() {
        $("#firstName").setValue(firstName);
        $("#lastName").setValue(lastName);
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
    }
}
