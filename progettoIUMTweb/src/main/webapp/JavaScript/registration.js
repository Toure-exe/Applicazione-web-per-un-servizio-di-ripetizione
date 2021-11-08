//verifica quanto la pws è sicura, non funziona esternamente, internamente a HTML funziona correttamente


function passwordCheck() {
    let pass = document.getElementById("password");
    let msg = document.getElementById("msg1");
    let str = document.getElementById("strength");

    pass.addEventListener("input", () => {

        if (pass.value.length > 0) {
            msg.style.display = "block";
        } else {
            msg.style.display = "none";
        }

        if(pass.value.length < 4) {
            str.innerHTML = "weak";
            pass.style.borderColor = "red";
            msg.style.color = "red";
        } else if (pass.value.length >= 4 && pass.value.length < 8){
            str.innerHTML = "medium";
            pass.style.borderColor = "yellow";
            msg.style.color = "yellow";
        } else {
            str.innerHTML = "strong";
            pass.style.borderColor = "green";
            msg.style.color = "green";
        }
    })
}

function equalsPassword() {

    let pass1 = document.getElementById("password");
    let pass2 = document.getElementById("rPassword");
    let msg1 = document.getElementById("msg2");
    let str1 = document.getElementById("equals");

    pass2.addEventListener("input", () => {
        if (pass2.value.length > 0) {
            msg1.style.display = "block";
        } else {
            msg1.style.display = "none";
        }

        if (pass1.value === pass2.value) {
            str1.innerHTML = "Passwords are equal";
            msg1.style.color = "green";
        } else {
            str1.innerHTML = "Passwords are not equal";
            msg1.style.color = "red";
        }
    })
}
