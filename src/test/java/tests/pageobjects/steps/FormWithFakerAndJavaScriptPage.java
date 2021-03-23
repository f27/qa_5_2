package tests.pageobjects.steps;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;

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
    private static final SelenideElement
            formTitleField = $(".practice-form-wrapper"),
            firstNameField = $("#firstName"),
            lastNameField = $("#lastName"),
            emailField = $("#userEmail"),
            genderField = $("#genterWrapper"),
            mobileField = $("#userNumber"),
            datePickerField = $("#dateOfBirthInput"),
            yearField = $(".react-datepicker__year-select"),
            monthField = $(".react-datepicker__month-select"),
            subjectsInput = $("#subjectsInput"),
            firstSubject = $("#react-select-2-option-0"),
            hobbiesWrapper = $("#hobbiesWrapper"),
            pictureUpload = $("#uploadPicture"),
            addressField = $("#currentAddress"),
            stateField = $("#state"),
            cityField = $("#city"),
            submitButton = $("#submit"),
            modalWindow = $(".modal-content"),
            closeModalButton = $("#closeLargeModal");
    private static final String classOfDay = ".react-datepicker__day--0%s:not(.react-datepicker__day--outside-month)";

    public void checkPageIsLoaded(String pageText) {
        formTitleField.shouldHave(text(pageText));
    }

    public void fillFirstName(String firstName) {
        firstNameField.setValue(firstName);
    }

    public void fillLastName(String lastName) {
        lastNameField.setValue(lastName);
    }

    public void fillEmail(String email) {
        emailField.setValue(email);
    }

    public void fillGender(String gender) {
        genderField.$(byText(gender)).scrollIntoView(true).click();
    }

    public void fillMobile(String mobile) {
        mobileField.setValue(mobile);
    }

    public void fillDateOfBirthday(String year, String month, String day) {
        datePickerField.scrollIntoView(true).click();
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
        pictureUpload.uploadFromClasspath(picture);
    }

    public void fillAddress(String address) {
        addressField.setValue(address);
    }

    public void fillState(String state) {
        stateField.scrollIntoView(true).click();
        stateField.find(byText(state)).scrollIntoView(true).click();
    }

    public void fillCity(String city) {
        cityField.scrollIntoView(true).click();
        cityField.find(byText(city)).scrollIntoView(true).click();
    }

    public void clickSubmit() {
        submitButton.scrollIntoView(true).click();
    }


    public void closeModal() {
        closeModalButton.scrollIntoView(true).click();
        modalWindow.shouldNotBe(visible);
    }

    public void checkData(Map<String, String> expectedData) {
        String js = readStringFromFile("./src/test/resources/js/get_table_data.js");
        Map<String, String> actualData = mapFromJson(executeJavaScript(js));

        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            assertThat(actualData.get(entry.getKey()), is(entry.getValue()));
        }

    }

    private static void fillDatePicker(String year, String month, String day) {
        monthField.selectOption(month);
        yearField.selectOption(year);
        $(String.format(classOfDay, day)).scrollIntoView(true).click();
    }

    private static void addSubject(String subject) {
        subjectsInput.setValue(subject);
        firstSubject.scrollIntoView(true).click();
    }

    private static void addHobby(String hobby) {
        hobbiesWrapper.$(byText(hobby)).scrollIntoView(true).click();
    }

}
