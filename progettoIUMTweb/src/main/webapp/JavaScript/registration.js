//verifica quanto la pws è sicura, non funziona esternamente, internamente a HTML funziona correttamente
/*var pass = document.getElementById("password");
var msg = document.getElementById("msg");
var str = document.getElementById("strength");

pass.addEventListener("input", () => {
    document.write("ci entri qui?");
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
})*/