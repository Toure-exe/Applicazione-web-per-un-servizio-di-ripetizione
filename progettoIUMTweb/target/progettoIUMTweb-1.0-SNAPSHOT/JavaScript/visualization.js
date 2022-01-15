$(document).ready(function () {
    var app = new Vue ({
        el: '#subjectsVisualization',
        data: {
            role: '',
            //account:  'guest',
            subjects:[],
            guest: true,
            student: false,
            admin: false,
            available: false,
            link: '/progettoIUMTweb_war_exploded/MainServlet'
        },
        mounted() {
            this.sessionFunction();
            this.subjectAvailable();
            console.log("entra nel mounted");
        },
        methods:{
            sessionFunction:function(){
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
            }
        }
    });

    var app1 = new Vue ({
        el: '#teachersVisualization',
        data: {
            subjectSelected: '',
            teacherSelected: '',
            rowSelected: '',
            columnSelected: '',
            teachers:[],
            booking:[],
            bookingMessage: '',
            link: '/progettoIUMTweb_war_exploded/MainServlet'
        },
        mounted() {
            console.log("entra nel mounted");
        },
        methods:{
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
                                case "Lunedi":
                                    self.setRedCell(0, self.booking[i].hour);
                                    break;
                                case "Martedi":
                                    self.setRedCell(1, self.booking[i].hour);
                                    break;
                                case "Mercoledi":
                                    self.setRedCell(2, self.booking[i].hour);
                                    break;
                                case "Giovedi":
                                    self.setRedCell(3, self.booking[i].hour);
                                    break;
                                case "Venerdi":
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
                            alert("La tua prenotazione è stata effettuata con successo");
                            window.location = "index.html";
                        } else {
                            alert("Errore, ripetere la procedura");
                        }

                    }
                });
            }
        }
    });

    $("#subjectSelected").change(function(){
        app1.getTeachers();
    });

    $("#teacherSelected").change(function(){
        resetColorCell();
        app1.teacherAvailability();
    });

    $('td').click(function() {
        if ($('#subjectSelected').find(":selected").text() !== "Seleziona la materia" && $('#teacherSelected').find(":selected").text() !== "Seleziona il docente") {
            var els = document.getElementById('table');
            var row_index = $(this).closest("tr").index();
            var col_index = $(this).index();
            var res = "";
            if (col_index !== 0) {
                if (els.rows[row_index].cells[col_index].style.background !== "red") {
                    var day = (document.getElementById('table').rows[row_index].cells[0]).innerHTML;
                    var hour = (document.getElementById('table').rows[row_index].cells[col_index]).innerHTML;
                    var res = "Hai selezionato, giorno: " + day + " ore: " + hour;
                    app1.printInfo(res);
                    app.showButton();
                    app1.rowSelected = day;
                    app1.columnSelected = hour;
                } else {
                    var res = "Hai selezionato una prenotazione non disponibile!";
                    app1.printInfo(res);
                    app.hideButton();
                }
            } else app.hideButton();

        }
    });

    $("#subjectsVisualization").on('click', '#actionBook', function () {
        var col_index = app1.columnSelected.split("-");
        app1.insertBooking(app1.subjectSelected, app1.teacherSelected, app1.rowSelected, col_index[0]);

    });

    function resetColorCell () {
        var els = document.getElementById('table');
        for (var i=0; i<5; i++) {
            for (var j=1; j<5; j++) {
                els.rows[i].cells[j].style.background = "transparent";
            }

        }
    }
});




