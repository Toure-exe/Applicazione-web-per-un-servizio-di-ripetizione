$(document).ready(function () {

    var app = new Vue ({
        el: '#sessionID',
        data: {
            role: '',
            subjects:[],
            available: false,
            session: '',
            subjectSelected: '',
            teacherSelected: '',
            rowSelected: '',
            columnSelected: '',
            teachers:[],
            booking:[],
            bookingMessage: '',
            guest: true,
            student: false,
            admin: false,
            index: true,
            login: false,
            registration: false,
            visualization: false,
            history: false,
            activeTutoring: false,
            adminPage: false,
            tutorings:[],
            histories:[],
            res:[] //auxiliary array get from the backend
        },
        mounted() {
            this.sessionFunction();
            console.log("entra nel mounted");
        },
        methods:{
            sessionFunction:function(){
                var self = this;
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "POST", // data type (can be get, post, put, delete)
                    data : {submit: "indexSession"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        console.log(data);
                        self.session = data;
                    }
                });
                var self1 = this;
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "POST", // data type (can be get, post, put, delete)
                    data : {submit: "getRoleSession"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {

                        console.log(data + "tipo: " + typeof data);
                        switch (data) {
                            case '0':
                                self1.guest = true;
                                self1.student = false;
                                self1.admin = false;
                                break;
                            case '1':
                                self1.guest = false;
                                self1.student = true;
                                self1.admin = false;
                                break;
                            case '2':
                                self1.guest = false;
                                self1.student = false;
                                self1.admin = true;
                                break;
                        }
                    }
                });
            },
            logout: function (){
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "POST", // data type (can be get, post, put, delete)
                    data : {submit: "logout"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        console.log(data);
                        location.reload();
                    }
                });
            },
            goLogin: function () {
                this.index = false;
                this.login = true;
                this.registration = false;
                this.visualization = false;
                this.history = false;
                this.activeTutoring = false;
                this.adminPage = false;
            },
            goRegistration: function () {
                this.index = false;
                this.login = false;
                this.registration = true;
                this.visualization = false;
                this.history = false;
                this.activeTutoring = false;
                this.adminPage = false;
            },
            goAvailable: function () {
                this.index = false;
                this.login = false;
                this.registration = false;
                this.visualization = true;
                this.history = false;
                this.activeTutoring = false;
                this.adminPage = false;
                this.getRoleSession();
                this.subjectAvailable();
            },
            goActive: function () {
                this.index = false;
                this.login = false;
                this.registration = false;
                this.visualization = false;
                this.history = false;
                this.activeTutoring = true;
                this.adminPage = false;
                this.getRoleSession();
                this.getTutorings();
                if (this.tutorings.length === 0) {
                    alert("The tutoring list is empty");
                    this.goIndex();
                }
            },
            goHistory: function () {
                this.index = false;
                this.login = false;
                this.registration = false;
                this.visualization = false;
                this.history = true;
                this.activeTutoring = false;
                this.adminPage = false;
                this.getRoleSession();
                this.getHistory();
                if (this.histories.length === 0) {
                    alert("The history list is empty");
                    this.goIndex();
                }
            },
            goAdmin: function () {
                this.index = false;
                this.login = false;
                this.registration = false;
                this.visualization = false;
                this.history = false;
                this.activeTutoring = false;
                this.adminPage = true;
            },
            goIndex: function () {
                this.subjects = [];
                this.teachers = [];
                this.sessionFunction();
                this.index = true;
                this.login = false;
                this.registration = false;
                this.visualization = false;
                this.history = false;
                this.activeTutoring = false;
                this.adminPage = false;
            },
            getRoleSession:function(){
                var self = this;
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "POST", // data type (can be get, post, put, delete)
                    data : {submit: "getRoleSession"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        console.log(data + "tipo: " + typeof data);
                        self.role = data;
                        switch (data) {
                            case '0': //guest
                                self.guest = true;
                                self.student = false;
                                self.admin = false;
                                break;
                            case '1': //student
                                self.guest = false;
                                self.student = true;
                                self.admin = false;
                                break;
                            case '2': //admin
                                self.guest = false;
                                self.student = false;
                                self.admin = true;
                                break;
                        }
                    }
                });
            },
            subjectAvailable:function() {
                var self = this;
                $.ajax({
                    url : "MainServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "subjectAvailable"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        console.log(data);
                        self.subjects = data;
                    }
                });
            },
            showButton:function () {
                var self = this;
                self.available = true;
            },
            hideButton:function () {
                var self = this;
                self.available = false;
            },
            getTeachers:function() {
                this.subjectSelected = $('#subjectSelected').find(":selected").text();
                console.log(this.subjectSelected.replace(/\s/g, ''));
                var self = this;
                $.ajax({
                    url : "MainServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "getTeachers", subject: this.subjectSelected.replace(/\s/g, ''), email: "false"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        self.teachers = data;
                        console.log(self.teachers);
                    }
                });
            },
            printInfo:function(res) {
                var self = this;
                self.bookingMessage = res;
            },
            teacherAvailability:function() {
                this.subjectSelected = $('#subjectSelected').find(":selected").text();
                this.teacherSelected = $('#teacherSelected').find(":selected").text();
                var self = this;
                $.ajax({
                    url : "MainServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "teacherAvailability", subject: this.subjectSelected, teacher: this.teacherSelected}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        self.booking = data;
                        for (var i=0; i<self.booking.length; i++) {
                            switch (self.booking[i].date) {
                                case "Monday":
                                    self.setRedCell(0, self.booking[i].hour);
                                    break;
                                case "Tuesday":
                                    self.setRedCell(1, self.booking[i].hour);
                                    break;
                                case "Wednesday":
                                    self.setRedCell(2, self.booking[i].hour);
                                    break;
                                case "Thursday":
                                    self.setRedCell(3, self.booking[i].hour);
                                    break;
                                case "Friday":
                                    self.setRedCell(4, self.booking[i].hour);
                                    break;
                                default:
                                    console.log("c'è qualcosa che non va");
                                    break;
                            }
                        }
                    }
                });
            },
            setRedCell:function (row, hour) {
                var els = document.getElementById('table');
                var index;
                switch (hour) {
                    case "15":
                        index = 1;
                        break;
                    case "16":
                        index = 2;
                        break;
                    case "17":
                        index = 3;
                        break;
                    case "18":
                        index = 4;
                        break;
                    default:
                        console.log("c'è qualcosa che non va");
                        break;
                }
                console.log(row);
                console.log(index);
                els.rows[row].cells[index].style.background = "red";
            },
            insertBooking:function (subject, teacher, day, hour) {
                var self = this;
                console.log("DEBUG ORA: DOCENTE: " +teacher);
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "insertBooking", subject: subject, teacher: teacher, day: day, hour: hour}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        console.log("questo è il data: "+data);
                        if (data === "true") {
                            alert("Your booking has been correctly inserted in the system");
                            self.teacherAvailability();
                            return false;
                        } else {
                            alert("Error");
                            return false;
                        }

                    }
                });
            },
            getTutorings:function(){
                var self = this;
                //obtain the array of tutorings
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "getStudentTutoring"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        self.tutorings = data;
                        console.log(self.tutorings);
                    }
                });
            },
            confirmTutoring:function (index) {
                var date = this.tutorings[index].date;
                var hour = this.tutorings[index].hour;
                var teacher = this.tutorings[index].teacher;
                var subject = this.tutorings[index].subject;
                var self = this;
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "confirmTutoring", date: date, hour: hour, teacher: teacher, subject: subject}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        if (data === "true") {
                            alert ("Your booking has been confirmed successfully");
                            self.getTutorings();
                            return false;
                        }
                        else {
                            alert ("Error");
                            return false;
                        }
                    }
                });
            },
            cancelTutoring:function (index) {
                var date = this.tutorings[index].date;
                var hour = this.tutorings[index].hour;
                var teacher = this.tutorings[index].teacher;
                var subject = this.tutorings[index].subject;
                var self = this;
                if (this.admin) {
                    //delete booking by admin
                    var emailStudent = this.tutorings[index].student;
                    $.ajax({
                        url : "SessionServlet", // Url of backend (can be python, php, etc..)
                        type: "POST", // data type (can be get, post, put, delete)
                        data : {submit: "deleteTutoring", date: date, hour: hour, teacher: teacher, subject: subject, emailStudent: emailStudent}, // data in json format
                        async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                        success: function(data) {
                            if (data === "true") {
                                alert ("Booking has been cancelled successfully");
                                self.getTutorings();
                                return false;
                            }
                            else {
                                alert ("Error");
                                return false;
                            }
                        }
                    });
                } else {
                    //delete booking by user
                    $.ajax({
                        url : "SessionServlet", // Url of backend (can be python, php, etc..)
                        type: "POST", // data type (can be get, post, put, delete)
                        data : {submit: "deleteTutoring", date: date, hour: hour, teacher: teacher, subject: subject}, // data in json format
                        async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                        success: function(data) {
                            if (data === "true") {
                                alert ("Your booking has been cancelled successfully");
                                self.getTutorings();
                            }
                            else {
                                alert ("Error");
                                return false;
                            }
                        }
                    });
                }
            },
            getHistory:function () {
                var self = this;
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "getHistory"}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        console.log("operazione history eseguita con successo");
                        self.histories = data;
                    }
                });
            },
            restrictionHistory:function (status) {
                var self = this;
                $.ajax({
                    url : "SessionServlet", // Url of backend (can be python, php, etc..)
                    type: "GET", // data type (can be get, post, put, delete)
                    data : {submit: "getRestrictedHistory", status: status}, // data in json format
                    async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                    success: function(data) {
                        console.log("operazione history eseguita con successo");
                        self.histories = data;
                    }
                });
            },
            getRestrictedTeachers:function (subject, email) {
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
                        alert("Teacher correctly deleted");
                        $('.collapse').collapse('hide');
                        return false;
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
                        alert("Association " + elem[2] + " - " + subject + " correctly deleted");
                        $('.collapse').collapse('hide');
                        $('#subjectSelected4').prop('selectedIndex',0);
                        return false;
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
                        alert("Course " + subject + " correctly deleted");
                        $('.collapse').collapse('hide');
                        return false;
                    }
                });
            },
            registrationUser:function (event){
                let self = this;
                let email = document.getElementById("emailRegistration").value;
                let pass = document.getElementById("password").value;
                let pass1 = document.getElementById("rPassword").value;
                if (pass === pass1) {
                    $.ajax({
                        url : "MainServlet", // Url of backend (can be python, php, etc..)
                        type: "POST", // data type (can be get, post, put, delete)
                        data : {submit: "registration", email: email, password: pass}, // data in json format
                        async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
                        success: function(data) {
                            if (data === "ok") {
                                alert("User correctly inserted in the system");
                                self.goIndex();
                            } else {
                                alert("Error, user already exists");
                                event.preventDefault();
                            }
                            return false;
                        }
                    });
                } else {
                    alert("Passwords are not equals");
                    event.preventDefault();
                    return false;
                }

            }
        }
    });

    $("#sessionID").on('click', '#loginButton', function (event) {
        let email = document.getElementById("email").value;
        let psw = document.getElementById("psw").value;
        console.log("EMAIL"+email);
        console.log("PWD"+psw);
        $.ajax({
            url : "SessionServlet", // Url of backend (can be python, php, etc..)
            type: "POST", // data type (can be get, post, put, delete)
            data : {submit: "login", email: email, password: psw}, // data in json format
            async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
            success: function(data) {
                console.log("risultato data: " + data + "tipo: " + typeof data);
                let res = Number(data);
                if(res === 0){
                    alert("User not found");
                    event.preventDefault();
                    return false;
                } else if(res === 1) {
                    console.log("entra");
                    event.preventDefault();
                    app.goIndex();
                }
            }
        });
    });

    $("#sessionID").on('change', '#subjectSelected', function () {
        $("#teacherSelected").val(0).change();
        resetColorCell();
        app.getTeachers();
    });


    $("#sessionID").on('change', '#teacherSelected', function () {
        resetColorCell();
        app.teacherAvailability();
    });

    $("#sessionID").on('click', 'td', function () {
        if ($('#subjectSelected').find(":selected").text() !== "Select a subject" && $('#teacherSelected').find(":selected").text() !== "Select a teacher") {
            var els = document.getElementById('table');
            var row_index = $(this).closest("tr").index();
            var col_index = $(this).index();
            var res = "";
            if (col_index !== 0) {
                if (els.rows[row_index].cells[col_index].style.background !== "red") {
                    var day = (document.getElementById('table').rows[row_index].cells[0]).innerHTML;
                    var hour = (document.getElementById('table').rows[row_index].cells[col_index]).innerHTML;
                    var res = "You have selected: \nDay: " + day + "\n Hour: " + hour;
                    app.printInfo(res);
                    app.showButton();
                    app.rowSelected = day;
                    app.columnSelected = hour;
                } else {
                    var res = "This booking is not available!";
                    app.printInfo(res);
                    app.hideButton();
                }
            } else app.hideButton();

        }
    });

    $("#sessionID").on('click', '#actionBook', function () {
        var col_index = app.columnSelected.split("-");
        app.insertBooking(app.subjectSelected, app.teacherSelected, app.rowSelected, col_index[0]);

    });

    function resetColorCell () {
        var els = document.getElementById('table');
        for (var i=0; i<5; i++) {
            for (var j=1; j<5; j++) {
                els.rows[i].cells[j].style.background = "transparent";
            }

        }
    }

    $("#sessionID").on('click', '#confirmButton', function () {
        var index = $(this).parent().index();
        app.confirmTutoring(index);
    });

    $("#sessionID").on('click', '#cancelButton', function () {
        var index = $(this).parent().index();
        app.cancelTutoring(index);
    });

    $("#sessionID").on('click', '#cancelButtonAdmin', function () {
        console.log("FASE 1");
        var index = $(this).parent().index();
        app.cancelTutoring(index);
        console.log("FASE 2");
    });

    $("#sessionID").on('click', '#restrictionSelected', function () {
        console.log("PROVA");
        if ($('#restrictionSelected').find(":selected").text() === "All bookings")
            app.getHistory();
        else if ($('#restrictionSelected').find(":selected").text() === "Active bookings")
            app.restrictionHistory(0);
        else if ($('#restrictionSelected').find(":selected").text() === "Confirmed bookings")
            app.restrictionHistory(1);
        else if ($('#restrictionSelected').find(":selected").text() === "Cancelled bookings")
            app.restrictionHistory(2);
    });

    $("#sessionID").on('click', '#invioDatiDocente', function (event) {
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
                    alert("Teacher successfully inserted in the system");
                    $('.collapse').collapse('hide');
                    $('#formInputNome').val("");
                    $('#formInputCognome').val("");
                    $('#formInputEmail').val("");
                    event.preventDefault();
                    return false;
                }
                else {
                    alert("Error, this teacher is already in the system");
                    event.preventDefault();
                    return false;
                }
            }
        });
    });

    $("#sessionID").on('click', '#sectionDeleteTeacher', function () {
        //$("#subjectSelected2").val(0).change();
        $('#subjectSelected2').prop('selectedIndex',0);
        app.teachers = [];
        app.subjectAvailable();
    });

    $("#sessionID").on('click', '#sectionInsertAssociation', function () {
        app.subjectAvailable();
        app.getRestrictedTeachers("All", "false");
    });

    $("#sessionID").on('click', '#confirmAssociationButton', function (event) {
        if ($('#teacherSelected3').find(":selected").text() !== "Select a teacher" && $('#subjectSelected3').find(":selected").text() !== "Select a subject") {
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
                        alert("Association has been inserted successfully in the system");
                        $('.collapse').collapse('hide');
                        event.preventDefault();
                        $('#teacherSelected3').prop('selectedIndex',0);
                        $('#subjectSelected3').prop('selectedIndex',0);
                        return false;
                    }
                    else {
                        alert("Error");
                        event.preventDefault();
                        return false;
                    }
                }
            });
        }
    });

    $("#sessionID").on('click', '#sectionDeleteAssociation', function () {
        app.subjectAvailable();
        app.teachers = [];
    });

    $("#sessionID").on('change', '#subjectSelected2', function () {
        if ($('#subjectSelected2').find(":selected").text() !== "Restrict by subjects") {
            app.getRestrictedTeachers($('#subjectSelected2').find(":selected").text(), "true");
        } else
            app.teachers = [];
    });

    $("#sessionID").on('click', '#confirmDeleteButton', function () {
        let index = $(this).parent().index();
        app.deleteTeacher(index);
    });

    $("#sessionID").on('change', '#subjectSelected4', function () {
        if ($('#subjectSelected4').find(":selected").text() !== "Select a subject") {
            app.getRestrictedTeachers($('#subjectSelected4').find(":selected").text());
        } else
            app.teachers = [];
    });

    $("#sessionID").on('click', '#confirmDeleteAssociation', function () {
        let index = $(this).parent().index();
        app.deleteAssociation(index, document.getElementById("subjectSelected4").value);
    });

    $("#sessionID").on('click', '#invioNomeCorso', function (event) {
        let subject = document.getElementById("formInputCorso").value;
        $.ajax({
            url : "MainServlet", // Url of backend (can be python, php, etc..)
            type: "GET", // data type (can be get, post, put, delete)
            data : {submit: "insertCourse", subject: subject}, // data in json format
            async : false, // enable or disable async (optional, but suggested as false if you need to populate data afterwards)
            success: function(data) {
                if (data === "true") {
                    alert("Course successfully inserted");
                    $('.collapse').collapse('hide');
                    event.preventDefault();
                    return false;
                } else {
                    alert("Error, this course is already in the system");
                    event.preventDefault();
                    return false;
                }
            }
        });
    });

    $("#sessionID").on('click', '#sectionDeleteCourse', function () {
        app.subjectAvailable();
    });

    $("#sessionID").on('click', '#confirmDeleteCourse', function () {
        app.deleteCourse($(this).parent().index());
    });

    $("#sessionID").on('click', '#registration', function (event) {
        app.registrationUser(event);
    });

    $("#formInsertTeacher").on('submit',function (e) {
        alert(test);
    })

});
