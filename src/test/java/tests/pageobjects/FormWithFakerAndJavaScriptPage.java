package tests.pageobjects;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;

import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class FormWithFakerAndJavaScriptPage {
    private static final SelenideElement
            formTitle = $(".practice-form-wrapper"),
            firstName = $("#firstName"),
            lastName = $("#lastName"),
            email = $("#userEmail"),
            gender = $("#genterWrapper"),
            mobile = $("#userNumber"),
            datePicker =  $("#dateOfBirthInput"),
            yearField = $(".react-datepicker__year-select"),
            monthField = $(".react-datepicker__month-select"),
            subjectsInput = $("#subjectsInput"),
            firstSubject = $("#react-select-2-option-0"),
            hobbiesWrapper = $("#hobbiesWrapper"),
            pictureUpload = $("#uploadPicture"),
            address = $("#currentAddress"),
            state = $("#state"),
            city = $("#city"),
            submit = $("#submit"),
            modal = $(".modal-content"),
            closeModal = $("#closeLargeModal");
    private static final String classOfDay = ".react-datepicker__day--0%s:not(.react-datepicker__day--outside-month)";


    public void fillForm(Map<String, String> userData) {
        formTitle.shouldHave(text(userData.get("Form Title")));
        firstName.setValue(userData.get("First Name"));
        lastName.setValue(userData.get("Last Name"));
        email.setValue(userData.get("Email"));
        gender.$(byText(userData.get("Gender"))).scrollIntoView(true).click();
        mobile.setValue(userData.get("Mobile"));
        datePicker.scrollIntoView(true).click();
        fillDatePicker(userData.get("Year Of Birth"), userData.get("Month Of Birth"), userData.get("Day Of Birth"));
        for (String subject : userData.get("Subjects").split(", ")) {
            addSubject(subject);
        }
        Configuration.clickViaJs = true; //Click via javascript for headless
        for (String hobby : userData.get("Hobbies").split(", ")) {
            addHobby(hobby);
        }
        Configuration.clickViaJs = false;
        pictureUpload.uploadFromClasspath(userData.get("Picture"));
        address.setValue(userData.get("Address"));
        state.scrollIntoView(true).click();
        state.find(byText(userData.get("State"))).scrollIntoView(true).click();
        city.scrollIntoView(true).click();
        city.find(byText(userData.get("City"))).scrollIntoView(true).click();
        submit.scrollIntoView(true).click();
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

    public void closeModal() {
        closeModal.scrollIntoView(true).click();
        modal.shouldNotBe(visible);
    }
}
