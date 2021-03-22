package tests.pageobjects;

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

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.FileUtils.readStringFromFile;
import static utils.JsonUtils.mapFromJson;

public class FormWithFakerAndJavaScriptTests {
    static FormWithFakerAndJavaScriptPage formWithFakerAndJavaScriptPage;
    static Faker faker = new Faker();

    static Date dateOfBirth = faker.date().birthday();

    static Map<String, String[]> statesAndCities = new HashMap<String, String[]>() {{
        put("NCR", new String[]{"Delhi", "Gurgaon", "Noida"});
        put("Uttar Pradesh", new String[]{"Agra", "Lucknow", "Merrut"});
        put("Haryana", new String[]{"Karnal", "Panipat"});
        put("Rajasthan", new String[]{"Jaipur", "Jaiselmer"});
    }};

    static List<String> statesList = new ArrayList<>(statesAndCities.keySet());

    static String state = statesList.get(faker.random().nextInt(statesList.size())),
            city = statesAndCities.get(state)[faker.random().nextInt(statesAndCities.get(state).length)];

    static String[] subjects = {"English", "Maths", "Arts", "Accounting"}, hobbies = {"Sports", "Music"};

    static Map<String, String> userData = new HashMap<String, String>() {{
        put("First Name", faker.name().firstName());
        put("Last Name", faker.name().lastName());
        put("Email", faker.internet().emailAddress());
        put("Gender", faker.demographic().sex());
        put("Mobile", faker.numerify("##########"));//10 digits
        put("Year Of Birth", new SimpleDateFormat("yyyy").format(dateOfBirth));
        put("Month Of Birth", new SimpleDateFormat("MMMM", Locale.ENGLISH).format(dateOfBirth));
        put("Day Of Birth", new SimpleDateFormat("dd").format(dateOfBirth));
        put("Subjects", String.join(", ", subjects));
        put("Hobbies", String.join(", ", hobbies));
        put("Picture", "cat.png");
        put("Address", faker.address().fullAddress());
        put("State", state);
        put("City", city);
        put("Form Title", "Student Registration Form");
        put("Thanks Title", "Thanks for submitting the form");
    }};

    static Map<String, String> expectedData = new HashMap<String, String>() {{
        put("Student Name", userData.get("First Name") + " " + userData.get("Last Name"));
        put("Student Email", userData.get("Email"));
        put("Gender", userData.get("Gender"));
        put("Mobile", userData.get("Mobile"));
        put("Date of Birth", userData.get("Day Of Birth") + " " + userData.get("Month Of Birth") + "," + userData.get("Year Of Birth"));
        put("Subjects", userData.get("Subjects"));
        put("Hobbies", userData.get("Hobbies"));
        put("Picture", userData.get("Picture"));
        put("Address", userData.get("Address"));
        put("State and City", userData.get("State") + " " + userData.get("City"));
    }};

    @BeforeAll
    static void setup() {
        Configuration.startMaximized = true;
        formWithFakerAndJavaScriptPage = open("https://demoqa.com/automation-practice-form", FormWithFakerAndJavaScriptPage.class);
        formWithFakerAndJavaScriptPage.fillForm(userData);
    }

    @AfterAll
    static void closeModal() {
        formWithFakerAndJavaScriptPage.closeModal();
    }

    @ParameterizedTest
    @MethodSource("getTableDataAsStream")
    void formFillTestWithFaker(String key, String actualValue) {
        assertThat(expectedData.get(key), is(actualValue));
    }

    public static Stream<Arguments> getTableDataAsStream() {
        String js = readStringFromFile("./src/test/resources/js/get_table_data.js");
        return createList(mapFromJson(executeJavaScript(js))).stream();
    }

    private static List<Arguments> createList(Map<String, String> data) {
        return data.entrySet()
                .stream()
                .map(e -> Arguments.of(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}
