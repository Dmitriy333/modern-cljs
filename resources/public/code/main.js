function submitRegistrationForm() {
    var password = document.getElementById("password").value;
    var confirmedPassword = document.getElementById("confirm-password").value;

    if (password === confirmedPassword) {
        alert("You have successfully registered")
        return true;
    } else {
        alert("Passwords are not the same. Please try again");
        return false;
    }
}