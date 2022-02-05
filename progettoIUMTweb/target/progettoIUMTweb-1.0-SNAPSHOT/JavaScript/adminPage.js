$(document).ready(function () {
    var app = new Vue ({
        el: '#accordion',
        data: {
            subjects:[], //array of subject available
            teachers:[], //array of teachers available
            res:[], //auxiliary array get from the backend
            link: '/progettoIUMTweb_war_exploded/MainServlet'
        },
        mounted() {
            console.log("entra nel mounted");
        },
        methods:{
            getSubjects:function () {
                var self = this;
                $.ajax({
                    url : "MainServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "subjectAvailable"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        self.subjects = data;
                    }
                });
            },
            getTeachers:function (subject, email) {
                var self = this;
                if (subject === "All") {
                    $.ajax({
                        url : "MainServlet", // Url of backend (can be python, php, etc..)
                        type: "GET", // data type (can be get, post, put, delete)
                        data : {submit: "getAllTeachers", email: email}, // data in json format
                        async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                        success: function(data) {
                            self.teachers = data;
                        }
                    });
                } else {
                    console.log("------"+subject.replace(/\s/g, '')+"------");
                    $.ajax({
                        url : "MainServlet", // Url of backend (can be python, php, etc..)
                        type: "GET", // data type (can be get, post, put, delete)
                        data : {submit: "getTeachers", subject: subject.replace(/\s/g, ''), email: "true"}, // data in json format
                        async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                        success: function(data) {
                            self.teachers = data;
                        }
                    });
                }

            },
            deleteTeacher:function (index) {
                let teacherElem = this.teachers[index];
                let elem = teacherElem.split(" ");
                $.ajax({
                    url : "MainServlet", // Url of backend (can be python, php, etc..)
                    type: "POST", // data type (can be get, post, put, delete)
                    data : {submit: "deleteTeacher", nome: elem[0], cognome: elem[1], email: elem[2]}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        alert("Docente eliminato correttamente");
                        location.reload();
                    }
                });
            },
            deleteAssociation:function (index, subject) {
                let teacherElem = this.teachers[index];
                let elem = teacherElem.split(" ");
                console.log("----"+subject+"-----");
                console.log("----"+elem[0]+"-----");
                console.log("----"+elem[1]+"-----");
                console.log("----"+elem[2]+"-----");
                $.ajax({
                    url : "MainServlet", // Url of backend (can be python, php, etc..)
                    type: "POST", // data type (can be get, post, put, delete)
                    data : {submit: "deleteAssociation", email: elem[2], subject: subject}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function() {
                        alert("Associazione " + elem[2] + " - " + subject + " eliminata correttamente");
                        location.reload();
                    }
                });
            },
            deleteCourse:function (index) {
                let self = this;
                let subject = this.subjects[index];
                $.ajax({
                    url : "MainServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "deleteCourse", subject: subject}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function() {
                        alert("Corso " + subject + " eliminato correttamente");
                        location.reload();
                    }
                });
            }
        }
    });

    $("#invioDatiDocente").click(function (event) {
        let nome = document.getElementById("formInputNome").value;
        let cognome = document.getElementById("formInputCognome").value;
        let email = document.getElementById("formInputEmail").value;
        $.ajax({
            url : "MainServlet", // Url of backend (can be python, php, etc..)
            type: "POST", // data type (can be get, post, put, delete)
            data : {submit: "insertTeacher", nome: nome, cognome: cognome, email: email}, // data in json format
            async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
            success: function(data) {
                if (data === "true"){
                    alert("Docente inserito con successo");
                    location.reload();
                }
                else
                    alert("Errore, il docente è già presente nel sistema");
            }
        });
    });

    $("#sectionDeleteTeacher").click(function (event) {
        app.getSubjects();
    });

    $("#sectionInsertAssociation").click(function (event) {
        app.getSubjects();
        app.getTeachers("All", "false");
    });

    $("#confirmAssociationButton").click(function (event) {
        if ($('#teacherSelected3').find(":selected").text() !== "Seleziona il docente" && $('#subjectSelected3').find(":selected").text() !== "Seleziona la materia") {
            let subject = $('#subjectSelected3').find(":selected").text();
            let teacherAux = $('#teacherSelected3').find(":selected").text();
            aux = teacherAux.split(" ");
            let teacher = aux[1] + " " + aux[2];
            $.ajax({
                url : "MainServlet", // Url of backend (can be python, php, etc..)
                type: "GET", // data type (can be get, post, put, delete)
                data : {submit: "insertAssociation", teacher: teacher, subject: subject.replace(/\s/g, '')}, // data in json format
                async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                success: function(data) {
                    if (data === "true") {
                        alert("Corso inserito con successo");
                        location.reload();
                    }
                    else
                        alert("Errore");
                }
            });
        }
    });

    $("#sectionDeleteAssociation").click(function (event) {
        app.getSubjects();
    });

    $("#subjectSelected2").change(function(){
        if ($('#subjectSelected2').find(":selected").text() !== "Restringi la tua ricerca") {
            app.getTeachers($('#subjectSelected2').find(":selected").text(), "true");
        } else
            app.teachers = [];
    });

    $("#listTeachers").on('click', '#confirmDeleteButton', function () {
        let index = $(this).parent().index();
        app.deleteTeacher(index);
    });

    $("#subjectSelected4").change(function(){
        if ($('#subjectSelected4').find(":selected").text() !== "Select a subject") {
            app.getTeachers($('#subjectSelected4').find(":selected").text());
        } else
            app.teachers = [];
    });

    $("#listAssociatedTeachers").on('click', '#confirmDeleteAssociation', function () {
        let index = $(this).parent().index();
            app.deleteAssociation(index, document.getElementById("subjectSelected4").value);
    });

    $("#invioNomeCorso").click(function (event) {
        let subject = document.getElementById("formInputCorso").value;
        $.ajax({
            url : "MainServlet", // Url of backend (can be python, php, etc..)
            type: "GET", // data type (can be get, post, put, delete)
            data : {submit: "insertCourse", subject: subject}, // data in json format
            async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
            success: function(data) {
                if (data === "true")
                    alert("Corso inserito con successo");
                else
                    alert("Errore, il corso è già nel sistema");
            }
        });
    });

    $("#sectionDeleteCourse").click(function (event) {
        app.getSubjects();
    });

    $("#listCourse").on('click', '#confirmDeleteCourse', function () {
        app.deleteCourse($(this).parent().index());
    });

});