$(document).ready(function () {
    $("#login").click(function (event) {
        let email = document.getElementById("email").value;
        let psw = document.getElementById("psw").value;
        $.ajax({
            url : "MainServlet", // Url of backend (can be python, php, etc..)
            type: "POST", // data type (can be get, post, put, delete)
            data : {submit: "login", email: email, password: psw}, // data in json format
            async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
            success: function(data) {
                console.log("risultato data: " + data + "tipo: " + typeof data);
                let res = Number(data);
                if(res === 0){
                    alert("User not found");
                } else if(res === 1) {
                    console.log("entra");
                    event.preventDefault();
                    window.location = "index.html";
                }
            }
        });
    });
});