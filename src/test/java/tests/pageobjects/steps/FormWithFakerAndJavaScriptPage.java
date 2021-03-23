package tests.pageobjects.steps;

import com.codeborne.selenide.Configuration;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static utils.FileUtils.readStringFromFile;
import static utils.JsonUtils.mapFromJson;

public class FormWithFakerAndJavaScriptPage {

    public void checkPageIsLoaded(String pageText) {
        $(".practice-form-wrapper").shouldHave(text(pageText));
    }

    public void fillFirstName(String firstName) {
        $("#firstName").setValue(firstName);
    }

    public void fillLastName(String lastName) {
        $("#lastName").setValue(lastName);
    }

    public void fillEmail(String email) {
        $("#userEmail").setValue(email);
    }

    public void fillGender(String gender) {
        $("#genterWrapper").$(byText(gender)).scrollIntoView(true).click();
    }

    public void fillMobile(String mobile) {
        $("#userNumber").setValue(mobile);
    }

    public void fillDateOfBirthday(String year, String month, String day) {
        $("#dateOfBirthInput").scrollIntoView(true).click();
        fillDatePicker(year, month, day);
    }

    public void fillSubjects(String subjects) {
        for (String subject : subjects.split(", ")) {
            addSubject(subject);
        }
    }

    public void fillHobbies(String hobbies) {
        Configuration.clickViaJs = true; //Click via javascript for headless
        for (String hobby : hobbies.split(", ")) {
            addHobby(hobby);
        }
        Configuration.clickViaJs = false;
    }

    public void fillPicture(String picture) {
        $("#uploadPicture").uploadFromClasspath(picture);
    }

    public void fillAddress(String address) {
        $("#currentAddress").setValue(address);
    }

    public void fillState(String state) {
        $("#state").scrollIntoView(true).click();
        $("#state").find(byText(state)).scrollIntoView(true).click();
    }

    public void fillCity(String city) {
        $("#city").scrollIntoView(true).click();
        $("#city").find(byText(city)).scrollIntoView(true).click();
    }

    public void clickSubmit() {
        $("#submit").scrollIntoView(true).click();
    }


    public void closeModal() {
        $("#closeLargeModal").scrollIntoView(true).click();
        $(".modal-content").shouldNotBe(visible);
    }

    public void checkData(Map<String, String> expectedData) {
        String js = readStringFromFile("./src/test/resources/js/get_table_data.js");
        Map<String, String> actualData = mapFromJson(executeJavaScript(js));

        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            assertThat(actualData.get(entry.getKey()), is(entry.getValue()));
        }

    }

    private static void fillDatePicker(String year, String month, String day) {
        $(".react-datepicker__month-select").selectOption(month);
        $(".react-datepicker__year-select").selectOption(year);
        $(String.format(".react-datepicker__day--0%s:not(.react-datepicker__day--outside-month)", day)).scrollIntoView(true).click();
    }

    private static void addSubject(String subject) {
        $("#subjectsInput").setValue(subject);
        $("#react-select-2-option-0").scrollIntoView(true).click();
    }

    private static void addHobby(String hobby) {
        $("#hobbiesWrapper").$(byText(hobby)).scrollIntoView(true).click();
    }

}
